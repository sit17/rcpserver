package com.i5i58.data.anchor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "WithdrawCashs", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
@JsonInclude(Include.NON_DEFAULT)
public class WithdrawCash implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8466476710362654820L;

	@Id
	@Column(nullable = false, length = 32)
	private String id = ""; // 唯一主键ID

	@Column(nullable = false, length = 32)
	private String accId = ""; // 唯一主键ID

	@Column(nullable = false, length = 18)
	private long amount = 0;

	@Column(nullable = false, length = 18)
	private long collectTime = 0;

	@Column(nullable = false, length = 15)
	private String clientIp = "";

	@Column(nullable = false, length = 1)
	private int status = 0;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public long getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(long collectTime) {
		this.collectTime = collectTime;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
