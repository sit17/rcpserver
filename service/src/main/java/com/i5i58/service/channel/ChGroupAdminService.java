package com.i5i58.service.channel;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.Videocloud163.Videocloud163;
import com.i5i58.apis.constants.ResponseData;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.group.IChannelGroupAdmin;
import com.i5i58.data.account.Account;
import com.i5i58.data.account.Wallet;
import com.i5i58.data.anchor.AnchorPushRecord;
import com.i5i58.data.channel.Channel;
import com.i5i58.data.channel.HotChannel;
import com.i5i58.data.channel.HotChannelMic;
import com.i5i58.data.channel.RemovedChannel;
import com.i5i58.data.group.AnchorContract;
import com.i5i58.data.group.AnchorContractStatus;
import com.i5i58.data.group.ChannelGroup;
import com.i5i58.data.group.ContractRequestDirection;
import com.i5i58.data.group.ForceCancelContract;
import com.i5i58.data.group.ForceCancelContractStatus;
import com.i5i58.data.group.GroupAdminor;
import com.i5i58.data.group.GroupAuth;
import com.i5i58.data.group.MyGroups;
import com.i5i58.data.profile.GroupProfile;
import com.i5i58.data.profile.GroupProfileStatus;
import com.i5i58.data.record.ChannelOpeRecord;
import com.i5i58.data.record.ContractOpeRecord;
import com.i5i58.data.record.GroupOpeRecord;
import com.i5i58.data.record.MoneyFlow;
import com.i5i58.data.record.MoneyFlowType;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.OpenIdPriDao;
import com.i5i58.primary.dao.channel.ChannelAdminorPriDao;
import com.i5i58.primary.dao.channel.ChannelPriDao;
import com.i5i58.primary.dao.channel.RemovedChannelPriDao;
import com.i5i58.primary.dao.group.AnchorContractPriDao;
import com.i5i58.primary.dao.group.ChannelGroupPriDao;
import com.i5i58.primary.dao.group.ForceCancelContractPriDao;
import com.i5i58.primary.dao.group.GroupAdminorPriDao;
import com.i5i58.primary.dao.profile.GroupProfilePriDao;
import com.i5i58.redis.all.HotChannelDao;
import com.i5i58.redis.all.HotChannelMicDao;
import com.i5i58.secondary.dao.account.AccountSecDao;
import com.i5i58.secondary.dao.account.WalletSecDao;
import com.i5i58.secondary.dao.anchor.AnchorPushRecordSecDao;
import com.i5i58.secondary.dao.channel.ChannelSecDao;
import com.i5i58.secondary.dao.group.AnchorContractSecDao;
import com.i5i58.secondary.dao.group.ChannelGroupSecDao;
import com.i5i58.secondary.dao.group.ForceCancelContractSecDao;
import com.i5i58.secondary.dao.group.GroupAdminorSecDao;
import com.i5i58.secondary.dao.profile.GroupProfileSecDao;
import com.i5i58.secondary.dao.record.ChannelOpeRecordSecDao;
import com.i5i58.secondary.dao.record.ContractOpeRecordSecDao;
import com.i5i58.secondary.dao.record.GroupOpeRecordSecDao;
import com.i5i58.secondary.dao.record.MoneyFlowSecDao;
import com.i5i58.util.AnchorUtils;
import com.i5i58.util.ChannelOpeRecordUtil;
import com.i5i58.util.ChannelUtils;
import com.i5i58.util.ContractOpeRecordUtil;
import com.i5i58.util.DateUtils;
import com.i5i58.util.GroupOpeRecordUtil;
import com.i5i58.util.GroupUtils;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.MyPageUtils;
import com.i5i58.util.OpenIdUtils;
import com.i5i58.util.ServerCode;
import com.i5i58.util.StringUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CreateChannelResult;
import com.i5i58.yunxin.Utils.YXResultSet;

@Service(protocol = "dubbo")
public class ChGroupAdminService implements IChannelGroupAdmin {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	JsonUtils jsonUtil;

	@Autowired
	GroupProfilePriDao groupProfilePriDao;

	@Autowired
	GroupProfileSecDao groupProfileSecDao;

	@Autowired
	ChannelGroupPriDao channelGroupPriDao;

	@Autowired
	ChannelGroupSecDao channelGroupSecDao;

	@Autowired
	GroupAdminorPriDao groupAdminorPriDao;

	@Autowired
	GroupAdminorSecDao groupAdminorSecDao;

	@Autowired
	AnchorContractPriDao anchorContractPriDao;

	@Autowired
	AnchorContractSecDao anchorContractSecDao;

	@Autowired
	ChannelPriDao channelPriDao;

	@Autowired
	ChannelSecDao channelSecDao;

	@Autowired
	AccountPriDao accountPriDao;

	@Autowired
	AccountSecDao accountSecDao;

	@Autowired
	ChannelAdminorPriDao channelAdminPriDao;

	@Autowired
	OpenIdPriDao openIdPriDao;

	@Autowired
	OpenIdUtils openIdUtils;

	@Autowired
	ChannelUtils channelUtils;

	@Autowired
	GroupUtils groupUtils;

	@Autowired
	HotChannelDao hotChannelDao;

	@Autowired
	AnchorUtils anchorUtils;

	@Autowired
	WalletSecDao walletSecDao;

	@Autowired
	AnchorPushRecordSecDao anchorPushRecordSecDao;
	
	@Autowired
	GroupOpeRecordUtil groupOpeRecordUtil;
	
	@Autowired
	ChannelOpeRecordUtil channelOpeRecordUtil;
	
	@Autowired
	ContractOpeRecordUtil contractOpeRecordUtil;

	@Autowired
	GroupOpeRecordSecDao groupOpeRecordSecDao;
	
	@Autowired
	ChannelOpeRecordSecDao channelOpeRecordSecDao;
	
	@Autowired
	ContractOpeRecordSecDao contractOpeRecordSecDao;
	
	@Autowired
	HotChannelMicDao hotChannelMicDao;
	
	@Autowired
	RemovedChannelPriDao removedChannelPriDao;
	
	@Autowired
	ForceCancelContractPriDao forceCancelContractPriDao;
	
	@Autowired
	ForceCancelContractSecDao forceCancelContractSecDao;
	
	@Autowired
	MoneyFlowSecDao moneyFlowSecDao;

	
	/**
	 * 创公会、经纪公司档案
	 */
	@Override
	public ResultDataSet createProfile(String accId, String name, String description, int type, String registerId,
			String operRange, String licenseUrl, String taxCertificateUrl, String organizationCodeUrl,
			String bankLicenseUrl, String address, String legalPerson, String legalPersonUrl, String legalPersonBackUrl,
			String dataUrl, double regCapital, String fixedPhone, String email, String createIp) {
		ResultDataSet rds = new ResultDataSet();
		Account acc;
		try {
			acc = accountPriDao.findOne(accId);
			if (acc == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("目标用户不存在！");
				return rds;
			}
			if (acc.isAnchor()) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("当前用户是主播,不能创建经纪公司");
				return rds;
			}

			// 审核

			GroupProfile gp = new GroupProfile();
			gp.setfId(StringUtils.createUUID());
			gp.setAccId(accId);
			gp.setName(name);
			gp.setDescription(description);
			gp.setCreateDate(DateUtils.getNowTime());
			gp.setCreateIp(createIp);
			gp.setAddress(address);
			gp.setCreateSubGroupCount(0);
			gp.setCreateTopGroupCount(0);
			gp.setCreateChannelCount(0);
			gp.setBankLicenseUrl(bankLicenseUrl);
			gp.setDataUrl(dataUrl);
			gp.setEmail(email);
			gp.setLegalPersonUrl(legalPersonUrl);
			gp.setLegalPersonBackUrl(legalPersonBackUrl);
			gp.setOrganizationCodeUrl(organizationCodeUrl);
			gp.setRegisterId(registerId);
			gp.setTaxCertificateUrl(taxCertificateUrl);
			gp.setOperRange(operRange);
			gp.setFixedPhone(fixedPhone);
			gp.setLegalPerson(legalPerson);
			gp.setLicenseUrl(licenseUrl);
			gp.setRegCapital(regCapital);
			gp.setType(type);
			gp.setNullity(true);
			gp.setStatus(GroupProfileStatus.EXAMINING.getValue());
			groupProfilePriDao.save(gp);
			rds.setData(gp);
			rds.setCode(ResultCode.SUCCESS.getCode());
		} catch (Exception e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("trans_exception");
			return rds;
		}
		return rds;
	}

	@Override
	public ResultDataSet createChannelTopGroup(String accId, String name, String faceUrl, String profileId,
			String createIp) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		try {
			Account acc = accountPriDao.findOne(accId);
			if (acc == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
				return rds;
			}
			GroupProfile groupProfile = groupProfilePriDao.findOne(profileId);
			if (groupProfile == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("该经纪公司资质不存在");
				return rds;
			}
			if (groupProfile.getStatus() != GroupProfileStatus.NORMAL.getValue()) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("该经纪公司资质不可用");
				return rds;
			}
			if (!groupProfile.getAccId().equals(accId)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("只有已备案的资质拥有者才可以创建公会");
				return rds;
			}
			if (!groupUtils.checkTopGroupCount(groupProfile)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("可创建公会数量已满");
				return rds;
			}
			String strOpenId = openIdUtils.getRandomOpenId();
			if (StringUtils.StringIsEmptyOrNull(strOpenId)) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("id exception");
				return rds;
			}
			ChannelGroup cg = new ChannelGroup();
			cg.setgId(StringUtils.createUUID());
			cg.setOwnerId(groupProfile.getAccId());
			cg.setGroupId(strOpenId);
			cg.setName(name);
			cg.setFaceUrl(faceUrl);
			cg.setChannelCount(0);
			cg.setCreateDate(DateUtils.getNowTime());
			cg.setCreateIp(createIp);
			cg.setProfileId(profileId);
			channelGroupPriDao.save(cg);
			rds.setData(cg);
			rds.setCode(ResultCode.SUCCESS.getCode());
			groupOpeRecordUtil.createTopGroup(accId, cg.getgId());
		} catch (Exception e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("trans_exception");
			return rds;
		}
		return rds;
	}

	/**
	 * 创建次级频道组
	 */
	@Override
	public ResultDataSet createChannelSubGroup(String accId, String name, String gId, String createIp) {
		ResultDataSet rds = new ResultDataSet();
		Account acc;
		try {
			acc = accountPriDao.findOne(accId);
			if (acc == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("目标用户不存在！");
				return rds;
			}
			ChannelGroup group = channelGroupPriDao.findOne(gId);
			if (group == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("该频道组不存在！");
				return rds;
			}
			if (!groupUtils.verifyTopGroupMember(group, accId)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("沒有權限");
				return rds;
			}
			if (!groupUtils.checkGroupCount(group)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("可创建频道组数量已满");
				return rds;
			}
			String strOpenId = openIdUtils.getRandomOpenId();
			if (StringUtils.StringIsEmptyOrNull(strOpenId)) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("id exception");
				return rds;
			}
			ChannelGroup channelGroup = new ChannelGroup();
			channelGroup.setgId(StringUtils.createUUID());
			channelGroup.setGroupId(strOpenId);
			channelGroup.setName(name);
			channelGroup.setOwnerId(accId);
			channelGroup.setParentId(gId);
			channelGroup.setProfileId(group.getProfileId());
			channelGroup.setChannelCount(0);
			channelGroup.setCreateDate(DateUtils.getNowTime());
			channelGroup.setCreateIp(createIp);
			channelGroupPriDao.save(channelGroup);
			rds.setData(channelGroup);
			rds.setCode(ResultCode.SUCCESS.getCode());
			
			groupOpeRecordUtil.createSubGroup(accId, channelGroup.getgId(), channelGroup.getParentId());
		} catch (Exception e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("trans_exception");
			return rds;
		}
		return rds;
	}

	/**
	 * 指派频道组拥有者，頂級組只有超管才可以
	 */
	@Override
	public ResultDataSet assignChannelGroupOwner(String owner, String accId, String gId) {
		ResultDataSet rds = new ResultDataSet();
		Account acc;
		try {
			acc = accountPriDao.findOne(owner);
			if (acc == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("目标用户不存在");
				return rds;
			}
			if (accId.equals(owner)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("不能指派自己！");
				return rds;
			}
			ChannelGroup channelGroup = channelGroupPriDao.findByGId(gId);
			if (channelGroup == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("沒有該頻道組");
				return rds;
			}
			/*
			 * if (!channelUtils.verifyGroupAuth(channelGroup, accId,
			 * ChannelAuth.ASSIGN_OW_AUTHORITY)) {
			 * rds.setCode(ResultCode.PARAM_INVALID.getCode());
			 * rds.setMsg("没有权限"); return rds; }
			 */
			// 工会id
			String topGroupId = channelGroup.getgId();
			if (!StringUtils.StringIsEmptyOrNull(channelGroup.getParentId())) {
				topGroupId = channelGroup.getParentId();
			}
			if (anchorUtils.isAnchorInOtherTopGroup(topGroupId, accId)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("已经与其它工会签约,不能设置为当前工会拥有者.");
				return rds;
			}
			channelGroup.setOwnerId(owner);
			channelGroupPriDao.save(channelGroup);
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
	 * 指派频道组管理员，只有本频道组拥有者（超管可以）
	 */
	@Override
	public ResultDataSet assignChannelGroupAdmin(String admin, String accId, String gId) {
		ResultDataSet rds = new ResultDataSet();
		Account acc;
		try {
			acc = accountPriDao.findOne(admin);
			if (acc == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("目标用户不存在");
				return rds;
			}
			if (accId.equals(admin)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("不能指派自己！");
				return rds;
			}
			ChannelGroup channelGroup = channelGroupPriDao.findByGId(gId);
			if (null == channelGroup) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("沒有該頻道組");
				return rds;
			}
			GroupAdminor groupAdminor = groupAdminorPriDao.findByAdminIdAndGId(admin, gId);
			if (null != groupAdminor) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("已經是管理員");
				return rds;
			}
			if (!groupUtils.verifyGroupAuth(channelGroup, accId, GroupAuth.ASSIGN_GROUP_ADMIN)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("没有权限");
				return rds;
			}
			// 工会id
			String topGroupId = channelGroup.getgId();
			if (!StringUtils.StringIsEmptyOrNull(channelGroup.getParentId())) {
				topGroupId = channelGroup.getParentId();
			}
			if (anchorUtils.isAnchorInOtherTopGroup(topGroupId, accId)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("已经与其它工会签约,不能设置为当前工会拥有者.");
				return rds;
			}

			int permission = groupUtils.getGroupAdminPermission();

			groupAdminor = new GroupAdminor();
			groupAdminor.setgId(gId);
			groupAdminor.setAdminId(admin);
			groupAdminor.setAdminRight(permission);
			groupAdminorPriDao.save(groupAdminor);
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
	 * 获取我的公会档案列表
	 */
	@Override
	public ResultDataSet getMyGroupFiles(String accId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			List<GroupProfile> gp = groupProfileSecDao.findByAccId(accId);
			rds.setData(gp);
			rds.setCode(ResultCode.SUCCESS.getCode());
		} catch (Exception e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("trans_exception");
			return rds;
		}
		return rds;
	}

	/**
	 * 获取我的公会列表
	 */
	@Override
	public ResultDataSet getMyGroups(String accId) {
		ResultDataSet rds = new ResultDataSet();
		// Query groupAdminQuery = entityManager.createNativeQuery(
		// "SELECT c.group_id, c.owner_id, c.parent_id, c.description FROM
		// channel_groups c JOIN group_admins g ON c.group_id = g.group_id WHERE
		// g.admin_id = "
		// + accId);
		// List<ChannelGroup> groupAdminList = groupAdminQuery.getResultList();
		// Channel channel = channelDao.findByOwnerId(accId);

		List<GroupAdminor> gas = groupAdminorSecDao.findByAdminId(accId);
		List<ChannelGroup> cgsList = new ArrayList<ChannelGroup>();
		if (gas != null && gas.size() > 0) {
			for (GroupAdminor ga : gas) {
				System.out.println(ga.getgId());
				ChannelGroup cgs = channelGroupSecDao.findByGId(ga.getgId());// ,
																				// ga.getgId());
				// for (ChannelGroup cg : cgs) {
				// System.out.println(cg.getgId());
				// }
				if (cgs != null) {
					cgsList.add(cgs);
				}
			}
		}
		List<ChannelGroup> ownerList = channelGroupSecDao.findByOwnerId(accId);
		MyGroups myGroups = new MyGroups();
		myGroups.setAdminGroup(cgsList);
		myGroups.setOwnerGroup(ownerList);
		if (null != myGroups) {
			rds.setData(myGroups);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getMyAgencyGroups(String accId) {
		ResultDataSet rds = new ResultDataSet();

		List<GroupProfile> myProfiles = groupProfileSecDao.findByAccId(accId);
		if (myProfiles == null) {
			return rds;
		}

		List<ChannelGroup> channelGroups = new ArrayList<ChannelGroup>();
		for (GroupProfile gp : myProfiles) {
			List<ChannelGroup> cgs = channelGroupSecDao.findByProfileId(gp.getfId());
			channelGroups.addAll(cgs);
		}

		rds.setData(channelGroups);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet createChannelInGroup(String adminAccId, String gId, String name, String createIp) {
		ResultDataSet rds = new ResultDataSet();
		Channel channel;
		try {
			Account acc = accountPriDao.findOne(adminAccId);
			if (acc == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("没有该账号");
				return rds;
			}
			ChannelGroup channelGroup = channelGroupPriDao.findByGId(gId);
			if (channelGroup == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("没有该频道组");
				return rds;
			}
			if (!channelUtils.checkChannelCount(channelGroup)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("可创建频道数量已满");
				return rds;
			}
			if (!groupUtils.verifyGroupAuth(channelGroup, adminAccId, GroupAuth.ASSIGN_GROUP_ADMIN)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("没有权限");
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
			YXResultSet resultR = YunxinIM.createChatRoom(adminAccId, strOpenId, "", pushUrl, "");
			if (resultR.getCode().equals("200")) {
				yunXinRId = resultR.getMap("chatroom").get("roomid").toString();
			} else {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg(resultR.getString("desc"));
				return rds;
			}

			String uuidStr = StringUtils.createUUID();
			channel = new Channel();
			channel.setCreatorId(adminAccId);
			channel.setcId(uuidStr);
			channel.setChannelId(strOpenId);
			channel.setOwnerId("");
			channel.setChannelName(name);
			channel.setTitle("");
			channel.setgId(gId);
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

			/*
			 * HotChannel hotChannel = new HotChannel();
			 * hotChannel.setId(uuidStr); hotChannel.setChannelId(strOpenId);
			 * hotChannel.setOwnerId(""); hotChannel.setChannelName(name);
			 * hotChannel.setTitle(""); hotChannel.setgId(gId);
			 * hotChannel.setStatus(0); hotChannel.setType(0);
			 * hotChannel.setYunXinCId(yunXinCId);
			 * hotChannel.setYunXinRId(yunXinRId);
			 * hotChannel.setHlsPullUrl(hlsPullUrl);
			 * hotChannel.setHttpPullUrl(httpPullUrl);
			 * hotChannel.setPushUrl(pushUrl);
			 * hotChannel.setRtmpPullUrl(rtmpPullUrl);
			 * hotChannel.setLocation(acc.getLocation());
			 * hotChannel.setWeekOffer(new Long(0));
			 * hotChannel.setBrightness(0); hotChannelDao.save(hotChannel);
			 */
			channelUtils.updateChannelCount(channelGroup, 1);
			String topGId = gId;
			if (!StringUtils.StringIsEmptyOrNull(channelGroup.getParentId())){
				topGId = channelGroup.getParentId();
			}
			channelOpeRecordUtil.createChannel(channel.getcId(), topGId, adminAccId, false);
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

	@Override
	public ResultDataSet editGroupName(String adminAccId, String gId, String name) {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(adminAccId);
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该账号");
			return rds;
		}
		ChannelGroup channelGroup = channelGroupPriDao.findByGId(gId);
		if (channelGroup == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该频道组");
			return rds;
		}
		if (!groupUtils.verifyGroupAuth(channelGroup, adminAccId, GroupAuth.ASSIGN_EDIT_INFO_AUTHORITY)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		String preName = channelGroup.getName();
		groupOpeRecordUtil.modifyGroupName(adminAccId, gId, preName, name, channelGroup.getParentId());

		channelGroup.setName(name);
		channelGroupPriDao.save(channelGroup);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet editGroupDesc(String adminAccId, String gId, String desc) {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(adminAccId);
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该账号");
			return rds;
		}
		ChannelGroup channelGroup = channelGroupPriDao.findByGId(gId);
		if (channelGroup == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该频道组");
			return rds;
		}
		if (!groupUtils.verifyGroupAuth(channelGroup, adminAccId, GroupAuth.ASSIGN_EDIT_INFO_AUTHORITY)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		channelGroup.setDescription(desc);
		channelGroupPriDao.save(channelGroup);
		rds.setCode(ResultCode.SUCCESS.getCode());
					
		groupOpeRecordUtil.modifyGroupDesc(adminAccId, gId, desc, channelGroup.getParentId());
		return rds;
	}

	@Override
	public ResultDataSet editGroupInfo(String adminAccId, String gId, String groupIconUrl, String groupName,
			String desc, String groupNotice) {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(adminAccId);
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该账号");
			return rds;
		}
		ChannelGroup channelGroup = channelGroupPriDao.findByGId(gId);
		if (channelGroup == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该频道组");
			return rds;
		}
		if (!groupUtils.verifyGroupAuth(channelGroup, adminAccId, GroupAuth.ASSIGN_EDIT_INFO_AUTHORITY)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		if (desc != null && !desc.equals(channelGroup.getDescription())){			
			groupOpeRecordUtil.modifyGroupDesc(adminAccId, gId, desc, channelGroup.getParentId());
		}
		if (groupName != null && !groupName.equals(channelGroup.getName())){	
			String preName = channelGroup.getName();
			groupOpeRecordUtil.modifyGroupName(adminAccId, gId, preName, groupName, channelGroup.getParentId());
		}
		if (groupNotice != null && !groupNotice.equals(channelGroup.getNotice())){			
			groupOpeRecordUtil.modifyGroupNotice(adminAccId, gId, groupNotice, channelGroup.getParentId());
		}
		
		channelGroup.setDescription(desc);
		channelGroup.setName(groupName);
		channelGroup.setFaceUrl(groupIconUrl);
		channelGroup.setNotice(groupNotice);
		channelGroupPriDao.save(channelGroup);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet requestContract(String adminAccId, String gId, String accId, int groupRate, long endDate,
			int settleMode) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(accId);
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		ChannelGroup channelGroup = channelGroupPriDao.findByGId(gId);
		if (channelGroup == null || !StringUtils.StringIsEmptyOrNull(channelGroup.getParentId())) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("公会ID错误");
			return rds;
		}
		if (!groupUtils.verifyTopGroupMember(channelGroup, adminAccId)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		AnchorContract ct;
		ct = anchorUtils.getAnchorContract(accId);
		if (ct != null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该主播已签约公会");
			return rds;
		}

		ct = null;
		try {
			ct = anchorContractPriDao.findByAccIdAndStatusAndEndDateGreaterThan(accId,
					AnchorContractStatus.REQUESTED.getValue(), DateUtils.getNowDate());
		} catch (ParseException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("date_parse_exception");
			return rds;
		}
		if (ct != null && ct.getgId().equals(gId)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该主播有未处理的合约");
			return rds;
		}
		// ct = null;
		// try {
		// ct =
		// anchorContractPriDao.findByAccIdAndStatusAndEndDateGreaterThan(accId,
		// AnchorContractStatus.REQUEST_CANCEL.getValue(),
		// DateUtils.getNowDate());
		// } catch (ParseException e) {
		// logger.error("", e);
		// rds.setCode(ResultCode.SERVICE_ERROR.getCode());
		// rds.setMsg("date_parse_exception");
		// return rds;
		// }
		// if (ct != null) {
		// rds.setCode(ResultCode.PARAM_INVALID.getCode());
		// rds.setMsg("该主播有未处理的合约");
		// return rds;
		// }
		AnchorContract contract = new AnchorContract();
		contract.setCtId(StringUtils.createUUID());
		contract.setAccId(accId);
		contract.setgId(gId);
		contract.setCreateTime(DateUtils.getNowTime());
		contract.setEndDate(endDate);
		contract.setGroupRate(groupRate);
		contract.setSettleMode(settleMode);
		contract.setStartDate(DateUtils.getNowTime());
		contract.setStatus(AnchorContractStatus.REQUESTED.getValue());
		contract.setDirection(ContractRequestDirection.TopGroup.getValue());
		anchorContractPriDao.save(contract);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet responseContract(String adminAccId, String ctId, boolean agree) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		AnchorContract contract = anchorContractPriDao.findOne(ctId);
		if (contract == null || contract.getStatus() != AnchorContractStatus.REQUESTED.getValue()
				|| contract.getDirection() != 0) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("未找到该合约或已处理");
			return rds;
		}
		
		ChannelGroup channelGroup = channelGroupPriDao.findByGId(contract.getgId());
		if (!groupUtils.verifyTopGroupMember(channelGroup, adminAccId)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		
		AnchorContract anchorContract;
		anchorContract = anchorUtils.getAnchorContract(contract.getAccId());
		if (anchorContract != null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该主播已有签约公会");
			return rds;
		}
		
		if (agree) {
			contract.setStatus(AnchorContractStatus.AGREED.getValue());
			contract.setStartDate(DateUtils.getNowTime());
			contractOpeRecordUtil.agreeSign(contract.getAccId(), contract.getgId());
		} else {
			contract.setStatus(AnchorContractStatus.REJECTED.getValue());
			contract.setStartDate(DateUtils.getNowTime());
			contractOpeRecordUtil.refuseSign(contract.getAccId(), contract.getgId());
		}
		anchorContractPriDao.save(contract);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryContractExpire(String adminAccId, String gId, long expire, int pageSize, int pageNum)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "startDate");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<AnchorContract> contract = anchorContractSecDao.findByGIdAndExpire(gId, DateUtils.getNowTime(), expire,
				pageable);
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setData(MyPageUtils.getMyPage(contract));
		return rds;
	}

	@Override
	public ResultDataSet getContract(String adminAccId, String ctId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		AnchorContract contract = anchorContractSecDao.findOne(ctId);
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setData(contract);
		return rds;
	}

	@Override
	public ResultDataSet queryContractByGId(String adminAccId, String gId, int status, int pageSize, int pageNum)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "startDate");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<AnchorContract> contract = anchorContractSecDao.findByGIdAndStatus(gId, status, pageable);
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setData(MyPageUtils.getMyPage(contract));
		return rds;
	}

	@Override
	public ResultDataSet queryAnchorByTopGroup(String adminAccId, String gId, int pageSize, int pageNum)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("asc"), "accId");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<Account> acc = accountSecDao.findAnchorInTopGroup(gId, pageable);
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setData(MyPageUtils.getMyPage(acc));
		return rds;
	}

	@Override
	public ResultDataSet queryAnchorNoChannelByTopGroup(String adminAccId, String gId, int pageSize, int pageNum)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("asc"), "accId");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<Account> acc = accountSecDao.findAnchorNoChannelInTopGroup(gId, pageable);

		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setData(MyPageUtils.getMyPage(acc));
		return rds;
	}

	@Override
	public ResultDataSet queryProfileList(String param, int status, int pageSize, int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "createDate");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<GroupProfile> data;
		if (status == 0) {
			data = groupProfileSecDao.findByParam(param, pageable);
		} else {
			data = groupProfileSecDao.findByParamWithStatus(param, status, pageable);
		}
		rds.setData(MyPageUtils.getMyPage(data));
		return rds;
	}

	@Override
	public ResultDataSet querySubGroup(String adminAccId, String gId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountSecDao.findOne(adminAccId);
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该账号");
			return rds;
		}
		ChannelGroup channelGroup = channelGroupSecDao.findByGId(gId);
		if (channelGroup == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该频道组");
			return rds;
		}
		if (!groupUtils.verifyGroupAuth(channelGroup, adminAccId, GroupAuth.ASSIGN_EDIT_INFO_AUTHORITY)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		List<ChannelGroup> subGroups = channelGroupSecDao.findByParentId(channelGroup.getgId());
		rds.setData(subGroups);

		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryChannelInTopGroup(String adminAccId, String gId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountSecDao.findOne(adminAccId);
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该账号");
			return rds;
		}
		ChannelGroup channelGroup = channelGroupSecDao.findByGId(gId);
		if (channelGroup == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该频道组");
			return rds;
		}
		if (!groupUtils.verifyGroupAuth(channelGroup, adminAccId, GroupAuth.ASSIGN_EDIT_INFO_AUTHORITY)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		List<ChannelGroup> subGroups = channelGroupSecDao.findByParentId(channelGroup.getgId());
		List<Channel> channels = new ArrayList<Channel>();
		List<Channel> channelsInTop = channelSecDao.findByGId(gId);
		channels.addAll(channelsInTop);
		for (ChannelGroup cg : subGroups) {
			List<Channel> channelsInSub = channelSecDao.findByGId(cg.getgId());
			channels.addAll(channelsInSub);
		}
		rds.setData(channels);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getTopGroup(String adminAccId, String gId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountSecDao.findOne(adminAccId);
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该账号");
			return rds;
		}
		ChannelGroup channelGroup = channelGroupSecDao.findByGId(gId);
		if (channelGroup == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该频道组");
			return rds;
		}
		
		if (!StringUtils.StringIsEmptyOrNull(channelGroup.getParentId())){
			channelGroup = channelGroupSecDao.findByGId(channelGroup.getParentId());
		}
		
		if (channelGroup == null) {
			logger.error("查询公会错误 gId = " + gId);
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("查询公会出错，请联系系统管理员");
			return rds;
		}
		
		if (!groupUtils.verifyGroupAuth(channelGroup, adminAccId, GroupAuth.ASSIGN_EDIT_INFO_AUTHORITY)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		rds.setData(channelGroup);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryAdminorList(String adminAccId, String gId, int pageNum, int pageSize) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountSecDao.findOne(adminAccId);
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该账号");
			return rds;
		}
		ChannelGroup channelGroup = channelGroupSecDao.findByGId(gId);
		if (channelGroup == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该频道组");
			return rds;
		}
		if (!groupUtils.verifyGroupAuth(channelGroup, adminAccId, GroupAuth.ASSIGN_EDIT_INFO_AUTHORITY)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		List<ChannelGroup> channelGroup1 = channelGroupSecDao.findByParentId(channelGroup.getgId());
		List<GroupAdminor> adminList = new ArrayList<GroupAdminor>();
		List<GroupAdminor> adminList1 = groupAdminorSecDao.findByGId(gId);
		adminList.addAll(adminList1);
		for (ChannelGroup cg : channelGroup1) {
			List<GroupAdminor> adminList2 = groupAdminorSecDao.findByGId(cg.getgId());
			adminList.addAll(adminList2);
		}
		rds.setData(adminList);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getMyGroupRight(String accId, String gId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ChannelGroup channelGroup = channelGroupSecDao.findByGId(gId);
		if (channelGroup == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该频道组");
			return rds;
		}
		GroupAdminor groupAdminor = groupAdminorSecDao.findByAdminIdAndGId(accId, gId);
		if (groupAdminor == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不是管理员");
			return rds;
		}
		rds.setData(groupAdminor);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryAnchorList(String adminAccId, String gId, int pageNum, int pageSize) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountSecDao.findOne(adminAccId);
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该账号");
			return rds;
		}
		ChannelGroup channelGroup = channelGroupSecDao.findByGId(gId);
		if (channelGroup == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该频道组");
			return rds;
		}
		if (!groupUtils.verifyGroupAuth(channelGroup, adminAccId, GroupAuth.ASSIGN_EDIT_INFO_AUTHORITY)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		Sort sort = new Sort(Direction.fromString("desc"), "endDate");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<AnchorContract> page = anchorContractSecDao.findByGId(gId, pageable);
		List<AnchorContract> records = page.getContent();
		List<ResponseData> rebuilds = new ArrayList<ResponseData>();
		ResponseData result = new ResponseData();
		for (AnchorContract rd : records){
			if (rd.getStatus() != AnchorContractStatus.AGREED.getValue()
					&& rd.getStatus() != AnchorContractStatus.REQUEST_CANCEL.getValue())
				continue;
			ResponseData tmp = new ResponseData();
			tmp.put("gId", rd.getgId());
			tmp.put("ctId", rd.getCtId());
			tmp.put("accId", rd.getAccId());
			tmp.put("groupRate", rd.getGroupRate());
			tmp.put("startDate", rd.getStartDate());
			tmp.put("endDate", rd.getEndDate());
			tmp.put("settleMode", rd.getSettleMode());
			tmp.put("createTime", rd.getCreateTime());
			tmp.put("status", rd.getStatus());
			tmp.put("direction", rd.getDirection());
			tmp.put("cancelDirection", rd.getCancelDirection());
			tmp.put("hide", rd.getHide());
			
			Channel channel=channelSecDao.findByOwnerId(rd.getAccId());	
			if(channel != null){
				tmp.put("cId", channel.getcId());
			}
			rebuilds.add(tmp);
		}
		result.put("content", 	rebuilds);
		result.put("count", 	page.getTotalElements());
		result.put("size", 		page.getSize());
		result.put("pages", 	page.getTotalPages());
		result.put("num", 		page.getNumber());
		rds.setData(result);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet removeChannelSubGroup(String accId, String name, String gId, String createIp) {
		ResultDataSet rds = new ResultDataSet();
		Account acc;
		try {
			acc = accountPriDao.findOne(accId);
			if (acc == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("目标用户不存在！");
				return rds;
			}
			ChannelGroup group = channelGroupSecDao.findOne(gId);
			if (group == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("该频道组不存在！");
				return rds;
			}
			if (!groupUtils.verifyTopGroupMember(group, accId)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("沒有權限");
				return rds;
			}
			List<Channel> channelsInTop = channelPriDao.findByGId(gId);
			for (Channel ch : channelsInTop) {
				ch.setgId(group.getParentId());
				channelPriDao.save(ch);
				channelOpeRecordUtil.changeGroup(ch.getcId(), gId, group.getParentId(), accId, group.getParentId());
			}
			List<HotChannel> hotChannelsInTop = hotChannelDao.findByGId(gId);
			for (HotChannel hotCh : hotChannelsInTop) {
				hotCh.setgId(group.getParentId());
				hotChannelDao.save(hotCh);
			}
			List<GroupAdminor> groupAdminor = groupAdminorPriDao.findByGId(gId);
			for (GroupAdminor gro : groupAdminor) {
				groupAdminorPriDao.delete(gro);
			}
			channelGroupPriDao.delete(group);
			rds.setCode(ResultCode.SUCCESS.getCode());
			rds.setMsg("组移除成功，已经将此组所属频道回收到公会下");
			
			groupOpeRecordUtil.deleteSubGroup(accId, gId, group.getParentId());
			
		} catch (Exception e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("trans_exception");
			return rds;
		}
		return rds;
	}

	@Override
	public ResultDataSet rechangeChannelGroup(String accId, String cId, String gId, String createIp) {
		ResultDataSet rds = new ResultDataSet();
		Account acc;
		try {
			acc = accountPriDao.findOne(accId);
			if (acc == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("目标用户不存在！");
				return rds;
			}
			Channel channel = channelPriDao.findByCId(cId);
			if (channel == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("该频道不存在！");
				return rds;
			}
			String oldParentId = "";
			String newParentId = "";
			oldParentId = channel.getgId();
			ChannelGroup oldGroup = channelGroupPriDao.findOne(channel.getgId());
			if (oldGroup != null && !StringUtils.StringIsEmptyOrNull(oldGroup.getParentId())) {
				oldParentId = oldGroup.getParentId();
			}

			if (channel.getgId().equals(gId)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("该频道已经属于此分组！");
				return rds;
			}

			ChannelGroup group = channelGroupPriDao.findOne(gId);
			if (group == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("该频道组不存在！");
				return rds;
			}
			newParentId = gId;
			if (!StringUtils.StringIsEmptyOrNull(group.getParentId())) {
				newParentId = group.getParentId();
			}
			if (!groupUtils.verifyTopGroupMember(group, accId)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("沒有權限");
				return rds;
			}
			ChannelGroup groupTop = channelGroupPriDao.findOne(group.getParentId());
			if (groupTop == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("no_top_group");
				return rds;
			}

			if (!groupUtils.verifyGroupAuth(groupTop, accId, GroupAuth.ASSIGN_GROUP_ADMIN)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("没有权限");
				return rds;
			}

			logger.info("oldParentId:" + oldParentId);
			logger.info("newParentId:" + newParentId);
			if (!oldParentId.equals(newParentId)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("频道只能在同一公会下移动");
				return rds;
			}
			String sourceGId = channel.getgId();
			channel.setgId(gId);
			channelPriDao.save(channel);
			channelOpeRecordUtil.changeGroup(cId, sourceGId, gId, accId, newParentId);
			
			HotChannel hotChannel = hotChannelDao.findOne(channel.getcId());
			if (hotChannel != null) {
				hotChannel.setgId(channel.getgId());
				hotChannelDao.save(hotChannel);
			} else {
				HotChannel newHotChannel = new HotChannel();
				newHotChannel.setId(channel.getcId());
				newHotChannel.setChannelId(channel.getChannelId());
				newHotChannel.setOwnerId(channel.getOwnerId());
				newHotChannel.setChannelName(channel.getChannelName());
				newHotChannel.setType(channel.getType());
				newHotChannel.setStatus(channel.getStatus());
				newHotChannel.setCoverUrl(channel.getCoverUrl());
				newHotChannel.setgId(channel.getgId());
				newHotChannel.setChannelNotice(channel.getChannelNotice());
				newHotChannel.setYunXinCId(channel.getYunXinCId());
				newHotChannel.setYunXinRId(channel.getYunXinRId());
				newHotChannel.setPushDevice(0);
				newHotChannel.setPushUrl(channel.getPushUrl());
				newHotChannel.setHlsPullUrl(channel.getHlsPullUrl());
				newHotChannel.setHttpPullUrl(channel.getHttpPullUrl());
				newHotChannel.setRtmpPullUrl(channel.getRtmpPullUrl());
				newHotChannel.setConnCid("");
				newHotChannel.setLocation("");
				newHotChannel.setPlayerCount(0);
				newHotChannel.setWeekOffer(0);
				newHotChannel.setHeartCount(0);
				newHotChannel.setHeartUserCount(0);
				newHotChannel.setBrightness(0);
				newHotChannel.setTitle(channel.getTitle());
				hotChannelDao.save(newHotChannel);
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

	@Override
	public ResultDataSet requestCancelContract(String requestAccId, String anchorAccId, String ctId)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		AnchorContract contract = anchorContractPriDao.findOne(ctId);
		if (contract == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("未找到该合约");
			return rds;
		}
		if (contract.getStatus() != AnchorContractStatus.AGREED.getValue()
				&& contract.getStatus() != AnchorContractStatus.REQUEST_CANCEL.getValue()){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("合约当前不可以取消");
			return rds;
		}

		Account acc = accountPriDao.findOne(anchorAccId);
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		ChannelGroup channelGroup = channelGroupPriDao.findByGId(contract.getgId());
		if (channelGroup == null || !StringUtils.StringIsEmptyOrNull(channelGroup.getParentId())) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("公会ID错误");
			return rds;
		}
		long nowTime = DateUtils.getNowTime();
		if (requestAccId.equals(anchorAccId)) {
			contract.setCancelDirection(0);
			contract.setStatus(AnchorContractStatus.REQUEST_CANCEL.getValue());
			contract.setStartDate(nowTime);
			contract.setEndDate(nowTime + DateUtils.dayMilliSecond * 2);
			anchorContractPriDao.save(contract);
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		}
		if (!groupUtils.verifyTopGroupMember(channelGroup, requestAccId)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		contract.setCancelDirection(1);
		contract.setStatus(AnchorContractStatus.REQUEST_CANCEL.getValue());
		contract.setStartDate(DateUtils.getNowTime());
		contract.setEndDate(nowTime + DateUtils.dayMilliSecond * 2);
		anchorContractPriDao.save(contract);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet responseCancelContractNormal(String adminAccId, String ctId, boolean agree)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		AnchorContract contract = anchorContractPriDao.findOne(ctId);

		if (contract == null || contract.getStatus() != AnchorContractStatus.REQUEST_CANCEL.getValue()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("未找到该合约或已处理");
			return rds;
		}
		ChannelGroup channelGroup = channelGroupPriDao.findByGId(contract.getgId());
		if (channelGroup == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("公会ID错误");
			return rds;
		}
		if (!adminAccId.equals(contract.getAccId())) {
			if (!groupUtils.verifyTopGroupMember(channelGroup, adminAccId)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("没有权限");
				return rds;
			}
		}
		if (!agree) {
			contract.setStatus(AnchorContractStatus.AGREED.getValue());
			anchorContractPriDao.save(contract);
			rds.setCode(ResultCode.SUCCESS.getCode());
			rds.setMsg("拒绝解除合约成功");
			contractOpeRecordUtil.refuseCancel(contract.getAccId(), contract.getgId());
			return rds;
		}
		Channel channel = channelPriDao.findByOwnerId(contract.getAccId());
		if (channel != null) {
			HotChannel hotChannel = hotChannelDao.findOne(channel.getcId());
			if (hotChannel == null) {
				HotChannel newHotChannel = new HotChannel();
				newHotChannel.setId(channel.getcId());
				newHotChannel.setChannelId(channel.getChannelId());
				newHotChannel.setOwnerId(channel.getOwnerId());
				newHotChannel.setChannelName(channel.getChannelName());
				newHotChannel.setType(channel.getType());
				newHotChannel.setStatus(channel.getStatus());
				newHotChannel.setCoverUrl(channel.getCoverUrl());
				newHotChannel.setgId(channel.getgId());
				newHotChannel.setChannelNotice(channel.getChannelNotice());
				newHotChannel.setYunXinCId(channel.getYunXinCId());
				newHotChannel.setYunXinRId(channel.getYunXinRId());
				newHotChannel.setPushDevice(0);
				newHotChannel.setPushUrl(channel.getPushUrl());
				newHotChannel.setHlsPullUrl(channel.getHlsPullUrl());
				newHotChannel.setHttpPullUrl(channel.getHttpPullUrl());
				newHotChannel.setRtmpPullUrl(channel.getRtmpPullUrl());
				newHotChannel.setConnCid("");
				newHotChannel.setLocation("");
				newHotChannel.setPlayerCount(0);
				newHotChannel.setWeekOffer(0);
				newHotChannel.setHeartCount(0);
				newHotChannel.setHeartUserCount(0);
				newHotChannel.setBrightness(0);
				newHotChannel.setTitle(channel.getTitle());
				hotChannelDao.save(newHotChannel);
			}
			HotChannel hotChannelNew = hotChannelDao.findOne(channel.getcId());
			hotChannelNew.setOwnerId("");
			hotChannelDao.save(hotChannelNew);

			channel.setOwnerId("");
			channelPriDao.save(channel);
			
			HotChannelMic hotChannelMic = hotChannelMicDao.findOne(channel.getcId() + "_" + contract.getAccId());
			if (hotChannelMic != null){
				hotChannelMicDao.delete(hotChannelMic);
			}
			
			channelOpeRecordUtil.removeOwner(channel.getcId(), contract.getAccId(), adminAccId, contract.getgId());
			contractOpeRecordUtil.agreeCancel(contract.getAccId(), contract.getgId());
		}

		contract.setStatus(AnchorContractStatus.CANCELED.getValue());
		contract.setStartDate(DateUtils.getNowTime());
		anchorContractPriDao.save(contract);
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("成功解除合约");
		return rds;
	}

	@Override
	public ResultDataSet hideContract(String accId, String ctId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		AnchorContract contract = anchorContractPriDao.findOne(ctId);
		if (contract == null || contract.getHide() == true) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("未找到该合约或已隐藏");
			return rds;
		}
		ChannelGroup channelGroup = channelGroupPriDao.findByGId(contract.getgId());
		if (channelGroup == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("公会ID错误");
			return rds;
		}

		if (!contract.getAccId().equals(accId)
				&& !groupUtils.verifyGroupAuth(channelGroup, accId, GroupAuth.ASSIGN_GROUP_ADMIN)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		// contract.setStatus(AnchorContractStatus.REMOVED.getValue());
		contract.setHide(true);
		anchorContractPriDao.save(contract);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet unbundlingChannelGroup(String adminAccId, String cId) {
		ResultDataSet rds = new ResultDataSet();

		Channel channel = channelPriDao.findOne(cId);
		if (channel == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有找到频道");
			return rds;
		}
		/*
		 * ChannelGroup channelGroup =
		 * channelGroupDao.findByGId(channel.getcId()); if
		 * (!channelUtils.verifyGroupAuth(channelGroup, adminAccId,
		 * ChannelAuth.ASSIGN_OW_AUTHORITY)) {
		 * rds.setCode(ResultCode.PARAM_INVALID.getCode()); rds.setMsg("没有权限");
		 * return rds; }
		 */
		String oldAnchor = channel.getOwnerId();
		channel.setOwnerId("");
		channel.setChannelNotice("");
		channel.setClubName("");
		channelPriDao.save(channel);

		ChannelGroup cg = channelGroupSecDao.findByGId(channel.getgId());
		String topGId = cg.getgId();
		if (!StringUtils.StringIsEmptyOrNull(cg.getParentId())){
			topGId = cg.getParentId();
		}
		channelOpeRecordUtil.removeOwner(channel.getcId(), oldAnchor, adminAccId, topGId);
		
		HotChannel hotChannel = hotChannelDao.findOne(channel.getcId());
		if (hotChannel != null) {
			hotChannelDao.delete(hotChannel);
		}
		HotChannelMic hotChannelMic = hotChannelMicDao.findOne(cId + "_" + oldAnchor);
		if (hotChannelMic != null){
			hotChannelMicDao.delete(hotChannelMic);
		}
		rds.setMsg("解绑成功！");
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet cancelChannelGroupAdmin(String adminAccId, String accId, String gId) {
		ResultDataSet rds = new ResultDataSet();

		GroupAdminor groupAdminor = groupAdminorPriDao.findByAdminIdAndGId(accId, gId);
		if (groupAdminor == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("此账号管理員权限已经取消");
			return rds;
		}
		ChannelGroup channelGroup = channelGroupPriDao.findByGId(gId);
		if (!groupUtils.verifyGroupAuth(channelGroup, adminAccId, GroupAuth.ASSIGN_GROUP_ADMIN)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		groupAdminorPriDao.delete(groupAdminor);
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("权限取消成功！");
		return rds;
	}

	@Override
	public ResultDataSet removeChannelInSubGroup(String accId, String cId, String gId, String createIp) {
		ResultDataSet rds = new ResultDataSet();
		Account acc;
		try {
			Channel ch = channelPriDao.findOne(cId);
			acc = accountPriDao.findOne(accId);
			if (acc == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("目标用户不存在！");
				return rds;
			}
			ChannelGroup group = channelGroupPriDao.findOne(gId);
			if (group == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("该频道组不存在！");
				return rds;
			}
			if (ch == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("该频道不存在！");
				return rds;
			}
			if (!groupUtils.verifyTopGroupMember(group, accId)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("沒有權限");
				return rds;
			}
			if (!ch.getgId().equals(gId)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("此频道不属于此组");
				return rds;
			}
			ch.setgId(group.getParentId());
			channelPriDao.save(ch);

			HotChannel hotCh = hotChannelDao.findOne(cId);
			if (hotCh != null) {
				hotCh.setgId(group.getParentId());
				hotChannelDao.save(hotCh);
			}

			channelOpeRecordUtil.changeGroup(cId, gId, group.getParentId(), accId, group.getParentId());
			
			rds.setCode(ResultCode.SUCCESS.getCode());
			rds.setMsg("移除成功，已经将此组所属频道回收到公会下");
		} catch (Exception e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("trans_exception");
			return rds;
		}
		return rds;
	}

	@Override
	public ResultDataSet queryChannelInTopGroupNoAssign(String adminAccId, String gId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountSecDao.findOne(adminAccId);
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该账号");
			return rds;
		}
		ChannelGroup channelGroup = channelGroupSecDao.findByGId(gId);
		if (channelGroup == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该频道组");
			return rds;
		}
		if (!groupUtils.verifyGroupAuth(channelGroup, adminAccId, GroupAuth.ASSIGN_EDIT_INFO_AUTHORITY)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		List<Channel> channelsInTop = channelSecDao.findByGId(gId);
		rds.setData(channelsInTop);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getGroupInfo(String accId, String gId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountSecDao.findOne(accId);
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该账号");
			return rds;
		}
		ChannelGroup channelGroup = channelGroupSecDao.findByGId(gId);
		if (channelGroup == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该频道组");
			return rds;
		}
		// if (!channelUtils.verifyGroupAuth(channelGroup, adminAccId,
		// ChannelAuth.ASSIGN_EDIT_INFO_AUTHORITY)) {
		// rds.setCode(ResultCode.PARAM_INVALID.getCode());
		// rds.setMsg("没有权限");
		// return rds;
		// }
		ChannelGroup foundGroup = new ChannelGroup();
		foundGroup.setGroupId(channelGroup.getGroupId());
		foundGroup.setName(channelGroup.getName());
		foundGroup.setDescription(channelGroup.getDescription());
		foundGroup.setFaceUrl(channelGroup.getFaceUrl());
		foundGroup.setNotice(channelGroup.getNotice());
		rds.setData(foundGroup);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	/*
	 * @Override public ResultDataSet responseCancelContract(String adminAccId,
	 * String ctId) throws IOException { // TODO Auto-generated method stub
	 * return null; }
	 */
	@Override
	public ResultDataSet getAnchorPushRecord(String adminAccId, String accId, long fromTime, long toTime) {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountSecDao.findOne(accId);
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该账号");
			return rds;
		}
		AnchorContract anchorContract = null;
		anchorContract = anchorUtils.getAnchorContract(accId);
		if (anchorContract == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不是签约主播");
			return rds;
		}
		if (StringUtils.StringIsEmptyOrNull(anchorContract.getgId())) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("主播不属于任何公会");
			return rds;
		}
		ChannelGroup channelGroup = channelGroupSecDao.findByGId(anchorContract.getgId());
		if (!groupUtils.verifyGroupAuth(channelGroup, adminAccId, GroupAuth.ASSIGN_EDIT_INFO_AUTHORITY)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}

		if (toTime < fromTime) {
			long tmp = toTime;
			toTime = fromTime;
			fromTime = tmp;
		}

		if (toTime - fromTime > DateUtils.monthMilliSecond * 2) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("查询时间间隔过长");
			return rds;
		}

		List<AnchorPushRecord> anchorPushRecords = anchorPushRecordSecDao.findByTime(accId, fromTime, toTime);
		rds.setData(anchorPushRecords);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet calcAnchorActiveTime(String adminAccId, String accId, long fromTime, long toTime) {
		ResultDataSet rds = new ResultDataSet();
		if (toTime < fromTime) {
			long tmp = toTime;
			toTime = fromTime;
			fromTime = tmp;
		}

		if (toTime - fromTime > DateUtils.monthMilliSecond * 2) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("统计时间间隔过长");
			return rds;
		}

		AnchorContract anchorContract = null;
		anchorContract = anchorUtils.getAnchorContract(accId);
		if (anchorContract == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不是签约主播");
			return rds;
		}
		if (StringUtils.StringIsEmptyOrNull(anchorContract.getgId())) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("主播不属于任何公会");
			return rds;
		}
		ChannelGroup channelGroup = channelGroupSecDao.findByGId(anchorContract.getgId());
		if (!groupUtils.verifyGroupAuth(channelGroup, adminAccId, GroupAuth.ASSIGN_EDIT_INFO_AUTHORITY)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}

		List<AnchorPushRecord> anchorPushRecords = anchorPushRecordSecDao.findByTime(accId, fromTime, toTime);
		long totalTime = 0;
		if (anchorPushRecords != null) {
			for (AnchorPushRecord record : anchorPushRecords) {
				if (record.getCloseTime() == 0) {
					continue;
				}
				if (record.getOpenTime() > record.getCloseTime()) {
					continue;
				}
				totalTime += record.getCloseTime() - record.getOpenTime();
			}
		}
		ResponseData rp = new ResponseData();
		rp.put("activeSeconds", totalTime / 1000);
		rds.setData(rp);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryRequestedContracts(String adminAccId, String gId, int direction, int pageSize,
			int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		ChannelGroup channelGroup = channelGroupSecDao.findByGId(gId);
		if (channelGroup == null) {
			rds.setData("不存在该公会");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (!groupUtils.verifyGroupAuth(channelGroup, adminAccId, GroupAuth.ACCESS_CONTRACT)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		Sort sort = new Sort(Direction.fromString("desc"), "createTime");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<AnchorContract> contracts = anchorContractSecDao.findByGIdAndRequested(gId, direction, pageable);
		rds.setData(MyPageUtils.getMyPage(contracts));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryTopGroupRecord(String adminAccId, String gId, int pageSize, int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		ChannelGroup channelGroup = channelGroupSecDao.findByGId(gId);
		if (channelGroup == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该频道组");
			return rds;
		}
		if(!StringUtils.StringIsEmptyOrNull(channelGroup.getParentId())){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该分组不是公会");
			return rds;
		}
		if (!groupUtils.verifyGroupAuth(channelGroup, adminAccId, GroupAuth.ASSIGN_EDIT_INFO_AUTHORITY)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		Sort sort = new Sort(Direction.fromString("desc"), "datetime");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<GroupOpeRecord> page = groupOpeRecordSecDao.findByGId(gId, pageable);
		List<GroupOpeRecord> records = page.getContent();
		List<ResponseData> rebuilds = new ArrayList<ResponseData>();
		ResponseData result = new ResponseData();
		for (GroupOpeRecord rd : records){
			ResponseData tmp = new ResponseData();
			tmp.put("gId", 			gId);
			tmp.put("curName", 		channelGroup.getName());
			tmp.put("opeType", 		rd.getOpeType());
			tmp.put("operatorId", 	rd.getOperatorId());
			tmp.put("datetime", 	rd.getDatetime());
			tmp.put("oldName", 		rd.getOldName());
			tmp.put("newName", 		rd.getNewName());
			Account account = accountSecDao.findOne(rd.getOperatorId());
			if (account != null){
				tmp.put("operatorName", account.getNickName());
			}
			rebuilds.add(tmp);
		}
		
		result.put("content", 	rebuilds);
		result.put("count", 	page.getTotalElements());
		result.put("size", 		page.getSize());
		result.put("pages", 	page.getTotalPages());
		result.put("num", 		page.getNumber());
		rds.setData(result);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}
	
	@Override
	public ResultDataSet querySubGroupRecord(String adminAccId, String gId, int pageSize, int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		ChannelGroup channelGroup = channelGroupSecDao.findByGId(gId);
		if (channelGroup == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该频道组");
			return rds;
		}
		if(!StringUtils.StringIsEmptyOrNull(channelGroup.getParentId())){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该分组不是公会");
			return rds;
		}
		if (!groupUtils.verifyGroupAuth(channelGroup, adminAccId, GroupAuth.ASSIGN_EDIT_INFO_AUTHORITY)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		Sort sort= new Sort(Direction.fromString("desc"), "datetime");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<GroupOpeRecord> page = groupOpeRecordSecDao.findByParentId(gId, pageable);
		List<GroupOpeRecord> records = page.getContent();
		List<ResponseData> rebuilds = new ArrayList<ResponseData>();
		ResponseData result = new ResponseData();
		for (GroupOpeRecord rd : records){
			ChannelGroup sub = channelGroupSecDao.findByGId(rd.getgId());
			if (sub == null)
				continue;
			ResponseData tmp = new ResponseData();
			tmp.put("gId", 			rd.getgId());
			tmp.put("curName", 		sub.getName());
			tmp.put("opeType", 		rd.getOpeType());
			tmp.put("operatorId", 	rd.getOperatorId());
			tmp.put("datetime", 	rd.getDatetime());
			tmp.put("oldName", 		rd.getOldName());
			tmp.put("newName", 		rd.getNewName());
			Account account = accountSecDao.findOne(rd.getOperatorId());
			if (account != null){
				tmp.put("operatorName", account.getNickName());
			}
			rebuilds.add(tmp);
		}
		
		result.put("content", 	rebuilds);
		result.put("count", 	page.getTotalElements());
		result.put("size", 		page.getSize());
		result.put("pages", 	page.getTotalPages());
		result.put("num", 		page.getNumber());
		rds.setData(result);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}
	
	@Override
	public ResultDataSet queryChannelOpeRecord(String adminAccId, String gId, int pageSize, int pageNum){
		ResultDataSet rds = new ResultDataSet();
		ChannelGroup channelGroup = channelGroupSecDao.findByGId(gId);
		if (channelGroup == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该频道组");
			return rds;
		}
		/**
		 * 以公会为主体进行查询
		 * */
		if(!StringUtils.StringIsEmptyOrNull(channelGroup.getParentId())){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("请在公会内查询");
			return rds;
		}
		if (!groupUtils.verifyGroupAuth(channelGroup, adminAccId, GroupAuth.ASSIGN_EDIT_INFO_AUTHORITY)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}

		Sort sort = new Sort(Direction.fromString("desc"), "datetime");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<ChannelOpeRecord> page = channelOpeRecordSecDao.findByTopGId(gId, pageable);
		
		List<ChannelOpeRecord> records = page.getContent();
		List<ResponseData> rebuilds = new ArrayList<ResponseData>();
		ResponseData result = new ResponseData();
		for (ChannelOpeRecord rd : records){
			Channel channel = channelSecDao.findByCId(rd.getcId());
			if (channel == null){
				continue;
			}
			ResponseData tmp = new ResponseData();
			tmp.put("topGId", 		rd.getTopGId());
			tmp.put("cId", 			rd.getcId());
			tmp.put("opeType", 		rd.getOpeType());
			tmp.put("operatorId", 	rd.getOperatorId());
			tmp.put("datetime", 	rd.getDatetime());
			tmp.put("sourceGId", 	rd.getSourceGId());
			tmp.put("targetGId", 	rd.getTargetGId());
			tmp.put("anchorId", 	rd.getAnchorId());
			
			tmp.put("chName", 		channel.getChannelName());
			Account anchor = accountSecDao.findOne(rd.getAnchorId());
			if (anchor != null){
				tmp.put("anchorName", anchor.getNickName());
			}
			Account operator = accountSecDao.findOne(rd.getOperatorId());
			if (operator != null){
				tmp.put("operatorName", operator.getNickName());
			}
			rebuilds.add(tmp);
		}
		
		result.put("content", 	rebuilds);
		result.put("count", 	page.getTotalElements());
		result.put("size", 		page.getSize());
		result.put("pages", 	page.getTotalPages());
		result.put("num", 		page.getNumber());
		rds.setData(result);
		rds.setCode(ResultCode.SUCCESS.getCode());
		
		return rds;
	}
	
	@Override
	public ResultDataSet queryContractOpeRecord(String adminAccId, String gId, int pageSize, int pageNum){
		ResultDataSet rds = new ResultDataSet();
		ChannelGroup channelGroup = channelGroupSecDao.findByGId(gId);
		if (channelGroup == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该公会");
			return rds;
		}
		/**
		 * 以公会为主体进行查询
		 * */
		if(!StringUtils.StringIsEmptyOrNull(channelGroup.getParentId())){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("请在公会内查询");
			return rds;
		}
		if (!groupUtils.verifyGroupAuth(channelGroup, adminAccId, GroupAuth.ASSIGN_EDIT_INFO_AUTHORITY)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		Sort sort = new Sort(Direction.fromString("desc"), "datetime");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<ContractOpeRecord> page = contractOpeRecordSecDao.findByGId(gId, pageable);
		
		List<ContractOpeRecord> records = page.getContent();
		List<ResponseData> rebuilds = new ArrayList<ResponseData>();
		ResponseData result = new ResponseData();
		for (ContractOpeRecord rd : records){
			ResponseData tmp = new ResponseData();
			tmp.put("anchorId", 	rd.getAnchorId());
			tmp.put("opeType", 		rd.getOpeType());
			tmp.put("datetime", 	rd.getDatetime());
			Account anchor = accountSecDao.findOne(rd.getAnchorId());
			if (anchor != null){
				tmp.put("anchorName", anchor.getNickName());
			}
			rebuilds.add(tmp);
		}
		
		result.put("content", 	rebuilds);
		result.put("count", 	page.getTotalElements());
		result.put("size", 		page.getSize());
		result.put("pages", 	page.getTotalPages());
		result.put("num", 		page.getNumber());
		rds.setData(result);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}


	@Override
	public ResultDataSet deleteFreeChannel(String adminAccId, String gId, String cId) {
		ResultDataSet rds = new ResultDataSet();
		Channel channel= channelPriDao.findByCId(cId);
		if(channel==null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("这个频道不存在");
			return rds;		
		}	
		if(!StringUtils.StringIsEmptyOrNull(channel.getOwnerId())){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("非空闲频道");
			return rds;
		}
		RemovedChannel rc = new RemovedChannel();
		rc.setcId(cId);
		rc.setChannelId(channel.getChannelId());
		rc.setOwnerId(channel.getOwnerId());
		rc.setChannelName(channel.getChannelName());
		rc.setType(0);
		rc.setStatus(0);
		rc.setStatusChanged(0);
		rc.setCoverUrl(channel.getCoverUrl());
		rc.setgId(channel.getgId());
		rc.setChannelNotice(channel.getChannelNotice());
		rc.setYunXinCId(channel.getYunXinCId());
		rc.setYunXinRId(channel.getYunXinRId());
		rc.setTitle(channel.getTitle());
		rc.setPushUrl(channel.getPushUrl());
		rc.setHttpPullUrl(channel.getHttpPullUrl());
		rc.setHlsPullUrl(channel.getHlsPullUrl());
		rc.setRtmpPullUrl(channel.getRtmpPullUrl());
		rc.setNullity(false);
		rc.setCreateDate(0);
		rc.setCreateIp(channel.getCreateIp());
		rc.setLocation(channel.getLocation());
		rc.setClubName(channel.getClubName());
		rc.setClubScore(channel.getClubScore());
		rc.setClubLevel(channel.getClubLevel());
		rc.setClubTitle(channel.getClubTitle());
		rc.setClubIcon(channel.getClubIcon());
		rc.setClubMemberCount(channel.getClubMemberCount());
		rc.setCreatorId(channel.getCreatorId());
		rc.setPlayerCount(channel.getPlayerCount());
		rc.setPlayerTimes(channel.getPlayerTimes());
		rc.setWeekOffer(channel.getWeekOffer());
		rc.setHeartCount(channel.getHeartCount());
		rc.setHeartUserCount(channel.getHeartUserCount());
		rc.setBrightness(channel.getBrightness());
		rc.setZegoPublishUrl(channel.getZegoPublishUrl());
		rc.setZegoRtmpUrl(channel.getZegoRtmpUrl());
		rc.setZegoHlsUrl(channel.getZegoHlsUrl());
		rc.setZegoHdlUrl(channel.getZegoHdlUrl());
		rc.setZegoStreamId (channel.getZegoStreamId());
		rc.setZegoLiveId(channel.getZegoLiveId());
		removedChannelPriDao.save(rc);
		
		channelPriDao.delete(channel);
		HotChannel hotChannel = hotChannelDao.findOne(channel.getcId());
		if(hotChannel!=null){
			hotChannelDao.delete(hotChannel);
		}
		ChannelGroup channelGroup= channelGroupPriDao.findByGId(gId);
		if(channelGroup == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该公会不存在");
			return rds;
		}	
		channelUtils.updateChannelCount(channelGroup, -1);
		int usableCount = channelUtils.getTopGroupUsableChannelCount(channelGroup);
		ResponseData rd= new ResponseData();
		rd.put("usableCount", usableCount);
		rds.setData(rd);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}
	

	@Override
	public ResultDataSet forceCancelContract(String requestAccId, String gId, String ctId) {
		ResultDataSet rds = new ResultDataSet();
		AnchorContract contract = anchorContractPriDao.findOne(ctId);
		if(contract==null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("合约不存在");
			return rds;	
		}
		if(contract.getStatus()!=AnchorContractStatus.AGREED.getValue()
			&&contract.getStatus()!=AnchorContractStatus.REQUEST_CANCEL.getValue()){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该合约不可取消");
			return rds;
		}
		List<ForceCancelContract> forceCancelContracts = forceCancelContractSecDao.findByCtIdAndStatus(ctId, 
				ForceCancelContractStatus.REQUESTED.getValue());
		if (forceCancelContracts != null && forceCancelContracts.size() > 0){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("正在处理中");
			return rds;
		}
		ChannelGroup channelGroup = channelGroupPriDao.findByGId(contract.getgId());
		if(channelGroup==null||!StringUtils.StringIsEmptyOrNull(channelGroup.getParentId())){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("公会Id错误");
			return rds;		
		}
		long nowTime=DateUtils.getNowTime();
		if(!groupUtils.verifyTopGroupMember(channelGroup, requestAccId)){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;			
		}
		ForceCancelContract fcc = new ForceCancelContract();
		fcc.setAccId(requestAccId);
		fcc.setCtId(contract.getCtId());
		fcc.setStatus(ForceCancelContractStatus.REQUESTED.getValue());
		fcc.setRequestedDateTime(nowTime);
		fcc.setResponsedDateTime(0);
		fcc.setCancelDirection(1);
		forceCancelContractPriDao.save(fcc);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryAnchorStatus(String adminAccId, String accId) {
		ResultDataSet rds = new ResultDataSet();
		if(accId==null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("用户不存在");
			return rds;	
		}
		AnchorContract contract= anchorUtils.getAnchorContract(accId);
		if(contract==null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("合约不存在");
			return rds;	
		}
		ChannelGroup channelGroup = channelGroupSecDao.findByGId(contract.getgId());
		if(channelGroup==null||!StringUtils.StringIsEmptyOrNull(channelGroup.getParentId())){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("公会Id错误");
			return rds;		
		}
		if(!groupUtils.verifyTopGroupMember(channelGroup, adminAccId)){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;			
		}
		Channel channel = channelSecDao.findByOwnerId(accId);
		HashMap<String,Object> respone=new HashMap<String,Object>();
		
		respone.put("contract",	contract != null);
		respone.put("assigned",	channel != null);
		rds.setData(respone);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryChannelCount(String adminAccId, String gId) {
		ResultDataSet rds=new ResultDataSet();
		ChannelGroup channelGroup=channelGroupSecDao.findByGId(gId);
		if(channelGroup==null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("公会不存在");
			return rds;
		}
		int usableCount=0;
		usableCount=channelUtils.getTopGroupUsableChannelCount(channelGroup);
		ResponseData rd = new ResponseData();
		rd.put("usableCount", usableCount);
		rds.setData(rd);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryAnchorWallet(String adminAccId, String gId,String accId) {
		ResultDataSet rds = new ResultDataSet();
		Account account = accountSecDao.findOne(accId);
		if (account == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("用户不存在");
			return rds;
		}
		if (!account.isAnchor()){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("只能查询主播");
			return rds;
		}
		ChannelGroup channelGroup = channelGroupSecDao.findByGId(gId);
		if (channelGroup == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("公会id错误");
			return rds;
		}
		if (!groupUtils.verifyTopGroupMember(channelGroup, adminAccId)){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		
		AnchorContract anchorContract = anchorUtils.getAnchorContract(accId);
		if (anchorContract == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该主播未签约");
			return rds;
		}
		String topGroupGId = channelGroup.getgId();
		if (!StringUtils.StringIsEmptyOrNull(channelGroup.getParentId())){
			topGroupGId = channelGroup.getParentId();
		}
		if (!anchorContract.getgId().equals(topGroupGId)){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该主播未和当前公会签约");
			return rds;
		}
		Wallet wallet = walletSecDao.findByAccId(accId);
		rds.setData(wallet);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryAverageGiftCommision(String adminAccId, String gId, String accId,long fromTime,long toTime) {
		ResultDataSet rds = new ResultDataSet();
		Account account = accountSecDao.findOne(accId);
		if (account == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("用户不存在");
			return rds;
		}
		if (!account.isAnchor()){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("只能查询主播");
			return rds;
		}
		ChannelGroup channelGroup = channelGroupSecDao.findByGId(gId);
		if (channelGroup == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("公会id错误");
			return rds;
		}
		if (!groupUtils.verifyTopGroupMember(channelGroup, adminAccId)){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		
		AnchorContract anchorContract = anchorUtils.getAnchorContract(accId);
		if (anchorContract == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该主播未签约");
			return rds;
		}
		String topGroupGId = channelGroup.getgId();
		if (!StringUtils.StringIsEmptyOrNull(channelGroup.getParentId())){
			topGroupGId = channelGroup.getParentId();
		}
		if (!anchorContract.getgId().equals(topGroupGId)){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该主播未和当前公会签约");
			return rds;
		}
		if(toTime-fromTime>DateUtils.monthMilliSecond*2){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("查询时间过长");
			return rds;
		}
		
		List<AnchorPushRecord> anchorPushRecords = anchorPushRecordSecDao.findByTime(accId, fromTime, toTime);
		
		int effectiveDay = 0;
		for (AnchorPushRecord a : anchorPushRecords){
			if (a.getCloseTime() <= 0)
				continue;
			if (a.isIgnored())
				continue;
			if (a.getOpenTime() >= a.getCloseTime())
				continue;
			if (a.getCloseTime() - a.getOpenTime() >= DateUtils.dayMilliSecond * 2){
				effectiveDay++;
			}
		}
		
		System.out.println(effectiveDay);
		
		if (effectiveDay <= 0){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("当月没有有效开播记录");
			return rds;
		}
		
		List<MoneyFlow> giftCommissionMoneyFlow = moneyFlowSecDao.getTotalGiftCommission(accId, 
				MoneyFlowType.GiftCommission.getValue(), fromTime, toTime);
		long giftCommission = 0;
		if (giftCommissionMoneyFlow != null && giftCommissionMoneyFlow.size() > 0){
			for(MoneyFlow moneyFlow : giftCommissionMoneyFlow){
				giftCommission += moneyFlow.getTargetCommission() - moneyFlow.getSourceCommission();
			}
		}
		double average = giftCommission * 1.0 / effectiveDay;
		ResponseData rp = new ResponseData();
		rp.put("days", effectiveDay);
		rp.put("dailyCommission", average);
		
		rds.setData(rp);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}
}
