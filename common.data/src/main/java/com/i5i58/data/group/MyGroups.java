package com.i5i58.data.group;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
public class MyGroups implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 590373489118525142L;

	private List<ChannelGroup> ownerGroup = null;

	private List<ChannelGroup> adminGroup = null;

	public List<ChannelGroup> getOwnerGroup() {
		return ownerGroup;
	}

	public void setOwnerGroup(List<ChannelGroup> ownerGroup) {
		this.ownerGroup = ownerGroup;
	}

	public List<ChannelGroup> getAdminGroup() {
		return adminGroup;
	}

	public void setAdminGroup(List<ChannelGroup> adminGroup) {
		this.adminGroup = adminGroup;
	}

}
