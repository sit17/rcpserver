package com.i5i58.apis.account;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

/**
 * 注册账号服务接口
 * 
 * @author frank
 *
 */
public interface IAccountRegister {

	/**
	 * 注册账号
	 * 
	 * @author frank
	 * @param phoneNo
	 * @param password
	 * @param clientIP
	 * @param verifCode
	 * @return
	 * @throws IOException
	 */
	ResultDataSet 
	
	registerAccountTemp(String phoneNo, String password, String clientIP, String verifCode, int device, String serialNum,String realName, String idCard)
			throws IOException;
	/**
	 * 注册账号
	 * 
	 * @author frank
	 * @param phoneNo
	 * @param password
	 * @param clientIP
	 * @param verifCode
	 * @return
	 * @throws IOException
	 */
	ResultDataSet registerAccount(String phoneNo, String password, String clientIP, String verifCode, int device, String serialNum)
			throws IOException;

	/**
	 * 发送手机验证码
	 * 
	 * @author frank
	 * @param phoneNo
	 * @return
	 * @throws IOException
	 */
	ResultDataSet sendCode(String phoneNo) throws IOException;

	/**
	 * 验证码检测
	 * 
	 * @author Lee
	 * @param phoneNo
	 * @param verifCode
	 * @return
	 * @throws IOException
	 */
	ResultDataSet checkCode(String phoneNo, String verifCode) throws IOException;

	/**
	 * 验证手机号码
	 * @param phoneNo
	 * @return
	 * @throws IOException
	 */
	ResultDataSet checkPhoneNo(String phoneNo) throws IOException;	

	
}
