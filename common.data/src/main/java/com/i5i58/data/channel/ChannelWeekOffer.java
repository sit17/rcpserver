package com.i5i58.data.channel;

import java.io.Serializable;

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
@Table(name = "ChannelWeekOffers", uniqueConstraints = {@UniqueConstraint(columnNames="id")})
@JsonInclude(Include.NON_DEFAULT)
public class ChannelWeekOffer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4323605778324354841L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private long id;
	
	private String cId = "";
	
	private String accId = "";
	
	private String nickName = "";
	
	private double offerAmount;
	
	private String faceUrl = "";
	
	/**
	 * 记录当周的第一天日期
	 */
	private long collectDate;

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

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public double getOfferAmount() {
		return offerAmount;
	}

	public void setOfferAmount(double offerAmount) {
		this.offerAmount = offerAmount;
	}

	public String getFaceUrl() {
		return faceUrl;
	}

	public void setFaceUrl(String faceUrl) {
		this.faceUrl = faceUrl;
	}

	public long getCollectDate() {
		return collectDate;
	}

	public void setCollectDate(long collectDate) {
		this.collectDate = collectDate;
	}
	
	
}
