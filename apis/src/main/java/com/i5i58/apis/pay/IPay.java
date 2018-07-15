package com.i5i58.apis.pay;

import java.io.IOException;
import java.util.IllegalFormatCodePointException;
import java.util.Map;

import com.i5i58.apis.constants.ResultDataSet;

public interface IPay {
	/**
	 * 生成充值订单
	 * 
	 * @author cw
	 * @param
	 * @return
	 */
	ResultDataSet createOrder(String accId, String toAccId, int shareId, long orderAmount, long payAmount,
			String ipAddress, String serialNum, int device) throws IOException;

	/**
	 * 生成充值订单(微信公共号)
	 * 
	 * @author songfl
	 * @param
	 * @return
	 */
	ResultDataSet createOrderForWxOffical(String accId, String toAccId, int shareId, long orderAmount, long payAmount,
			String ipAddress, String serialNum, int device, String wxOpenId) throws IOException;

	
	/**
	 * 充值回调方法
	 * 
	 * @author cw
	 * 
	 * @param state
	 *            订单状态 1充值成功；2、充值失败
	 * @param customerId
	 *            商户ID
	 * @param sd51no
	 *            订单在网关的订单号
	 * @param sdcustomno
	 *            商户订单号
	 * @param orderMoney
	 *            订单实际金额
	 * @param cardno
	 *            支付类型
	 * @param mark
	 *            商户自定义信息
	 * @param sign
	 *            签名
	 * @param des
	 *            支付备注
	 * @return
	 * @throws IOException
	 */
	ResultDataSet payCallBack(int state, String customerId, String sd51no, String sdcustomno, long orderMoney,
			String cardno, String mark, String sign, String des,String clientIp) throws IOException;

	/**
	 * 获取苹果支付货物列表
	 * 
	 * @author frank
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getApplePayList(String accId, String version) throws IOException;

	/**
	 * 验证苹果票据有效性
	 * 
	 * @author cw
	 * @param accId
	 * @param orderId
	 * @param receipt
	 *            苹果支付票据
	 * @param chooseEnv
	 *            选择验证类型 沙盒 ｜ 正式
	 * @return
	 */
	ResultDataSet setIapCertificate(String accId, String orderId, String receipt, boolean chooseEnv,String clientIp);
	
	/**
	 * 验证苹果票据有效性
	 * 
	 * @author songfl
	 * @param accId
	 * @param orderId
	 * @param receipt
	 * @param device
	 * @param version
	 * @return
	 */
	ResultDataSet verifyIapCertificate(String accId, String orderId, String receipt, int device, String version,String clientIp);

	/**
	 * 支付宝回调
	 * @author frank
	 * @param params
	 * @return
	 */
	ResultDataSet aliPayCallback(Map<String, String[]> params,String clientIp);
	
	/**
	 * 微信支付回调
	 * @author songfl
	 * @param params
	 * @return
	 * */
	ResultDataSet wxPayCallback(String params,String clientIp);
	
	/**
	 * 查询订单状态
	 * @author songfl
	 * @param accId
	 * @param orderId
	 * @return
	 * */
	ResultDataSet queryOrderStatus(String accId, String orderId);
	
	/**
	 * 查询订单
	 * @author songfl
	 * @param orderId
	 * @return
	 * */
	ResultDataSet getOrderById(String orderId);
	
	/**
	 * 获取商品列表
	 * @author songfl
	 * @param 
	 * */
	ResultDataSet getProducts(int device);
	
	/**
	 * 验证支付宝网页支付同步结果
	 * */
	ResultDataSet verifyAlipayTradePagePayResult(Map<String, String[]> requestParams)throws Exception;
}
