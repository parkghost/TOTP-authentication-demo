<%@ page session="false"%><!DOCTYPE html>
<html lang="en">
<head>
<link rel="shortcut icon" type="image/x-icon" href="static/favicon.ico">
<link rel="stylesheet" type="text/css" href="static/css/bootstrap.min.css">
<title>TOTP authentication Demo</title>
</head>
<body>
	<div class="container">
		<a href="https://github.com/parkghost/TOTP-authentication-demo"><img style="position: absolute; top: 0; right: 0; border: 0;" src="https://a248.e.akamai.net/assets.github.com/img/abad93f42020b733148435e2cd92ce15c542d320/687474703a2f2f73332e616d617a6f6e6177732e636f6d2f6769746875622f726962626f6e732f666f726b6d655f72696768745f677265656e5f3030373230302e706e67" alt="Fork me on GitHub"></a>
		<div class="row">
			<div class="span16">
				<h1>TOTP authentication Demo</h1>
			</div>
			<div class="span16">
				<div id="message"></div>
			</div>

			<div class="row">
				<div class="span8">
					<section>
						<div class="page-header">
							<h2>Create Account</h2>
						</div>

						<form>
							<fieldset>
								<div class="clearfix">
									<label for="name">name</label>
									<div class="input">
										<input id="name" type="text">
									</div>
								</div>
								<div class="clearfix">
									<label for="password">password</label>
									<div class="input">
										<input id="password" type="text">
									</div>
								</div>

							</fieldset>

						</form>
					</section>
				</div>
				<div class="span8">
					<section>
						<div class="page-header">
							<h2>Verify Account</h2>
						</div>

						<form>
							<fieldset>
								<div class="clearfix">
									<label for="vName">name</label>
									<div class="input">
										<input id="vName" type="text">
									</div>
								</div>
								<div class="clearfix">
									<label for="vPassword">password</label>
									<div class="input">
										<input id="vPassword" type="text">
									</div>
								</div>
								<div class="clearfix">
									<label for="vCode">code</label>
									<div class="input">
										<input id="vCode" type="number">
									</div>
								</div>
							</fieldset>
						</form>
					</section>
				</div>
			</div>
			<div class="row">
				<div class="span8">
					<div class="actions">
						<button id="createAccount" class="btn">Submit</button>
					</div>
				</div>
				<div class="span8">
					<div class="actions">
						<button id="verifyCode" class="btn">Verify</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="qrcode" class="modal hide fade" style="display: none;">
		<div class="modal-header">
			<a class="close" href="#">x</a>
			<h3>QR Code</h3>
		</div>
		<div class="modal-body">
			<img id="qrcodeImg">
		</div>
		<div class="modal-footer">
			<span class="label notice">notice</span> <span> use <a
				href="http://support.google.com/accounts/bin/answer.py?hl=en&answer=1066447"
				target="_blank">Google Authenticator</a> to keep your secret key
			</span>
		</div>

	</div>
<script type="text/javascript" src="static/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="static/js/bootstrap-alerts.js"></script>
<script type="text/javascript" src="static/js/bootstrap-modal.js"></script>
<script type="text/javascript" src="static/js/app.js"></script>
</body>
</html>
