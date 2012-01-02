$(function() {
	$("#createAccount").click(function() {
		var name = $("#name").val();

		$.post('account', {
			name : name,
			password : $("#password").val()
		}, function(response) {
			if (response.status == "0") {
				showMessage("Your account has been created successfully", "success");
				$("#qrcodeImg").attr("src", "account/" + name + "/qrcode");
				$('#qrcode').modal({
					backdrop : true,
					keyboard : true,
					show : true
				});
			} else {
				showMessage(response.message, "error");
			}
		});
	});

	$("#verifyCode").click(function() {
		$.post('account/' + $("#vName").val() + '/verify', {
			password : $("#vPassword").val(),
			code : $("#vCode").val()
		}, function(response) {
			if (response.status == "0") {
				showMessage("Verification passed", "success");
			} else {
				showMessage(response.message, "info");
			}
		});
	});

	function showMessage(message, type) {
		var element = '<div class="alert-message ' + type
				+ '"><a class="close" href="#">x</a><p>' + message
				+ '</p></div>';
		$("#message").append($(element));
		$("#message").alert();
	}

	$(document).ajaxError(function() {
		alert("Some unknown error happened");
	});

});