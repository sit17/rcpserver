package com.i5i58.apis.social;

import com.i5i58.apis.constants.ResultDataSet;

/**
 * 朋友圈服务接口
 * @author frank
 *
 */
public interface IFriendsNet {

	/**
	 * 获取散文（图文、视频）分页查询
	 * @author frank
	 * @param pageSize
	 * @param pageNum
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	ResultDataSet getEssays(Integer pageSize, Integer pageNum, 
			String sortBy, String sortDirection);

	/**
	 * 上传散文
	 * @author frank
	 * @param accId
	 * @param type (briefshot or pictrue or song or ...)
	 * @param url
	 * @param text
	 * @return
	 */
	ResultDataSet postEssay(String accId, String type, String url, String text);

	/**
	 * 关注
	 * @author frank
	 * @param accId
	 * @param attAccId
	 * @return
	 */
	ResultDataSet follow(String accId, String attAccId);

	/**
	 * 取消关注
	 * @author frank
	 * @param accId
	 * @param attAccId
	 * @return
	 */
	ResultDataSet cancelFollow(String accId, String attAccId);

	/**
	 * 我的关注
	 * @author frank
	 * @param accId
	 * @param attAccId
	 * @return
	 */
	ResultDataSet getMyFollow(String accId, Integer pageNum);

	/**
	 * 我的粉丝
	 * @author frank
	 * @param accId
	 * @param attAccId
	 * @return
	 */
	ResultDataSet getMyFans(String accId, Integer pageNum);

	/**
	 * TA的关注
	 * @author frank
	 * @param accId
	 * @param attAccId
	 * @return
	 */
	ResultDataSet getTaFollow(String accId, String ta, Integer pageNum);

	/**
	 * TA的粉丝
	 * @author frank
	 * @param accId
	 * @param attAccId
	 * @return
	 */
	ResultDataSet getTaFans(String accId, String ta, Integer pageNum);

	/**
	 * TA的粉丝数量
	 * @param accId
	 * @param ta
	 * @return
	 */
	ResultDataSet getTaFansCount(String accId, String ta);
	
	/**
	 * 获取关注状态，0，未关注，1已关注，2被关注， 3互相关注
	 * @author frank
	 * @param accId
	 * @param target
	 * @return
	 */
	ResultDataSet getFollowStatus(String accId, String target);

	/**
	 * 是否关注
	 * 
	 * @author frank
	 * @param accId
	 * @param target
	 * @return
	 */
	ResultDataSet isFollow(String accId, String target);

	/**
	 * 获取关注数量
	 * 
	 * @author frank
	 * @param accId
	 * @return
	 */
	ResultDataSet getFollowCount(String accId);

	/**
	 * 获取粉丝数量
	 * 
	 * @author frank
	 * @param accId
	 * @return
	 */
	ResultDataSet getFansCount(String accId);
	
	/**
	 * 获取关注与粉丝数量
	 * 
	 * @author frank
	 * @param accId
	 * @return
	 */
	ResultDataSet getFollowFansCount(String accId);
}
