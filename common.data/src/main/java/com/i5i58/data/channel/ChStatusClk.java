package com.i5i58.data.channel;

import java.io.Serializable;

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
@Table(name = "ChStatusClks", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
@JsonInclude(Include.NON_DEFAULT)
public class ChStatusClk implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column
	private String body;
	
	@Column(length=32)
	private String sign;
	
	@Column(length=32)
	private String yunXinCId = "";
	
	@Column
	private int status = 0;
	
	@Column
	private long time = 0L;
	
	@Column
	private long arrivalTime = 0L;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getYunXinCId() {
		return yunXinCId;
	}

	public void setYunXinCId(String yunXinCId) {
		this.yunXinCId = yunXinCId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(long arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
}
