package com.i5i58.utils;

public class VersionUtils {

	public static int updateVersion(int version) {
		if (version >= Integer.MAX_VALUE - 100) {
			return 1;
		} else {
			return version + 1;
		}
	}
}
