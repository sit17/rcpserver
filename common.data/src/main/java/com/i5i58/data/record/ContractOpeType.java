package com.i5i58.data.record;

public enum ContractOpeType {
	Invalie						(0, "无效值"),
	AgreeSign					(1, "签订合约"),
	AgreeCancel					(2, "解除合约"),
	RefuseSign					(3, "拒绝签约请求"),
	RefuseCancel				(4, "拒绝解约请求"),
	ForceCancelContractByGroup			(5, "公会强制解约请求"),
	ForceCancelContractByAnchor			(6, "主播强制解约请求");
	
	private int value;
	private String desc;
	
	private ContractOpeType (int value, String desc){
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
