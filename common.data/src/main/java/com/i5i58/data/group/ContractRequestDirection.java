package com.i5i58.data.group;

public enum ContractRequestDirection {
	
	Anchor(0, "主播发起"),
	TopGroup(1, "公会发起"),
	SuperAdmin(2, "超管分配");
	
	private int value;
	private String code;
	
	private ContractRequestDirection(int value, String code){
		this.value = value;
		this.code = code;
	}

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
	
	
}
