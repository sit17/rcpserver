package com.i5i58.data.record;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 现金流向表
 * songfl
 * */

@Entity
@Table(name = "MoneyFlow")
@JsonInclude(Include.NON_DEFAULT)
public class MoneyFlow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5578658405393694855L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(nullable = false, length = 32)
	private String accId="";
	
	@Column
	private long sourceIGold = 0L;
	
	@Column
	private long sourceDiamond = 0L;
	
	@Column
	private long sourceRedDiamond = 0L;
	
	@Column
	private long sourceCommission = 0L;
	
	@Column
	private long sourceGiftTicket = 0L;
	
	@Column
	private long targetIGold = 0L;
	
	@Column
	private long targetDiamond = 0L;
	
	@Column
	private long targetRedDiamond = 0L;
	
	@Column
	private long targetCommission = 0L;
	
	@Column
	private long targetGiftTicket = 0L;
	
	@Column
	private long dateTime = 0L;
	
	@Column
	private int type = 0;
	
	@Column
	private String description="";
	
	@Column(nullable = false)
	private String IpAddress="";
	
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
	public long getSourceIGold() {
		return sourceIGold;
	}
	public void setSourceIGold(long sourceIGold) {
		this.sourceIGold = sourceIGold;
	}
	public long getSourceDiamond() {
		return sourceDiamond;
	}
	public long getSourceRedDiamond() {
		return sourceRedDiamond;
	}
	public void setSourceRedDiamond(long sourceRedDiamond) {
		this.sourceRedDiamond = sourceRedDiamond;
	}
	public void setSourceDiamond(long sourceDiamond) {
		this.sourceDiamond = sourceDiamond;
	}
	public long getSourceCommission() {
		return sourceCommission;
	}
	public void setSourceCommission(long sourceCommission) {
		this.sourceCommission = sourceCommission;
	}
	public long getSourceGiftTicket() {
		return sourceGiftTicket;
	}
	public void setSourceGiftTicket(long sourceGiftTicket) {
		this.sourceGiftTicket = sourceGiftTicket;
	}
	public long getTargetIGold() {
		return targetIGold;
	}
	public void setTargetIGold(long targetIGold) {
		this.targetIGold = targetIGold;
	}
	public long getTargetDiamond() {
		return targetDiamond;
	}
	public void setTargetDiamond(long targetDiamond) {
		this.targetDiamond = targetDiamond;
	}
	public long getTargetRedDiamond() {
		return targetRedDiamond;
	}
	public void setTargetRedDiamond(long targetRedDiamond) {
		this.targetRedDiamond = targetRedDiamond;
	}
	public long getTargetCommission() {
		return targetCommission;
	}
	public void setTargetCommission(long targetCommission) {
		this.targetCommission = targetCommission;
	}
	public long getTargetGiftTicket() {
		return targetGiftTicket;
	}
	public void setTargetGiftTicket(long targetGiftTicket) {
		this.targetGiftTicket = targetGiftTicket;
	}
	public long getDateTime() {
		return dateTime;
	}
	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String desc) {
		this.description = desc;
	}
	public String getIpAddress() {
		return IpAddress;
	}
	public void setIpAddress(String ipAddress) {
		IpAddress = ipAddress;
	}
}
