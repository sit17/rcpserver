package com.i5i58.data.account;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@Entity
@Table(name = "AccountConfigs", uniqueConstraints = {@UniqueConstraint(columnNames={"accId"})})
@JsonInclude(Include.NON_DEFAULT)
public class AccountConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2526034421043941724L;
	
	@Id
	@Column(nullable = false, length = 32)
	private String accId = "";

	@Column(nullable = false)
	private boolean enableNoticedOnLive = true;

	@Column(nullable = false)
	private boolean enableNoDisturb = false;

	@Column(nullable = false, length = 11)
	private int addFirendsType = 0;
	
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	public boolean isEnableNoticedOnLive() {
		return enableNoticedOnLive;
	}
	public void setEnableNoticedOnLive(boolean enableNoticedOnLive) {
		this.enableNoticedOnLive = enableNoticedOnLive;
	}
	public boolean isEnableNoDisturb() {
		return enableNoDisturb;
	}
	public void setEnableNoDisturb(boolean enableNoDisturb) {
		this.enableNoDisturb = enableNoDisturb;
	}
	public int getAddFirendsType() {
		return addFirendsType;
	}
	public void setAddFirendsType(int addFirendsType) {
		this.addFirendsType = addFirendsType;
	}
}
