package com.i5i58.data.record;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "RecordConsumptions", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
@JsonInclude(Include.NON_DEFAULT)
public class RecordConsumption implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7079315250707763206L;

	@Id
	@Column(nullable = false, length = 32)
	@JsonIgnore
	private String id = "";

	@Column(nullable = false)
	private String accId = "";
	private String channelId = "";
	private String goodsId = "";// 购买内容Id
	private int goodsType = 0;// 购买内容的类型 1:礼物｜2：VIP｜3：坐骑｜4：守护｜5：开通粉丝团
	private int goodsNumber = 0;
	private String description = "";// 购买内容描述
	private double amount = 0.0;// 购买金额
	private long deadline = 0L;// 购买期限
	private long date = 0L;// 购买时间
	@JsonIgnore
	private String clientIp = "";

	public int getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(int goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getDescribe() {
		return description;
	}

	public void setDescribe(String describe) {
		description = describe;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public long getDeadline() {
		return deadline;
	}

	public void setDeadline(long deadline) {
		this.deadline = deadline;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public int getGoodsType() {
		return goodsType;
	}

	/**
	 * 购买内容的类型 1:礼物｜2：VIP｜3：坐骑｜4：守护｜5：开通粉丝团
	 */
	public void setGoodsType(int goodsType) {
		this.goodsType = goodsType;
	}

}
