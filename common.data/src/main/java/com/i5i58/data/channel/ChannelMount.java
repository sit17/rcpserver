package com.i5i58.data.channel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "ChannelMounts", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
@JsonInclude(Include.NON_DEFAULT)
public class ChannelMount implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4404948148389495433L;

	@Id
	private int id;

	@Column(nullable = false)
	private long price;
	private String name = "";
	private boolean forGuard;
	private boolean forVip;
	private boolean forFansClubs;
	private boolean nullity;
	private String version = "";
	private String function = "";
	private String path = "";
	private int validity;
	private int level;

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

	public boolean isNullity() {
		return nullity;
	}

	public void setNullity(boolean nullity) {
		this.nullity = nullity;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getValidity() {
		return validity;
	}

	public void setValidity(int validity) {
		this.validity = validity;
	}

	public boolean isForFansClubs() {
		return forFansClubs;
	}

	public void setForFansClubs(boolean forFansClubs) {
		this.forFansClubs = forFansClubs;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
