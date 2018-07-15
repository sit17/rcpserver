package com.i5i58.data.channel;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@RedisHash("HotChannelGifts")
@JsonInclude(Include.NON_DEFAULT)
public class HotChannelGift implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7981200848742137110L;

	/**
	 * sortId
	 */
	@JsonProperty("sortId")
	@Id
	private int id;

	private String name = "";
	private long price;
	private long anchorPrice;
	private boolean forGuard;
	private boolean forVip;
	private String unit = "";
	private int maxCount;
	private String version = "";
	private String path = "";
	private String function = "";
	private int node;
	
	private String flashPath;
	
	public String getFlashPath() {
		return flashPath;
	}
	public void setFlashPath(String flashPath) {
		this.flashPath = flashPath;
	}
	@JsonIgnore
	private int condition;
	
	@JsonIgnore
	private boolean broadcast;

	@JsonProperty("id")
	@Indexed
	private int mainId;
	
	public int getSortId() {
		return id;
	}
	public void setSortId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public long getAnchorPrice() {
		return anchorPrice;
	}
	public void setAnchorPrice(long anchorPrice) {
		this.anchorPrice = anchorPrice;
	}
	public boolean isForGuard() {
		return forGuard;
	}
	public void setForGuard(boolean forGuard) {
		this.forGuard = forGuard;
	}
	public boolean isForVip() {
		return forVip;
	}
	public void setForVip(boolean forVip) {
		this.forVip = forVip;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public boolean isBroadcast() {
		return broadcast;
	}
	public void setBroadcast(boolean broadcast) {
		this.broadcast = broadcast;
	}
	public int getMainId() {
		return mainId;
	}
	public void setMainId(int mainId) {
		this.mainId = mainId;
	}
	public int getCondition() {
		return condition;
	}
	public void setCondition(int condition) {
		this.condition = condition;
	}
	public int getNode() {
		return node;
	}
	public void setNode(int node) {
		this.node = node;
	}
}
