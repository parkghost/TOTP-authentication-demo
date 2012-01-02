package me.brandonc.security.totp.web.response;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.std.ToStringSerializer;

@JsonSerialize(using = ToStringSerializer.class)
public enum ApplicationStatus {
	SUCCESSED(0), FAILED(-1), EXISTED(-2);

	private int value;

	ApplicationStatus(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}

}
