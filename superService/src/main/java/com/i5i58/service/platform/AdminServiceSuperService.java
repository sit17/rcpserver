package com.i5i58.service.platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.constants.ResponseData;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.platform.IPlatformAdmin;
import com.i5i58.data.account.Account;
import com.i5i58.data.account.OpenId;
import com.i5i58.data.channel.Channel;
import com.i5i58.data.channel.ChannelId;
import com.i5i58.data.channel.HotChannel;
import com.i5i58.data.home.Carousel;
import com.i5i58.data.pay.OnLineOrder;
import com.i5i58.data.superAdmin.HotSuperAdmin;
import com.i5i58.data.superAdmin.SuperAdmin;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.AuthAnchorPriDao;
import com.i5i58.primary.dao.account.OpenIdPriDao;
import com.i5i58.primary.dao.channel.ChannelIdPriDao;
import com.i5i58.primary.dao.channel.ChannelPriDao;
import com.i5i58.primary.dao.home.CarouselPriDao;
import com.i5i58.primary.dao.superAdmin.SuperAdminPriDao;
import com.i5i58.redis.all.HotChannelDao;
import com.i5i58.redis.all.HotChannelViewerDao;
import com.i5i58.redis.all.HotSuperAdminDao;
import com.i5i58.secondary.dao.channel.ChannelIdSecDao;
import com.i5i58.secondary.dao.channel.ChannelSecDao;
import com.i5i58.secondary.dao.home.CarouselSecDao;
import com.i5i58.secondary.dao.pay.OnLineOrderSecDao;
import com.i5i58.secondary.dao.superAdmin.SuperAdminSecDao;
import com.i5i58.util.AccountUtils;
import com.i5i58.util.DateUtils;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.MyPageUtils;
import com.i5i58.util.OpenIdUtils;
import com.i5i58.util.ServerCode;
import com.i5i58.util.StringUtils;
import com.i5i58.util.SuperAdminUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CodeToString;
import com.i5i58.yunxin.Utils.RefreshTokenResult;

@Service(protocol = "dubbo")
public class AdminServiceSuperService implements IPlatformAdmin {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	JsonUtils jsonUtil;

	@Autowired
	OpenIdPriDao openIdPriDao;

	@Autowired
	AccountPriDao accountPriDao;

	@Autowired
	AuthAnchorPriDao authAnchorPriDao;

	@Autowired
	SuperAdminPriDao superAdminPriDao;

	@Autowired
	SuperAdminSecDao superAdminSecDao;

	@Autowired
	HotSuperAdminDao hotSuperAdminDao;

	@Autowired
	OpenIdUtils openIdUtils;

	@Autowired
	SuperAdminUtils superAdminUtils;

	@Autowired
	AccountUtils accountUtils;

	@Autowired
	CarouselPriDao carouselPriDao;

	@Autowired
	CarouselSecDao carouselSecDao;

	@Autowired
	HotChannelDao hotChannelDao;

	@Autowired
	ChannelPriDao channelPriDao;

	@Autowired
	ChannelSecDao channelSecDao;

	@Autowired
	HotChannelViewerDao hotChannelViewerDao;

	@Autowired
	ChannelIdPriDao channelIdPriDao;

	@Autowired
	ChannelIdSecDao channelIdSecDao;
	
	@Autowired
	OnLineOrderSecDao onLineOrderSecDao;

	@Override
	public ResultDataSet insertTestDataToOpenIds(String superAccId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		for (int i = 0; i < 1000; i++) {
			OpenId oi = new OpenId();
			oi.setOpenId(Integer.toString(i + 10000));
			oi.setUsed((byte) 0);
			oi.setUsedDate(DateUtils.getNowTime());
			openIdPriDao.save(oi);
		}
		return rds;
	}

	@Override
	public ResultDataSet addSuperAdmin(String superAccId, String accId, long brithday, String depart, String email,
			byte gender, String location, String realName, int auth) {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(accId);
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		if (superAdminUtils.isSuperAdmin(accId)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该账号已经是超管");
			return rds;
		}
		SuperAdmin superAdmin = superAdminUtils.createSuperAdmin(accId, brithday, depart, email, gender, location,
				acc.getOpenId(), acc.getPassword(), acc.getPhoneNo(), realName, auth);
		rds.setData(superAdmin);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet disableSuperAdmin(String superAccId, String accId) {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(accId);
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		if (!superAdminUtils.isSuperAdmin(accId)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该账号不是超管");
			return rds;
		}
		superAdminUtils.disableSuperAdmin(accId);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet updateSuperAdmin(String superAccId) {
		ResultDataSet rds = new ResultDataSet();
		hotSuperAdminDao.deleteAll();
		List<SuperAdmin> SuperAdmins = superAdminUtils.getAllSuperAdmin();
		for (SuperAdmin sa : SuperAdmins) {
			HotSuperAdmin hsa = new HotSuperAdmin();
			hsa.setAdminRight(sa.getAdminRight());
			hsa.setDepart(sa.getDepart());
			hsa.setEmailAddress(sa.getEmailAddress());
			hsa.setGender(sa.getGender());
			hsa.setId(sa.getAccId());
			hsa.setLocation(sa.getLocation());
			hsa.setOpenId(sa.getOpenId());
			hsa.setPassword(sa.getPassword());
			hsa.setPhoneNo(sa.getPhoneNo());
			hsa.setRealName(sa.getRealName());
			hotSuperAdminDao.save(hsa);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	/*
	 * private void loadHotAccount(Account acc, String token) { AccountProperty
	 * accountProperty = accountPropertyDao.findOne(acc.getAccId()); HotAccount
	 * hotAccount = new HotAccount(acc.getAccId(), acc.getOpenId(),
	 * acc.getPhoneNo(), acc.getNickName(), acc.getStageName(), acc.isAnchor(),
	 * acc.getGender(), acc.getBirthDate(), acc.getFaceSmallUrl(),
	 * acc.getFaceOrgUrl(), acc.getVersion(), accountProperty.getVip(),
	 * accountProperty.getVipDeadline(), accountProperty.getRichScore(),
	 * accountProperty.getScore(), accountProperty.getMountsId(),
	 * accountProperty.getMountsName(), accountProperty.getClubCid(),
	 * accountProperty.getClubName(), 0, accountProperty.getFansCount(),
	 * accountProperty.getFocusCount(), accountProperty.getEssayCount(),
	 * accountProperty.getMedals(), acc.getLocation(), acc.getSignature(),
	 * acc.getPersonalBrief(), Constant.ACC_TOKEN_TIME_TO_LIVE);
	 * hotAccountsDao.save(hotAccount); Wallet wallet =
	 * walletDao.findOne(acc.getAccId()); HotWallet hotWallet = new HotWallet();
	 * hotWallet.setId(wallet.getAccId());
	 * hotWallet.setiGold(wallet.getiGold());
	 * hotWallet.setDiamond(wallet.getDiamond());
	 * hotWallet.setTicket(wallet.getTicket());
	 * hotWallet.setCommission(wallet.getCommission());
	 * hotWalletDao.save(hotWallet);
	 * accountUtils.setTokenExpire(acc.getAccId()); }
	 */

	@Override
	public ResultDataSet superLoginByOpenId(String openId, String password, int accountVersion, String clientIP)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findByOpenIdAndPassword(openId, password);
		if (null != acc) {
			if (!superAdminPriDao.exists(acc.getAccId())) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("没有权限");
				return rds;
			}
			String token = accountUtils.getToken(acc.getAccId());
			if (token == null) {
				RefreshTokenResult refreshTokenResult = YunxinIM.refreshTokenAccount(acc.getAccId());
				if (refreshTokenResult == null) {
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					rds.setMsg("yx_exception:logid null");
					return rds;
				}
				if (!refreshTokenResult.getCode().equals("200")) {
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					rds.setMsg("yx_exception:" + CodeToString.getString(refreshTokenResult.getCode()) + " desc:"
							+ refreshTokenResult.getString("desc"));
					return rds;
				}
				if (!refreshTokenResult.getInfo().getAccid().equals(acc.getAccId())) {
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					rds.setMsg("yx_exception:accid dif");
					return rds;
				}
				token = refreshTokenResult.getInfo().getToken();
				accountUtils.setToken(acc.getAccId(), token);
			}
			// loadHotAccount(acc, token);
			superAdminUtils.superAdminRecord(acc.getAccId(), "登录");
			if (acc.getVersion() != accountVersion) {
				acc.setPhoneNo(StringUtils.addMask(acc.getPhoneNo(), '*', 2, 2));
				acc.setEmailAddress(StringUtils.addEmailMask(acc.getEmailAddress(), '*', 2, 2));
				ResponseData response = new ResponseData();
				response.put("acc", acc);
				response.put("token", token);
				rds.setData(response);
			} else {
				rds.setData(token);
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
		} else {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账户或密码错误");
		}
		return rds;
	}

	@Override
	public ResultDataSet querySuperAdminList(int pageSize, int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "adminRight");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<SuperAdmin> superAdmins = superAdminSecDao.findAll(pageable);
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setData(MyPageUtils.getMyPage(superAdmins));
		return rds;
	}

	@Override
	public ResultDataSet queryCarousel(int device, long startTime, long endTime) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		List<Carousel> carousels = carouselSecDao.findByDeviceInTime(device, startTime, endTime);
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setData(carousels);
		return rds;
	}

	/**
	 * Clean All cached under name roomcache
	 */
	@Override
	@CacheEvict(value = "Carousel", allEntries = true)
	public ResultDataSet refreashCarouselCache() {
		// Define Response
		ResultDataSet rds = new ResultDataSet();
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("Server Suc");
		return rds;
	}

	@Override
	public ResultDataSet addCarousel(String superAccId, int device, int index, String imgUrl, String action,
			String params, boolean isChannel, String cId, long startTime, long endTime) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Carousel carousel = new Carousel();
		carousel.setcId(cId);
		carousel.setAction(action);
		carousel.setDevice(device);
		carousel.setStartTime(startTime);
		carousel.setEndTime(endTime);
		carousel.setImgUrl(imgUrl);
		carousel.setParams(params);
		carousel.setPosition(index);
		if (isChannel && !StringUtils.StringIsEmptyOrNull(cId)) {
			Channel channel = channelPriDao.findByCId(cId);
			if (channel == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("频道不存在");
				return rds;
			}
			carousel.setHlsPullUrl(channel.getHlsPullUrl());
			carousel.setHttpPullUrl(channel.getHttpPullUrl());
			carousel.setRtmpPullUrl(channel.getRtmpPullUrl());
			carousel.setYunXinRId(channel.getYunXinRId());
			carousel.setCoverUrl(channel.getCoverUrl());
			carousel.setZegoHdlUrl(channel.getZegoHdlUrl());
			carousel.setZegoHlsUrl(channel.getZegoHlsUrl());
			carousel.setZegoRtmpUrl(channel.getZegoRtmpUrl());
			carousel.setZegoStreamAlias(channel.getZegoStreamAlias());
		}
		carouselPriDao.save(carousel);
		superAdminUtils.superAdminRecord(superAccId,
				"添加轮播图{cId:%s, action:%s, device:%s, startTime:%s, endTime,imgUrl:%s, params:%s, index:%s}", cId,
				action, device, startTime, endTime, imgUrl, params, index);
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setData(carousel);
		return rds;
	}

	@Override
	public ResultDataSet removeCarousel(String superAccId, long id) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		if (!carouselPriDao.exists(id)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该轮播不存在");
			return rds;
		}
		carouselPriDao.delete(id);
		superAdminUtils.superAdminRecord(superAccId, "删除轮播图{id:%s}", id);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet updateCarousel(String superAccId, long id, int device, int index, String imgUrl, String action,
			String params, boolean isChannel, String cId, long startTime, long endTime) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Carousel carousel = carouselPriDao.findOne(id);
		if (carousel == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该轮播不存在");
			return rds;
		}
		carousel.setcId(cId);
		carousel.setAction(action);
		carousel.setDevice(device);
		carousel.setStartTime(startTime);
		carousel.setEndTime(endTime);
		carousel.setImgUrl(imgUrl);
		carousel.setParams(params);
		carousel.setPosition(index);
		if (isChannel && !StringUtils.StringIsEmptyOrNull(cId)) {
			Channel channel = channelPriDao.findByCId(cId);
			if (channel == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("频道不存在");
				return rds;
			}
			carousel.setHlsPullUrl(channel.getHlsPullUrl());
			carousel.setHttpPullUrl(channel.getHttpPullUrl());
			carousel.setRtmpPullUrl(channel.getRtmpPullUrl());
			carousel.setYunXinRId(channel.getYunXinRId());
			carousel.setCoverUrl(channel.getCoverUrl());
			carousel.setZegoHdlUrl(channel.getZegoHdlUrl());
			carousel.setZegoHlsUrl(channel.getZegoHlsUrl());
			carousel.setZegoRtmpUrl(channel.getZegoRtmpUrl());
			carousel.setZegoStreamAlias(channel.getZegoStreamAlias());
		} else {
			carousel.setHlsPullUrl("");
			carousel.setHttpPullUrl("");
			carousel.setRtmpPullUrl("");
			carousel.setYunXinRId("");
			carousel.setCoverUrl("");
		}
		carouselPriDao.save(carousel);
		superAdminUtils.superAdminRecord(superAccId, "更新轮播图{id:%s}", id);
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setData(carousel);
		return rds;
	}

	@Override
	public ResultDataSet getCarousel(long id) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Carousel carousel = carouselSecDao.findOne(id);
		if (carousel == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该轮播不存在");
			return rds;
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setData(carousel);
		return rds;
	}

	@Override
	public ResultDataSet syncHotAccountCache(String superAccid) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		// hotAccountsDao.deleteAll();
		// Iterable<Account> Accs = accountDao.findAll();
		// List<HotAccount> newHotAccs = new ArrayList<>();
		// for (Account acc : Accs) {
		// String key = Constant.HOT_ACCOUNT_TOKEN_SET_KEY + acc.getAccId();
		// if (new JedisUtils().exist(key)) {
		// AccountProperty accPty = accountPropertyDao.findOne(acc.getAccId());
		// if (acc != null && accPty != null) {
		// HotAccount newHotAcc = new HotAccount(acc.getAccId(),
		// acc.getOpenId(), acc.getPhoneNo(),
		// acc.getNickName(), acc.getStageName(), acc.isAnchor(),
		// acc.getGender(), acc.getBirthDate(),
		// acc.getFaceSmallUrl(), acc.getFaceOrgUrl(), acc.getVersion(),
		// accPty.getVip(),
		// accPty.getVipDeadline(), accPty.getRichScore(), accPty.getScore(),
		// accPty.getMountsId(),
		// accPty.getMountsName(), accPty.getClubCid(), accPty.getClubName(), 0,
		// accPty.getFansCount(),
		// accPty.getFocusCount(), accPty.getEssayCount(), accPty.getMedals(),
		// acc.getLocation(),
		// acc.getSignature(), acc.getPersonalBrief(),
		// Constant.ACC_TOKEN_TIME_TO_LIVE);
		// newHotAcc.setAnchor(acc.isAndroid());
		// newHotAcc.setFaceUseInGame(acc.isFaceUseInGame());
		// newHotAccs.add(newHotAcc);
		// }
		// }
		// }
		// hotAccountsDao.save(newHotAccs);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	// @Override
	// public ResultDataSet clearHotChannelCache(String superAccid) throws
	// IOException {
	// ResultDataSet rds = new ResultDataSet();
	// hotChannelDao.deleteAll();
	// rds.setCode(ResultCode.SUCCESS.getCode());
	// return rds;
	// }

	@Override
	public ResultDataSet clearHotChannel(String superAccId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		hotChannelDao.deleteAll();
		superAdminUtils.superAdminRecord(superAccId, "清除频道缓存");
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet syncHotChannel(String superAccId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		List<Channel> channels = channelPriDao.findByOwnerIdNotAndNullity("", false);
		List<HotChannel> hotChannels = new ArrayList<HotChannel>();
		for (Channel c : channels) {
			
			if (!StringUtils.StringIsEmptyOrNull(c.getOwnerId()) && c.getType() != 0) {
				Account acc = accountPriDao.findOne(c.getOwnerId());
				if (acc != null && acc.isAnchor() && !acc.isNullity()) {
					if (!StringUtils.StringIsEmptyOrNull(acc.getLocation())) {
						c.setLocation(acc.getLocation());
					}
					HotChannel hotc = hotChannelDao.findOne(c.getcId());
					if (hotc == null) {
						HotChannel hotChannel = new HotChannel();
						hotChannel.setId(c.getcId());
						hotChannel.setChannelId(c.getChannelId());
						hotChannel.setOwnerId(c.getOwnerId());
						hotChannel.setChannelName(c.getChannelName());
						if (acc != null && !StringUtils.StringIsEmptyOrNull(acc.getStageName())) {
							hotChannel.setChannelName(acc.getStageName());
						}
						hotChannel.setType(c.getType());
						hotChannel.setStatus(c.getStatus());
						hotChannel.setCoverUrl(c.getCoverUrl());
						hotChannel.setgId(c.getgId());
						hotChannel.setChannelNotice(c.getChannelNotice());
						hotChannel.setYunXinCId(c.getYunXinCId());
						hotChannel.setYunXinRId(c.getYunXinRId());
						hotChannel.setPushDevice(0);
						hotChannel.setPushUrl(c.getPushUrl());
						hotChannel.setHlsPullUrl(c.getHlsPullUrl());
						hotChannel.setHttpPullUrl(c.getHttpPullUrl());
						hotChannel.setRtmpPullUrl(c.getRtmpPullUrl());
						hotChannel.setConnCid("");
						if (acc != null && acc.getLocation() != null) {
							hotChannel.setLocation(acc.getLocation());
						} else {
							hotChannel.setLocation("");
						}
						hotChannel.setPlayerCount(0);
						hotChannel.setWeekOffer(0);
						hotChannel.setHeartCount(0);
						hotChannel.setHeartUserCount(0);
						hotChannel.setBrightness(0);
						hotChannel.setTitle(c.getTitle());
						hotChannel.setClubLevel(c.getClubLevel());
						hotChannel.setClubName(c.getClubName());
						hotChannel.setClubScore(c.getClubScore());
						hotChannel.setClubTitle(c.getClubTitle());
						hotChannel.setClubIcon(c.getClubIcon());
						hotChannel.setClubMemberCount(c.getClubMemberCount());
						hotChannels.add(hotChannel);
					} else {
						hotc.setChannelId(c.getChannelId());
						hotc.setOwnerId(c.getOwnerId());
						hotc.setChannelName(c.getChannelName());
						if (acc != null && !StringUtils.StringIsEmptyOrNull(acc.getStageName())) {
							hotc.setChannelName(acc.getStageName());
						}
						hotc.setType(c.getType());
						hotc.setStatus(c.getStatus());
						hotc.setCoverUrl(c.getCoverUrl());
						hotc.setgId(c.getgId());
						hotc.setChannelNotice(c.getChannelNotice());
						hotc.setYunXinCId(c.getYunXinCId());
						hotc.setYunXinRId(c.getYunXinRId());
						hotc.setPushUrl(c.getPushUrl());
						hotc.setHlsPullUrl(c.getHlsPullUrl());
						hotc.setHttpPullUrl(c.getHttpPullUrl());
						hotc.setRtmpPullUrl(c.getRtmpPullUrl());
						hotc.setConnCid("");
						if (acc != null && acc.getLocation() != null) {
							hotc.setLocation(acc.getLocation());
						} else {
							hotc.setLocation("");
						}
						hotChannels.add(hotc);
					}
				}
			}
		}
		channelPriDao.save(channels);
		hotChannelDao.save(hotChannels);
		superAdminUtils.superAdminRecord(superAccId, "同步频道缓存");
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet clearChannelViewer(String superAccId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		hotChannelViewerDao.deleteAll();
		superAdminUtils.superAdminRecord(superAccId, "清除频道观众缓存");
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet addChannelId(int count) {
		ResultDataSet rds = new ResultDataSet();
		int minId = 600000;
		ChannelId channelId = channelIdSecDao.findMaxOne();
		if (channelId == null) {

		} else {
			minId = Integer.parseInt(channelId.getChannelId()) + 1;
		}
		List<ChannelId> ids = new ArrayList<ChannelId>();
		for (int i = 0; i < count; i++) {
			ChannelId chId = new ChannelId();
			chId.setChannelId(((Integer) (i + minId)).toString());
			chId.setUsed(false);
			chId.setUsedDate(0);
			ids.add(chId);
		}
		channelIdPriDao.save(ids);
		return rds;
	}

	@Override
	public ResultDataSet getRandomChannelId(boolean consume) {
		ResultDataSet rds = new ResultDataSet();
		String strId = openIdUtils.getRandomChannelId(consume);
		if (strId == null || strId.equals("")) {
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("没有空闲的频道id");
		} else {
			rds.setCode(ResultCode.SUCCESS.getCode());
			rds.setData(strId);
		}
		return rds;
	}

	@Override
	public ResultDataSet getOnlineOrders(int status, int pageNum, int pageSize) {
		ResultDataSet rds =new ResultDataSet();
		Pageable pageable =new PageRequest(pageNum, pageSize);
		Page<OnLineOrder> onlineOrders =onLineOrderSecDao.findByOrderStatus(status, pageable);
		if(onlineOrders==null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("无人充值");
			return rds;
		}
		rds.setData(onlineOrders);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}
}
