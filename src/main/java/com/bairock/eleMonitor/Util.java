package com.bairock.eleMonitor;

import java.math.BigDecimal;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Util {

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
			stringBuilder.append(" ");
		}
		return stringBuilder.toString();
	}
	
	/**
	 * accurate to the second decimal place
	 * @param f
	 * @return
	 */
	public static float scale(float f) {
		BigDecimal b = new BigDecimal(f);
		return b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
	}
	
	public static int bytesToInt(byte[] by) {
		int value = 0;
		for (int i = 0; i < by.length; i++) {
			value = value << 8 | (by[i] & 0xff);
		}
		return value;
	}
	
	public static String encodePassword(String password) {
		BCryptPasswordEncoder bcry = new BCryptPasswordEncoder();
		String hashpw = bcry.encode(password);
		return hashpw;
	}
}
