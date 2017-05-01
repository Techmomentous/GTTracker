/**
 * Custom-Marker.js Project Name : GTracker Version 1.0 Date : 5 July 2015
 * Author : Rizwan Ahmad Khan
 */

// Default Path of Pictures
var MEMBER_PIC_FOLDER_PATH = 'member_pic';

/**
 * Construct a Custom override marker on map
 * 
 * @param locations
 */
function addMarker(locations) {
	var fullName = locations.fullname;
	var autoId = locations.id;
	var titleText = "Member ID : " + autoId + " Name : " + fullName;
	var latitued = locations.latitude;
	var longitude = locations.longitude;
	var address = locations.address;
	var email = locations.email;
	var phone = locations.phone;
	var markerpic = locations.markerpic;

	var infowindow = getInfoWindow(autoId, titleText, fullName, latitued,
			longitude, address, email, phone, markerpic);

	var marker = new google.maps.Marker({
		position : new google.maps.LatLng(latitued, longitude),
		map : map,
		title : "Member Name : " + fullName
	});
	marker.id = locations.id;
	marker.fullname = fullName;
	marker.titleText = titleText;
	marker.mlatitued = latitued;
	marker.mlongitude = longitude;
	marker.address = address;
	marker.email = email;
	marker.phone = phone;
	marker.markerpic = markerpic;

	// Open the info window on marker click
	// This event expects a click on a marker
	// When this event is fired the Info Window is opened.
	google.maps.event.addListener(marker, 'click', function() {
		infowindow.open(map, marker);
		var member_idDiv = document.getElementById('member_' + this.id);

		// Remove previous add buttons
		member_idDiv.innerHTML = "";
		member_idDiv.appendChild(getButtonsDiv(this));
	});

	// Event that closes the Info Window with a click on the map
	google.maps.event.addListener(map, 'click', function() {
		infowindow.close();
	});

	// Customize the info window
	customizInfoWindow(infowindow);

	markers[marker['id']] = marker;
}

/**
 * Return the custom made info window with member details
 * 
 * @param autoId
 * @param titleText
 * @param fullName
 * @param latitued
 * @param longitude
 * @param address
 * @param email
 * @param phone
 * @returns {google.maps.InfoWindow}
 */
function getInfoWindow(autoId, titleText, fullName, latitued, longitude,
		address, email, phone, markerpic) {

	// InfoWindow content
	var content = '<div id="iw-container">' + '<div class="iw-title">'
			+ titleText
			+ '</div>'
			+ '<div class="iw-content">'
			+ '<div class="iw-subTitle">'
			+ fullName
			+ '</div>'
			+ '<img class="img-thumbnail" id= "pic_'
			+ autoId
			+ '" src="'
			+ getProfilePic(autoId, markerpic)
			+ '" alt="'
			+ fullName
			+ '" height="115" width="83">'
			+ '<p>Latitued : '
			+ latitued
			+ ' <br> Longitude :'
			+ longitude
			+ '</p>'
			+ '<div class="iw-subTitle">Contacts</div>'
			+ '<p>Address : '
			+ address
			+ '<br>'
			+ '<br>Phone.'
			+ phone
			+ '<br>e-mail: '
			+ email
			+ '<br></p>'
			+ '</div>'
			+ '<div'
			+ ' class="iw-bottom-gradient"></div> <div class="actButtons" id="member_'
			+ autoId + '"></div></div>';

	// A new Info Window is created and set content
	var infowindow = new google.maps.InfoWindow({
		content : content,

		// Assign a maximum value for the width of the infowindow allows
		// greater control over the various content elements
		maxWidth : 350
	});

	return infowindow;
}

/**
 * START INFOWINDOW CUSTOMIZE. The google.maps.event.addListener() event expects
 * the creation of the infowindow HTML structure 'domready' and before the
 * opening of the infowindow, defined styles are applied.
 * 
 * @param infowindow
 */
function customizInfoWindow(infowindow) {

	google.maps.event.addListener(infowindow, 'domready', function() {

		// Reference to the DIV that wraps the bottom of infowindow
		var iwOuter = $('.gm-style-iw');

		/*
		 * Since this div is in a position prior to .gm-div style-iw. We use
		 * jQuery and create a iwBackground variable, and took advantage of the
		 * existing reference .gm-style-iw for the previous div with .prev().
		 */
		var iwBackground = iwOuter.prev();

		// Removes background shadow DIV
		iwBackground.children(':nth-child(2)').css({
			'display' : 'none'
		});

		// Removes white background DIV
		iwBackground.children(':nth-child(4)').css({
			'display' : 'none'
		});

		// Moves the infowindow 115px to the right.
		iwOuter.parent().parent().css({
			left : '115px'
		});

		// Moves the shadow of the arrow 76px to the left margin.
		iwBackground.children(':nth-child(1)').attr('style', function(i, s) {
			return s + 'left: 76px !important;'
		});

		// Moves the arrow 76px to the left margin.
		iwBackground.children(':nth-child(3)').attr('style', function(i, s) {
			return s + 'left: 76px !important;'
		});

		// Changes the desired tail shadow color.
		iwBackground.children(':nth-child(3)').find('div').children().css({
			'box-shadow' : 'rgba(72, 181, 233, 0.6) 0px 1px 6px',
			'z-index' : '1'
		});

		// Reference to the div that groups the close button elements.
		var iwCloseBtn = iwOuter.next();

		// Apply the desired effect to the close button
		iwCloseBtn.css({
			opacity : '1',
			right : '38px',
			top : '3px',
			border : '7px solid #48b5e9',
			'border-radius' : '13px',
			'box-shadow' : '0 0 5px #3990B9'
		});

		// The API automatically applies 0.7 opacity to the button after the
		// mouseout event. This function reverses this event to the desired
		// value.
		iwCloseBtn.mouseout(function() {
			$(this).css({
				opacity : '1'
			});
		});
	});
}

/**
 * Return the Profile Pictuer path
 * 
 * @param autoID
 * @param markerpic
 * @returns {String}
 */
function getProfilePic(autoID, markerpic) {
	return MEMBER_PIC_FOLDER_PATH + '/' + markerpic;
}
/**
 * Return the button div
 * 
 * @returns {___anonymous5126_5136}
 */
function getButtonsDiv(e) {
	var autoID = e.id;
	var fullName = e.fullname;
	var btnGroupDiv = document.createElement('div');
	btnGroupDiv.className = 'btn-group';

	// Profile Picture Button
	var btnPicUpload = document.createElement("button");
	btnPicUpload.className = "btn btn-primary btn-xs";
	btnPicUpload.innerHTML = "Upload Profie Pic";
	btnPicUpload.style.margin = '5px';
	btnPicUpload
			.setAttribute('onclick', 'javascript:updatePic(' + autoID + ')');
	btnPicUpload.setAttribute('type', 'button');
	btnPicUpload.setAttribute('autoID', autoID);
	btnPicUpload.setAttribute('fullName', fullName);
	btnGroupDiv.appendChild(btnPicUpload);

	// Send Message Button
	var btnSendMessage = document.createElement("button");
	btnSendMessage.className = "btn btn-info btn-xs";
	btnSendMessage.innerHTML = "Send Message";
	btnSendMessage.style.margin = '5px';
	btnSendMessage.setAttribute('onclick', 'javascript:postMessage(' + autoID
			+ ')');
	btnSendMessage.setAttribute('type', 'button');
	btnGroupDiv.appendChild(btnSendMessage);

	return btnGroupDiv;
}
