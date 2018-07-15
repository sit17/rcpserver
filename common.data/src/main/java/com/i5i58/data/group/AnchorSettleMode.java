package com.i5i58.data.group;

import com.i5i58.util.KVCode;

public enum AnchorSettleMode implements KVCode {
	BY_PLATFORM("by_platform", 0), BY_AUTHORIZTION("by_authorization", 1);

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

	private AnchorSettleMode(String code, int value) {
		this.code = code;
		this.value = value;
	}

	public static AnchorSettleMode getStatus(int value) {
		switch (value) {
		case 0:
			return AnchorSettleMode.BY_PLATFORM;
		case 1:
			return AnchorSettleMode.BY_AUTHORIZTION;
		}
		return null;
	}
}