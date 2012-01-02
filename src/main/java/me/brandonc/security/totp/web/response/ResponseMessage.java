package me.brandonc.security.totp.web.response;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize
public class ResponseMessage {

	public static final ResponseMessage SUCCESSED = new ResponseMessage(ApplicationStatus.SUCCESSED, "");

	private final ApplicationStatus status;
	private final String message;

	public ResponseMessage(ApplicationStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	public ApplicationStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
}
