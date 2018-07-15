package com.i5i58.data.group;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "ForceCancelContracts")
@JsonInclude(Include.NON_DEFAULT)
public class ForceCancelContract implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1358753665818402443L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(length = 32, nullable = false)
	private String accId;
	
	
	@Column(length = 32, nullable = false)
	private String ctId = "";
	
	@Column(nullable = false)
	private int status = 0;
	@Column(nullable = false)
	private long requestedDateTime = 0L;
	@Column(nullable = false)
	private long responsedDateTime = 0L;
	@Column(nullable = false)
	private int cancelDirection=0;
	
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCtId() {
		return ctId;
	}
	public void setCtId(String ctId) {
		this.ctId = ctId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getRequestedDateTime() {
		return requestedDateTime;
	}
	public void setRequestedDateTime(long requestedDateTime) {
		this.requestedDateTime = requestedDateTime;
	}
	public long getResponsedDateTime() {
		return responsedDateTime;
	}
	public void setResponsedDateTime(long responsedDateTime) {
		this.responsedDateTime = responsedDateTime;
	}
	public int getCancelDirection() {
		return cancelDirection;
	}
	public void setCancelDirection(int cancelDirection) {
		this.cancelDirection = cancelDirection;
	}
}
