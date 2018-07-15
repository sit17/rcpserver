package com.i5i58.data.channel;

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
@Table(name = "TakeHeartRecords", uniqueConstraints = {@UniqueConstraint(columnNames="id")})
@JsonInclude(Include.NON_DEFAULT)
public class TakeHeartRecord implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6618282319266741366L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private long id;

	private String accId = "";
	private long takeDate;
	
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
	public long getTakeDate() {
		return takeDate;
	}
	public void setTakeDate(long takeDate) {
		this.takeDate = takeDate;
	}
}
