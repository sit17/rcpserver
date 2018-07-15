package com.i5i58.data.game;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "LoveLinessExchangeRecord") 
@JsonInclude(Include.NON_DEFAULT)
public class LoveLinessExchangeRecord implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -33100580609862946L;
	
	@Id
	@Column(nullable=false, length=32)
	private String accId = "";
	
	private long exchanged = 0L;
	
	private long date=0L;

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public long getExchanged() {
		return exchanged;
	}

	public void setExchanged(long exchanged) {
		this.exchanged = exchanged;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}
}
