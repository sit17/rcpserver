package com.i5i58.data.group;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "AnchorContracts", uniqueConstraints = { @UniqueConstraint(columnNames = { "ctId" }) })
@JsonInclude(Include.NON_DEFAULT)
public class AnchorContract implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1358753665818402443L;

	@Id
	@Column(length = 32)
	private String ctId = "";

	/**
	 * 公会ID
	 */
	private String gId = "";

	/**
	 * 主播ID
	 */
	private String accId = "";

	/**
	 * 公会抽成比例
	 */
	private int groupRate = 0;

	/**
	 * 同意日期
	 */
	private long startDate = 0L;

	/**
	 * 到期日期
	 */
	private long endDate = 0L;

	/**
	 * 结算模式{0：平台分配，1：授权分配}
	 */
	private int settleMode = 0;

	/**
	 * 发起签约时间
	 */
	private long createTime = 0L;

	/**
	 * 合约状态{0：已发起，1：已同意，2：已拒绝}
	 */
	private int status = 0;

	/**
	 * 0：主播发起，1：公会发起 签约，2：超管分配
	 */
	private int direction = 0;
	/**
	 * 0:主播发起，1：公会发起 解约
	 */
	
	private int cancelDirection = 0;
	/**
	 * 主播端显示状态 默认为显示
	 */
	private boolean hide = false;

	public int getCancelDirection() {
		return cancelDirection;
	}

	public void setCancelDirection(int cancelDirection) {
		this.cancelDirection = cancelDirection;
	}

	public String getCtId() {
		return ctId;
	}

	public void setCtId(String ctId) {
		this.ctId = ctId;
	}

	public String getgId() {
		return gId;
	}

	public void setgId(String gId) {
		this.gId = gId;
	}

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public int getGroupRate() {
		return groupRate;
	}

	public void setGroupRate(int groupRate) {
		this.groupRate = groupRate;
	}

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	public int getSettleMode() {
		return settleMode;
	}

	public void setSettleMode(int settleMode) {
		this.settleMode = settleMode;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public boolean getHide() {
		return hide;
	}

	public void setHide(boolean hide) {
		this.hide = hide;
	}
	
 
	
}
