/**
 * Message.js
 * Project Name : GTracker
 * Version 1.0
 * Date : 5 July 2015
 * Author : Rizwan Ahmad Khan
 */

/**
 * Get the push notifications in every 2 minutes
 * <p>
 * The Time Interval is Set 2 minutes because we need delay of 1 minute between
 * the location update service and message service to over come the n number of
 * connections with the database
 * </p>
 */
function fetchNotifications() {
	// Update Progress bar value
	setTimeout(function() {
		updateProgressBar("Finishing....", "100%");
		setTimeout(function() {
			toggleProgressBar();
			resetProgressBar();

		}, 5000);

	}, 10000);

	setInterval(function() {

		$.ajax({
			url : "MessageService",
			type : "GET",
			data : {
				request : 'getMessages'
			},
			success : function(data) {
				parsePushMessagJSON(JSON.parse(data));
			}
		});
	}, 60000); // <-------Interval Is Set To 2 Minute
}

/**
 * Parse The Push Message JSON And Display on ui
 * 
 * @param jsonArray
 */
function parsePushMessagJSON(jsonArray) {
	var messageCount = jsonArray.length;
	// Remove all the previous messages from tbody
	document.getElementById('messageBox').innerHTML = '';
	document.getElementById("messageCount").innerHTML = messageCount;
	for (var i = 0; i < messageCount; i++) {
		var jsonObject = jsonArray[i];
		addPushNotifications(jsonObject);
	}
}

/**
 * Create The Push Notification Item in UI
 * 
 * @param jsonObject
 */
function addPushNotifications(jsonObject) {
	console.log(jsonObject);
	var messageID = jsonObject.idmessages;
	var memberID = jsonObject.memberid;
	var name = jsonObject.name;
	var flag = jsonObject.flagnew;
	var type = jsonObject.type;
	var time = jsonObject.time;
	var message = jsonObject.message;
	var tr = document.createElement("tr");
	tr.setAttribute('id', messageID);
	tr.className = 'messageTr' + messageID;
	var checkBoxTD = document.createElement("td");
	checkBoxTD.className = "mailbox-star";

	var input = document.createElement("input");
	input.setAttribute('type', 'checkbox');
	input.setAttribute('name', 'messageCheckBox');
	input.setAttribute('messageID', messageID);
	input.setAttribute('memberID', memberID);

	checkBoxTD.appendChild(input);
	tr.appendChild(checkBoxTD);

	var mailboxStarTD = document.createElement("td");
	mailboxStarTD.className = "mailbox-star";

	switch (type) {
	case '101':
		mailboxStarTD.innerHTML = 'It`s Urgent';
		break;
	case '102':
		mailboxStarTD.innerHTML = 'Call Notice';
		break;
	case '103':
		mailboxStarTD.innerHTML = 'Requset';
		break;
	case '104':
		mailboxStarTD.innerHTML = 'ASAP';
		break;
	default:
		mailboxStarTD.innerHTML = 'Unknown';
		break;
	}
	tr.appendChild(mailboxStarTD);
	tr.setAttribute('messageID', messageID);
	tr.setAttribute('memberID', memberID);

	var nameTD = document.createElement("td");
	nameTD.className = "mailbox-name";
	nameTD.style.color = "#337ab7";
	nameTD.innerHTML = name;
	tr.appendChild(nameTD);

	var subjectTD = document.createElement("td");
	subjectTD.className = "mailbox-subject";
	subjectTD.innerHTML = message;
	tr.appendChild(subjectTD);

	var attTD = document.createElement("td");
	attTD.className = "mailbox-attachment";
	tr.appendChild(attTD);

	var dateTD = document.createElement("td");
	dateTD.className = "mailbox-date";
	dateTD.innerHTML = time;
	tr.appendChild(dateTD);

	var tbody = document.getElementById('messageBox');
	tbody.appendChild(tr);
}
/**
 * Delete the selected messages
 */
function deleteMessage() {
	var selectedCheckBox = $('input:checkbox:checked').map(function() {
		return this.getAttribute('messageid');
	}).get();

	for (var i = 0; i < selectedCheckBox.length; i++) {
		$("#messageBox").find(".messageTr" + selectedCheckBox[i]).remove();
	}
	// Fetch Members Locations
	$.ajax({
		url : "MessageService",
		type : "POST",
		data : {
			request : 'deleteMessage',
			ids : JSON.stringify(selectedCheckBox)
		},
		success : function(data) {
			console.log(data);

		}
	});

}
// Pass the checkbox name to the function
function getCheckedBoxes(chkboxName) {
	var checkboxes = document.getElementsByName(chkboxName);
	var checkboxesChecked = [];
	// loop over them all
	for (var i = 0; i < checkboxes.length; i++) {
		// And stick the checked ones onto an array...
		if (checkboxes[i].checked) {
			checkboxesChecked.push(checkboxes[i]);
		}
	}
	// Return the array if it is non-empty, or null
	return checkboxesChecked.length > 0 ? checkboxesChecked : null;
}
