package com.i5i58.data.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "MountStore", uniqueConstraints = { @UniqueConstraint(columnNames = { "id" }) })
@JsonInclude(Include.NON_DEFAULT)
public class MountStore implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2652920253933711487L;

	@Id
	@Column(nullable = false, length = 32)
	private String id = "";

	@Column(nullable = false)
	private String accId = "";

	@Column(nullable = false)
	private int mountsId;

	private long startTime;

	private long endTime;

	private String cId = "";

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

	public int getMountsId() {
		return mountsId;
	}

	public void setMountsId(int mountsId) {
		this.mountsId = mountsId;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	} 

}
