package com.i5i58.data.account;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "UserWatchRecords", uniqueConstraints = {@UniqueConstraint(columnNames="accId")})
@JsonInclude(Include.NON_DEFAULT)
public class UserWatchRecord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5890090049604225180L;

	@Id
	@Column(nullable = false, length = 32)
	private String accId="";   //唯一主键ID
	
	private long startTime;
	private long finsheTime;
	private long durationFree;		//未消耗的观看时长
	private long durationCost; //已经消耗的观看时长
	
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getFinsheTime() {
		return finsheTime;
	}
	public void setFinsheTime(long finsheTime) {
		this.finsheTime = finsheTime;
	}
	public long getDurationFree() {
		return durationFree;
	}
	public void setDurationFree(long durationFree) {
		this.durationFree = durationFree;
	}
	public long getDurationCost() {
		return durationCost;
	}
	public void setDurationCost(long durationCost) {
		this.durationCost = durationCost;
	}
}
