package com.i5i58.data.wechat;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "LotteryChance")
@JsonInclude(Include.NON_DEFAULT)
public class LotteryChance implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(nullable= false,length=32)
	private String accId="";
	
	@Column(nullable=false)
	private int lotteryCount=0;
	
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}

	public int getLotteryCount() {
		return lotteryCount;
	}

	public void setLotteryCount(int lotteryCount) {
		this.lotteryCount = lotteryCount;
	}
}
