package com.i5i58.data.wechat;

public enum AwardConfigType {
	
	GameGold     (1,"欢乐豆"),
	IGold		 (2,"虎币"),
	Diamond		 (3,"钻石"),
	Again		 (4,"继续加油"),
	NoAward		 (5,"就差一点点");
	
	private int value;
	private String desc;
	
	private AwardConfigType(int value,String desc){
		this.value=value;
		this.desc=desc;
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
