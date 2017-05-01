/**
 * Utilit.js Project Name : GTracker Version 1.0 Date : 5 July 2015 Author :
 * Rizwan Ahmad Khan
 */

/**
 * Clear the google search box
 */
function clearSearchBox() {
	var items = document.getElementsByClassName('pac-item');
	for (var i = 0; i < items.length; i++) {
		items[i].style.display = "none";
		items[i].innerHTML = "";
	}
}

/**
 * Show Message Box
 * 
 * @param jsonResponse
 */
function showMessage(jsonResponse) {
	var messageType = JSON.parse(jsonResponse).messageType;
	var message = JSON.parse(jsonResponse).responseMessage;
	var result = JSON.parse(jsonResponse).result;
	switch (messageType) {
	case "Alert":
		showAlert(message);
		break;
	default:
		break;
	}
}

/**
 * Show Bootstrap Alert Dialog
 * 
 * @param message
 */
function showAlert(message) {
	modal({
		type : 'alert',
		title : options.appTitle,
		text : message
	});
}

/**
 * Show Bootstrap Primary Message Dialog
 * 
 * @param text
 */
function showPrimaryMessage(text) {
	var myModalLabel = document.getElementById("primaryModalLabel");
	myModalLabel.innerHTML = options.appTitle;
	var msg_primary_text = document.getElementById("primaryModalText");
	msg_primary_text.innerHTML = text;
	$('#myModal').modal('show');
}

/**
 * Show Bootstrap Warning Message Dialog
 * 
 * @param text
 */
function showWarningMessage(text) {
	var myModalLabel = document.getElementById("modelTitle");
	myModalLabel.innerHTML = options.appTitle;
	var msg_primary_text = document.getElementById("modelText");
	msg_primary_text.innerHTML = text;
	$('#warning-modal').modal('show');
}

function toggleProgressBar() {

	$('#progressDialog').modal('toggle');
}

function updateProgressBar(value, persentage) {

	var progressText = document.getElementById('progressValue');
	progressText.innerHTML = value;
	var pText = document.getElementById('progressText');
	pText.innerHTML = value;

	var persentValue = document.getElementById('pBar');
	persentValue.style.width = persentage;

	console.log("Progressbar Update : " + value + " %" + persentage);
}

function resetProgressBar() {
	document.getElementById('progressValue').innerHTML = '';
	document.getElementById('progressText').innerHTML = '';
	document.getElementById('pBar').style.width = '10%';
}