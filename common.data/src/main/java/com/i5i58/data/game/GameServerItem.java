package com.i5i58.data.game;

import java.io.Serializable;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonProperty;

@RedisHash("GameServerItems")
public class GameServerItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7134590586093141485L;

	@Id
	@JsonProperty("serverId")
	private int id; // 房间索引
	
	@Indexed
	private int kindId; // 名称索引
	
	@Indexed
	private int nodeId; // 节点索引
	
	private int sortId; // 排序索引
	
	@Indexed
	private int serverKind; // 房间类型
	
	@Indexed
	private int serverType; // 房间类型
	private int serverLevel; // 房间等级
	private int serverPort; // 房间端口
	private long cellScore; // 单元积分
	private long enterScore; // 进入积分
	private int serverRule; // 房间规则
	private int lineCount; // 在线人数
	private int androidCount; // 机器人数
	private int fullCount; // 满员人数
	private String serverAddr; // 房间名称
	private String serverName; // 房间名称
	public int getKindId() {
		return kindId;
	}
	public void setKindId(int kindId) {
		this.kindId = kindId;
	}
	public int getNodeId() {
		return nodeId;
	}
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}
	public int getSortId() {
		return sortId;
	}
	public void setSortId(int sortId) {
		this.sortId = sortId;
	}
	public int getServerId() {
		return id;
	}
	public void setServerId(int serverId) {
		this.id = serverId;
	}
	public int getServerKind() {
		return serverKind;
	}
	public void setServerKind(int serverKind) {
		this.serverKind = serverKind;
	}
	public int getServerType() {
		return serverType;
	}
	public void setServerType(int serverType) {
		this.serverType = serverType;
	}
	public int getServerLevel() {
		return serverLevel;
	}
	public void setServerLevel(int serverLevel) {
		this.serverLevel = serverLevel;
	}
	public int getServerPort() {
		return serverPort;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	public long getCellScore() {
		return cellScore;
	}
	public void setCellScore(long cellScore) {
		this.cellScore = cellScore;
	}
	public long getEnterScore() {
		return enterScore;
	}
	public void setEnterScore(long enterScore) {
		this.enterScore = enterScore;
	}
	public int getServerRule() {
		return serverRule;
	}
	public void setServerRule(int serverRule) {
		this.serverRule = serverRule;
	}
	public int getLineCount() {
		return lineCount;
	}
	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}
	public int getAndroidCount() {
		return androidCount;
	}
	public void setAndroidCount(int androidCount) {
		this.androidCount = androidCount;
	}
	public int getFullCount() {
		return fullCount;
	}
	public void setFullCount(int fullCount) {
		this.fullCount = fullCount;
	}
	public String getServerAddr() {
		return serverAddr;
	}
	public void setServerAddr(String serverAddr) {
		this.serverAddr = serverAddr;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
}
