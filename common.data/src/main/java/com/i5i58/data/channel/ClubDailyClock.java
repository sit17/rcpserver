package com.i5i58.data.channel;

import java.io.Serializable;

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
@Table(name = "ClubDailyClocks", uniqueConstraints = {@UniqueConstraint(columnNames="id")})
@JsonInclude(Include.NON_DEFAULT)
public class ClubDailyClock implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3820276865773048255L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private long id;
	
	private String cId = "";
	
	private String accId = "";
	
	private long clockDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public long getClockDate() {
		return clockDate;
	}

	public void setClockDate(long clockDate) {
		this.clockDate = clockDate;
	}
	
	
}
