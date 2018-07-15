package com.i5i58.data.channel;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@Entity
@Table(name = "MountPresent", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
@JsonInclude(Include.NON_DEFAULT)
public class MountPresent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8251185359127453715L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private long id;

	@Column(nullable = false)
	private int type; //1:首冲,2 : 购买守护，3：购买vip
	
	@Column(nullable = false)
	private int level=1; //对应守护，vip的等级，首冲固定为1
	
	@Column(nullable = false)
	private int mountId;
	
	@Column(nullable= false)
	private int month = 0; //有效时间，只对首冲有效
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getMountId() {
		return mountId;
	}

	public void setMountId(int mountId) {
		this.mountId = mountId;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}
	
}