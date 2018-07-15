package com.i5i58.data.record;

public enum MoneyFlowType {
	Undeined				(0, "未定义"),
	Recharge				(1, "充值"), 
	GiveGift				(2, "赠送礼物"), 
	GiftCommission			(3, "主播礼物俸禄"),
	BuyVip					(4, "购买VIP"),
	UpgradeVip				(5, "升级VIP"),
	BuyMount				(6, "购买坐骑"),
	BuyGuardMount			(7, "购买守护坐骑"),
	OpenClub				(8, "开通粉丝图"),
	OpenGuard				(9, "开通守护"),
	ChangeOpenId			(10, "修改openId"),
	ExchangeGameGold		(11, "虎币兑换欢乐"),
	ExchangeDiamond			(12, "虎币兑换钻石"),
	CommissionToGameGold	(13, "俸禄兑换游戏币"),
	CommissionToDiamond		(14, "俸禄兑换钻石"),
	CommissionToCash 		(15, "俸禄提现"),
	DriftComment 			(16, "发送弹幕"),
	CommissionOnOpenGuard	(17, "用户开通骑士,主播获取俸禄"),
	BuyGameVip				(18, "购买游戏会员"),
	SuperUpdateGiftTicket	(19, "超管更新礼物券"),
	WechatAward             (20,"微信抽奖");
	
	private int value;
	private String desc;
	
	private MoneyFlowType(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
