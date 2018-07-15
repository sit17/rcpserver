package com.i5i58.data.channel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
public class WeekOffer implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2674571561631161540L;

	private String accId = "";

	private long offer;

	private String name = "";

	private String faceSmallUrl = "";

	private int vip;

	private int guardLevel;

	private long richScore;

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public long getOffer() {
		return offer;
	}

	public void setOffer(long offer) {
		this.offer = offer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getGuardLevel() {
		return guardLevel;
	}

	public void setGuardLevel(int guardLevel) {
		this.guardLevel = guardLevel;
	}

	public long getRichScore() {
		return richScore;
	}

	public void setRichScore(long richScore) {
		this.richScore = richScore;
	}
}
