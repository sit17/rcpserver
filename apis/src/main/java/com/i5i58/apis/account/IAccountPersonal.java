package com.i5i58.apis.account;

import java.io.IOException;
import java.util.List;

import com.i5i58.apis.constants.ResultDataSet;

/**
 * 个人信息管理接口
 * 
 * @author Administrator
 *
 */

public interface IAccountPersonal {

	/**
	 * 修改头像(小图)
	 * 
	 * @author lee
	 * @param accId
	 * @param iconUrl
	 * @return
	 */
	public ResultDataSet setIconSmall(String accId, String iconUrl);

	/**
	 * 修改头像(高清)
	 * 
	 * @author lee
	 * @param accId
	 * @param iconUrl
	 * @return
	 */
	public ResultDataSet setIconOrg(String accId, String iconUrl);

	/**
	 * 修改昵称
	 * 
	 * @author lee
	 * @param accId
	 * @param nickName
	 * @return
	 */
	public ResultDataSet setNickName(String accId, String nickName);

	/**
	 * 修改艺名
	 * 
	 * @author lee
	 * @param accId
	 * @param stageName
	 * @return
	 */
	public ResultDataSet setStageName(String accId, String stageName);

	/**
	 * 性别
	 * 
	 * @author lee
	 * @param accId
	 * @param gender
	 * @return
	 */
	public ResultDataSet setGender(String accId, String gender);

	/**
	 * 修改生日
	 * 
	 * @author lee
	 * @param accId
	 * @param brith
	 * @return
	 */
	public ResultDataSet setBirth(String accId, long brith);

	/**
	 * 修改地区
	 * 
	 * @author lee
	 * @param accId
	 * @param address
	 * @return
	 */
	public ResultDataSet setAddress(String accId, String address);

	/**
	 * 修改个性签名
	 * 
	 * @author lee
	 * @param accId
	 * @param personalBrief
	 * @return
	 */
	public ResultDataSet setPersonalBrief(String accId, String personalBrief);

	/**
	 * 获取我的个人信息
	 * 
	 * @author lee
	 * @param accId
	 * @return
	 */
	public ResultDataSet getMyPersonal(String accId);

	/**
	 * 获取我的信息（手机我的页面顶部）
	 * 
	 * @author frank
	 * @param accId
	 * @return
	 */
	public ResultDataSet getMyInfo(String accId);

	/**
	 * 获取我的信息（手机我的页面顶部）（姓名字段为nickName）；
	 * 
	 * @author frank
	 * @param accId
	 * @return
	 */
	public ResultDataSet getMyInfo1(String accId);

	/**
	 * 获取TA的信息
	 * 
	 * @param accId
	 * @return
	 */
	public ResultDataSet getTAInfo(String accId);

	/**
	 * 获取TA的信息（姓名属性nickName）
	 * 
	 * @param accId
	 * @return
	 */
	public ResultDataSet getTAInfo1(String accId);

	/**
	 * 选择坐骑
	 * 
	 * @author frank
	 * @param accId
	 * @param vipNorMount
	 * @param guardMount
	 * @param fansClubMount
	 * @return
	 */
	public ResultDataSet selectMount(String accId, String selectJson);

	/**
	 * 购买坐骑
	 * 
	 * @author frank
	 * @param accId
	 * @param mountId
	 * @param clientIP
	 * @param month
	 * @return
	 */
	public ResultDataSet buyMount(String accId, int mountId, String clientIP, int month);

	/**
	 * 查询我的钱包
	 * 
	 * @author frank
	 * @param accId
	 * @param appKey
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet getWallet(String accId) throws IOException;

	/**
	 * 查询我的坐骑
	 * 
	 * @author cw
	 * @param accId
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	public ResultDataSet getMyMount(String accId, int pageSize, int pageNum) throws IOException;

	/**
	 * 获取我的账号
	 * 
	 * @author frank
	 * @param accId
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet getMyAccount(String accId) throws IOException;

	/**
	 * 获取我的账号(姓名属性为nickname)
	 * 
	 * @author frank
	 * @param accId
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet getMyAccount1(String accId) throws IOException;


	/**
	 * 获取我的守护
	 * 
	 * @author frank
	 * @param accId
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet getMyGuard(String accId, int pageNum, int pageSize) throws IOException;

	/**
	 * 修改我的个人信息
	 * 
	 * @author lee
	 * @param accId
	 * @param nickName
	 * @return
	 */
	public ResultDataSet setMyInfo(String accId, String nickName, long brith, String address, byte gender, 
			String location, String signature, String personalBrief, String stageName)
			throws IOException;

	/**
	 * 修改我的签名
	 * 
	 * @author frank
	 * @param accId
	 * @param signature
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet setSignature(String accId, String signature) throws IOException;

	/**
	 * 获取多个马甲相关账号信息
	 * 
	 * @author frank
	 * @param accIds
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet getMajiaAccounts(List<String> accIds) throws IOException;
	
	/**
	 * 设置绑定手机号
	 * 
	 * @author songfl
	 * @param accId
	 * @param phoneNo
	 * @param password
	 * @param verifyCode
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet setBindMobile(String accId, String phoneNo, String password, String verifyCode) throws IOException;
	
	/**
	 * 修改绑定手机号
	 * 
	 * @author songfl
	 * @param accId
	 * @param password
	 * @param phoneNo
	 * @param verifyCode
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet modifyBindMobile(String accId, String password, String phoneNo, String verifyCode) throws IOException;
	
	/**
	 * 获取绑定的手机号
	 * 
	 * @author songfl
	 * @param accId
	 * */
	public ResultDataSet getBindMobile(String accId)throws IOException;
	
	/**
	 * 发送验证码，验证绑定手机号
	 * 
	 * @author songfl
	 * @param accId
	 * @param phoneNo
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet sendBindingMobileVerifyCode(String accId, String phoneNo) throws IOException;
	
	/**
	 * 根据accId获取头像
	 * 
	 * @author songfl
	 * @param accId
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet getFaceUrlByAccId(String accId) throws IOException;
	
	/**
	 * 根据openId获取头像
	 * 
	 * @author songfl
	 * @param openId
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet getFaceUrlByOpenId(String openId) throws IOException;
	
	/**
	 * 获取用户属性
	 * 
	 * @author songfl
	 * @param accId
	 * */
	public ResultDataSet getAccountProperty(String accId) throws IOException;
}
