package com.i5i58.data.channel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "ChannelGifts", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
@JsonInclude(Include.NON_DEFAULT)
public class ChannelGift implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7158325823060521342L;

	@Id
	private int id;

	@Column(nullable = false)
	private String name = "";
	private long price;
	private long anchorPrice;
	private boolean forGuard;
	private boolean forVip;
	private String unit = "";
	private int maxCount;
	private boolean nullity;
	private String version = "";
	private String path = "";
	private String function = "";

	private String flashPath = "";

	public String getFlashPath() {
		return flashPath;
	}

	public void setFlashPath(String flashPath) {
		this.flashPath = flashPath;
	}

	@JsonProperty("condition")
	private int countCondition;
	private int sortId;
	private int node;

	@JsonIgnore
	private boolean broadcast;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isNullity() {
		return nullity;
	}

	public void setNullity(boolean nullity) {
		this.nullity = nullity;
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

	public long getAnchorPrice() {
		return anchorPrice;
	}

	public void setAnchorPrice(long anchorPrice) {
		this.anchorPrice = anchorPrice;
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

	public int getSortId() {
		return sortId;
	}

	public void setSortId(int sortId) {
		this.sortId = sortId;
	}

	public boolean isBroadcast() {
		return broadcast;
	}

	public void setBroadcast(boolean broadcast) {
		this.broadcast = broadcast;
	}

	public int getCondition() {
		return countCondition;
	}

	public void setCondition(int condition) {
		this.countCondition = condition;
	}

	public int getNode() {
		return node;
	}

	public void setNode(int node) {
		this.node = node;
	}

}
