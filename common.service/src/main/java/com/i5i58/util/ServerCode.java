package com.i5i58.util;

public enum ServerCode implements KVCode {
	NO_GIFT("该礼物不存在", 1), NO_CHANNEL("该频道不存在", 2), NO_ACCOUNT("该账号不存在", 3), NO_WALLET("该钱包不存在", 4), IGOLD_NOT_ENOUGH(
			"您的虎币不足",
			5), NO_MOUNT("该坐骑不存在", 6), NO_VIEWER("该观众不存在", 7), NO_VIP("该vip不存在", 8), ILLEGAL("illegal_request", 9);

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

	private ServerCode(String code, int value) {
		this.code = code;
		this.value = value;
	}
}