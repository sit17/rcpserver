package com.i5i58.data.channel;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
public class MyChannels implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2279961734345786867L;
	
	private Channel ownerChannel = null;
	private List<Channel> adminChannels = null;

	public Channel getOwnerChannel() {
		return ownerChannel;
	}
	public void setOwnerChannel(Channel ownerChannel) {
		this.ownerChannel = ownerChannel;
	}
	public List<Channel> getAdminChannels() {
		return adminChannels;
	}
	public void setAdminChannels(List<Channel> adminChannels) {
		this.adminChannels = adminChannels;
	}
	
}
