package com.i5i58.data.pay;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
public class TotalPay implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String accId;
	private String nickName;
	private String faceSmallUrl;
	private String faceOrgUrl;
	private long total;
	
	public TotalPay(){
		
	}
	
	public TotalPay(String accId, long total){
		this.accId = accId;
		this.total = total;
	}
	
	public TotalPay(String accId, String nickName, String faceSmallUrl, String faceOrgUrl, long total){
		this.accId = accId;
		this.nickName = nickName;
		this.faceSmallUrl = faceSmallUrl;
		this.faceOrgUrl = faceOrgUrl;
		this.total = total;
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
	public String getFaceSmallUrl() {
		return faceSmallUrl;
	}
	public void setFaceSmallUrl(String faceSmallUrl) {
		this.faceSmallUrl = faceSmallUrl;
	}
	public String getFaceOrgUrl() {
		return faceOrgUrl;
	}
	public void setFaceOrgUrl(String faceOrgUrl) {
		this.faceOrgUrl = faceOrgUrl;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
}
