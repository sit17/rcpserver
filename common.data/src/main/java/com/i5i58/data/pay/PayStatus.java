package com.i5i58.data.pay;

import com.i5i58.util.KVCode;

public enum PayStatus implements KVCode {

	PAY_STATUS_UNKOWN("未知", 0), PAY_STATUS_SUCCESS("充值成功", 1), PAY_STATUS_FAILED("充值失败", 2), PAY_STATUS_CLOSED("交易关闭",
			3);

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

	private PayStatus(String code, int value) {
		this.code = code;
		this.value = value;
	}
}