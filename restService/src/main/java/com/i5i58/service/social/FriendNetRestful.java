package com.i5i58.service.social;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.social.IFriendsNet;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author frank
 *
 */
@Api(value = "社交-朋友圈")
@RestController
@RequestMapping("${rpc.controller.version}")
public class FriendNetRestful {

	private Logger logger = Logger.getLogger(getClass());

	/**
	 * Call Dubbo Service
	 */
	@Reference
	private IFriendsNet friendsNet;

	@ApiOperation(value = "关注", notes = "")
	@RequestMapping(value = "/social/follow", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet follow(@ApiParam(value = "对方AccId") @RequestParam(value = "target") String target) {
		ResultDataSet rds = friendsNet.follow(HttpUtils.getAccIdFromHeader(), target);
		return rds;
	}

	@ApiOperation(value = "取消关注", notes = "")
	@RequestMapping(value = "/social/cancelFollow", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet cancelFollow(@ApiParam(value = "对方AccId") @RequestParam(value = "target") String target) {
		ResultDataSet rds = friendsNet.cancelFollow(HttpUtils.getAccIdFromHeader(), target);
		return rds;
	}

	@ApiOperation(value = "获取我的关注", notes = "")
	@RequestMapping(value = "/social/getMyFollow", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getMyFollow(@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = friendsNet.getMyFollow(HttpUtils.getAccIdFromHeader(), pageNum);
		return rds;
	}

	@ApiOperation(value = "获取我的粉丝", notes = "")
	@RequestMapping(value = "/social/getMyFans", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getMyFans(@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = friendsNet.getMyFans(HttpUtils.getAccIdFromHeader(), pageNum);
		return rds;
	}

	@ApiOperation(value = "获取TA的关注", notes = "")
	@RequestMapping(value = "/social/getTaFollow", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getTaFollow(@ApiParam(value = "TA的 accId") @RequestParam(value = "ta") String ta,
			@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = friendsNet.getTaFollow(HttpUtils.getAccIdFromHeader(), ta, pageNum);
		return rds;
	}

	@ApiOperation(value = "获取TA的粉丝", notes = "")
	@RequestMapping(value = "/social/getTaFans", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getTaFans(@ApiParam(value = "TA的 accId") @RequestParam(value = "ta") String ta,
			@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = friendsNet.getTaFans(HttpUtils.getAccIdFromHeader(), ta, pageNum);
		return rds;
	}

	@ApiOperation(value = "获取TA的粉丝数量", notes = "")
	@RequestMapping(value = "/social/getTaFansCount", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getTaFansCount(@ApiParam(value = "TA的 accId") @RequestParam(value = "ta") String ta) {
		ResultDataSet rds = friendsNet.getTaFansCount(HttpUtils.getAccIdFromHeader(), ta);
		return rds;
	}

	@ApiOperation(value = "获取关注状态，0，未关注，1已关注，2被关注， 3互相关注", notes = "")
	@RequestMapping(value = "/social/getFollowStatus", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getFollowStatus(@ApiParam(value = "对方AccId") @RequestParam(value = "target") String target) {
		ResultDataSet rds = friendsNet.getFollowStatus(HttpUtils.getAccIdFromHeader(), target);
		return rds;
	}

	@ApiOperation(value = "是否关注", notes = "")
	@RequestMapping(value = "/social/isFollow", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet isFollow(@ApiParam(value = "对方AccId") @RequestParam(value = "target") String target) {
		ResultDataSet rds = friendsNet.isFollow(HttpUtils.getAccIdFromHeader(), target);
		return rds;
	}

	@ApiOperation(value = "获取关注数量", notes = "")
	@RequestMapping(value = "/social/getFollowCount", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getFollowCount() {
		ResultDataSet rds = friendsNet.getFollowCount(HttpUtils.getAccIdFromHeader());
		return rds;
	}

	@ApiOperation(value = "获取粉丝数量", notes = "")
	@RequestMapping(value = "/social/getFansCount", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getFansCount() {
		ResultDataSet rds = friendsNet.getFansCount(HttpUtils.getAccIdFromHeader());
		return rds;
	}

	@ApiOperation(value = "获取关注与粉丝数量", notes = "")
	@RequestMapping(value = "/social/getFollowFansCount", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getFollowFansCount() {
		ResultDataSet rds = friendsNet.getFollowFansCount(HttpUtils.getAccIdFromHeader());
		return rds;
	}
}
