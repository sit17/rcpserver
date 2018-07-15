package com.i5i58.message.service;

import javax.websocket.Session;

public class Player{

	private Session session;
	
	private String sessionId;

	private String remoteAddress;

	private String accId;

	private String token;

	private String cId;

	private boolean isAuthed;

	private int device;

	public Player(Session session) {
		this.session = session;
		this.sessionId = session.getRequestParameterMap().get("httpRequest").get(0);
		this.remoteAddress = session.getRequestParameterMap().get("httpRequest").get(1);
		System.out.println(String.format("[%s]:connected by [%s]", sessionId, remoteAddress));
		isAuthed = false;
		device = 0;
	}

	public Session getSession() {
		return session;
	}
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public boolean isAuthed() {
		return isAuthed;
	}

	public void setAuthed(boolean isAuthed) {
		this.isAuthed = isAuthed;
	}

	public int getDevice() {
		return device;
	}

	public void setDevice(int device) {
		this.device = device;
	}

	
}
