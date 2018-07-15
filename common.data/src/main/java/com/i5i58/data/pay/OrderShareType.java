package com.i5i58.data.pay;

import com.i5i58.util.KVCode;

public enum OrderShareType  implements KVCode
{
	PAY_DUOBAOTONG_ALIPAY_WEB("多宝通_支付宝WEB充值", 0), PAY_DUOBAOTONG_ALIPAY_APP("多宝通_支付宝APP充值", 1), 
	PAY_DUOBAOTONG_WECHAT_WEB("多宝通_微信WEB充值", 2), PAY_DUOBAOTONG_WECHAT_APP("多宝通_微信APP充值", 3), 
	PAY_DUOBAOTONG_BANK_WEB("多宝通_网银WEB充值", 4), PAY_DUOBAOTONG_BANK_APP("多宝通_网银APP充值", 5),
	
	PAY_IOS("苹果充值", 6),
	PAY_ALIPAY_WEB("支付宝WEB充值", 7), PAY_ALIPAY_APP("支付宝APP充值", 8), 
	PAY_WECHAT_WEB("微信WEB充值", 9), PAY_WECHAT_APP("微信APP充值", 10), 
	PAY_BANK_WEB("网银WEB充值", 11), PAY_BANK_APP("网银APP充值", 12),
	PAY_WECHAT_JS("微信公众号支付", 13);

	private String code;
	private int value;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	private OrderShareType(String code, int value) {
		this.code = code;
		this.value = value;
	}
}