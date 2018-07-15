package com.i5i58.data.record;

import com.i5i58.util.KVCode;
	/**
	 * 购买内容的类型 1:礼物｜2：VIP｜3：坐骑｜4：守护｜5：开通粉丝团
	 */
public enum GoodsType implements KVCode {


	BUY_GIFT("礼物", 1), BUY_VIP("VIP", 2), BUY_MOUNT("坐骑", 3), BUY_GUARD("守护", 4), BUY_FANSCLUBS("粉丝团", 5);

	private GoodsType(String code, int value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}

	private String code;
	private int value;

}
