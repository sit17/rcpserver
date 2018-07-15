package com.i5i58.data.netBar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "NetBarAgents")
@JsonInclude(Include.NON_DEFAULT)
public class NetBarOrder implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6546358955572993450L;
	/**
	 * 订单ID
	 */
	@Id
	@Column(nullable = false, length = 32)
	private String orderId = "";
	
	/**
	 * 用户ID
	 */
	private String accId = "";
	/**
	 * 产品ID
	 */

	private int goodsId;
	/***
	 * 数量
	 */
	private int num;

	/**
	 * 订单时间
	 */
	private long createTime = 0;

	/**
	 * 最后操作时间
	 */
	private long lastOperTime = 0;

	/**
	 * 订单状态
	 */
	private int type = 0;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getLastOperTime() {
		return lastOperTime;
	}

	public void setLastOperTime(long lastOperTime) {
		this.lastOperTime = lastOperTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}	
}
