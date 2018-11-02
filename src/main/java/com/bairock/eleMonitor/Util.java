package com.bairock.eleMonitor;

import java.math.BigDecimal;

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
}
