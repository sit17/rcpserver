package com.i5i58.data.channel;

public enum ChGuardCode {
	KNIGHT_1("骑士", 1), KNIGHT_2("大骑士", 2), KNIGHT_3("圣骑士", 3);

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

	private ChGuardCode(String code, int value) {
		this.code = code;
		this.value = value;
	}
}
