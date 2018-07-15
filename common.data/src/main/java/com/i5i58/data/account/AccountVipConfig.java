package com.i5i58.data.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "AccountVipConfig", uniqueConstraints = {
		@UniqueConstraint(columnNames = "id")})
@JsonInclude(Include.NON_DEFAULT)
public class AccountVipConfig implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1328917593872650959L;

	@Id
	@Column(nullable = false, length = 9)
	private int id;

	@Column(nullable = false, length = 11)
	private int level;

	@Column(nullable = false, length = 11)
	private int month;
	
	private long price;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

}
