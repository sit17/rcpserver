package com.i5i58.service.game;

import java.io.Serializable;

public class GameVipInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int  memberOrder = 0;
	private long  memberOverDate = 0;
	private long memberSwitchDate = 0L;
	private int  userRight = 0;
	
	public int getMemberOrder() {
		return memberOrder;
	}
	public void setMemberOrder(int memberOrder) {
		this.memberOrder = memberOrder;
	}
	long getMemberOverDate() {
		return memberOverDate;
	}
	public void setMemberOverDate(long memberOverDate) {
		this.memberOverDate = memberOverDate;
	}
	public long getMemberSwitchDate() {
		return memberSwitchDate;
	}
	public void setMemberSwitchDate(long memberSwitchDate) {
		this.memberSwitchDate = memberSwitchDate;
	}
	public int getUserRight() {
		return userRight;
	}
	public void setUserRight(int userRight) {
		this.userRight = userRight;
	}
}
