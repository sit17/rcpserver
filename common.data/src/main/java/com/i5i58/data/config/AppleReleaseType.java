package com.i5i58.data.config;

import java.io.Serializable;

import com.i5i58.util.KVCode;

public enum AppleReleaseType implements KVCode, Serializable {
	APPLE("apple", 0), MINE("mine", 1);

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

	private AppleReleaseType(String code, int value) {
		this.code = code;
		this.value = value;
	}
}
