package com.i5i58.data.game;

import java.io.Serializable;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonProperty;

@RedisHash("GameKindItems")
public class GameKindItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -999099565820716780L;

	@Id
	@JsonProperty("kindId")
	private int id;                           //类型索引
	
	@Indexed
	private int gameId;                           //游戏索引

	@Indexed
	private int typeId;                           //大类索引

	@Indexed
	private int joinId;                         //加入索引

	@Indexed
	private int sortId;                        //排序类型
	
	private String kindName;                 //类型名称
	private String processName;			//进程名称
	private boolean supportMobile;            //支持手机
	private String gameRuleUrl;      //规则地址
	private String downLoadUrl;      //下载地址
	
	public int getKindId() {
		return id;
	}
	public void setKindId(int kindId) {
		this.id = kindId;
	}
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public int getJoinId() {
		return joinId;
	}
	public void setJoinId(int joinId) {
		this.joinId = joinId;
	}
	public int getSortId() {
		return sortId;
	}
	public void setSortId(int sortId) {
		this.sortId = sortId;
	}
	public String getKindName() {
		return kindName;
	}
	public void setKindName(String kindName) {
		this.kindName = kindName;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public boolean isSupportMobile() {
		return supportMobile;
	}
	public void setSupportMobile(boolean supportMobile) {
		this.supportMobile = supportMobile;
	}
	public String getGameRuleUrl() {
		return gameRuleUrl;
	}
	public void setGameRuleUrl(String gameRuleUrl) {
		this.gameRuleUrl = gameRuleUrl;
	}
	public String getDownLoadUrl() {
		return downLoadUrl;
	}
	public void setDownLoadUrl(String downLoadUrl) {
		this.downLoadUrl = downLoadUrl;
	}
}
