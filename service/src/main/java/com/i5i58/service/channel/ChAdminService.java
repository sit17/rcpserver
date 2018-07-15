package com.i5i58.service.channel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.Videocloud163.Videocloud163;
import com.i5i58.apis.channel.IChannelAdmin;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.data.account.Account;
import com.i5i58.data.channel.Channel;
import com.i5i58.data.channel.ChannelAdminor;
import com.i5i58.data.channel.ChannelAuth;
import com.i5i58.data.channel.HotChannel;
import com.i5i58.data.channel.MyChannels;
import com.i5i58.data.group.AnchorContract;
import com.i5i58.data.group.ChannelGroup;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.OpenIdPriDao;
import com.i5i58.primary.dao.channel.ChannelAdminorPriDao;
import com.i5i58.primary.dao.channel.ChannelPriDao;
import com.i5i58.primary.dao.group.AnchorContractPriDao;
import com.i5i58.primary.dao.group.ChannelGroupPriDao;
import com.i5i58.primary.dao.group.GroupAdminorPriDao;
import com.i5i58.redis.all.HotChannelDao;
import com.i5i58.secondary.dao.account.AccountSecDao;
import com.i5i58.secondary.dao.channel.ChannelAdminorSecDao;
import com.i5i58.secondary.dao.channel.ChannelSecDao;
import com.i5i58.secondary.dao.group.ChannelGroupSecDao;
import com.i5i58.util.AnchorUtils;
import com.i5i58.util.ChannelOpeRecordUtil;
import com.i5i58.util.ChannelUtils;
import com.i5i58.util.DateUtils;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.OpenIdUtils;
import com.i5i58.util.ServerCode;
import com.i5i58.util.StringUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CreateChannelResult;
import com.i5i58.yunxin.Utils.UpdateAddressResult;
import com.i5i58.yunxin.Utils.UpdateChatRoomMessageResult;
import com.i5i58.yunxin.Utils.YXResultSet;

@Service(protocol = "dubbo")
public class ChAdminService implements IChannelAdmin {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	JsonUtils jsonUtil;

	@Autowired
	ChannelGroupPriDao channelGroupPriDao;

	@Autowired
	ChannelPriDao channelPriDao;

	@Autowired
	ChannelSecDao channelSecDao;

	@Autowired
	GroupAdminorPriDao groupAdminorPriDao;

	@Autowired
	ChannelAdminorPriDao channelAdminorPriDao;

	@Autowired
	ChannelAdminorSecDao channelAdminorSecDao;

	@Autowired
	AnchorContractPriDao anchorContractPriDao;

	@Autowired
	OpenIdPriDao openIdPriDao;

	@Autowired
	AccountPriDao accountPriDao;

	@Autowired
	AccountSecDao accountSecDao;

	@Autowired
	HotChannelDao hotChannelDao;

	@Autowired
	OpenIdUtils openIdUtils;

	@Autowired
	ChannelUtils channelUtils;
	
	@Autowired
	AnchorUtils anchorUtils;

	@Autowired
	ChannelOpeRecordUtil channelOpeRecordUtil;
	
	@Autowired
	ChannelGroupSecDao channelGroupSecDao;
	/**
	 * 获取我的频道
	 */
	@Override
	public ResultDataSet getMyChannels(String accId) {
		ResultDataSet rds = new ResultDataSet();
		Channel ownerChannel;
		List<Channel> cList;
		try {
			ownerChannel = channelSecDao.findByOwnerId(accId);
			List<ChannelAdminor> admins = channelAdminorSecDao.findByAccId(accId);
			cList = new ArrayList<Channel>();
			if (admins != null && admins.size() > 0) {
				for (ChannelAdminor ga : admins) {
					Channel c = channelSecDao.findOne(ga.getcId());
					cList.add(c);
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException("事物异常");
		}
		MyChannels myChannels = new MyChannels();
		myChannels.setOwnerChannel(ownerChannel);
		myChannels.setAdminChannels(cList);
		if (null != myChannels) {
			rds.setData(myChannels);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	/**
	 * 创建频道
	 */
	@Override
	public ResultDataSet createChannel(String accId, String createIp) {
		ResultDataSet rds = new ResultDataSet();
		Channel channel;
		try {
			Account acc = accountPriDao.findOne(accId);
			if (acc == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
				return rds;
			}
			if (!acc.isAnchor()) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("请先通过认证成为主播");
				return rds;
			}
			Channel c = channelPriDao.findByOwnerId(accId);
			if (c != null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("每个账号只能拥有一个频道");
				return rds;
			}
			String strOpenId = openIdUtils.getRandomChannelId(true);
			if (StringUtils.StringIsEmptyOrNull(strOpenId)) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("id exception");
				return rds;
			}

			// 创建频道
			String yunXinCId = "";
			String pushUrl = "";
			String httpPullUrl = "";
			String rtmpPullUrl = "";
			String hlsPullUrl = "";
			CreateChannelResult resultC = Videocloud163.createChannel(strOpenId, "0");
			if (resultC.getCode().equals("200")) {
				yunXinCId = resultC.getRet().getCid();
				pushUrl = resultC.getRet().getPushUrl();
				httpPullUrl = resultC.getRet().getHttpPullUrl();
				rtmpPullUrl = resultC.getRet().getRtmpPullUrl();
				hlsPullUrl = resultC.getRet().getHlsPullUrl();
			} else {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg(resultC.getString("msg"));
				return rds;
			}
			// >>>创建聊天室
			String yunXinRId = "";
			YXResultSet resultR = YunxinIM.createChatRoom(accId, strOpenId, "", pushUrl, "");
			if (resultR.getCode().equals("200")) {
				yunXinRId = resultR.getMap("chatroom").get("roomid").toString();
			} else {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg(resultR.getString("desc"));
				return rds;
			}

			String uuidStr = StringUtils.createUUID();
			channel = new Channel();
			channel.setCreatorId(accId);
			channel.setcId(uuidStr);
			channel.setChannelId(strOpenId);
			channel.setOwnerId(accId);
			channel.setChannelName(acc.getStageName());
			channel.setStatus(0);
			channel.setType(0);
			channel.setYunXinCId(yunXinCId);
			channel.setYunXinRId(yunXinRId);
			channel.setPushUrl(pushUrl);
			channel.setHttpPullUrl(httpPullUrl);
			channel.setRtmpPullUrl(rtmpPullUrl);
			channel.setHlsPullUrl(hlsPullUrl);
			channel.setCreateIp(createIp);
			channel.setCreateDate(DateUtils.getNowTime());
			channel.setClubLevel(1);
			channel.setClubScore(0L);
			channelPriDao.save(channel);

			HotChannel hotChannel = new HotChannel();
			hotChannel.setId(uuidStr);
			hotChannel.setChannelId(strOpenId);
			hotChannel.setOwnerId(accId);
			hotChannel.setChannelName(acc.getStageName());
			hotChannel.setTitle(acc.getStageName());
			hotChannel.setStatus(0);
			hotChannel.setType(0);
			hotChannel.setYunXinCId(yunXinCId);
			hotChannel.setYunXinRId(yunXinRId);
			hotChannel.setHlsPullUrl(hlsPullUrl);
			hotChannel.setHttpPullUrl(httpPullUrl);
			hotChannel.setPushUrl(pushUrl);
			hotChannel.setRtmpPullUrl(rtmpPullUrl);
			hotChannel.setLocation(acc.getLocation());
			hotChannel.setWeekOffer(new Long(0));
			hotChannel.setBrightness(0);
			hotChannel.setClubLevel(1);
			hotChannel.setClubScore(0L);
			hotChannelDao.save(hotChannel);
			
			channelOpeRecordUtil.createChannel(channel.getcId(), "", accId, false);
			/*
			 * int permissiom =
			 * channelAdminAuthVerify.getPermission(ChAdminAuth.PROHIBIT_SPEAK,
			 * ChAdminAuth.KICKOUT_ROOM, ChAdminAuth.ASSIGN_ADMIN_AUTHORITY,
			 * ChAdminAuth.ASSIGN_EDIT_INFO_AUTHORITY,
			 * ChAdminAuth.OPERATE_USER_MIC_SEQUENCE_AUTHORITY); ChannelAdminor
			 * admin = new ChannelAdminor(); admin.setAccId(accId);
			 * admin.setcId(strOpenId); admin.setAdminRight(permissiom);
			 * channelAdminDao.save(admin);
			 */
		} catch (Exception e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("trans_exception");
			return rds;
		}
		if (channel != null) {
			rds.setData(channel);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	/**
	 * 修改频道名称
	 */
	@Override
	public ResultDataSet editChannelName(String name, String accId, String cId) {
		ResultDataSet rds = new ResultDataSet();
		Channel channel;
		try {
			if (StringUtils.StringIsEmptyOrNull(name)) {// >>>判断用户名是否合法
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("请输入合法的频道名！");
				return rds;
			}

			channel = channelPriDao.findByCId(cId);
			if (channel == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("频道不存在！");
				return rds;
			}
			if (!channelUtils.verifyChannelAuth(channel, accId, ChannelAuth.ASSIGN_EDIT_INFO_AUTHORITY)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("没有权限");
				return rds;
			}

			/*
			 * // >>>修改频道名 UpdateChannelResult resultC =
			 * Videocloud163.updateChannel(name, accId, "2"); if
			 * (resultC.getCode().equals("200")) {
			 * rds.setMsg(resultC.getString("msg")); } else {
			 * rds.setCode(ResultCode.PARAM_INVALID.getCode());
			 * rds.setMsg(resultC.getString("msg")); return rds; }
			 */
			
			
			// >>>修改聊天室名
			UpdateChatRoomMessageResult ResultR = YunxinIM.updateChatRoomMessage(channel.getYunXinRId(), name, "", "",
					"", "", "");
			if (ResultR.getCode().equals("200")) {
				rds.setMsg(ResultR.getString("msg"));
			} else {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg(ResultR.getString("msg"));
				return rds;
			}
			channel.setChannelName(name);
			channelPriDao.save(channel);

			HotChannel hotChannel = hotChannelDao.findOne(cId);
			if (hotChannel != null) {
				hotChannel.setChannelName(name);
				hotChannelDao.save(hotChannel);
			}

		} catch (Exception e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("trans_exception");
			return rds;
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("频道名修改成功！");
		return rds;
	}

	/**
	 * 修改频道公告
	 */
	@Override
	public ResultDataSet editChannelNotice(String notice, String accId, String cId) {
		ResultDataSet rds = new ResultDataSet();
		Channel channel;
		try {
			if ("".equals(notice)) {// >>>判断公告是否合法
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("请输入合法的频道名！");
				return rds;
			}
			channel = channelPriDao.findByCId(cId);
			if (channel == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("频道不存在");
				return rds;
			}
			if (!channelUtils.verifyChannelAuth(channel, accId, ChannelAuth.ASSIGN_EDIT_INFO_AUTHORITY)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("没有权限");
				return rds;
			}

			// >>>修改聊天室公告
			UpdateChatRoomMessageResult ResultR = YunxinIM.updateChatRoomMessage(channel.getYunXinRId(), "", notice, "",
					"", "", "");
			if (ResultR.getCode().equals("200")) {
				rds.setMsg(ResultR.getString("msg"));
			} else {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg(ResultR.getString("msg"));
				return rds;
			}
			channel.setChannelNotice(notice);
			channelPriDao.save(channel);

			HotChannel hotChannel = hotChannelDao.findOne(cId);
			if (hotChannel != null) {
				hotChannel.setChannelNotice(notice);
				hotChannelDao.save(hotChannel);
			}
		} catch (Exception e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("trans_exception");
			return rds;
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	/**
	 * 指派频道拥有者
	 */
	@Override
	public ResultDataSet assignChannelOwner(String owner, String accId, String cId) {
		ResultDataSet rds = new ResultDataSet();
		Account acc;
		Channel channel;
		try {
			acc = accountPriDao.findOne(owner);
			if (acc == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("目标用户不存在");
				return rds;
			}
			if (!acc.isAnchor()) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("目标用户不是主播");
				return rds;
			}
			if (accId.equals(owner)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("不能指派自己");
				return rds;
			}
			Channel preChannel = channelSecDao.findByOwnerId(owner);
			if (preChannel != null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("只能绑定一个频道");
				return rds;
			}
			channel = channelPriDao.findByCId(cId);
			if (channel == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("没有该频道");
				return rds;
			}
			if (!channelUtils.verifyChannelAuth(channel, accId, ChannelAuth.ASSIGN_OW_AUTHORITY)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("没有权限");
				return rds;
			}
			AnchorContract anchor = anchorUtils.getAnchorContract(owner);
			if (anchor == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("该主播不是签约主播");
				return rds;
			}
			
			String topGId = channel.getgId();
			ChannelGroup cg = channelGroupSecDao.findByGId(channel.getgId());
			if (cg != null){
				if (!StringUtils.StringIsEmptyOrNull(cg.getParentId())){
					topGId = cg.getParentId();
				}
			}
			if (!anchor.getgId().equals(channel.getgId())
				&&!anchor.getgId().equals(topGId)){
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("主播未和当前公会签约");
				return rds;
			}
			
			channel.setOwnerId(owner);
			channel.setChannelName(acc.getStageName());
			channel.setClubLevel(1);
			channel.setClubScore(0L);
			channelPriDao.save(channel);
			
			if (cg != null){
				channelOpeRecordUtil.assignOwner(channel.getcId(), owner, accId, topGId, false);
			}
//			HotChannel hotChannel = new HotChannel();
//			hotChannel.setId(channel.getcId());
//			hotChannel.setChannelId(channel.getChannelId());
//			hotChannel.setOwnerId(channel.getOwnerId());
//			hotChannel.setChannelName(channel.getChannelName());
//			hotChannel.setTitle(channel.getTitle());
//			hotChannel.setgId(channel.getgId());
//			hotChannel.setStatus(channel.getStatus());
//			hotChannel.setType(channel.getType());
//			hotChannel.setYunXinCId(channel.getYunXinCId());
//			hotChannel.setYunXinRId(channel.getYunXinRId());
//			hotChannel.setHlsPullUrl(channel.getHlsPullUrl());
//			hotChannel.setHttpPullUrl(channel.getHttpPullUrl());
//			hotChannel.setPushUrl(channel.getPushUrl());
//			hotChannel.setRtmpPullUrl(channel.getRtmpPullUrl());
//			hotChannel.setLocation(acc.getLocation());
//			hotChannel.setWeekOffer(new Long(0));
//			hotChannel.setBrightness(0);
//			hotChannel.setClubLevel(channel.getClubLevel());
//			hotChannel.setClubScore(channel.getClubScore());
//			hotChannelDao.save(hotChannel);

		} catch (Exception e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("trans_exception");
			return rds;
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("指派成功！");
		return rds;
	}

	/**
	 * 指派频道管理员
	 */
	@Override
	public ResultDataSet assignChannelAdmin(String admin, String accId, String cId) {
		ResultDataSet rds = new ResultDataSet();
		Account acc;
		Channel channel;
		try {
			channel = channelPriDao.findByCId(cId);
			if (channel == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("沒有該頻道");
				return rds;
			}
			if (channel.getOwnerId().equals(admin)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg(admin + "是当前频道的擁有者");
				return rds;
			}
			acc = accountPriDao.findOne(admin);
			if (acc == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("目标用户不存在");
				return rds;
			}
			ChannelAdminor channelAdminor = channelAdminorPriDao.findByCIdAndAccId(cId, admin);
			if (channelAdminor != null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("該用户已經是管理員");
				return rds;
			}
			if (!channelUtils.verifyChannelAuth(channel, accId, ChannelAuth.ASSIGN_ADMIN_AUTHORITY)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("没有权限");
				return rds;
			}
			ChannelAdminor newAdminor = new ChannelAdminor();
			newAdminor.setAccId(admin);
			newAdminor.setcId(cId);
			newAdminor.setAdminRight(channelUtils.getChannelAdminPermission());
			channelAdminorPriDao.save(newAdminor);
		} catch (Exception e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("trans_exception");
			return rds;
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("指派成功！");
		return rds;
	}

	/**
	 * 更新频道封面
	 */
	@Override
	public ResultDataSet updateChannelCoverImage(String imageUrl, String accId, String cId) {
		ResultDataSet rds = new ResultDataSet();
		Channel channel;

		// >>>判断imageurl是否可用

		channel = channelPriDao.findByCId(cId);
		if (channel == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("沒有該頻道");
			return rds;
		}
		if (!channelUtils.verifyChannelAuth(channel, accId, ChannelAuth.ASSIGN_EDIT_INFO_AUTHORITY)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		channel.setCoverUrl(imageUrl);
		channelPriDao.save(channel);

		HotChannel hotChannel = hotChannelDao.findOne(cId);
		if (hotChannel != null) {
			hotChannel.setCoverUrl(imageUrl);
			hotChannelDao.save(hotChannel);
		}

		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("频道封面修改成功！");
		return rds;
	}

	/**
	 * 重新获取推、拉流地址
	 */
	@Override
	public ResultDataSet getLiveAddress(String accId, String cId) {
		ResultDataSet rds = new ResultDataSet();

		HotChannel hotChannel = hotChannelDao.findOne(cId);
		try {
			if (hotChannel == null) {
				rds.setMsg(ServerCode.NO_CHANNEL.getCode());
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				return rds;
			}
			if (!hotChannel.getOwnerId().equals(accId)) {
				rds.setMsg("只有频道拥有者才能直播");
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				return rds;
			}
			String pushUrl = "";
			String httpPullUrl = "";
			String rtmpPullUrl = "";
			String hlsPullUrl = "";
			UpdateAddressResult resultC = Videocloud163.updateAddress(hotChannel.getYunXinCId());
			if (resultC.getCode().equals("200")) {
				rds.setMsg(resultC.getString("msg"));
				pushUrl = resultC.getRet().getPushUrl();
				httpPullUrl = resultC.getRet().getHttpPullUrl();
				rtmpPullUrl = resultC.getRet().getRtmpPullUrl();
				hlsPullUrl = resultC.getRet().getHlsPullUrl();
			} else {
				rds.setCode(ResultCode.PARAM_INVALID.getCode() + "_1");
				rds.setMsg(resultC.getString("msg"));
				return rds;
			}
			hotChannel.setPushUrl(pushUrl);
			hotChannel.setHttpPullUrl(httpPullUrl);
			hotChannel.setRtmpPullUrl(rtmpPullUrl);
			hotChannel.setHlsPullUrl(hlsPullUrl);
			rds.setData(hotChannel);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			rds.setCode(ResultCode.PARAM_INVALID.getCode() + "_2");
			rds.setMsg(e.getMessage());
			return rds;
		}
		rds.setCode(ResultCode.SUCCESS.getCode());

		return rds;
	}

	@Override
	public ResultDataSet editChannelTitle(String title, String accId, String cId) {
		ResultDataSet rds = new ResultDataSet();
		Channel channel;
		try {
			if (StringUtils.StringIsEmptyOrNull(title)) {// >>>判断用户名是否合法
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("请输入合法的标题名！");
				return rds;
			}

			channel = channelPriDao.findByCId(cId);
			if (channel == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("频道不存在！");
				return rds;
			}
			if (!channelUtils.verifyChannelAuth(channel, accId, ChannelAuth.ASSIGN_EDIT_INFO_AUTHORITY)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("没有权限");
				return rds;
			}
			channel.setTitle(title);
			channelPriDao.save(channel);
			HotChannel hotChannel = hotChannelDao.findOne(cId);
			if (hotChannel != null) {
				hotChannel.setTitle(title);
				hotChannelDao.save(hotChannel);
			}
		} catch (Exception e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("trans_exception");
			return rds;
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("频道名修改成功！");
		return rds;
	}
}
