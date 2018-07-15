package com.i5i58.data.wechat;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "WechatAccount")
@JsonInclude(Include.NON_DEFAULT)
public class WechatAccount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3768078710262404795L;

	
	@Id	
	@Column(nullable = false, length = 32)
	private String accId = "";

	@Column(nullable = false, length = 32)
	private String openId = "";
	
	/**
	 * 是否默认账号
	 */
	@Column(nullable = false, length = 1)
	private boolean selected;
	
	@Column(nullable = false, length = 18)
	private long bindDate = 0L;		
	
	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public long getBindDate() {
		return bindDate;
	}

	public void setBindDate(long bindDate) {
		this.bindDate = bindDate;
	}
}
