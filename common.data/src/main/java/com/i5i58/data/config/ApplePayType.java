package com.i5i58.data.config;

import java.io.Serializable;

import com.i5i58.util.KVCode;

public enum ApplePayType implements KVCode, Serializable {
	APPLE_PAY("apple_pay", 0), THIRD_PAY("third_pay", 1), APPLE_THIRD_PAY("apple_third_pay", 2);

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

	private ApplePayType(String code, int value) {
		this.code = code;
		this.value = value;
	}
}
