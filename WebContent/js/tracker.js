/**
 * Tracker.JS
 * Project Name : GTracker
 * Version 1.0
 * Date : 5 July 2015
 * Author : Rizwan Ahmad Khan
 */

/**
 * Display Member Registration Modal Dialog
 */
function addMember() {
	var w=1004;
	var h=462;
	var left = (screen.width / 2) - (w / 2);
	var top = (screen.height / 2) - (h / 2);
	return window.open('registration.jsp', "GTracker New Member Signup",
	        'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, width='
	                + w + ', height=' + h + ', top=' + top + ', left=' + left);
}

function toggleMenu() {

	$("#wrapper").toggleClass("toggled");
}
/**
 * Construct the Member List and Display send Message Modal Dialog
 * 
 */
function sendMessageToMember() {
	// Create members list at run time
	var dropDown = document.getElementById('memberSelList');
	document.getElementById('memberSelList').innerHTML = '';

	for (var i = 1; i < markers.length; i++) {
		var memberName = markers[i].fullname;
		var id = markers[i].id;

		var li = document.createElement("li");
		li.value = id;

		var anchor = document.createElement("a");
		anchor.text = memberName;
		anchor.setAttribute('href', "javascript:setMemberID('" + id + "');");
		li.appendChild(anchor);

		dropDown.appendChild(li);

		// create separator
		var divider = document.createElement('li');
		divider.className = 'divider';
		dropDown.appendChild(divider);
	}
	// Reset label and text field
	document.getElementById('messageText').value = '';
	document.getElementById('messagesType').innerHTML = "";
	// visible the drop down div
	document.getElementById("memeberSelection").style.display = 'block';
	$('#sendMessageModal').modal('show');
}

/**
 * Display The Notifications Window
 * 
 */
function viewNotifications() {
	// close side menu if open
	toggleMenu();
	$('#messageModal').modal('show');
}

/**
 * Display The Google Map Search Box
 * 
 */
function searchPlace() {

	$('#searchAPlaceModal').modal('show');
}

/**
 * Add The Member Marker Locations
 * <p>
 * Its create markers on map of the given locations details and Trigger the
 * fetch update method after 1 seconds
 * </p>
 * 
 * @param locations
 */
function addMembers(locations) {
	markers = new Array();
	for (var i = 0; i < locations.length; i++) {
		addMarker(locations[i]);
	}

	// Once finished adding members on map start fetch method after one seconds
	setTimeout(function() {
		// Update Progress bar value
		updateProgressBar("Initializing Location Service", "70%");

		fetchUpdate();
	}, 5000);

	// Execute The Fetch Push Notification method after five seconds
	setTimeout(function() {
		// Update Progress bar value
		updateProgressBar("Initializing GCM Service", "90%");
		fetchNotifications();
	}, 10000);
}

/**
 * Fetch the latest members locations from database after every 1 minute
 */
function fetchUpdate() {

	setInterval(function() {
		$.ajax({
		    url : "LocationService",
		    type : "GET",
		    data : {
			    request : 'getAllMembersLocation'
		    },
		    success : function(data) {
			    parseLocationsJOSN(JSON.parse(data));
		    }
		});
	}, 60000); // <-------Interval Is Set To 1 Minute
}

/**
 * Loop through the locations json and trigger update marker function
 * 
 * @param locationsJSON
 */
function parseLocationsJOSN(locationsJSON) {
	for (var i = 0; i < locationsJSON.length; i++) {
		var jsonObject = locationsJSON[i];
		updateMarker(jsonObject);
	}
}

/**
 * This function takes an array argument containing a list of marker data
 * 
 * @param locations
 */
function updateMarker(locations) {
	// if member is new and his tracker app is not started we get null
	if (locations == null) {
		return;
	}
	// Get the marker by member id
	var marker = markers[locations.memberid];
	var titleText = "Member ID : " + locations.memberid + " Name : " + locations.username;
	marker.title = titleText;

	var markerPicture = locations.markerpic;
	marker.markerpic = markerPicture;

	var latlng = new google.maps.LatLng(locations.latitude, locations.longitude);
	map.setCenter(latlng);
	marker.setPosition(latlng);
	console.log("Updated Location : " + locations.latitude + " ## " + locations.longitude + " ## " + +locations.title
	        + " ID " + locations.memberid);

}

/**
 * Open The Find Member Modal Dialog
 */
function openFindMemberModal() {
	// Show the dialog
	$('#modalfind').modal('show');

	// Empty the dropdown list
	var dropDown = document.getElementById('membersList');
	document.getElementById('membersList').innerHTML = '';

	// Loop through the markers and add member details in the list
	for (var i = 1; i < markers.length; i++) {
		var memberName = markers[i].fullname;
		var id = markers[i].id;

		var option = document.createElement("option");
		option.text = memberName;
		option.value = id;
		dropDown.options.add(option);
	}

}

/**
 * Go To the selected member marker position
 */
function locateMember() {
	$('#modalfind').modal('hide');
	var markerIdentifier = $('#membersList').val();

	// Set the member id
	setMemberID(markerIdentifier);
	var selectedMarker = markers[markerIdentifier];
	var position = selectedMarker.getPosition();
	var name = $('#membersList option:selected').text();
	var pt = position;
	newpt = new google.maps.LatLng(pt.lat() + .02, pt.lng());
	map.panTo(newpt);
	var infowindow = getInfoWindow(selectedMarker.id, selectedMarker.titleText, selectedMarker.fullName,
	        selectedMarker.mlatitued, selectedMarker.mlongitude, selectedMarker.address, selectedMarker.email,
	        selectedMarker.phone, selectedMarker.markerpic);
	infowindow.position = position;

	infowindow.open(map, selectedMarker);
	var member_idDiv = document.getElementById('member_' + selectedMarker.id);

	// Remove previous add buttons
	member_idDiv.innerHTML = "";
	member_idDiv.appendChild(getButtonsDiv(this));
	infowindow.open(map, selectedMarker);
}


/**
 * Display send Message Dialog and set the member id in data tag
 * 
 * @param autoid
 */
function postMessage(autoid) {
	// Hide the member list dropdown
	document.getElementById("memeberSelection").style.display = 'none';

	document.getElementById('messageText').value = '';
	document.getElementById('messagesType').innerHTML = "";

	if (autoid != undefined) {
		$('#data')[0].setAttribute('autoid', autoid);
	}
	$('#sendMessageModal').modal('show');
}

/**
 * Display the update profile dialog and send the data once image is selected to
 * the server
 */
function updatePic(autoid) {
	// if Auto ID is undefine in case of locate Member
	if (autoid == undefined) {
		autoid = $('#data')[0].getAttribute('autoid');
	}

	$('#addMemberPicModal').modal('show');
	// Reset the file dialog
	$("#fileuploader")[0].innerHTML = '';
	for (var i = 0; i < $(".ajax-file-upload-container").length; i++) {
		$(".ajax-file-upload-container")[i].innerHTML = '';
	}

	$("#fileuploader").uploadFile({
	    url : "MembersService?request=updatePic&autoid=" + autoid,
	    maxFileCount : 1,
	    allowedTypes : "png,gif,jpg,jpeg",
	    maxFileSize : 5242880, // 5MB -> 5 * 1024 *
	    // 1024
	    showProgress : true,
	    multiDragErrorStr : "Only 1 file can be uploaded.",
	    extErrorStr : "Invalid Extension. Allowed Extensions are [png,gif,jpg,jpeg] :",
	    sizeErrorStr : "Max size exceeded. Max Size is 5mb",
	    uploadErrorStr : "Upload Failed",
	    onSuccess : function(files, data, xhr, pd) {
		    $('#addMemberPicModal').modal('hide');
		    showPrimaryMessage(data);

	    },
	    onError : function(files, status, errMsg, pd) {
		    $('#addMemberPicModal').modal('hide');
		    showPrimaryMessage(errMsg);
	    }
	});

}

/**
 * Set the type of message to be send in send message dialog
 * 
 * @param messageCode
 * @param messageText
 */
function setMessageType(messageCode, messageText) {
	// Set messageCode
	$('#data')[0].setAttribute('messageCode', messageCode);
	document.getElementById('messagesType').innerHTML = "You Selected :" + messageText;

}

/**
 * Set the member id from the member dropdown in send message dialog
 * 
 * @param autoid
 */
function setMemberID(autoid) {
	$('#data')[0].setAttribute('autoid', autoid);
}

/**
 * Send The GCM Push Notification
 */
function sendMessage() {
	if ((document.getElementById('messagesType').innerHTML == "")) {
		showAlert("Please Select Message Type");
		return;
	}
	var currentDate = new Date();
	var currentTime = currentDate.getHours() + " : " + currentDate.getMinutes() + " : " + currentDate.getSeconds();
	var messageCode = $('#data')[0].getAttribute('messageCode');
	var memberID = $('#data')[0].getAttribute('autoid');
	var message = document.getElementById('messageText').value;

	jQuery.ajax({
	    url : 'GCMService',
	    type : 'POST',
	    data : {
	        request : 'sendMessage',
	        messageCode : messageCode,
	        memberID : memberID,
	        message : message,
	        date : currentDate.toDateString(),
	        time : currentTime
	    },
	    async : false
	}).done(function(response) {

		if (response != undefined || response != "") {
			$('#sendMessageModal').modal('hide');
			document.getElementById('messageText').value = "";
			showPrimaryMessage(response);
		}
	});
}