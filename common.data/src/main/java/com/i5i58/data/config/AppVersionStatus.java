package com.i5i58.data.config;

import com.i5i58.util.KVCode;

public enum AppVersionStatus implements KVCode {
	DISCARD("discard", 0), OBSOLETE("Obsolete", 1), CURRENT("current", 2), EXAMINE("examine", 3), TEST("test", 4);

	private String code;
	private int value;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	private AppVersionStatus(String code, int value) {
		this.code = code;
		this.value = value;
	}
}
