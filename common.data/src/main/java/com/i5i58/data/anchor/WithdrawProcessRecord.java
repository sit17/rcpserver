package com.i5i58.data.anchor;

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
@Table(name = "WithdrawProcessRecords")
@JsonInclude(Include.NON_DEFAULT)
public class WithdrawProcessRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(nullable=false, length=32)
	private String supperId;
	
	@Column(nullable=false, length=32)
	private String withdrawId;

	@Column(nullable=false)
	private int oldStatus= 0;

	@Column(nullable=false)
	private  int newStatus= 0;
	
	@Column(nullable=false)
	private long dateTime = 0L;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSupperId() {
		return supperId;
	}

	public void setSupperId(String supperId) {
		this.supperId = supperId;
	}

	public String getWithdrawId() {
		return withdrawId;
	}

	public void setWithdrawId(String withdrawId) {
		this.withdrawId = withdrawId;
	}

	public int getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(int oldStatus) {
		this.oldStatus = oldStatus;
	}

	public int getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(int newStatus) {
		this.newStatus = newStatus;
	}

	public long getDateTime() {
		return dateTime;
	}

	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}
}
