package com.i5i58.data.game;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
public class ExChangeGameGoldResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1667396573680804073L;

	/**
	 * 兑换的accId
	 */
	private String accId = "";

	/**
	 * 兑换时间
	 */
	private long exTime = 0L;

	/**
	 * 原有I币
	 */
	private long orgIGold = 0L;

	/**
	 * 兑换的I币
	 */
	private long exIGold = 0L;

	/**
	 * 兑换比例，1I币=？游戏币
	 */
	private long rate = 0L;

	/**
	 * 原有游戏币
	 */
	private long orgGameGold = 0L;

	/**
	 * 当前游戏币，兑换后
	 */
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

	public long getOrgIGold() {
		return orgIGold;
	}

	public void setOrgIGold(long orgIGold) {
		this.orgIGold = orgIGold;
	}

	public long getExIGold() {
		return exIGold;
	}

	public void setExIGold(long exIGold) {
		this.exIGold = exIGold;
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
