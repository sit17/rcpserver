package com.i5i58.base.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class Helper {

	public static boolean StringIsEmptyOrNull(String str) {
		if (str != null && !str.isEmpty()) {
			return false;
		}
		return true;
	}

	public static String addMask(String orgStr, char mask, int startCount, int endCount) {
		if (!StringIsEmptyOrNull(orgStr) && orgStr.length() > startCount + endCount) {
			StringBuilder sb = new StringBuilder();
			sb.append(orgStr.substring(0, startCount));
			for (int i = 0; i < orgStr.length() - startCount - endCount; i++) {
				sb.append(mask);
			}
			sb.append(orgStr.substring(orgStr.length() - endCount));
			return sb.toString();
		}
		return orgStr;
	}

	public static String addEmailMask(String orgEmail, char mask, int startCount, int endCount) {
		if (!StringIsEmptyOrNull(orgEmail)) {
			int atIndex = orgEmail.indexOf("@");
			if (atIndex > startCount + endCount) {
				String orgStr = orgEmail.substring(0, atIndex);
				String mailStr = orgEmail.substring(atIndex);
				StringBuilder sb = new StringBuilder();
				sb.append(orgStr.substring(0, startCount));
				for (int i = 0; i < orgStr.length() - startCount - endCount; i++) {
					sb.append(mask);
				}
				sb.append(orgStr.substring(orgStr.length() - endCount));
				sb.append(mailStr);
				return sb.toString();
			}
		}
		return orgEmail;
	}

	public static String createUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static String getMd5(String text) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(text.getBytes());
		String res = new BigInteger(1, md.digest()).toString(16).toUpperCase();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 32 - res.length(); ++i) {
			builder.append("0");
		}
		return builder.append(res).toString();
	}
}
