package com.i5i58.data.game;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
public class ExChangeDiamondResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8139738988635905950L;

	private String accId = "";

	private long exTime = 0L;

	private long orgDiamond = 0L;

	private long exDiamond = 0L;

	private long rate = 0L;

	private long orgGameGold = 0L;

	private long currGameGold = 0L;

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public long getExTime() {
		return exTime;
	}

	public void setExTime(long exTime) {
		this.exTime = exTime;
	}

	public long getOrgDiamond() {
		return orgDiamond;
	}

	public void setOrgDiamond(long orgDiamond) {
		this.orgDiamond = orgDiamond;
	}

	public long getExDiamond() {
		return exDiamond;
	}

	public void setExDiamond(long exDiamond) {
		this.exDiamond = exDiamond;
	}

	public long getRate() {
		return rate;
	}

	public void setRate(long rate) {
		this.rate = rate;
	}

	public long getOrgGameGold() {
		return orgGameGold;
	}

	public void setOrgGameGold(long orgGameGold) {
		this.orgGameGold = orgGameGold;
	}

	public long getCurrGameGold() {
		return currGameGold;
	}

	public void setCurrGameGold(long currGameGold) {
		this.currGameGold = currGameGold;
	}
}
