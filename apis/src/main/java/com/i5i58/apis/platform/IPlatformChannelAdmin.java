package com.i5i58.apis.platform;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

public interface IPlatformChannelAdmin {

	/**
	 * 频道禁播/开启
	 * 
	 * @author frank
	 * @param superAccid
	 * @param cId
	 * @param enable
	 * @return
	 * @throws IOException
	 */
	ResultDataSet enableChannel(String superAccid, String cId, boolean enable) throws IOException;

	/**
	 * 获取直播间详细信息,所属经纪公司, 主播, 直播间等
	 * 
	 * @author frank
	 * @param channelId
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getChannelInfo(String cId) throws IOException;

	/**
	 * 获取频道守护列表
	 * 
	 * @author frank
	 * @param cId
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getChannelGuardlist(String cId, int pageSize, int pageNum) throws IOException;

	/**
	 * 按頻道ID，頻道名稱、主播ID等查找頻道
	 * 
	 * @author frank
	 * @param param
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws IOException
	 */
	ResultDataSet queryChannelList(String param, int pageSize, int pageNum) throws IOException;

	/**
	 * 添加频道右侧信息
	 * 
	 * @author frank
	 * @param action
	 * @param target
	 * @param imgUrl
	 * @param name
	 * @param params
	 * @return
	 * @throws IOException
	 */
	ResultDataSet addChannelRightInfo(String superAccid, int id, String action, String target, String imgUrl,
			String name, String params) throws IOException;

	/**
	 * 更新频道右侧信息
	 * 
	 * @author frank
	 * @param action
	 * @param target
	 * @param imgUrl
	 * @param name
	 * @param params
	 * @return
	 * @throws IOException
	 */
	ResultDataSet updateChannelRightInfo(String superAccid, int id, String action, String target, String imgUrl,
			String name, String params) throws IOException;

	/**
	 * 删除频道右侧信息
	 * 
	 * @author frank
	 * @param id
	 * @return
	 * @throws IOException
	 */
	ResultDataSet deleteChannelRightInfo(String superAccid, int id) throws IOException;

	/**
	 * 获取频道右侧信息
	 * 
	 * @author frank
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getChannelRightInfo(int id) throws IOException;

	/**
	 * 获取所有频道右侧信息
	 * 
	 * @author frank
	 * @return
	 * @throws IOException
	 */
	ResultDataSet queryChannelRightInfo() throws IOException;

	/**
	 * 同步频道右侧信息到redis缓存
	 * 
	 * @author frank
	 * @return
	 * @throws IOException
	 */
	ResultDataSet syncChannelRightInfo(String superAccid) throws IOException;

	/**
	 * 获取新秀/大家都在看的频道
	 * 
	 * @author frank
	 * @param superAccid
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getNewLotChannelList(String superAccid, int pageNum, int pageSize) throws IOException;

	/**
	 * 添加新秀/大家都在看的频道
	 * 
	 * @author frank
	 * @param superAccid
	 * @return
	 * @throws IOException
	 */
	ResultDataSet addNewLotChannel(String superAccid, String cId, int type, int status) throws IOException;

	/**
	 * 删除新秀/大家都在看的频道
	 * 
	 * @author frank
	 * @param superAccid
	 * @return
	 * @throws IOException
	 */
	ResultDataSet deleteNewLotChannel(String superAccid, String cId) throws IOException;

	/**
	 * 同步新秀/大家都在看频道
	 * 
	 * @param superAccid
	 * @return
	 * @throws IOException
	 */
	ResultDataSet syncNewLotChannel(String superAccid) throws IOException;

	/**
	 * 修改频道类型
	 * 
	 * @author frank
	 * @param superAccid
	 * @param cId
	 * @param type
	 * @return
	 * @throws IOException
	 */
	ResultDataSet editChannelType(String superAccid, String cId, int type) throws IOException;


	/**
	 * 清空首页热门缓存
	 * 
	 * @author frank
	 * @return
	 * @throws IOException
	 */
	ResultDataSet clearHotPageCache() throws IOException;

	/**
	 * 同步缓存频道数据到mysql
	 * 
	 * @author frank
	 * @return
	 * @throws IOException
	 */
	ResultDataSet syncChannelData() throws IOException;
	
	/**
	 * 获取主播守护列表
	 * 
	 * @author songfl
	 * @param accId
	 * @param pageNum
	 * @param pageSize
	 * */
	ResultDataSet queryAchorGuardList(String accId, int pageNum, int pageSize);
	
	/**
	 * 频道禁言/解禁
	 * 
	 * @author songfl
	 * @param admin 管理员需要分配禁言权限
	 * @param cId
	 * @param accId
	 * @param optvalue "true" or "false"
	 * @return
	 */
	public ResultDataSet setMute(String admin, String cId, String accId, String optValue);
	
	/**
	 * 频道临时禁言/解禁
	 * 
	 * @author songfl
	 * @param admin 管理员需要分配禁言权限
	 * @param cId
	 * @param accId
	 * @param duration =0时解禁，>0时禁言,最大24小时，单位秒
	 * @return
	 */
	public ResultDataSet setTempMute(String admin, String cId, String accId, Long duration);
}
