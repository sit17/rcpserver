package com.i5i58.yunxin.Utils;

import java.util.List;

public class QueryTeamInfo {
	/*			"icon": null,
            "announcement": null,
            "uptinfomode": 0,
            "maxusers": 200,
            "intro": null,
            "size": 1,
            "upcustommode": 0,
            "owner": "3ed2637e5716406184e735b2d378701e",
            "tname": "first team",
            "beinvitemode": 0,
            "joinmode": 1,
            "tid": 7831324,
            "members": [],
            "invitemode": 0,
            "custom": "\"this is cutom data\""
 * */
	private String icon;
	private String announcement;
	private int uptinfomode;
	private int maxusers;
	private String intro;
	private int  size;
	private int upcustommode;
	private String owner;
	private String tname;
	private int beinvitemode;
	private int joinmode;
	private int tid;
	private List<String> members;
	private String invitemode;
	private String custom;
	private List<String> admins;
	
	public List<String> getAdmins() {
		return admins;
	}
	public void setAdmins(List<String> admins) {
		this.admins = admins;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getAnnouncement() {
		return announcement;
	}
	public void setAnnouncement(String announcement) {
		this.announcement = announcement;
	}
	public int getUptinfomode() {
		return uptinfomode;
	}
	public void setUptinfomode(int uptinfomode) {
		this.uptinfomode = uptinfomode;
	}
	public int getMaxusers() {
		return maxusers;
	}
	public void setMaxusers(int maxusers) {
		this.maxusers = maxusers;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getUpcustommode() {
		return upcustommode;
	}
	public void setUpcustommode(int upcustommode) {
		this.upcustommode = upcustommode;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
	}
	public int getBeinvitemode() {
		return beinvitemode;
	}
	public void setBeinvitemode(int beinvitemode) {
		this.beinvitemode = beinvitemode;
	}
	public int getJoinmode() {
		return joinmode;
	}
	public void setJoinmode(int joinmode) {
		this.joinmode = joinmode;
	}
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public List<String> getMembers() {
		return members;
	}
	public void setMembers(List<String> members) {
		this.members = members;
	}
	public String getInvitemode() {
		return invitemode;
	}
	public void setInvitemode(String invitemode) {
		this.invitemode = invitemode;
	}
	public String getCustom() {
		return custom;
	}
	public void setCustom(String custom) {
		this.custom = custom;
	}

}
