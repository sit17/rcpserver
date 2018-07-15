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
@Table(name = "ChannelRecords", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
@JsonInclude(Include.NON_DEFAULT)
public class ChannelRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5223048776050193786L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(nullable = false)
	private String cId = "";
	private String accId = "";
	private int goodsId;
	private int goodsCount;
	private int goodsType;
	private long amount;
	private long collectDate;
	private String description = "";
	private String ipAddress = "";

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

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public int getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(int goodsCount) {
		this.goodsCount = goodsCount;
	}

	public int getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(int goodsType) {
		this.goodsType = goodsType;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public long getCollectDate() {
		return collectDate;
	}

	public void setCollectDate(long collectDate) {
		this.collectDate = collectDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public ChannelRecord(String cId, String accId, int goodsId, int goodsCount, int goodsType, long amount,
			long collectDate, String description, String ipAddress) {
		super();
		this.cId = cId;
		this.accId = accId;
		this.goodsId = goodsId;
		this.goodsCount = goodsCount;
		this.goodsType = goodsType;
		this.amount = amount;
		this.collectDate = collectDate;
		this.description = description;
		this.ipAddress = ipAddress;
	}

	public ChannelRecord() {

	}

}
