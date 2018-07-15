package com.i5i58.apis.channel;

import com.i5i58.apis.constants.ResultDataSet;

public interface IChannelPlay {
	/**
	 * 送礼物
	 * 
	 * @author frank
	 * @param accId
	 * @param device{0:none，1:pc，2:web，3:ios,
	 *            4:android}
	 * @param cId
	 * @param giftId
	 * @param giftCount
	 * @return
	 */
	public ResultDataSet giveChannelGift(String accId, int device, String cId, int giftId, int giftCount,
			int continuous,String clientIp);

	/**
	 * 弹幕
	 * 
	 * @author frank
	 * @param accId
	 * @param device
	 * @param cId
	 * @param content
	 * @return
	 */
	public ResultDataSet driftComment(String accId, int device, String cId, String content,String clientIp);
	
	/**
	 * 获取周贡献榜
	 * 
	 * @author Lee
	 * @param cId
	 * @return
	 */
	public ResultDataSet getWeekOffer(String cId);

	/**
	 * 请求连麦
	 * 
	 * @author frank
	 * @param accId
	 * @param cId,自己的频道
	 * @param connCid,对方的频道
	 * @return
	 */
	public ResultDataSet requestConnectMic(String accId, String cId, String connCid);

	/**
	 * 回应连麦请求
	 * 
	 * @author frank
	 * @param accId
	 * @param connMicOrderId,连麦订单号
	 * @param agree
	 * @return
	 */
	public ResultDataSet responseConnectMic(String accId, String connMicOrderId, boolean agree);

	/**
	 * 获取沙发列表
	 * 
	 * @author frank
	 * @param cId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public ResultDataSet getSofaList(String cId, int pageNum);

	/**
	 * 获取观众分页列表
	 * 
	 * @author frank
	 * @param cId
	 * @param pageNum
	 * @return
	 */
	public ResultDataSet getViewerList(String cId, int pageNum);

	/**
	 * 获取聊天室地址forweb
	 * 
	 * @param roomId
	 * @param accId
	 * @param device
	 * @return
	 */
	public ResultDataSet getChatRoomAddr(String roomId, String accId);

	/**
	 * 获取频道配置
	 * @param device{0:none，1:pc，2:web，3:ios,
	 *            4:android}
	 * @param giftVersion
	 * @param mountVersion
	 * @param animationVersion
	 * @return
	 */
	public ResultDataSet getChannelConfig(int device, String giftVersion, String mountVersion,
			String animationVersion);

	/**
	 * 进入直播间
	 * 
	 * @author frank
	 * @param accId
	 * @param device{0:none，1:pc，2:web，3:ios,
	 *            4:android}
	 * @param cId
	 * @param giftVersion
	 * @param mountVersion
	 * @param htmlVersion
	 * @return
	 */
	public ResultDataSet enterChannel(String accId, int device, String cId, String sessionId);

	/**
	 * 重新进入直播间
	 * 
	 * @author frank
	 * @param accId
	 * @param device
	 * @param cId
	 * @return
	 */
	public ResultDataSet reEnterChannel(String accId, int device, String cId, String sessionId);

	/**
	 * 无登录进入直播间
	 * 
	 * @author frank
	 * @param device{0:none，1:pc，2:web，3:ios,
	 *            4:android}
	 * @param cId
	 * @param giftVersion
	 * @param mountVersion
	 * @param htmlVersion
	 * @return
	 */
	public ResultDataSet enterChannelNoAcc(int device, String cId);

	/**
	 * 退出直播间
	 * 
	 * @author frank
	 * @param accId
	 * @param cId
	 * @return
	 */
	public ResultDataSet exitChannel(String accId, String cId, String sessionId);

	/**
	 * 直播时通知
	 * 
	 * @author frank
	 * @param accid
	 * @param cId
	 * @param isNotify
	 * @return
	 */
	public ResultDataSet noticeLive(String accid, String cId, boolean isNotify);
	
	/**
	 * 获取直播时通知状态
	 * 
	 * @author songfl
	 * @param accid
	 * @param cId
	 * @return
	 */
	public ResultDataSet getNoticeLiveStatus(String accid, String cId);

	/**
	 * 获取直播时通知我的且正在直播的频道
	 * @author songfl
	 * @param accId
	 * @return
	 * */
	public ResultDataSet getLivingChannelNoticeMe(String accId);
	
	/**
	 * 设置麦序
	 * 
	 * @author Lee
	 * @param accId
	 * @param micAccId
	 * @param cId
	 * @param index
	 * @return
	 */
	public ResultDataSet setMicSequence(String accId, String micAccId, String cId, int index);

	/**
	 * 移除麦序
	 * 
	 * @author frank
	 * @param accId
	 * @param micAccId
	 * @param cId
	 * @return
	 */
	public ResultDataSet removeMicSequence(String accId, String micAccId, String cId);

	/**
	 * 清空麦序
	 * 
	 * @param accId
	 * @param cId
	 * @return
	 */
	public ResultDataSet clearMicSequence(String accId, String cId);

	/**
	 * 获取麦序
	 * 
	 * @author frank
	 * @param cId
	 * @return
	 */
	public ResultDataSet getMicSequence(String cId, int pageNum, int pageSize);

	/**
	 * 获取推流地址
	 * 
	 * @author frank
	 * @param accId
	 * @param cId
	 * @return
	 */
	public ResultDataSet getPushAddr(String accId, String cId);

	/**
	 * 开始推流
	 * 
	 * @author frank
	 * @param cId
	 * @return
	 */
	public ResultDataSet openPush(String accId, String cId, int device);

	/**
	 * 结束推流
	 * 
	 * @author frank
	 * @param cId
	 * @return
	 */
	public ResultDataSet closePush(String accId, String cId);

	/**
	 * 获取频道马甲列表
	 * 
	 * @author frank
	 * @param cId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public ResultDataSet getMajiaList(String cId, Integer pageNum, Integer pageSize);

	/**
	 * 添加频道马甲
	 * 
	 * @author frank
	 * @param admin 管理员需要分配荣誉权限
	 * @param cId
	 * @param accId
	 * @param majia
	 * @return
	 */
	public ResultDataSet addMajia(String admin, String cId, String accId, int majia);

	/**
	 * 删除马甲
	 * 
	 * @author frank
	 * @param admin 管理员需要分配荣誉权限
	 * @param cId
	 * @param accId
	 * @return
	 */
	public ResultDataSet removeMajia(String admin, String cId, String accId);
	
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
	
	/**
	 * 获取有马甲的频道
	 * 
	 * @author songfl
	 * @param accId
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	public ResultDataSet getChannelsByMajia(String accId, int pageSize, int pageNum);
	
	/**
	 * 
	 * @param huangling
	 * @param admin 频道管理员或者主播拥有将用户踢出直播间权限
	 * @param accId
	 * @param cId
	 * @return
	 */
	public ResultDataSet kickChannel(String admin,String accId, String cId);
	
	/**
	 * 
	 * @param huangling 
	 * @param accId 获取我的关注主播的个数
	 * @return
	 */
	public ResultDataSet getNoticeCount(String accId);
	
	/**
	 * 直播频道回调
	 * 
	 * @author songfl
	 * 
	 * @param cid	  云信频道ID
	 * @param time   频道状态更改时间戳(毫秒) 
	 * @param status 频道状态（0：空闲； 1：直播； 2：禁用； 3：直播录制）
	 * */
	public ResultDataSet chStatusCallabck(String body, String sign);
}
