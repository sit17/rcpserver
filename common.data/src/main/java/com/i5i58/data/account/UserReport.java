package com.i5i58.data.account;

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
@Table(name = "UserReport", uniqueConstraints = {@UniqueConstraint(columnNames="id")})
@JsonInclude(Include.NON_DEFAULT)
public class UserReport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2407181582740701040L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private int id;   //唯一主键ID
	
	@Column(nullable = false)
	private long reportDate = 0;
	
	@Column(nullable = true, length = 300)
	private String reason = "";

	@Column(nullable = false)
	private String fromAccId = ""; //举报者
	
	@Column(nullable = false)
	private String reportedAccId = ""; //被举报的主播
	
	@Column(nullable = false)
	private int state = 0;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getReportDate() {
		return reportDate;
	}

	public void setReportDate(long reportDate) {
		this.reportDate = reportDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getFromAccId() {
		return fromAccId;
	}

	public void setFromAccId(String fromAccId) {
		this.fromAccId = fromAccId;
	}

	public String getReportedAccId() {
		return reportedAccId;
	}

	public void setReportedAccId(String reportedAccId) {
		this.reportedAccId = reportedAccId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
}
