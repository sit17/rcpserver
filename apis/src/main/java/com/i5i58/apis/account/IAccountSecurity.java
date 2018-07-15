package com.i5i58.apis.account;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

/**
 * 账号安全服务接口
 * @author frank
 *
 */
public interface IAccountSecurity {

	/**
	 * 忘记密码通过手机号码、ID、email查询手机、eamil
	 * @author frank
	 * @param type (phone or email or id)
	 * @param account (value of type)
	 * @return
	 */
	ResultDataSet queueForgotPassword(String type, String account) throws IOException;
	
	/**
	 * 重置密码
	 * @author songfl
	 * @param type (accId,openId,phoneNo,email)
	 * @param account (value of type)
	 * @param verifyCode
	 * @param password
	 * @return
	 */
	ResultDataSet resetPassword(String type, String account, String verifyCode, String password) throws IOException;
	
	/**
	 * 发送手机验证码
	 * @author songfl
	 * @param type (accId,openId,phoneNo,email)
	 * @param account (value of type)
	 * @return
	 * @throws IOException
	 */
	ResultDataSet sendVerifToPhone(String type, String account) throws IOException;
	
	/**
	 * 重置密码的时候, 检查验证码是否正确
	 * @author songfl
	 * @param type (accId,openId,phoneNo,email)
	 * @param account (value of type)
	 * @param verifyCode
	 * @return
	 * */
	ResultDataSet isValidVerifyCode(String type, String account, String verifyCode) throws IOException;
	
	/**
	 * 验证登录密码
	 * @author songfl
	 * @param password
	 * @return
	 * @throws IOException
	 * */
	ResultDataSet checkLoginPassword(String accId, String password) throws IOException;

	/**
	 * 验证绑定手机
	 * @author HL
	 * @param bindMobile
	 * @return
	 * @throws IOException
	 * */
	ResultDataSet verifyBindMobile(String accId,String bindMobile) throws IOException;
}
