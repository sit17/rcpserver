package com.i5i58.data.record;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "RecordPays", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
@JsonInclude(Include.NON_DEFAULT)

public class RecordPay implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5644887353205123468L;

	@Id
	@Column(nullable = false, length = 32)
	private String id = "";

	@Column(nullable = false)
	private String accId = "";

	private String orderId = "";

	private String target = "";

	private long amount = 0L;

	private String fromSource = "";

	private long createTime = 0;

	private int status = 0;

	@Column(nullable = false, length = 10)
	private String statusDescribe="";

	private String cardNo = "";

	@Column(nullable = false, length = 10)
	private String env = "";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccId() {
		return accId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public String getFromSource() {
		return fromSource;
	}

	public void setFromSource(String fromSource) {
		this.fromSource = fromSource;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusDescribe() {
		return statusDescribe;
	}

	public void setStatusDescribe(String statusDescribe) {
		this.statusDescribe = statusDescribe;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}
}
