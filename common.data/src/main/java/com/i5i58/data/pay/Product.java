package com.i5i58.data.pay;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "Products", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
@JsonInclude(Include.NON_DEFAULT)
public class Product implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4789504167544250829L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id = 0L;

	/**
	 * 设备号，标志商品用于哪个品台
	 */
	private int device = 0;

	/**
	 * 订单虎币数*100
	 */
	private long iGold = 0L;

	/**
	 * 订单价格，单位人民币，精确到分
	 */
	private long price = 0L;

	/**
	 * 商品id,只在ios上有用
	 */
	private String productId = "";

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getDevice() {
		return device;
	}

	public void setDevice(int device) {
		this.device = device;
	}

	public long getiGold() {
		return iGold;
	}

	public void setiGold(long iGold) {
		this.iGold = iGold;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
}
