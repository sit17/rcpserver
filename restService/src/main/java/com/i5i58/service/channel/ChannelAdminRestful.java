package com.i5i58.service.channel;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.channel.IChannelAdmin;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "频道-管理服务")
@RestController
@RequestMapping("${rpc.controller.version}")
public class ChannelAdminRestful {

	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private IChannelAdmin channelAdmin;

	@ApiOperation(value = "获取我的频道", notes = "")
	@RequestMapping(value = "/channel/getMyChannels", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getMyChannels() {
		ResultDataSet rds = new ResultDataSet();
		rds = channelAdmin.getMyChannels(HttpUtils.getAccIdFromHeader());
		return rds;
	}

	@ApiOperation(value = "创建频道", notes = "")
	@RequestMapping(value = "/channel/create", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet createChannel() {
		ResultDataSet rds = new ResultDataSet();
		rds = channelAdmin.createChannel(HttpUtils.getAccIdFromHeader(), HttpUtils.getRealClientIpAddress());
		return rds;
	}

	@ApiOperation(value = "修改频道名称", notes = "")
	@RequestMapping(value = "/channel/editName", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet editChannelName(@RequestParam(value = "name") String name,
			@RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelAdmin.editChannelName(name, HttpUtils.getAccIdFromHeader(), cId);
		return rds;
	}

	@ApiOperation(value = "修改频道公告", notes = "")
	@RequestMapping(value = "/channel/editNotice", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet editChannelNotice(@RequestParam(value = "notice") String notice,
			@RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelAdmin.editChannelNotice(notice, HttpUtils.getAccIdFromHeader(), cId);
		return rds;
	}

	@ApiOperation(value = "指派频道拥有者", notes = "")
	@RequestMapping(value = "/channel/assignOwner", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet assignChannelOwner(@RequestParam(value = "owner") String owner,
			@RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelAdmin.assignChannelOwner(owner, HttpUtils.getAccIdFromHeader(), cId);
		return rds;
	}

	@ApiOperation(value = "指派频道管理员", notes = "")
	@RequestMapping(value = "/channel/assignAdminor", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet assignChannelAdmin(@RequestParam(value = "admin") String admin,
			@RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelAdmin.assignChannelAdmin(admin, HttpUtils.getAccIdFromHeader(), cId);
		return rds;
	}

	@ApiOperation(value = "更新频道封面", notes = "")
	@RequestMapping(value = "/channel/updateCoverImage", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet updateChannelCoverImage(@RequestParam(value = "imageUrl") String imageUrl,
			@RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelAdmin.updateChannelCoverImage(imageUrl, HttpUtils.getAccIdFromHeader(), cId);
		return rds;
	}

	@ApiOperation(value = "重新获取推、拉流地址", notes = "")
	@RequestMapping(value = "/channel/getLiveAddress", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getLiveAddress(@RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelAdmin.getLiveAddress(HttpUtils.getAccIdFromHeader(), cId);
		return rds;
	}

	@ApiOperation(value = "修改频道标题", notes = "")
	@RequestMapping(value = "/channel/editChannelTitle", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet editChannelTitle(@RequestParam(value = "title") String title,
			@RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelAdmin.editChannelTitle(title, HttpUtils.getAccIdFromHeader(), cId);
		return rds;
	}

}
