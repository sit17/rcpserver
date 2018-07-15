package com.i5i58.data.vendor;

public enum VendorUserActionType {
	Register("用户注册", 1),
	Open("打开", 2),
	Download("下载", 3);
	private VendorUserActionType(String code, int value){
		this.code = code;
		this.value = value;
	}
	String code;
	int value;
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
}
