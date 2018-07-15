package com.i5i58.data.profile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "GroupsProfiles", uniqueConstraints = { @UniqueConstraint(columnNames = "fId") })
@JsonInclude(Include.NON_DEFAULT)
public class GroupProfile implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2103956821929749269L;

	@Id
	@Column(nullable = false, length = 32)
	private String fId = "";

	@Column(nullable = false)
	private String accId = "";
	private String name = "";
	/**
	 * 公会组织、经纪公司等
	 */
	private int type = 0;
	/**
	 * 可以建立的顶级组数量
	 */
	private int createTopGroupCount;
	/**
	 * 每个顶级组可以建立的次级组数量
	 */
	private int createSubGroupCount;
	/**
	 * 每个次级组可以建立的频道数量
	 */
	private int createChannelCount;
	/**
	 * 通信地址，注册地址
	 */
	private String address;
	/**
	 * 法人或责任人
	 */
	private String legalPerson;
	/**
	 * 法人或责任人正面照地址
	 */
	private String legalPersonUrl;
	/**
	 * 法人或责任人背面照地址
	 */
	private String legalPersonBackUrl;
	/**
	 * 公司资料地址
	 */
	private String dataUrl;
	/**
	 * email地址
	 */
	private String email;

	@Column(length = 32)
	private String operRange;

	@Column(nullable = false)
	@JsonIgnore
	private long createDate;
	@Column(nullable = false)
	@JsonIgnore
	private String createIp;

	@Column(nullable = true)
	private String description;

	/**
	 * 营业执照注册号
	 */
	private String registerId;
	/**
	 * 营业执照等
	 */
	private String licenseUrl;
	/**
	 * 税务登记证地址
	 */
	private String taxCertificateUrl;
	/**
	 * 组织机构代码证地址
	 */
	private String organizationCodeUrl;
	/**
	 * 开户许可证
	 */
	private String bankLicenseUrl;
	/**
	 * 注册资金
	 */
	private double regCapital;

	private String fixedPhone;
	/**
	 * 审核中、正常、禁用
	 */
	private int status;
	/**
	 * 不可用
	 */
	private boolean nullity;

	public String getfId() {
		return fId;
	}

	public void setfId(String fId) {
		this.fId = fId;
	}

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCreateTopGroupCount() {
		return createTopGroupCount;
	}

	public void setCreateTopGroupCount(int createTopGroupCount) {
		this.createTopGroupCount = createTopGroupCount;
	}

	public int getCreateSubGroupCount() {
		return createSubGroupCount;
	}

	public void setCreateSubGroupCount(int createSubGroupCount) {
		this.createSubGroupCount = createSubGroupCount;
	}

	public int getCreateChannelCount() {
		return createChannelCount;
	}

	public void setCreateChannelCount(int createChannelCount) {
		this.createChannelCount = createChannelCount;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOperRange() {
		return operRange;
	}

	public void setOperRange(String operRange) {
		this.operRange = operRange;
	}

	public String getLegalPerson() {
		return legalPerson;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

	public String getLegalPersonUrl() {
		return legalPersonUrl;
	}

	public void setLegalPersonUrl(String legalPersonUrl) {
		this.legalPersonUrl = legalPersonUrl;
	}

	public String getLegalPersonBackUrl() {
		return legalPersonBackUrl;
	}

	public void setLegalPersonBackUrl(String legalPersonBackUrl) {
		this.legalPersonBackUrl = legalPersonBackUrl;
	}

	public String getDataUrl() {
		return dataUrl;
	}

	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public String getCreateIp() {
		return createIp;
	}

	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRegisterId() {
		return registerId;
	}

	public void setRegisterId(String registerId) {
		this.registerId = registerId;
	}

	public String getLicenseUrl() {
		return licenseUrl;
	}

	public void setLicenseUrl(String licenseUrl) {
		this.licenseUrl = licenseUrl;
	}

	public String getTaxCertificateUrl() {
		return taxCertificateUrl;
	}

	public void setTaxCertificateUrl(String taxCertificateUrl) {
		this.taxCertificateUrl = taxCertificateUrl;
	}

	public String getOrganizationCodeUrl() {
		return organizationCodeUrl;
	}

	public void setOrganizationCodeUrl(String organizationCodeUrl) {
		this.organizationCodeUrl = organizationCodeUrl;
	}

	public String getBankLicenseUrl() {
		return bankLicenseUrl;
	}

	public void setBankLicenseUrl(String bankLicenseUrl) {
		this.bankLicenseUrl = bankLicenseUrl;
	}

	public double getRegCapital() {
		return regCapital;
	}

	public void setRegCapital(double regCapital) {
		this.regCapital = regCapital;
	}

	public String getFixedPhone() {
		return fixedPhone;
	}

	public void setFixedPhone(String fixedPhone) {
		this.fixedPhone = fixedPhone;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isNullity() {
		return nullity;
	}

	public void setNullity(boolean nullity) {
		this.nullity = nullity;
	}

}
