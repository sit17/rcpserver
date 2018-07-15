package com.i5i58.data.pay;

import com.i5i58.util.KVCode;

public enum OrderStatus implements KVCode {
	ORDER_NO_PAY("未付款", 0), ORDER_DONE_PAY("已付款待处理", 1), ORDER_COMPLETE_PAY("处理完成", 2), ORDER_CLOSE("交易关闭", 3);

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

	private OrderStatus(String code, int value) {
		this.code = code;
		this.value = value;
	}
}