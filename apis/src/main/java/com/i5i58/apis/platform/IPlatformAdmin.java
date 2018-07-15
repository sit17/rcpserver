package com.i5i58.apis.platform;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

public interface IPlatformAdmin {

	/**
	 * OpendIds表插入测试数据
	 * 
	 * @author frank
	 * @return
	 * @throws IOException
	 */
	ResultDataSet insertTestDataToOpenIds(String superAccId) throws IOException;

	/**
	 * 
	 * 添加超管
	 * 
	 * @author frank
	 * @param superAccId
	 * @param accId
	 * @param verAnchorId
	 * @param vergroupProfile
	 * @param createTopGroup
	 * @return
	 */
	ResultDataSet addSuperAdmin(String superAccId, String accId, long brithday, String depart, String email,
			byte gender, String location, String realName, int auth);

	/**
	 * 禁用
	 * 
	 * @author frank
	 * @param superAccId
	 * @param accId
	 * @return
	 */
	ResultDataSet disableSuperAdmin(String superAccId, String accId);

	/**
	 * 超管上线，清空超管缓存再加载数据库中的超管到缓存中
	 * 
	 * @author frank
	 * @param superAccId
	 * @return
	 */
	ResultDataSet updateSuperAdmin(String superAccId);

	/**
	 * 超管登录
	 * 
	 * @param openId
	 * @param password
	 * @param accountVersion
	 * @param clientIP
	 * @return
	 * @throws IOException
	 */
	ResultDataSet superLoginByOpenId(String openId, String password, int accountVersion, String clientIP)
			throws IOException;

	/**
	 * 查询超管列表
	 * 
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws IOException
	 */
	ResultDataSet querySuperAdminList(int pageSize, int pageNum) throws IOException;

	/**
	 * 获取轮播
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getCarousel(long id) throws IOException;
	
	/**
	 * 设置首页轮播，设置完清除对应轮播缓存
	 * 
	 * @author frank
	 * @param superAccId
	 * @param device
	 * @param index
	 * @param imgUrl
	 * @param action
	 * @param params
	 * @param cId
	 * @param hlsPullUrl
	 * @param httpPullUrl
	 * @param rtmpPullUrl
	 * @param yunXinRId
	 * @param startTime
	 *            轮播起始时间
	 * @param endTime
	 *            轮播结束时间
	 * @return
	 * @throws IOException
	 */
	ResultDataSet addCarousel(String superAccId, int device, int index, String imgUrl, String action,
			String params, boolean isChannel, String cId, long startTime, long endTime) throws IOException;

	/**
	 * 删除轮播
	 * 
	 * @author frank
	 * @param superAccId
	 * @param cfId
	 *            轮播ID
	 * @return
	 * @throws IOException
	 */
	ResultDataSet removeCarousel(String superAccId, long id) throws IOException;

	/**
	 * 修改轮播
	 * 
	 * @author frank
	 * @param superAccId
	 * @param cfId
	 * @param device
	 * @param index
	 * @param imgUrl
	 * @param action
	 * @param params
	 * @param cId
	 * @param hlsPullUrl
	 * @param httpPullUrl
	 * @param rtmpPullUrl
	 * @param yunXinRId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws IOException
	 */
	ResultDataSet updateCarousel(String superAccId, long cfId, int device, int index, String imgUrl, String action,
			String params, boolean isChannel, String cId, long startTime, long endTime) throws IOException;

	/**
	 * 查询指定时间内轮播列表(播出时段部分在指定时间内也算)
	 * 
	 * @author frank
	 * @param device{0:全部，1:pc，2:web，3:手机}
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws IOException
	 */
	ResultDataSet queryCarousel(int device, long startTime, long endTime) throws IOException;

	/**
	 * 清空轮播缓存
	 * 
	 * @author frank
	 * @return
	 * @throws IOException
	 */
	ResultDataSet refreashCarouselCache() throws IOException;
	
	/**
	 * 清空账号缓存
	 * 
	 * @author frank
	 * @return
	 * @throws IOException
	 */
	ResultDataSet syncHotAccountCache(String superAccid) throws IOException;

	/**
	 * 清空频道缓存
	 * 
	 * @author frank
	 * @return
	 * @throws IOException
	 */
//	ResultDataSet clearHotChannelCache(String superAccid) throws IOException;
	
	/**
	 * 同步数据至redis缓存
	 * 
	 * @author frank
	 * @param superAccid
	 * @return
	 */
	ResultDataSet syncHotChannel(String superAccid) throws IOException;
	
	/**
	 * 清除HotChannel在redis缓存
	 * 
	 * @author frank
	 * @param superAccid
	 * @return
	 */
	ResultDataSet clearHotChannel(String superAccid) throws IOException;

	/**
	 * 清空频道观众
	 * @author frank
	 * @param superAccid
	 * @return
	 * @throws IOException
	 */
	ResultDataSet clearChannelViewer(String superAccid) throws IOException;
	
	/**
	 * 增加频道id库存
	 * @author songfl
	 * @param count 增长量
	 * */
	ResultDataSet addChannelId(int count);
	
	/**
	 * 获取一个随机的空闲频道id
	 * @author songfl
	 * @param consume 是否立刻消耗该id
	 * */
	ResultDataSet getRandomChannelId(boolean consume);
	
	/**
	 * 查询充值记录
	 * @author 
	 * @param status
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	ResultDataSet getOnlineOrders(int status,int pageNum,int pageSize);
}
