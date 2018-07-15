package com.i5i58.service.pay.async;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
public class MsgYxPay implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3286524206407044281L;

	private String orderId = "";
	private String accId = "";
	private int shareId = 0;
	private String toAccId = "";
	private long orderAmount = 0;
	private long payAmount = 0;
	private int orderStatus = 0;
	private long iGold = 0;
	private long applyDate = 0;
	private long completeDate = 0;

	private String ipAddress = "";
	private int discount = 100;
	private String serialNum = "";
	private int device = 0;

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

	public int getShareId() {
		return shareId;
	}

	public void setShareId(int shareId) {
		this.shareId = shareId;
	}

	public String getToAccId() {
		return toAccId;
	}

	public void setToAccId(String toAccId) {
		this.toAccId = toAccId;
	}

	public long getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(long orderAmount) {
		this.orderAmount = orderAmount;
	}

	public long getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(long payAmount) {
		this.payAmount = payAmount;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public long getiGold() {
		return iGold;
	}

	public void setiGold(long iGold) {
		this.iGold = iGold;
	}

	public long getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(long applyDate) {
		this.applyDate = applyDate;
	}

	public long getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(long completeDate) {
		this.completeDate = completeDate;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public int getDevice() {
		return device;
	}

	public void setDevice(int device) {
		this.device = device;
	}

}
