package me.brandonc.security.totp.util;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordUtils {

	public static String encrypt(String password, String salt) {
		return DigestUtils.shaHex(password + salt);
	}
}
