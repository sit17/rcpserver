package com.i5i58.apis.im;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

public interface IIMTeam {

	/**
	 * 创建群
	 * @param accId
	 * @param tname
	 * @param members ["aaa","bbb"](JSONArray对应的accid，如果解析出错会报414)，一次最多拉200个成员
	 * @param msg 邀请发送的文字，最大长度150字节
	 * @param magree 管理后台建群时，0不需要被邀请人同意加入群，1需要被邀请人同意才可以加入群。其它会返回414
	 * @param joinMode 群建好后，sdk操作时，0不用验证，1需要验证,2不允许任何人加入。其它返回414
	 * @param custom
	 * @param clientIp
	 * @return
	 * @throws IOException
	 */
	ResultDataSet create(String accId, String tname, String members, String msg, String magree,
			String joinMode, String custom, String clientIp) throws IOException;

	/**
	 * 解散群
	 * @param accId
	 * @param tid
	 * @param clientIp
	 * @return
	 * @throws IOException
	 */
	ResultDataSet remove(String accId, String tid, String clientIp) throws IOException;

	/**
	 * 查询群成员，内部使用，暂不实现
	 * @param tids
	 * @param ope
	 * @return
	 * @throws IOException
	 */
	ResultDataSet query(String tids, String ope) throws IOException;

	/**
	 * 移交群主
	 * @param accId
	 * @param tid
	 * @param newOwner
	 * @return
	 * @throws IOException
	 */
	ResultDataSet changeOwner(String accId, String tid, String newOwner) throws IOException;
	
	
	/**
	 * 批量添加用户到群组
	 * @param accId
	 * @param tid	 群ID
	 * @param owner  群组accId
	 * @param members JSON 数组形式的参数，数组元素为需要添加到群组的用户accId
	 * @param magree 0:不需要被邀请人同意，1:需要被邀请人同意
	 * @param attach 自定义字段
	 * @return ResultDataSet @throws IOException
	 */
	ResultDataSet addTeamMembers(String accId, String tid, String owner, String members, String magree, String msg, String attach) throws IOException;
	
	
	/**
	 * 把用户踢出群
	 * @param accId
	 * @param tid
	 * @param owner
	 * @param member
	 * @param attach
	 * @return ResultDataSet @throws 
	 */
	ResultDataSet kickTeamMemeber(String accId, String tid, String owner, String member, String attach) throws IOException;
	
	/**
	 * 更新群信息
	 * @param accId  
	 * @param tid    
	 * @param tname  
	 * @param owner  
	 * @param announcement  Unnecessary
	 * @param intro  		Unnecessary
	 * @param joinMode 	 	Unnecessary 群建好后，sdk操作时，0不用验证，1需要验证,2不允许任何人加入
	 * @param custom  		Unnecessary
	 * @param icon   		Unnecessary
	 * @param beinvteMode   Unnecessary 被邀请人同意方式，0-需要同意(默认),1-不需要同意
	 * @param inviteMode 	Unnecessary 谁可以邀请他人入群，0-管理员(默认),1-所有人
	 * @param uptinfoMode 	Unnecessary 0-管理员(默认),1-所有人
	 * @param upcustomMode 	Unnecessary 0-管理员(默认),1-所有人
	 * @return ResultDataSet @throws 
	 */
	ResultDataSet updateTeam(String accId, String tid, String tname, String owner, String announcement, String intro,
							String joinMode, String custom, String icon, String beinvteMode, String inviteMode, 
							String uptinfoMode, String upcustomMode) throws IOException;
	
	/**
	 * 批量添加群管理员
	 * @param accId
	 * @param tid
	 * @param owner
	 * @param members JSON数组
	 * @return ResultDataSet @throws 
	 */
	ResultDataSet addTeamManager(String accId, String tid, String owner, String members) throws IOException;
	
	/**
	 * 批量移除群管理员
	 * @param accId
	 * @param tid
	 * @param owner
	 * @param members JSON数组
	 * @return ResultDataSet @throws 
	 */
	ResultDataSet removeTeamManager(String accId, String tid, String owner, String members) throws IOException;
	
	/**
	 * 查询用户已加入的群
	 * @param accId
	 * @return
	 * @throws IOException ResultDataSet @throws 
	 */
	ResultDataSet queryUserJoinedTeamsInfo(String accId) throws IOException;
	
	/**
	 * 更新用户群昵称
	 * @param tid
	 * @param owner
	 * @param accId
	 * @param nick
	 * @return
	 * @throws IOException ResultDataSet @throws 
	 */
	ResultDataSet updateTeamNick(String tid, String owner, String accId, String nick) throws IOException;
	
	/**
	 * 修改群消息提醒设置
	 * @param accId
	 * @param tid
	 * @param targetAccId
	 * @param ope 1：关闭消息提醒，2：打开消息提醒，其他值无效
	 * @return
	 * @throws IOException ResultDataSet @throws 
	 */
	ResultDataSet updateMuteTeamConfig(String accId, String tid, String targetAccId, String ope) throws IOException ;
	
	/**
	 * 对群成员禁言或解禁
	 * @param accId
	 * @param tid
	 * @param owner
	 * @param targetAccId
	 * @param mute 1-禁言，0-解禁
	 * @return
	 * @throws IOException ResultDataSet @throws 
	 */
	ResultDataSet setTeamMuteList(String accId, String tid, String owner, String targetAccId, String mute) throws IOException;
	
	/**
	 * 离开群
	 * @param tid
	 * @param accId
	 * @return
	 * @throws IOException ResultDataSet @throws 
	 */
	ResultDataSet leaveTeam(String tid, String accId) throws IOException;
}
