package com.i5i58.data.superAdmin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "SuperAdminRecord")
@JsonInclude(Include.NON_DEFAULT)
public class SuperAdminRecord implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -497350993290837180L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long recordId;

	@Column(nullable = false, length = 32)
	private String accId;

	@Column(nullable = false, length = 10)
	private String realName;

	@Column(nullable = false)
	private long operateTime;

	@Column(nullable = false, length = 512)
	private String recordContent;

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public long getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(long operateTime) {
		this.operateTime = operateTime;
	}

	public String getRecordContent() {
		return recordContent;
	}

	public void setRecordContent(String recordContent) {
		this.recordContent = recordContent;
	}
	
	
}
