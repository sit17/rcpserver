package com.i5i58.apis.account;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

/**
 * 账号登陆服务接口
 * 
 * @author Administrator
 *
 */
public interface IAccountLogin {

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
	 * 发送登陆短信验证码
	 * @author frank
	 * @param openId
	 * @param clientIP
	 * @param device
	 * @param serialNum
	 * @return
	 * @throws IOException
	 */
	ResultDataSet sendSMSForlogin(String openId, String clientIP, int device,
			String serialNum) throws IOException;

	/**
	 * 短信验证码登陆
	 * @author frank
	 * @param openId
	 * @param sms
	 * @param clientIP
	 * @param device
	 * @param serialNum
	 * @return
	 * @throws IOException
	 */
	ResultDataSet loginBySMS(String openId, String sms, String clientIP, int device,
			String serialNum) throws IOException;
	
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
	ResultDataSet loginByThird(int third, String openId, String name, String face, byte gender, String clientIP, int device,
			String serialNum) throws IOException;

	/**
	 * 第三方登陆
	 * 
	 * @author frank
	 * @param third
	 * @param openId
	 * @param uId
	 * @param unionId
	 * @param name
	 * @param face
	 * @param gender
	 * @param clientIP
	 * @param device
	 * @param serialNum
	 * @return
	 * @throws IOException
	 */
	ResultDataSet loginByThird1(int third, String openId, String uId, String unionId, String name, String face, byte gender, String clientIP, int device,
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
