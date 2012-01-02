package me.brandonc.security.totp.util;

public class KeyFormatUtils {

	public static String getGlobalIdKey() {
		return "account:global:id";
	}

	public static String getAccountDataKey(String id) {
		return "account:" + id;
	}

	public static String getAccountNameIndexKey(String name) {
		return "account:name:" + name;
	}

	public static String getQRcodeTickeyKey(String name) {
		return "qrcode:name:" + name + ":ticket";
	}
}
