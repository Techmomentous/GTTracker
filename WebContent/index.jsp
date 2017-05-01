<%@page import="com.lt.logs.LogUtility,com.google.gson.JsonObject"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>Tracker</title>


<!-- favicon -->
<link rel="icon" href="images/favicon.jpg" type="images/jpg"
	sizes="16x16">
<!-- Jquery For Google MAP -->
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<!-- Google Maps -->
<link
	href="https://google-developers.appspot.com/maps/documentation/javascript/examples/default.css"
	rel="stylesheet">

<!-- Google Maps API Authentication Key -->
<script
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAF6yMHKAXEqd7DcU4-hTVLIGd_mkyt34A&libraries=places"></script>


<!-- Font Awesome Icons -->
<link
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css"
	rel="stylesheet" type="text/css" />

<!-- Ionicons -->
<link href="css/ionicons/ionicons.min.css" rel="stylesheet"
	type="text/css" />

<!-- Bootstrap Core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">

<!-- JQuery Bootstrap File Dialog -->
<link
	href="http://hayageek.github.io/jQuery-Upload-File/4.0.1/uploadfile.css"
	rel="stylesheet">
<script
	src="http://hayageek.github.io/jQuery-Upload-File/4.0.1/jquery.uploadfile.min.js"></script>

<!-- iCheck -->
<link href="plugins/iCheck/flat/blue.css" rel="stylesheet"
	type="text/css" />

<!-- Jquery Modal CSS -->
<link href="plugins/jqueryModel/jquery.modal.css" type="text/css"
	rel="stylesheet" />
<link href="plugins/jqueryModel/jquery.modal.theme-xenon.css"
	type="text/css" rel="stylesheet" />
<link href="plugins/jqueryModel/jquery.modal.theme-atlant.css"
	type="text/css" rel="stylesheet" />

<!-- Jquery Modal -->
<script type="text/javascript" src="plugins/jqueryModel/jquery.modal.js"></script>

<!-- Custom CSS -->
<link href="css/simple-sidebar.css" rel="stylesheet">
<link href="css/gt.css" rel="stylesheet">
<style>
.sidebar-menu li>a>.pull-right {
	position: absolute;
	top: 50%;
	right: 10px;
	margin-top: -7px;
}

.sidebar-menu>li {
	position: relative;
	margin: 0;
	padding: 0;
}

.sidebar-menu>li.active>a {
	color: #fff;
	background: #1e282c;
	border-left-color: #3c8dbc;
}

.sidebar-menu>li>a>.fa,.sidebar-menu>li>a>.glyphicon,.sidebar-menu>li>a>.ion
	{
	width: 20px;
}

.fa {
	display: inline-block;
	font: normal normal normal 14px/1 FontAwesome;
	font-size: inherit;
	text-rendering: auto;
	-webkit-font-smoothing: antialiased;
	-moz-osx-font-smoothing: grayscale;
	transform: translate(0, 0);
}

.sidebar-menu>li .label,.sidebar-menu>li .badge {
	margin-top: 3px;
	margin-right: 5px;
}

.bg-red,.callout.callout-danger,.alert-danger,.alert-error,.label-danger,.modal-danger .modal-body
	{
	background-color: #dd4b39 !important;
}
</style>
<!-- Validator -->
<script type="text/javascript" src="plugins/jQuery/jquery.validate.js"></script>
<script type="text/javascript" src="plugins/validator/validator.js"></script>

<!-- Custom JS -->
<script type="text/javascript" src="js/config.js"></script>
<script type="text/javascript" src="js/utility.js"></script>
<script type="text/javascript" src="js/custom-marker.js"></script>
<script type="text/javascript" src="js/push_message.js"></script>
<script type="text/javascript" src="js/tracker.js"></script>
<script>
	var markers = new Array();
	var map;
	//create empty LatLngBounds object
	var bounds = new google.maps.LatLngBounds();
	var infowindow = new google.maps.InfoWindow();

	// Cretes the map
	function initialize() {
		
		//Start Progress bar
		toggleProgressBar();
		//Hide menu
		$("#wrapper").toggleClass("toggled");
		map = new google.maps.Map(document.getElementById('map'), {
			zoom : 15,
			center : new google.maps.LatLng(options.homeLat, options.homeLong),
			mapTypeId : google.maps.MapTypeId.ROADMAP
		});
		setTimeout(function(){
			//Update Progress bar value
			updateProgressBar("Getting Members Locations", "20%");
		}, 20000);	
		
		//Fetch Members Locations
		$.ajax({
			url : "LocationService",
			type : "GET",
			data : {
				request : 'getMembers'
			},
			success : function(data) {
				setTimeout(function(){
				//Update Progress bar value
				updateProgressBar("Plotting Members Locations On Map", "45%");
				addMembers(JSON.parse(data));
					
				}, 20000);
					
			}
		});

		var searchBox = new google.maps.places.SearchBox(document
				.getElementById("mapsearch"));

		google.maps.event.addListener(searchBox, 'places_changed', function() {

			$('#searchAPlaceModal').modal('hide');
			var places = searchBox.getPlaces();

			if (places.length == 0) {
				return;
			}
			//Update Progress bar Value	
			updateProgressBar("Initializing Google Maps Search", "60%");
			// For each place, get the icon, place name, and location.
			var pointers = [];
			var bounds = new google.maps.LatLngBounds();
			for (var i = 0, place; place = places[i]; i++) {
				var image = {
					url : place.icon,
					size : new google.maps.Size(71, 71),
					origin : new google.maps.Point(0, 0),
					anchor : new google.maps.Point(17, 34),
					scaledSize : new google.maps.Size(25, 25)
				};

				// Create a marker for each place.
				var pointer = new google.maps.Marker({
					map : map,
					icon : image,
					title : place.name,
					position : place.geometry.location,
					draggable : true,
					icon : "images/go-down.png"
				});

				pointers.push(pointer);
				bounds.extend(place.geometry.location);
			}
			
			map.fitBounds(bounds);
			setTimeout(function() {
				clearSearchBox();
			}, 1000);

		});
	}
	
	
</script>
</head>

<body class="background">
	<div>
		<%
			if (request.getParameter("responseMessage") != null) {
		%>
		<script>
				showMessage('<%=(String) request.getParameter("responseMessage")%>');
		</script>
		<%
			}
		%>
	</div>
	<div id="wrapper">

		<!-- Sidebar -->
		<div id="sidebar-wrapper">
			<ul class="sidebar-nav">

				<li><a href="javascript:openFindMemberModal();">Locate
						Member</a></li>
				<li><a href="javascript:addMember();">Add New Member</a></li>
				<li><a href="javascript:sendMessageToMember();">Send
						Message To Member</a></li>

				<li><a href="javascript:viewNotifications();">View
						Notifications<small class="label pull-right bg-red"
						id="messageCount" style="margin-top: 13px;">0</small>
				</a></li>

				<li><a href="javascript:searchPlace();">Search A Place</a></li>
			</ul>
		</div>
		<!-- /#sidebar-wrapper -->

		<!-- Page Content -->
		<div id="page-content-wrapper">
			<!-- Progress Bar Start-->
			<div class="modal fade" id="progressDialog" tabindex="-1"
				role="dialog" aria-labelledby="myModalLabel" style="display: none"
				data-backdrop="static" data-keyboard="false">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<h4 class="modal-title">Initializing GTracker</h4>
						</div>
						<div class="modal-body" id="myModalText">
							<div class="progress">
								<div
									class="progress-bar progress-bar-info progress-bar-striped active"
									role="progressbar" aria-valuenow="45" aria-valuemin="0"
									aria-valuemax="100" style="width: 10%" id="pBar">
									<span class="sr-only" id="progressValue">10% Complete</span>
								</div>
							</div>
						</div>
						<div class="modal-footer">
							<span class="label label-info" id="progressText">Please
								Wait...</span>
						</div>
					</div>
				</div>
			</div>

			<!-- Progress Bar End -->
			<!-- Meesage Primary Start -->
			<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
				aria-labelledby="myModalLabel" style="display: none">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<h4 class="modal-title" id="primaryModalLabel">GTracker</h4>
						</div>
						<div class="modal-body" id="primaryModalText"></div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default"
								data-dismiss="modal">Close</button>
						</div>
					</div>
				</div>
			</div>
			<!-- Meesage Primary End -->
			<!-- Meesage Waring Start -->
			<div id="warning-modal" style="display: none">
				<div class="modal modal-danger">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<h4 class="modal-title" id="modelTitle">Modal Warning</h4>
							</div>
							<div class="modal-body">
								<p id="modelText"></p>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-outline pull-left"
									data-dismiss="modal">Close</button>
								<button type="button" class="btn btn-outline">Save
									changes</button>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- Modal Container Fluid Div-->
			<div class="container-fluid">
				<!-- Search a place modal-->
				<div id="searchAPlaceModal" class="modal fade">
					<div class="modal-dialog" id="searchDialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-hidden="true">&times;</button>
								<h4 class="modal-title">Search A Place</h4>
							</div>
							<div class="modal-body">
								<!-- text input -->
								<div class="form-group">
									<label>Enter A Place Name</label> <input type="text"
										class="form-control" id="mapsearch" placeholder="Enter ..." />
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- Find Member Modal Start -->
				<div id="modalfind" class="modal modal-info">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-hidden="true">&times;</button>
								<h4 class="modal-title">Locate Member on Map</h4>
							</div>
							<div class="modal-body">
								<!-- text input -->
								<div class="form-group" style="text-align: center; width: 100%;">
									<div class="btn-group">
										<!-- Subject Drop Down -->
										<label>Members</label> <select class="form-control select2"
											id="membersList">
										</select>
										<button class="btn btn-block btn-primary btn-sm"
											style="margin-top: 15px;" onclick="locateMember()">Locate</button>
									</div>
								</div>
							</div>

						</div>
					</div>
				</div>
				<!-- Find Member Modal End -->
				<!-- Add Member Pic Modal Start -->
				<div id="addMemberPicModal" class="modal modal-info">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-hidden="true">&times;</button>
								<h4 class="modal-title">Upload A Marker Picture</h4>
							</div>
							<div class="modal-body">
								<!-- text input -->
								<div class="form-group" style="text-align: center; width: 100%;">
									<div id="fileuploader">Upload</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- Add Member PIC Modal End -->
				<!-- Send Message Modal Start-->
				<div id="sendMessageModal" class="modal modal-info">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-hidden="true">&times;</button>
								<h4 class="modal-title">Send Message</h4>
							</div>
							<div class="modal-body">
								<div class="margin">
									<div class="btn-group">

										<p class="text-green" id="messagesType"></p>
										<div id="memeberSelection" style="display: none">
											<!-- Member Selection UL LI -->
											<div class="btn-group">
												<button type="button" class="btn btn-info">Select
													Member</button>
												<button type="button" class="btn btn-info dropdown-toggle"
													data-toggle="dropdown">
													<span class="caret"></span> <span class="sr-only">Toggle
														Dropdown</span>
												</button>
												<ul class="dropdown-menu" role="menu" id="memberSelList">

												</ul>
											</div>
										</div>
										<!-- Message Type UL LI -->
										<button type="button" class="btn btn-default"
											id="actionButton">Select Message Type</button>
										<button type="button" class="btn btn-default dropdown-toggle"
											data-toggle="dropdown">
											<span class="caret"></span> <span class="sr-only">Select
												Message Type</span>
										</button>

										<ul class="dropdown-menu" role="menu">
											<li><a
												href="javascript:setMessageType('101','It`s Urgent')">It`s
													Urgent</a></li>
											<li><a
												href="javascript:setMessageType('102','Call Notice')">Call
													Notice</a></li>
											<li><a href="javascript:setMessageType('103','Requset')">Request</a></li>
											<li><a href="javascript:setMessageType('104','ASAP')">ASAP</a></li>
										</ul>
										<div class="input-group margin">
											<input type="text" id="messageText" class="form-control">
											<span class="input-group-btn">
												<button class="btn btn-info btn-flat"
													onclick="javascript:sendMessage()" type="button">Go!</button>
											</span>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- 				</div> -->
				<!-- Send Message Modal End -->
				<!-- Member Singup Modal  Start-->
				<div id="addMemberModal" class="modal modal-info">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-hidden="true">&times;</button>
								<h4 class="modal-title">New Member Submission</h4>
							</div>
							<div class="modal-body">
								<!-- text input -->
								<div class="form-group">
									<form name="memberRegistrationForm" id="memberRegistrationForm"
										data-toggle="validator" method="post" role="form"
										action="MembersService?request=add">
										<!-- Name -->
										<div class="form-group has-feedback">
											<input type="text" class="form-control"
												placeholder="Full name" name="fullName" id="inputFullName"
												data-error="In valid Name" required /> <span
												class="glyphicon glyphicon-user form-control-feedback"></span>
											<div class="help-block with-errors"></div>
										</div>
										<!-- Age -->
										<div class="form-group">
											<input class="form-control" rows="1" placeholder="Enter Age"
												name="age" required></input>
											<div class="help-block with-errors"></div>
										</div>
										<!-- Phone -->
										<div class="form-group has-feedback">
											<input type="text" class="form-control" placeholder="Phone"
												name="phone" required /> <span
												class="glyphicon glyphicon-phone-alt form-control-feedback"></span>
											<div class="help-block with-errors"></div>
										</div>
										<!-- Email -->
										<div class="form-group has-feedback">
											<input type="email" class="form-control" placeholder="Email"
												name="email" id="inputEmail"
												data-error="that email address is invalid" required /> <span
												class="glyphicon glyphicon-envelope form-control-feedback"></span>
											<div class="help-block with-errors"></div>
										</div>
										<!-- Address -->
										<div class="form-group">
											<textarea class="form-control" rows="3"
												placeholder="Enter Address" name="address" required></textarea>
											<div class="help-block with-errors"></div>
										</div>
										<!-- Latituted -->
										<div class="form-group has-feedback">
											<input type="text" class="form-control"
												placeholder="Home Latituted" name="startlatitude" required />
											<span
												class="glyphicon glyphicon-map-marker form-control-feedback"></span>
											<div class="help-block with-errors"></div>
										</div>
										<!-- Longituted -->
										<div class="form-group has-feedback">
											<input type="text" class="form-control"
												placeholder="Home Longituted" name="startlongitude" required />
											<span
												class="glyphicon glyphicon-map-marker form-control-feedback"></span>
											<div class="help-block with-errors"></div>
										</div>

										<div class="row">

											<!-- /.col -->
											<div class="col-xs-4">
												<button type="submit"
													class="btn btn-primary btn-block btn-flat">Add
													Member</button>
											</div>
											<!-- /.col -->
										</div>
									</form>
								</div>
							</div>

						</div>
					</div>
				</div>
				<!-- Message Modal Start -->
				<div id="messageModal" class="modal modal-info">
					<div class="modal-dialog" style="width: 100%;">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-hidden="true">&times;</button>
								<h4 class="modal-title">Push Notification</h4>
							</div>
							<div class="modal-body">
								<!-- Main content -->
								<div class="row" style="width: 100%;">
									<!-- /.col -->
									<div class="col-md-9"
										style="box-shadow: 1px 1px 1px 1px; border-radius: 5px; width: 100% !important;">
										<div class="box box-primary">

											<!-- /.box-header -->
											<div class="box-body no-padding">
												<div class="mailbox-controls">
													<!-- Check all button -->
													<button class="btn btn-default btn-sm checkbox-toggle">
														<i class="fa fa-square-o"></i>
													</button>
													<div class="btn-group">
														<button class="btn btn-default btn-sm"
															onclick="javascript:deleteMessage()">
															<i class="fa fa-trash-o"></i>
														</button>
													</div>
												</div>
												<div class="table-responsive mailbox-messages">
													<table class="table table-hover table-striped">
														<thead>
															<tr>
																<th>Action</th>
																<th>Message Type</th>
																<th>Member Name</th>
																<th>Message</th>
																<th></th>
																<th>Date / Time</th>
															</tr>
														</thead>
														<tbody id="messageBox">

														</tbody>
													</table>
												</div>
											</div>
										</div>
									</div>
								</div>
								<!-- Div Row End -->
							</div>
						</div>
					</div>
				</div>
				<!-- Message Modal End -->
				<!-- Map Div -->
				<div class="row">
					<div class="col-lg-12">
						<!-- Map Col Div -->
						<h1 class="title">Your Family Members Are Here</h1>
						<a href="#menu-toggle" class="btn btn-default" id="menu-toggle"
							style="background-image: url('images/menu.png'); background-repeat: no-repeat; background-size: 35px 35px; text-indent: 25px; color: #2198FF;">
							Menu</a>
						<div id="map"
							style="width: 100%; height: 750px; overflow: hidden; transform: translateZ(0px); box-shadow: 1px 1px 1px 1px; border-radius: 10px;"></div>

					</div>
					<!-- Map Col Div End -->
				</div>
				<!-- Map Div End-->
			</div>
			<!-- Modal Container Fluid Div End-->
		</div>
		<!-- Page Content End -->
	</div>
	<!-- /#page-content-wrapper End -->

	<div id="data" style="display: none"></div>
	<!-- /#wrapper -->

	<!-- Bootstrap Core JavaScript -->
	<script src="js/bootstrap.min.js"></script>
	<!-- Bootstrap File Input -->
	<link href="plugins/fileinput/fileinput.css" media="all"
		rel="stylesheet" type="text/css" />
	<script src="plugins/fileinput/fileinput.min.js" type="text/javascript"></script>
	<!-- Slimscroll -->
	<script src="plugins/slimScroll/jquery.slimscroll.min.js"
		type="text/javascript"></script>
	<!-- FastClick -->
	<script src='plugins/fastclick/fastclick.min.js'></script>
	<!-- iCheck -->
	<script src="plugins/iCheck/icheck.min.js" type="text/javascript"></script>

	<!-- Menu Toggle Script -->
	<script>
		window.onload = function() {
			
			//We only initalize if page is loaded first time not for every time when 
			//a response message reload the page
			if (location.href.lastIndexOf('responseMessage') == -1) {
				initialize();
			}
		};
		$("#menu-toggle").click(function(e) {
			e.preventDefault();
			$("#wrapper").toggleClass("toggled");
		});
		$(function() {
			//Enable iCheck plugin for checkboxes
			//iCheck for checkbox and radio inputs
			$('.mailbox-messages input[type="checkbox"]').iCheck({
				checkboxClass : 'icheckbox_flat-blue',
				radioClass : 'iradio_flat-blue'
			});

			//Enable check and uncheck all functionality
			$(".checkbox-toggle").click(
					function() {
						var clicks = $(this).data('clicks');
						if (clicks) {
							//Uncheck all checkboxes
							$(".mailbox-messages input[type='checkbox']")
									.iCheck("uncheck");
							$(".fa", this).removeClass("fa-check-square-o")
									.addClass('fa-square-o');
						} else {
							//Check all checkboxes
							$(".mailbox-messages input[type='checkbox']")
									.iCheck("check");
							$(".fa", this).removeClass("fa-square-o").addClass(
									'fa-check-square-o');
						}
						$(this).data("clicks", !clicks);
					});

		});
	</script>
</body>

</html>
