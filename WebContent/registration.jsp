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
<!-- favicon -->
<link rel="icon" href="images/favicon.jpg" type="images/jpg"
	sizes="16x16">

<!-- Jquery For Google MAP -->
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>

<!-- Font Awesome Icons -->
<link
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css"
	rel="stylesheet" type="text/css" />

<!-- Ionicons -->
<link href="css/ionicons/ionicons.min.css" rel="stylesheet"
	type="text/css" />

<!-- Bootstrap Core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">


<!-- Jquery Modal CSS -->
<link href="plugins/jqueryModel/jquery.modal.css" type="text/css"
	rel="stylesheet" />
<link href="plugins/jqueryModel/jquery.modal.theme-xenon.css"
	type="text/css" rel="stylesheet" />
<link href="plugins/jqueryModel/jquery.modal.theme-atlant.css"
	type="text/css" rel="stylesheet" />

<!-- Jquery Modal -->
<script type="text/javascript" src="plugins/jqueryModel/jquery.modal.js"></script>
<!-- iCheck -->
<link href="plugins/iCheck/flat/blue.css" rel="stylesheet"
	type="text/css" />
	
	<!-- Validator -->
<script type="text/javascript" src="plugins/jQuery/jquery.validate.js"></script>
<script type="text/javascript" src="plugins/validator/validator.js"></script>
<!-- Custom CSS -->
<link href="css/simple-sidebar.css" rel="stylesheet">
<link href="css/gt.css" rel="stylesheet">
<script type="text/javascript" src="js/config.js"></script>
<script type="text/javascript" src="js/utility.js"></script>
<script type="text/javascript" src="js/push_message.js"></script>
<title>Tracker</title>
</head>
<body>
	<div>
		<!-- This div used for response messages -->
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
	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- Main content -->
		<section class="message-content">
			<div class="row">
				<!-- /.col -->
				<div class="col-md-9"
					style="box-shadow: 1px 1px 1px 1px; border-radius: 5px;">
					<!-- Member Singup Modal  Start-->
						
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

				<!-- Message Modal Start -->
				</div>
				<!-- /.col -->
			</div>
			<!-- Div Row End -->
		</section>
		<!-- /.content -->
	</div>
	<!-- /.content-wrapper -->
	<!-- Slimscroll -->
	<script src="plugins/slimScroll/jquery.slimscroll.min.js"
		type="text/javascript"></script>
	<!-- FastClick -->
	<script src='plugins/fastclick/fastclick.min.js'></script>
	<!-- iCheck -->
	<script src="plugins/iCheck/icheck.min.js" type="text/javascript"></script>
	<script>
      $(function () {
        //Enable iCheck plugin for checkboxes
        //iCheck for checkbox and radio inputs
        $('.mailbox-messages input[type="checkbox"]').iCheck({
          checkboxClass: 'icheckbox_flat-blue',
          radioClass: 'iradio_flat-blue'
        });

        //Enable check and uncheck all functionality
        $(".checkbox-toggle").click(function () {
          var clicks = $(this).data('clicks');
          if (clicks) {
            //Uncheck all checkboxes
            $(".mailbox-messages input[type='checkbox']").iCheck("uncheck");
            $(".fa", this).removeClass("fa-check-square-o").addClass('fa-square-o');
          } else {
            //Check all checkboxes
            $(".mailbox-messages input[type='checkbox']").iCheck("check");
            $(".fa", this).removeClass("fa-square-o").addClass('fa-check-square-o');
          }          
          $(this).data("clicks", !clicks);
        });


      });
    </script>
</body>
</html>