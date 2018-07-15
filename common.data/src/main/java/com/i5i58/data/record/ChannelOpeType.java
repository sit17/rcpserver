package com.i5i58.data.record;

public enum ChannelOpeType {
	Invalie						(0, "无效值"),
	CreateChannel				(1, "创建频道"),
	AssignOwner					(2, "绑定主播"),
	RemoveOwner					(3, "解绑主播"),
	ChangeGroup					(4, "分组移动"),
	SuperAssignOwner			(5, "超管分配频道"),
	SuperCreateChannel			(6, "超管创建频道");
	
	private int value;
	private String desc;
	
	private ChannelOpeType (int value, String desc){
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
