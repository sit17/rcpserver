package com.i5i58.data.netBar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "NetBarAgents")
@JsonInclude(Include.NON_DEFAULT)
public class NetBarGoods implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2264796155081359226L;

	@Id
	@Column(nullable = false, length = 32)
	private String goodsId = "";
	/**
	 * 名称
	 */
	@Column(nullable = false, length = 32)
	private String name = "";
	/**
	 * 价格
	 */
	private long goodsPrice = 0;

	/**
	 * 产品描述
	 */
	private String remarks = "";

	/**
	 * 单位
	 */
	private String goodsUnit = "";

	/**
	 * 产品类型（0｜实体物品;1｜虚拟产品）
	 */
	private int goodsKind = 0;

	/**
	 * 库存
	 */
	private int stock = 0;

	/**
	 * 图片路径
	 */
	private String imgPaht = "";

	/**
	 * 排序字段（越小越靠前）
	 */
	private int sortId;

	/**
	 * 节点（预留产品分类字段，如果0｜常规、1｜热门、2｜节日等）
	 */
	private int node = 0;

	/**
	 * 加入时间
	 */
	private long createTime = 0;

	/**
	 * 产品状态（0｜上架；1｜下架）
	 */
	private boolean nullity = true;

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(long goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getGoodsUnit() {
		return goodsUnit;
	}

	public void setGoodsUnit(String goodsUnit) {
		this.goodsUnit = goodsUnit;
	}

	public int getGoodsKind() {
		return goodsKind;
	}

	public void setGoodsKind(int goodsKind) {
		this.goodsKind = goodsKind;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getImgPaht() {
		return imgPaht;
	}

	public void setImgPaht(String imgPaht) {
		this.imgPaht = imgPaht;
	}

	public int getSortId() {
		return sortId;
	}

	public void setSortId(int sortId) {
		this.sortId = sortId;
	}

	public int getNode() {
		return node;
	}

	public void setNode(int node) {
		this.node = node;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public boolean isNullity() {
		return nullity;
	}

	public void setNullity(boolean nullity) {
		this.nullity = nullity;
	}
}
