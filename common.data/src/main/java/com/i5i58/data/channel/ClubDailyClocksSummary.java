package com.i5i58.data.channel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@Entity
@Table(name = "ClubDailyClocksSummary", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
@JsonInclude(Include.NON_DEFAULT)
public class ClubDailyClocksSummary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1291966445265356732L;


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private long id;

	@Column(nullable = false, length = 32)
	private String accId = "";
	
	@Column(nullable = false, length = 32)
	private String cId = "";
	
	@Column(nullable = false)
	private long lastClockTime = 0L;

	@Column(nullable = false)
	private int curTimes = 0;
	
	@Column(nullable = false)
	private int maxTimes = 0;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public int getCurTimes() {
		return curTimes;
	}

	public void setCurTimes(int curTimes) {
		this.curTimes = curTimes;
	}

	public int getMaxTimes() {
		return maxTimes;
	}

	public void setMaxTimes(int maxTimes) {
		this.maxTimes = maxTimes;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public long getLastClockTime() {
		return lastClockTime;
	}

	public void setLastClockTime(long lastClockTime) {
		this.lastClockTime = lastClockTime;
	}

}