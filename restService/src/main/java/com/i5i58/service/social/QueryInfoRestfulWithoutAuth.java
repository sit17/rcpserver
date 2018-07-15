package com.i5i58.service.social;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.social.IQueryInfoWithoutAuth;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author songfl
 *
 */
@Api(value = "查询信息-不需要登录验证")
@RestController
@RequestMapping("${rpc.controller.version}")
public class QueryInfoRestfulWithoutAuth {
	private Logger logger = Logger.getLogger(getClass());
	
	@Reference
	private IQueryInfoWithoutAuth iQueryInfoWithoutAuth;

	@ApiOperation(value = "获取TA的信息", notes = "")
	@RequestMapping(value = "/social/getTAInfo", method = RequestMethod.GET)
	public ResultDataSet getTAInfo(@ApiParam(value = "对方AccId") @RequestParam(value = "target") String target) {
		ResultDataSet rds = iQueryInfoWithoutAuth.getTAInfo(target);
		return rds;
	}
	
	@ApiOperation(value = "TA的关注", notes = "")
	@RequestMapping(value = "/social/getFollowByAccId", method = RequestMethod.GET)
	public ResultDataSet getFollowByAccId(@ApiParam(value = "TA的 accId") @RequestParam(value = "ta") String ta,
			@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = iQueryInfoWithoutAuth.getFollowByAccId(ta, pageNum);
		return rds;
	}
	
	@ApiOperation(value = "获取有马甲的频道", notes = "")
	@RequestMapping(value = "/social/getChannelsByMajia", method = RequestMethod.GET)
	public ResultDataSet getChannelsByMajia(@ApiParam(value = "目标用户id") @RequestParam(value = "targetAccId") String targetAccId,
			@ApiParam(value = "") @RequestParam(value = "pageSize") int pageSize,
			@ApiParam(value = "") @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = iQueryInfoWithoutAuth.getChannelsByMajia(targetAccId, pageSize, pageNum);
		return rds;
	}
	
	@ApiOperation(value="获取用户拥有的频道", notes="")
	@RequestMapping(value = "/social/getChannelByOwner", method = RequestMethod.GET)
	public ResultDataSet getChannelByOwner(@ApiParam(value = "频道owner") @RequestParam(value = "owner") String owner){
		ResultDataSet rds = new ResultDataSet();
		rds = iQueryInfoWithoutAuth.getChannelByOwner(owner);
		return rds;
	}
	
	@ApiOperation(value = "获取频道信息", notes = "")
	@RequestMapping(value = "/social/getChannelInfo", method = RequestMethod.GET)
	public ResultDataSet getChannelInfo(@RequestParam(value = "cId") String cId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = iQueryInfoWithoutAuth.getChannelInfo(cId);
		return rds;
	}
	
	@ApiOperation(value = "查詢公会或分组的展示信息", notes = "")
	@RequestMapping(value = "/social/getGroupInfo", method = RequestMethod.GET)
	public ResultDataSet getGroupInfo(@RequestParam(value = "gId") String gId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = iQueryInfoWithoutAuth.getGroupInfo(gId);
		return rds;
	}
}
