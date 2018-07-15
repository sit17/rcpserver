package com.i5i58.data.anchor;

import com.i5i58.util.KVCode;

public enum WithdrawCashStatus implements KVCode {
	REQUEST("request", 0), HANDLE("Handle", 1), DONE("done", 2), ERROR("error", 3);

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

	private WithdrawCashStatus(String code, int value) {
		this.code = code;
		this.value = value;
	}
}
