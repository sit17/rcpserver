package com.i5i58.data.wechat;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "AwardOpeRecords")
@JsonInclude(Include.NON_DEFAULT)
public class AwardOpeRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;	
	
	@Column(length=32)
	private String accId="";
	
	@Column
	private int awardId=0;
	
	@Column
	private long amount=0L;
	
	@Column(length=32)
	private int unit=0;
	
	@Column
	private long rewardDateTime=0L;

	@Column(nullable=false)
	private long deliveryDateTime=0L;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public int getAwardId() {
		return awardId;
	}

	public void setAwardId(int awardId) {
		this.awardId = awardId;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public int getUnit() {
		return unit;
	}

	public void setUnit(int unit) {
		this.unit = unit;
	}

	public long getRewardDateTime() {
		return rewardDateTime;
	}

	public void setRewardDateTime(long rewardDateTime) {
		this.rewardDateTime = rewardDateTime;
	}

	public long getDeliveryDateTime() {
		return deliveryDateTime;
	}

	public void setDeliveryDateTime(long deliveryDateTime) {
		this.deliveryDateTime = deliveryDateTime;
	}
}
