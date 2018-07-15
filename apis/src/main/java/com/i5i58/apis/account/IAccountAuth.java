package com.i5i58.apis.account;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

/**
 * 实名认证
 * 
 * @author hexiaoming
 *
 */
public interface IAccountAuth {

	/**
	 * 主播的实名认证申请
	 * 
	 * @author hexiaoming
	 * @param
	 * @return
	 */
	ResultDataSet anchorAuth(String accId, String certificateId, String realName, String imgCertificateFace,
			String imgcertificateBack, String imgPerson,String bankCardNum,String bankKeepPhone,String location,String bankName) throws IOException;

	/**
	 * 普通用户的实名认证申请
	 * 
	 * @author hexiaoming
	 */
	ResultDataSet userAuth(String accId, String realName, String certificateId) throws IOException;
	
	/**
	 * 获取主播实名认证状态
	 * @param accId
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getMyUserAuthInfo(String accId) throws IOException;
	
	/**
	 * 发送实名认证的验证码
	 * @author songfl
	 * @param accId
	 * @return
	 * @throws IOException
	 */
	ResultDataSet sendAuthVerifyCode(String accId) throws IOException;
	
	/**
	 * 查询我的实名认证信息
	 * @author songfl
	 * @param accId
	 * @param bindMobile
	 * @return
	 * @throws IOException
	 */
	ResultDataSet queryMyAuthInfo(String accId) throws IOException;

}
