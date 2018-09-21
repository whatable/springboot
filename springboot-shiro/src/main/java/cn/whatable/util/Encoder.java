package cn.whatable.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Encoder {
	private Encoder() {
	}

	public static String base64Encode(byte[] b) {
		try {
			return Base64.getEncoder().encodeToString(MessageDigest.getInstance("MD5").digest(b));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String base64Encode(String s) {
		return base64Encode(s.getBytes());
	}

	public static String base64Encode(char[] c) {
		return base64Encode(new String(c).getBytes());
	}
}
