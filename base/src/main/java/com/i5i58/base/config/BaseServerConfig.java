package com.i5i58.base.config;

import io.netty.channel.ChannelHandler;

public abstract class BaseServerConfig implements IServerConfig {

	protected String name = "";
	protected int port = 0;
	protected int bossthread = 1;
	protected int workerthread = 0;
	protected boolean epoll = false;
	protected boolean keepalive = true;
	protected boolean nodelay = true;
	protected boolean ssl = false;
	protected int sslMode = 0;
	protected int conntimeout = 3000;
	protected boolean isGate = false;
	protected int backlog = 1024;

	protected ChannelHandler handler;
	protected ChannelHandler childHandler;
	public void init() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getBossthread() {
		return bossthread;
	}

	public void setBossthread(int bossthread) {
		this.bossthread = bossthread;
	}

	public int getWorkerthread() {
		return workerthread;
	}

	public void setWorkerthread(int workerthread) {
		this.workerthread = workerthread;
	}

	public boolean isEpoll() {
		return epoll;
	}

	public void setEpoll(boolean epoll) {
		this.epoll = epoll;
	}

	public boolean isKeepalive() {
		return keepalive;
	}

	public void setKeepalive(boolean keepalive) {
		this.keepalive = keepalive;
	}

	public boolean isNodelay() {
		return nodelay;
	}

	public void setNodelay(boolean nodelay) {
		this.nodelay = nodelay;
	}

	public boolean isSsl() {
		return ssl;
	}

	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}

	public int getSslMode(){
		return sslMode;
	}
	
	public void setSslMode(int sslMode){
		this.sslMode = sslMode;
	}
	
	public boolean isGate() {
		return isGate;
	}

	public void setGate(boolean isGate) {
		this.isGate = isGate;
	}

	public int getConntimeout() {
		return conntimeout;
	}

	public void setConntimeout(int conntimeout) {
		this.conntimeout = conntimeout;
	}

	public ChannelHandler getHandler() {
		return handler;
	}

	public void setHandler(ChannelHandler handler) {
		this.handler = handler;
	}

	public ChannelHandler getChildHandler() {
		return childHandler;
	}

	public void setChildHandler(ChannelHandler childHandler) {
		this.childHandler = childHandler;
	}

	public int getBacklog() {
		return backlog;
	}

	public void setBacklog(int backlog) {
		this.backlog = backlog;
	}
}
