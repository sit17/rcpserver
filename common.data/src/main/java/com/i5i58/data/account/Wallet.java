package com.i5i58.data.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "Wallets", uniqueConstraints = { @UniqueConstraint(columnNames = { "accId" }) })
@JsonInclude(Include.NON_DEFAULT)
public class Wallet implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2822513247264229737L;

	@Id
	@Column(nullable = false, length = 32)
	private String accId = ""; // 唯一主键ID

	@Column(nullable = false)
	private long iGold;
	private long diamond;
	private long redDiamond;
	private String ticket = "";
	private long commission;
	private long giftTicket;

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public long getiGold() {
		return iGold;
	}

	public void setiGold(long iGold) {
		this.iGold = iGold;
	}

	public long getDiamond() {
		return diamond;
	}

	public void setDiamond(long diamond) {
		this.diamond = diamond;
	}

	public long getRedDiamond() {
		return redDiamond;
	}

	public void setRedDiamond(long redDiamond) {
		this.redDiamond = redDiamond;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public long getCommission() {
		return commission;
	}

	public void setCommission(long commission) {
		this.commission = commission;
	}

	public long getGiftTicket() {
		return giftTicket;
	}

	public void setGiftTicket(long giftTicket) {
		this.giftTicket = giftTicket;
	}

}
