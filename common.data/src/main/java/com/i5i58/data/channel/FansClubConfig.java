package com.i5i58.data.channel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "FansClubConfigs", uniqueConstraints = { @UniqueConstraint(columnNames = "month") })
@JsonInclude(Include.NON_DEFAULT)
public class FansClubConfig implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -808969301837152368L;

	@Id
	private int month;
	
	private float discount;

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	} 
	
}
