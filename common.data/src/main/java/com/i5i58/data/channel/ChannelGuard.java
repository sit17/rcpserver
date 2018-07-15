package com.i5i58.data.channel;

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
@Table(name = "ChannelGuards", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
@JsonInclude(Include.NON_DEFAULT)
public class ChannelGuard implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4088853587085202894L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private long id;

	@Column(nullable = false, length=32)
	private String cId = "";
	@Column(nullable = false, length=32)
	private String accId = "";
	
	private int guardLevel=0;
	private long startLine=0L;
	private long deadLine=0L;
	private long buyTime=0L;
	
	private int mountsId=0;
	private String mountsName = "";

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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

	public int getGuardLevel() {
		return guardLevel;
	}

	public void setGuardLevel(int guardLevel) {
		this.guardLevel = guardLevel;
	}

	public long getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(long deadLine) {
		this.deadLine = deadLine;
	}

	public long getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(long buyTime) {
		this.buyTime = buyTime;
	}

	public int getMountsId() {
		return mountsId;
	}

	public void setMountsId(int mountsId) {
		this.mountsId = mountsId;
	}

	public String getMountsName() {
		return mountsName;
	}

	public void setMountsName(String mountsName) {
		this.mountsName = mountsName;
	}
	
	
	public long getStartLine() {
		return startLine;
	}

	public void setStartLine(long startLine) {
		this.startLine = startLine;
	}


}
