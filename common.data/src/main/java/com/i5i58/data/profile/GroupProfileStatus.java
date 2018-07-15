package com.i5i58.data.profile;

import com.i5i58.util.KVCode;

public enum GroupProfileStatus implements KVCode {
	EXAMINING("审核中", 1), NORMAL("正常", 2), DISABLE("禁用", 3);

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

	private GroupProfileStatus(String code, int value) {
		this.code = code;
		this.value = value;
	}
}
