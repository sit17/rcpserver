package com.i5i58.data.record;

public enum GroupOpeType {
	Invalie						(0, "无效值"),
	CreateTopGroup				(1, "创建公会"),
	ModifyGroupName				(2, "修改公会名称"),
	ModifyGroupDesc				(3, "修改公会简介"),
	ModifyGroupNotice			(4, "修改公会公告"),
	CreateSubGroup				(5, "创建公会分组"),
	DeleteSubGroup				(6, "删除公会分组");
	
	private int value;
	private String desc;
	
	private GroupOpeType (int value, String desc){
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
