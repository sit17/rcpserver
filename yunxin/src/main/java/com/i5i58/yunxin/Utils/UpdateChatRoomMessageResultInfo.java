package com.i5i58.yunxin.Utils;

public class UpdateChatRoomMessageResultInfo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4544512564107649716L;
	private String roomid;
	private Boolean valid;
	private String announcement;
	private String name;
	private String broadcasturl;
	private String ext;
	private String creator;
	public String getRoomid() {
		return roomid;
	}
	public void setRoomid(String roomid) {
		this.roomid = roomid;
	}
	public Boolean getValid() {
		return valid;
	}
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	public String getAnnouncement() {
		return announcement;
	}
	public void setAnnouncement(String announcement) {
		this.announcement = announcement;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBroadcasturl() {
		return broadcasturl;
	}
	public void setBroadcasturl(String broadcasturl) {
		this.broadcasturl = broadcasturl;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	

}
