package com.i5i58.apis.account;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

public interface IAccountLoginForJWT {

	/**
	 * ID登陆
	 * 
	 * @author frank
	 * @param openId
	 * @param password
	 * @param accountVersion
	 * @param clientIP
	 * @return
	 * @throws IOException
	 */
	ResultDataSet loginByOpenId(String openId, String password, int accountVersion, String clientIP, int device,
			String serialNum) throws IOException;

	/**
	 * 手机号码登陆
	 * 
	 * @author frank
	 * @param phoneNo
	 * @param password
	 * @param accountVersion
	 * @param clientIP
	 * @return
	 * @throws IOException
	 */
	ResultDataSet loginByPhoneNo(String phoneNo, String password, int accountVersion, String clientIP, int device,
			String serialNum) throws IOException;

	/**
	 * accId与token登陆
	 * 
	 * @author frank
	 * @param phoneNo
	 * @param accountVersion
	 * @param clientIP
	 * @return
	 * @throws IOException
	 */
	ResultDataSet loginByToken(String accId, int accountVersion, String clientIP, int device, String serialNum)
			throws IOException;

	/**
	 * 第三方登陆
	 * 
	 * @author frank
	 * @param third
	 * @param openId
	 * @param name
	 * @param face
	 * @param gender
	 * @param clientIP
	 * @param device
	 * @param serialNum
	 * @return
	 * @throws IOException
	 */
	ResultDataSet loginByThird(int third, String openId, String name, String face, byte gender, String clientIP,
			int device, String serialNum) throws IOException;

	/****
	 * 微信公众号登陆
	 * 
	 * @param wechatOpenId
	 * @param accountVersion
	 * @param clientIP
	 * @param device
	 * @param serialNum
	 * @return
	 * @throws IOException
	 */
	ResultDataSet loginByWechatOpenId(String wechatOpenId, int accountVersion, String clientIP, int device,
			String serialNum) throws IOException;
	
	/**
	 * 扫码登录
	 * @author songfl
	 * @param accId
	 * @param qrString
	 * @return
	 */
	ResultDataSet loginByQrCode(String accId, String qrString);
}
