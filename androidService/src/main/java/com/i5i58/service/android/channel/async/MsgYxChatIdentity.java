package com.i5i58.service.android.channel.async;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
public class MsgYxChatIdentity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3607524475635742485L;
	
	private String accId = "";

	private String name = "";
	
	private String face = "";

	private int vip = 0;

	private long vipDeadLine = 0L;

	private int guard = 0;

	private long guardDeadLine = 0L;

	private long richScore = 0L;
	
	private long score = 0L;
	
	private int fansClub = 0;
	
	private String clubName = "";
	
	private long fansClubDeadLine = 0L;
	

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	public long getVipDeadLine() {
		return vipDeadLine;
	}

	public void setVipDeadLine(long vipDeadLine) {
		this.vipDeadLine = vipDeadLine;
	}

	public int getGuard() {
		return guard;
	}

	public void setGuard(int guard) {
		this.guard = guard;
	}

	public long getGuardDeadLine() {
		return guardDeadLine;
	}

	public void setGuardDeadLine(long guardDeadLine) {
		this.guardDeadLine = guardDeadLine;
	}

	public long getRichScore() {
		return richScore;
	}

	public void setRichScore(long richScore) {
		this.richScore = richScore;
	}

	public int getFansClub() {
		return fansClub;
	}

	public void setFansClub(int fansClub) {
		this.fansClub = fansClub;
	}

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public long getFansClubDeadLine() {
		return fansClubDeadLine;
	}

	public void setFansClubDeadLine(long fansClubDeadLine) {
		this.fansClubDeadLine = fansClubDeadLine;
	}
}