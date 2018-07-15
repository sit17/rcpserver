package com.i5i58.data.anchor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "AnchorPushRecords")
@JsonInclude(Include.NON_DEFAULT)
public class AnchorPushRecord implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(nullable = false, length = 32)
	private String accId = ""; // 唯一主键频道ID
	
	@Column(nullable = false, length = 32)
	private String cId = ""; // 唯一主键频道ID
	
	@Column(nullable = false, length = 20)
	private long openTime = 0;
	
	@Column(nullable = false, length = 20)
	private long closeTime = 0;

	@Column(nullable = false, length = 1)
	private boolean ignored = false;
	
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

	public long getOpenTime() {
		return openTime;
	}

	public void setOpenTime(long openTime) {
		this.openTime = openTime;
	}

	public long getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(long closeTime) {
		this.closeTime = closeTime;
	}

	public boolean isIgnored() {
		return ignored;
	}

	public void setIgnored(boolean ignored) {
		this.ignored = ignored;
	}
}
