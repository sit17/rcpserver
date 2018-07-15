package com.i5i58.data.anchor;

public enum AuthType {
	Rejected			(3, "认证不通过"),
	Check				(2, "正在审核"), 
	Success				(1, "认证成功");
	private int value;
	private String desc;
	
	private AuthType(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	

}
