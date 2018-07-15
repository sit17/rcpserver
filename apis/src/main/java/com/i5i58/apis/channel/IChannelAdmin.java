package com.i5i58.apis.channel;

import com.i5i58.apis.constants.ResultDataSet;

/**
 * 频道管理接口
 * 
 * @author Administrator
 *
 */
public interface IChannelAdmin {

	/**
	 * 获取我的频道
	 * 
	 * @param accId
	 * @return
	 */
	public ResultDataSet getMyChannels(String accId);

	/**
	 * 创建频道
	 * 
	 * @param accId
	 * @param createIp
	 * @return
	 */
	public ResultDataSet createChannel(String accId, String createIp);

	/**
	 * 修改频道名称
	 * 
	 * @param name
	 * @param accId
	 * @param cId
	 * @return
	 */
	public ResultDataSet editChannelName(String name, String accId, String cId);

	/**
	 * 修改频道公告
	 * 
	 * @param name
	 * @param accId
	 * @param cId
	 * @return
	 */
	public ResultDataSet editChannelNotice(String notice, String accId, String cId);

	/**
	 * 指派频道拥有者，只有上级拥有者或管理员才可以操作（超管可以）
	 * 
	 * @param name
	 * @param accId
	 * @param cId
	 * @return
	 */
	public ResultDataSet assignChannelOwner(String owner, String accId, String cId);

	/**
	 * 指派频道管理员，只有本频道拥有者，上级拥有者或管理员才可以操作（超管可以）
	 * 
	 * @param name
	 * @param accId
	 * @param cId
	 * @return
	 */
	public ResultDataSet assignChannelAdmin(String admin, String accId, String cId);

	/**
	 * 更新频道封面
	 * 
	 * @param imageUrl
	 * @param accId
	 * @param cId
	 * @return
	 */
	public ResultDataSet updateChannelCoverImage(String imageUrl, String accId, String cId);

	/**
	 * 重新获取推、拉流地址
	 * 
	 * @param accId
	 * @return
	 */
	public ResultDataSet getLiveAddress(String accId, String cId);
	
	/**
	 * 修改频道title
	 * cw
	 * @param title
	 * @param accId
	 * @param cId
	 * @return
	 */
	public ResultDataSet editChannelTitle(String title, String accId, String cId);
}
