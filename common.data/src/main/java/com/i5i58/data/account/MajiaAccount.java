package com.i5i58.data.account;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
public class MajiaAccount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1311990568515969195L;

	private String accId = "";

	private String openId = "";

	private String nickName = "";

	private String signature = "";

	private String faceSmallUrl = "";

	private int vip;

	private long vipDeadline;

	private long score;

	private long richScore;

	public MajiaAccount(String accId, String openId, String nickName, String signature, String faceSmallUrl, int vip,
			long vipDeadline, long score, long richScore) {
		super();
		this.accId = accId;
		this.openId = openId;
		this.nickName = nickName;
		this.signature = signature;
		this.faceSmallUrl = faceSmallUrl;
		this.vip = vip;
		this.vipDeadline = vipDeadline;
		this.score = score;
		this.richScore = richScore;
	}

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

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getFaceSmallUrl() {
		return faceSmallUrl;
	}

	public void setFaceSmallUrl(String faceSmallUrl) {
		this.faceSmallUrl = faceSmallUrl;
	}

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	public long getVipDeadline() {
		return vipDeadline;
	}

	public void setVipDeadline(long vipDeadline) {
		this.vipDeadline = vipDeadline;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public long getRichScore() {
		return richScore;
	}

	public void setRichScore(long richScore) {
		this.richScore = richScore;
	}

}
