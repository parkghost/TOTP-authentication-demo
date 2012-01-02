package me.brandonc.security.totp.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;

/**
 * @see http
 *      ://thegreyblog.blogspot.com/2011/12/google-authenticator-using-it-in-
 *      your.html
 */
public class TOTPUtils {

	private static final int SECRET_SIZE = 10;
	private static final Random rand = new Random();

	public static String generateSecret() {

		// Allocating the buffer
		byte[] buffer = new byte[SECRET_SIZE];

		// Filling the buffer with random numbers.
		rand.nextBytes(buffer);

		// Getting the key and converting it to Base32
		Base32 codec = new Base32();
		byte[] secretKey = Arrays.copyOf(buffer, SECRET_SIZE);
		byte[] bEncodedKey = codec.encode(secretKey);
		String encodedKey = new String(bEncodedKey);
		return encodedKey;
	}

	public static boolean checkCode(String secret, long code, long t, int window) throws NoSuchAlgorithmException, InvalidKeyException {
		Base32 codec = new Base32();
		byte[] decodedKey = codec.decode(secret);

		// Window is used to check codes generated in the near past.
		// You can use this value to tune how far you're willing to go.

		for (int i = -window; i <= window; ++i) {
			long hash = verifyCode(decodedKey, t + i);

			if (hash == code) {
				return true;
			}
		}

		// The validation code is invalid.
		return false;
	}

	public static int verifyCode(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
		byte[] data = new byte[8];
		long value = t;
		for (int i = 8; i-- > 0; value >>>= 8) {
			data[i] = (byte) value;
		}

		SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signKey);
		byte[] hash = mac.doFinal(data);

		int offset = hash[20 - 1] & 0xF;

		// We're using a long because Java hasn't got unsigned int.
		long truncatedHash = 0;
		for (int i = 0; i < 4; ++i) {
			truncatedHash <<= 8;
			// We are dealing with signed bytes:
			// we just keep the first byte.
			truncatedHash |= (hash[offset + i] & 0xFF);
		}

		truncatedHash &= 0x7FFFFFFF;
		truncatedHash %= 1000000;

		return (int) truncatedHash;
	}

}