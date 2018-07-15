package com.i5i58.data.anchor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "CommissionByGuardConfig", uniqueConstraints = {
		@UniqueConstraint(columnNames = "id")})
@JsonInclude(Include.NON_DEFAULT)
public class CommissionByGuardConfig implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7882759007530957718L;
	@Id
	@Column(nullable = false, length = 9)
	private int id;

	@Column(nullable = false, length = 11)
	private int guardLevel;

	@Column(nullable = false, length = 11)
	private long moneyOneMonth;

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getMoneyOneMonth() {
		return moneyOneMonth;
	}

	public void setMoneyOneMonth(long moneyOneMonth) {
		this.moneyOneMonth = moneyOneMonth;
	}

	public int getGuardLevel() {
		return guardLevel;
	}

	public void setGuardLevel(int guardLevel) {
		this.guardLevel = guardLevel;
	}

	 

}
