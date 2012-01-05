package me.brandonc.security.totp.util;

import static org.junit.Assert.assertEquals;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

public class TOTPUtilsTest {

	/**
	 * The test token shared secret uses the ASCII string value
	 * "12345678901234567890". With Time Step X = 30, and the Unix epoch as the
	 * initial value to count time steps, where T0 = 0, the TOTP algorithm will
	 * display the following values for specified modes and timestamps.
	 */
	@Test
	public void testVerifyCode() {
		// Seed for HMAC-SHA1 - 20 bytes
		String seed = "3132333435363738393031323334353637383930";
		long T0 = 0;
		long X = 30;
		long testTime[] = { 59L, 1111111109L, 1111111111L, 1234567890L, 2000000000L, 20000000000L };

		String steps = "0";
		try {

			for (int i = 0; i < testTime.length; i++) {
				long T = (testTime[i] - T0) / X;
				steps = Long.toHexString(T).toUpperCase();
				while (steps.length() < 16)
					steps = "0" + steps;

				assertEquals(Integer.parseInt(TOTP.generateTOTP(seed, steps, "6", "HmacSHA1")), TOTPUtils.verifyCode(Hex.decodeHex(seed.toCharArray()), T));
			}
		} catch (final Exception e) {
			System.out.println("Error : " + e);
		}

	}
}
