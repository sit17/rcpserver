package com.i5i58.service.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
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
import com.i5i58.apis.platform.IPlatformConfig;
import com.i5i58.data.account.AccountVipConfig;
import com.i5i58.data.account.HotAccountVipConfig;
import com.i5i58.data.anchor.CommissionByGuardConfig;
import com.i5i58.data.channel.ChannelGift;
import com.i5i58.data.channel.ChannelGiftType;
import com.i5i58.data.channel.ChannelGuardConfig;
import com.i5i58.data.channel.ChannelMount;
import com.i5i58.data.channel.ChannelSound;
import com.i5i58.data.channel.ChannelType;
import com.i5i58.data.channel.ClubTaskConfig;
import com.i5i58.data.channel.FansClubConfig;
import com.i5i58.data.channel.HotChannelGift;
import com.i5i58.data.channel.HotChannelGiftType;
import com.i5i58.data.channel.HotChannelGuardConfig;
import com.i5i58.data.channel.HotChannelMount;
import com.i5i58.data.channel.HotClubTaskConfig;
import com.i5i58.data.channel.HotFansClubConfig;
import com.i5i58.data.channel.MountPresent;
import com.i5i58.data.config.AppVersionControl;
import com.i5i58.data.config.AppVersionStatus;
import com.i5i58.data.config.ApplePayType;
import com.i5i58.data.config.AppleReleaseType;
import com.i5i58.data.config.PlatformConfig;
import com.i5i58.data.config.ReactNativeConfig;
import com.i5i58.data.config.ReactNativeVersionConfig;
import com.i5i58.data.game.GameKindItem;
import com.i5i58.data.game.GameVipConfig;
import com.i5i58.data.pay.Product;
import com.i5i58.primary.dao.account.AccountVipConfigPriDao;
import com.i5i58.primary.dao.anchor.CommissionByGuardConfigPriDao;
import com.i5i58.primary.dao.channel.ChannelGiftPriDao;
import com.i5i58.primary.dao.channel.ChannelGiftTypePriDao;
import com.i5i58.primary.dao.channel.ChannelGuardConfigPriDao;
import com.i5i58.primary.dao.channel.ChannelMountPriDao;
import com.i5i58.primary.dao.channel.ChannelSoundPriDao;
import com.i5i58.primary.dao.channel.ChannelTypePriDao;
import com.i5i58.primary.dao.channel.ClubTaskConfigPriDao;
import com.i5i58.primary.dao.channel.FansClubConfigPriDao;
import com.i5i58.primary.dao.channel.MountPresentPriDao;
import com.i5i58.primary.dao.config.AppVersionControlPriDao;
import com.i5i58.primary.dao.config.PlatformConfigPriDao;
import com.i5i58.primary.dao.config.ReactNativeConfigPriDao;
import com.i5i58.primary.dao.config.ReactNativeVersionConfigPriDao;
import com.i5i58.primary.dao.game.GameVipConfigPriDao;
import com.i5i58.primary.dao.pay.ProductPriDao;
import com.i5i58.redis.all.HotAccountVipConfigDao;
import com.i5i58.redis.all.HotChannelGiftDao;
import com.i5i58.redis.all.HotChannelGiftTypeDao;
import com.i5i58.redis.all.HotChannelGuardConfigDao;
import com.i5i58.redis.all.HotChannelMountDao;
import com.i5i58.redis.all.HotClubTaskConfigDao;
import com.i5i58.redis.all.HotFansClubConfigDao;
import com.i5i58.secondary.dao.account.AccountVipConfigSecDao;
import com.i5i58.secondary.dao.anchor.CommissionByGuardConfigSecDao;
import com.i5i58.secondary.dao.channel.ChannelGiftSecDao;
import com.i5i58.secondary.dao.channel.ChannelGiftTypeSecDao;
import com.i5i58.secondary.dao.channel.ChannelGuardConfigSecDao;
import com.i5i58.secondary.dao.channel.ChannelMountSecDao;
import com.i5i58.secondary.dao.channel.ChannelSoundSecDao;
import com.i5i58.secondary.dao.channel.ChannelTypeSecDao;
import com.i5i58.secondary.dao.channel.ClubTaskConfigSecDao;
import com.i5i58.secondary.dao.channel.FansClubConfigSecDao;
import com.i5i58.secondary.dao.channel.MountPresentSecDao;
import com.i5i58.secondary.dao.config.AppVersionControlSecDao;
import com.i5i58.secondary.dao.config.PlatformConfigSecDao;
import com.i5i58.secondary.dao.config.ReactNativeConfigSecDao;
import com.i5i58.secondary.dao.config.ReactNativeVersionConfigSecDao;
import com.i5i58.secondary.dao.game.GameVipConfigSecDao;
import com.i5i58.secondary.dao.pay.ProductSecDao;
import com.i5i58.util.Constant;
import com.i5i58.util.DateUtils;
import com.i5i58.util.DeviceCode;
import com.i5i58.util.JedisUtils;
import com.i5i58.util.MyPageUtils;
import com.i5i58.util.StringUtils;
import com.i5i58.yunxin.Utils.CodeToString;
import com.i5i58.yunxin.Utils.YXResultSet;

@Service(protocol = "dubbo")
public class ConfigSuperService implements IPlatformConfig {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	ChannelGiftPriDao channelGiftPriDao;

	@Autowired
	ChannelGiftSecDao channelGiftSecDao;

	@Autowired
	HotChannelGiftDao hotChannelGiftDao;

	@Autowired
	ChannelMountPriDao channelMountPriDao;

	@Autowired
	ChannelGiftTypeSecDao channelGiftTypeSecDao;

	@Autowired
	ChannelMountSecDao channelMountSecDao;

	@Autowired
	HotChannelMountDao hotChannelMountDao;

	@Autowired
	PlatformConfigPriDao platformConfigPriDao;

	@Autowired
	PlatformConfigSecDao platformConfigSecDao;

	@Autowired
	JedisUtils jedisUtils;

	@Autowired
	ReactNativeConfigPriDao reactNativeConfigPriDao;

	@Autowired
	ReactNativeConfigSecDao reactNativeConfigSecDao;

	@Autowired
	AccountVipConfigPriDao accountVipConfigPriDao;

	@Autowired
	AccountVipConfigSecDao accountVipConfigSecDao;

	@Autowired
	CommissionByGuardConfigPriDao commissionByGuardConfigPriDao;

	@Autowired
	CommissionByGuardConfigSecDao commissionByGuardConfigSecDao;

	@Autowired
	HotAccountVipConfigDao hotAccountVipConfigDao;

	@Autowired
	ChannelGuardConfigPriDao channelGuardConfigPriDao;

	@Autowired
	ChannelGuardConfigSecDao channelGuardConfigSecDao;

	@Autowired
	HotChannelGuardConfigDao hotChannelGuardConfigDao;

	@Autowired
	ChannelGiftTypePriDao channelGiftTypePriDao;

	@Autowired
	HotChannelGiftTypeDao hotChannelGiftTypeDao;

	@Autowired
	AppVersionControlPriDao appVersionControlPriDao;

	@Autowired
	AppVersionControlSecDao appVersionControlSecDao;

	@Autowired
	FansClubConfigPriDao fansClubConfigPriDao;

	@Autowired
	FansClubConfigSecDao fansClubConfigSecDao;

	@Autowired
	HotFansClubConfigDao hotFansClubConfigDao;

	@Autowired
	MountPresentPriDao mountPresentPriDao;

	@Autowired
	ClubTaskConfigPriDao clubTaskConfigPriDao;

	@Autowired
	MountPresentSecDao mountPresentSecDao;

	@Autowired
	ClubTaskConfigSecDao clubTaskConfigSecDao;

	@Autowired
	HotClubTaskConfigDao hotClubTaskConfigDao;

	@Autowired
	ChannelTypePriDao channelTypePriDao;

	@Autowired
	ChannelTypeSecDao channelTypeSecDao;

	@Autowired
	ReactNativeVersionConfigPriDao reactNativeVersionConfigPriDao;

	@Autowired
	ReactNativeVersionConfigSecDao reactNativeVersionConfigSecDao;

	@Autowired
	ProductPriDao productPriDao;

	@Autowired
	ProductSecDao productSecDao;

	@Autowired
	ChannelSoundPriDao channelSoundPriDao;

	@Autowired
	ChannelSoundSecDao channelSoundSecDao;
	
	@Autowired
	GameVipConfigSecDao gameVipConfigSecDao;
	
	@Autowired
	GameVipConfigPriDao gameVipConfigPriDao;

	@Override
	public ResultDataSet addGift(String superAccid, int id, long price, long anchorPrice, String name, boolean nullity,
			boolean isForGuard, boolean isForVip, String unit, int maxCount, String function, int condition,
			String path, boolean broadcast, int sortId, int node, String flashPath) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		if (channelGiftPriDao.findOne(id)!= null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("礼物id已存在");
			return rds;
		}
		ChannelGift channelGift = new ChannelGift();
		channelGift.setId(id);
		channelGift.setName(name);
		channelGift.setPrice(price);
		channelGift.setAnchorPrice(anchorPrice);
		channelGift.setForVip(isForVip);
		channelGift.setForGuard(isForGuard);
		channelGift.setNullity(nullity);
		channelGift.setUnit(unit);
		channelGift.setMaxCount(maxCount);
		channelGift.setVersion(StringUtils.createUUID());
		channelGift.setFunction(function);
		channelGift.setCondition(condition);
		channelGift.setPath(path);
		channelGift.setBroadcast(broadcast);
		channelGift.setSortId(sortId);
		channelGift.setNode(node);
		channelGift.setFlashPath(flashPath);
		channelGiftPriDao.save(channelGift);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet updateGift(String superAccid, int id, long price, long anchorPrice, String name,
			boolean nullity, boolean isForGuard, boolean isForVip, String unit, int maxCount, String function,
			int condition, String path, boolean broadcast, int sortId, int node, String flashPath) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ChannelGift channelGift = channelGiftPriDao.findOne(id);
		if (channelGift == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("礼物id不存在");
		}
		channelGift.setName(name);
		channelGift.setPrice(price);
		channelGift.setAnchorPrice(anchorPrice);
		channelGift.setForVip(isForVip);
		channelGift.setForGuard(isForGuard);
		channelGift.setNullity(nullity);
		channelGift.setUnit(unit);
		channelGift.setMaxCount(maxCount);
		channelGift.setVersion(StringUtils.createUUID());
		channelGift.setFunction(function);
		channelGift.setCondition(condition);
		channelGift.setPath(path);
		channelGift.setBroadcast(broadcast);
		channelGift.setSortId(sortId);
		channelGift.setNode(node);
		channelGift.setFlashPath(flashPath);
		channelGiftPriDao.save(channelGift);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet deleteGift(String superAccid, int id) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		if (!channelGiftPriDao.exists(id)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("礼物id不存在");
		}
		channelGiftPriDao.delete(id);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryGiftList(int pageSize, int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("asc"), "id");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<ChannelGift> data = channelGiftSecDao.findAll(pageable);
		rds.setData(MyPageUtils.getMyPage(data));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryGift(String name) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		List<ChannelGift> data = channelGiftSecDao.queryByName(name);
		rds.setData(data);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getGift(int id) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ChannelGift data = channelGiftSecDao.findOne(id);
		rds.setData(data);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	/**
	 * 清空礼物设置
	 */
	@Override
	public ResultDataSet refreashChannelGiftConfigCache() {
		hotChannelGiftDao.deleteAll();
		ResultDataSet rds = new ResultDataSet();
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet newGiftVersion() {
		ResultDataSet rds = new ResultDataSet();
		String value = StringUtils.createUUID();
		PlatformConfig platformConfig = platformConfigPriDao.findOne(Constant.HOT_GIFT_CONFIG_VERSION_KEY);
		if (platformConfig != null) {
			platformConfig.setcValue(value);
			platformConfigPriDao.save(platformConfig);
		} else {
			PlatformConfig newPlatformConfig = new PlatformConfig();
			newPlatformConfig.setcKey(Constant.HOT_GIFT_CONFIG_VERSION_KEY);
			newPlatformConfig.setcValue(value);
			newPlatformConfig.setcDesc("礼物配置版本");
			platformConfigPriDao.save(newPlatformConfig);
		}
		jedisUtils.set(Constant.HOT_GIFT_CONFIG_VERSION_KEY, value);
		hotChannelGiftTypeDao.deleteAll();
		Iterable<ChannelGiftType> channelGiftTypes = channelGiftTypePriDao.findAll();
		List<HotChannelGiftType> types = new ArrayList<HotChannelGiftType>();
		for (ChannelGiftType type : channelGiftTypes) {
			HotChannelGiftType ht = new HotChannelGiftType();
			ht.setId(type.getId());
			ht.setNode(type.getName());
			types.add(ht);
		}
		hotChannelGiftTypeDao.save(types);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet createUUID() {
		ResultDataSet rds = new ResultDataSet();
		rds.setData(StringUtils.createUUID());
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	/**
	 * 礼物设置
	 */
	@Override
	public ResultDataSet getChannelGiftConfig() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ResponseData response = new ResponseData();
		List<HotChannelGift> hotChannelGift = (List<HotChannelGift>) hotChannelGiftDao.findAll();
		if (hotChannelGift == null || hotChannelGift.isEmpty()) {
			Sort sort = new Sort(Direction.fromString("asc"), "sortId");
			List<ChannelGift> data = channelGiftSecDao.findByNullity(false, sort);
			for (ChannelGift gift : data) {
				HotChannelGift hotGift = new HotChannelGift();
				hotGift.setMainId(gift.getId());
				hotGift.setName(gift.getName());
				hotGift.setForGuard(gift.isForGuard());
				hotGift.setForVip(gift.isForVip());
				hotGift.setPrice(gift.getPrice());
				hotGift.setAnchorPrice(gift.getAnchorPrice());
				hotGift.setUnit(gift.getUnit());
				hotGift.setMaxCount(gift.getMaxCount());
				hotGift.setVersion(gift.getVersion());
				hotGift.setPath(gift.getPath());
				hotGift.setFunction(gift.getFunction());
				hotGift.setCondition(gift.getCondition());
				hotGift.setSortId(gift.getSortId());
				hotGift.setNode(gift.getNode());
				hotGift.setFlashPath(gift.getFlashPath());
				hotChannelGiftDao.save(hotGift);
			}
			response.put("gift", data);
		} else {
			response.put("gift", hotChannelGift);
		}
		if (jedisUtils.exist(Constant.HOT_GIFT_CONFIG_VERSION_KEY)) {
			String value = jedisUtils.get(Constant.HOT_GIFT_CONFIG_VERSION_KEY);
			response.put(Constant.HOT_GIFT_CONFIG_VERSION_KEY, value);
		} else {
			PlatformConfig platformConfig = platformConfigPriDao.findOne(Constant.HOT_GIFT_CONFIG_VERSION_KEY);
			String version = "no_version";
			if (platformConfig != null) {
				version = platformConfig.getcValue();
			}
			jedisUtils.set(Constant.HOT_GIFT_CONFIG_VERSION_KEY, version);
			response.put(Constant.HOT_GIFT_CONFIG_VERSION_KEY, version);
		}
		Iterable<HotChannelGiftType> types = hotChannelGiftTypeDao.findAll();
		response.put("node", types);
		rds.setData(response);
		// set code and message
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("Server Suc");
		return rds;
	}

	@Override
	public ResultDataSet addMount(String superAccid, int id, long price, String name, boolean nullity,
			boolean isForGuard, boolean isForVip, String function, String path, int validity, boolean forFansClubs,
			int level) {
		ResultDataSet rds = new ResultDataSet();
		if (channelMountPriDao.findOne(id) != null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("坐骑id已存在");
			return rds;
		}
		ChannelMount channelMount = new ChannelMount();
		channelMount.setId(id);
		channelMount.setName(name);
		channelMount.setPrice(price);
		channelMount.setForVip(isForVip);
		channelMount.setForGuard(isForGuard);
		channelMount.setNullity(nullity);
		channelMount.setVersion(StringUtils.createUUID());
		channelMount.setFunction(function);
		channelMount.setPath(path);
		channelMount.setValidity(validity);
		channelMount.setForFansClubs(forFansClubs);
		channelMount.setLevel(level);
		channelMountPriDao.save(channelMount);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet updateMount(String superAccid, int id, long price, String name, boolean nullity,
			boolean isForGuard, boolean isForVip, String function, String path, int validity, boolean forFansClubs,
			int level) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ChannelMount channelMount = channelMountPriDao.findOne(id);
		if (channelMount == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("坐骑id不存在");
		}
		channelMount.setId(id);
		channelMount.setName(name);
		channelMount.setPrice(price);
		channelMount.setForVip(isForVip);
		channelMount.setForGuard(isForGuard);
		channelMount.setNullity(nullity);
		channelMount.setVersion(StringUtils.createUUID());
		channelMount.setFunction(function);
		channelMount.setPath(path);
		channelMount.setValidity(validity);
		channelMount.setForFansClubs(forFansClubs);
		channelMount.setLevel(level);
		channelMountPriDao.save(channelMount);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet deleteMount(String superAccid, int id) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		if (!channelMountPriDao.exists(id)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("礼物id不存在");
		}
		channelMountPriDao.delete(id);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryMountList(int pageSize, int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("asc"), "id");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<ChannelMount> data = channelMountSecDao.findAll(pageable);
		rds.setData(MyPageUtils.getMyPage(data));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getMount(int id) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ChannelMount data = channelMountSecDao.findOne(id);
		rds.setData(data);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet refreashChannelMountConfigCache() {
		hotChannelMountDao.deleteAll();
		ResultDataSet rds = new ResultDataSet();
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getChannelMountConfig() throws IOException {
		// Define Response
		ResultDataSet rds = new ResultDataSet();
		ResponseData response = new ResponseData();
		List<HotChannelMount> hotChannelMount = (List<HotChannelMount>) hotChannelMountDao.findAll();
		if (hotChannelMount == null || hotChannelMount.isEmpty()) {
			List<ChannelMount> data = channelMountSecDao.findByNullity(false);
			for (ChannelMount mount : data) {
				HotChannelMount hotMount = new HotChannelMount();
				hotMount.setId(mount.getId());
				hotMount.setName(mount.getName());
				hotMount.setForGuard(mount.isForGuard());
				hotMount.setForVip(mount.isForVip());
				hotMount.setPrice(mount.getPrice());
				hotMount.setVersion(mount.getVersion());
				hotMount.setFunction(mount.getFunction());
				hotMount.setPath(mount.getPath());
				hotMount.setForFansClubs(mount.isForFansClubs());
				hotMount.setValidity((mount.getValidity()));
				hotChannelMountDao.save(hotMount);
			}
			response.put("mount", data);
		} else {
			response.put("mount", hotChannelMount);
		}
		if (jedisUtils.exist(Constant.HOT_MOUNT_CONFIG_VERSION_KEY)) {
			String value = jedisUtils.get(Constant.HOT_MOUNT_CONFIG_VERSION_KEY);
			response.put(Constant.HOT_MOUNT_CONFIG_VERSION_KEY, value);
		} else {
			PlatformConfig platformConfig = platformConfigPriDao.findOne(Constant.HOT_MOUNT_CONFIG_VERSION_KEY);
			String version = "no_version";
			if (platformConfig != null) {
				version = platformConfig.getcValue();
			}
			jedisUtils.set(Constant.HOT_MOUNT_CONFIG_VERSION_KEY, version);
			response.put(Constant.HOT_MOUNT_CONFIG_VERSION_KEY, version);
		}
		rds.setData(response);
		// set code and message
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("Server Suc");
		return rds;
	}

	@Override
	public ResultDataSet newMountVersion() {
		ResultDataSet rds = new ResultDataSet();
		String value = StringUtils.createUUID();
		PlatformConfig platformConfig = platformConfigPriDao.findOne(Constant.HOT_MOUNT_CONFIG_VERSION_KEY);
		if (platformConfig != null) {
			platformConfig.setcValue(value);
			platformConfigPriDao.save(platformConfig);
		} else {
			PlatformConfig newPlatformConfig = new PlatformConfig();
			newPlatformConfig.setcKey(Constant.HOT_MOUNT_CONFIG_VERSION_KEY);
			newPlatformConfig.setcValue(value);
			newPlatformConfig.setcDesc("坐骑配置版本");
			platformConfigPriDao.save(newPlatformConfig);
		}
		jedisUtils.set(Constant.HOT_MOUNT_CONFIG_VERSION_KEY, value);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet setMobileMountMin(int mobileMountMin) {
		ResultDataSet rds = new ResultDataSet();
		jedisUtils.set("mobileMountMin", Integer.toString(mobileMountMin));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getMobileMountMin() {
		ResultDataSet rds = new ResultDataSet();
		rds.setData(jedisUtils.get("mobileMountMin"));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getAnimationConfig() {
		ResultDataSet rds = new ResultDataSet();
		ResponseData response = new ResponseData();
		if (!jedisUtils.exist(Constant.HOT_ANIMATION_ZIP_CONFIG_KEY)
				|| !jedisUtils.exist(Constant.HOT_ANIMATION_RES_CONFIG_KEY)) {
			PlatformConfig animZipConfig = platformConfigPriDao.findOne(Constant.HOT_ANIMATION_ZIP_CONFIG_KEY);
			PlatformConfig animResConfig = platformConfigPriDao.findOne(Constant.HOT_ANIMATION_RES_CONFIG_KEY);
			if (animZipConfig == null || animResConfig == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("no_ch_animation");
				return rds;
			}
			jedisUtils.set(Constant.HOT_ANIMATION_ZIP_CONFIG_KEY, animZipConfig.getcValue());
			response.put(Constant.HOT_ANIMATION_ZIP_CONFIG_KEY, animZipConfig.getcValue());
			jedisUtils.set(Constant.HOT_ANIMATION_RES_CONFIG_KEY, animResConfig.getcValue());
			response.put(Constant.HOT_ANIMATION_RES_CONFIG_KEY, animResConfig.getcValue());
		} else {
			String animZipConfig = jedisUtils.get(Constant.HOT_ANIMATION_ZIP_CONFIG_KEY);
			response.put(Constant.HOT_ANIMATION_ZIP_CONFIG_KEY, animZipConfig);
			String animResConfig = jedisUtils.get(Constant.HOT_ANIMATION_RES_CONFIG_KEY);
			response.put(Constant.HOT_ANIMATION_RES_CONFIG_KEY, animResConfig);
		}
		if (!jedisUtils.exist(Constant.HOT_ANIMATION_CONFIG_VERSION_KEY)) {
			PlatformConfig versionConfig = platformConfigPriDao.findOne(Constant.HOT_ANIMATION_CONFIG_VERSION_KEY);
			if (versionConfig == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("no_ch_html_version");
				return rds;
			}
			jedisUtils.set(Constant.HOT_ANIMATION_CONFIG_VERSION_KEY, versionConfig.getcValue());
			response.put(Constant.HOT_ANIMATION_CONFIG_VERSION_KEY, versionConfig.getcValue());
		} else {
			String versionConfig = jedisUtils.get(Constant.HOT_ANIMATION_CONFIG_VERSION_KEY);
			response.put(Constant.HOT_ANIMATION_CONFIG_VERSION_KEY, versionConfig);
		}
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet refreashAnimationConfigCache() {
		ResultDataSet rds = new ResultDataSet();
		if (jedisUtils.exist(Constant.HOT_ANIMATION_CONFIG_VERSION_KEY)) {
			jedisUtils.del(Constant.HOT_ANIMATION_CONFIG_VERSION_KEY);
		}
		if (jedisUtils.exist(Constant.HOT_ANIMATION_ZIP_CONFIG_KEY)) {
			jedisUtils.del(Constant.HOT_ANIMATION_ZIP_CONFIG_KEY);
		}
		if (jedisUtils.exist(Constant.HOT_ANIMATION_RES_CONFIG_KEY)) {
			jedisUtils.del(Constant.HOT_ANIMATION_RES_CONFIG_KEY);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet saveAnimationConfig(String zipUrl, String resUrl) {
		ResultDataSet rds = new ResultDataSet();
		PlatformConfig zipConfig = platformConfigPriDao.findOne(Constant.HOT_ANIMATION_ZIP_CONFIG_KEY);
		if (zipConfig != null) {
			zipConfig.setcValue(zipUrl);
			platformConfigPriDao.save(zipConfig);
		} else {
			PlatformConfig newZipConfig = new PlatformConfig();
			newZipConfig.setcKey(Constant.HOT_ANIMATION_ZIP_CONFIG_KEY);
			newZipConfig.setcDesc("动画zip包url");
			newZipConfig.setcValue(zipUrl);
			platformConfigPriDao.save(newZipConfig);
		}
		PlatformConfig resConfig = platformConfigPriDao.findOne(Constant.HOT_ANIMATION_RES_CONFIG_KEY);
		if (resConfig != null) {
			resConfig.setcValue(resUrl);
			platformConfigPriDao.save(resConfig);
		} else {
			PlatformConfig newResConfig = new PlatformConfig();
			newResConfig.setcKey(Constant.HOT_ANIMATION_RES_CONFIG_KEY);
			newResConfig.setcDesc("动画Res包url");
			newResConfig.setcValue(resUrl);
			platformConfigPriDao.save(newResConfig);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet newAnimationVersion() {
		ResultDataSet rds = new ResultDataSet();
		PlatformConfig animationConfig = platformConfigPriDao.findOne(Constant.HOT_ANIMATION_CONFIG_VERSION_KEY);
		if (animationConfig != null) {
			animationConfig.setcValue(StringUtils.createUUID());
			platformConfigPriDao.save(animationConfig);
		} else {
			PlatformConfig newAnimationConfig = new PlatformConfig();
			newAnimationConfig.setcKey(Constant.HOT_ANIMATION_CONFIG_VERSION_KEY);
			newAnimationConfig.setcValue(StringUtils.createUUID());
			newAnimationConfig.setcDesc("频道动画配置版本");
			platformConfigPriDao.save(newAnimationConfig);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet addRn(String superAccid, String id, String node, String name, String icon, String module,
			String rnZip, String type, int section, String version) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ReactNativeConfig config = reactNativeConfigPriDao.findByIdAndVersion(id, version);
		if (config != null) {
			rds.setMsg("该版本内该模块已存在");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		ReactNativeConfig reactNativeConfig = new ReactNativeConfig();
		reactNativeConfig.setId(id);
		reactNativeConfig.setNode(node);
		reactNativeConfig.setName(name);
		reactNativeConfig.setIcon(icon);
		reactNativeConfig.setModule(module);
		reactNativeConfig.setRnZip(rnZip);
		reactNativeConfig.setType(type);
		reactNativeConfig.setSection(section);
		reactNativeConfig.setVersion(version);
		reactNativeConfigPriDao.save(reactNativeConfig);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getRn(String id, String version) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ReactNativeConfig reactNativeConfig = reactNativeConfigPriDao.findByIdAndVersion(id, version);
		rds.setData(reactNativeConfig);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet updateRn(String superAccid, String id, String node, String name, String icon, String module,
			String rnZip, String type, int section, String version) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ReactNativeConfig reactNativeConfig = reactNativeConfigPriDao.findByIdAndVersion(id, version);
		reactNativeConfig.setNode(node);
		reactNativeConfig.setName(name);
		reactNativeConfig.setIcon(icon);
		reactNativeConfig.setModule(module);
		reactNativeConfig.setRnZip(rnZip);
		reactNativeConfig.setType(type);
		reactNativeConfig.setSection(section);
		reactNativeConfigPriDao.save(reactNativeConfig);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet deleteRn(String superAccid, String id, String version) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ReactNativeConfig reactNativeConfig = reactNativeConfigPriDao.findByIdAndVersion(id, version);
		reactNativeConfigPriDao.delete(reactNativeConfig);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryRn(String params) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		List<ReactNativeConfig> reactNativeConfig = reactNativeConfigSecDao.findByParams(params);
		rds.setData(reactNativeConfig);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryRnList(int pageSize, int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("asc"), "id");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<ReactNativeConfig> reactNativeConfig = reactNativeConfigSecDao.findAll(pageable);
		rds.setData(MyPageUtils.getMyPage(reactNativeConfig));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	@CacheEvict(value = "RNConfig", allEntries = true)
	public ResultDataSet clearRnChache() {
		ResultDataSet rds = new ResultDataSet();

		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet addRnVersion(String superAccid, String version, String rnZipUrl, String rnDescribe)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ReactNativeVersionConfig config = reactNativeVersionConfigPriDao.findOne(version);
		if (config == null) {
			ReactNativeVersionConfig newConfig = new ReactNativeVersionConfig();
			newConfig.setVersion(version);
			newConfig.setRnZipUrl(rnZipUrl);
			newConfig.setRnDescribe(rnDescribe);
			reactNativeVersionConfigPriDao.save(newConfig);
		} else {
			rds.setMsg("版本已存在，不能添加，只能换版本");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet deleteRnVersion(String superAccid, String version) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ReactNativeVersionConfig config = reactNativeVersionConfigPriDao.findOne(version);
		if (config == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不存在");
			return rds;
		}
		reactNativeVersionConfigPriDao.delete(config);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryRnVersion(String superAccid, int pageNum, int pageSize) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "version");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<ReactNativeVersionConfig> config = reactNativeVersionConfigSecDao.findAll(pageable);
		rds.setData(MyPageUtils.getMyPage(config));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet addAccountVip(String superAccid, int id, int level, int month, long price) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		if (accountVipConfigPriDao.findOne(id) != null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("VIP等级已存在");
			return rds;
		}
		AccountVipConfig accountVipConfig = new AccountVipConfig();
		accountVipConfig.setId(id);
		accountVipConfig.setLevel(level);
		accountVipConfig.setMonth(month);
		accountVipConfig.setPrice(price);
		accountVipConfigPriDao.save(accountVipConfig);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet addGuardCommissionConfig(String superAccid, int id, int guardLevel, long moneyOneMonth)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		if (commissionByGuardConfigPriDao.findOne(id) != null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该骑士/佣金配置等级已存在");
			return rds;
		}
		CommissionByGuardConfig commissionByGuardConfig = new CommissionByGuardConfig();
		commissionByGuardConfig.setId(id);
		commissionByGuardConfig.setGuardLevel(guardLevel);
		commissionByGuardConfig.setMoneyOneMonth(moneyOneMonth);
		commissionByGuardConfigPriDao.save(commissionByGuardConfig);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet updateAccountVip(String superAccid, int id, int level, int month, long price)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		AccountVipConfig accountVipConfig = accountVipConfigPriDao.findOne(id);
		if (accountVipConfig == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("VIP等级不存在");
		}
		accountVipConfig.setLevel(level);
		accountVipConfig.setMonth(month);
		accountVipConfig.setPrice(price);
		accountVipConfigPriDao.save(accountVipConfig);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet deleteAccountVip(String superAccid, int id) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		if (!accountVipConfigPriDao.exists(id)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("VIP等级不存在");
		}
		accountVipConfigPriDao.delete(id);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryAccountVipList(int pageSize, int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("asc"), "id");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<AccountVipConfig> data = accountVipConfigSecDao.findAll(pageable);
		rds.setData(MyPageUtils.getMyPage(data));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getAccountVip(int id) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		AccountVipConfig data = accountVipConfigSecDao.findOne(id);
		rds.setData(data);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet refreashAccountVipCache() {
		hotAccountVipConfigDao.deleteAll();
		ResultDataSet rds = new ResultDataSet();
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getAccountVipConfig() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		List<HotAccountVipConfig> hotAccountVipConfig = (List<HotAccountVipConfig>) hotAccountVipConfigDao.findAll();
		if (hotAccountVipConfig == null || hotAccountVipConfig.isEmpty()) {
			List<AccountVipConfig> data = (List<AccountVipConfig>) accountVipConfigSecDao.findAll();
			for (AccountVipConfig config : data) {
				HotAccountVipConfig hotConfig = new HotAccountVipConfig();
				hotConfig.setId(config.getId());
				hotConfig.setLevel(config.getLevel());
				hotConfig.setMonth(config.getMonth());
				hotConfig.setPrice(config.getPrice());
				hotAccountVipConfigDao.save(hotConfig);
			}
			rds.setData(data);
		} else {
			rds.setData(hotAccountVipConfig);
		}
		// set code and message
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("Server Suc");
		return rds;
	}

	@Override
	public ResultDataSet updateGuardCommissionConfig(String superAccid, int id, int guardLevel, long moneyOneMonth)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		CommissionByGuardConfig commissionByGuardConfig = commissionByGuardConfigPriDao.findOne(id);
		if (commissionByGuardConfig == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该骑士等级/佣金配置不存在");
		}
		commissionByGuardConfig.setGuardLevel(guardLevel);
		;
		commissionByGuardConfig.setMoneyOneMonth(moneyOneMonth);
		commissionByGuardConfigPriDao.save(commissionByGuardConfig);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet deleteGuardCommissionConfig(String superAccid, int id) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		if (!commissionByGuardConfigPriDao.exists(id)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("骑士/佣金配置不存在");
		}
		commissionByGuardConfigPriDao.delete(id);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryGuardCommissionConfigList(int pageSize, int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("asc"), "id");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<CommissionByGuardConfig> data = commissionByGuardConfigSecDao.findAll(pageable);
		rds.setData(MyPageUtils.getMyPage(data));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getGuardCommissionConfig(int id) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		CommissionByGuardConfig data = commissionByGuardConfigSecDao.findOne(id);
		rds.setData(data);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getGuardCommissionConfigList() throws IOException {
		ResultDataSet rds = new ResultDataSet();

		List<CommissionByGuardConfig> data = (List<CommissionByGuardConfig>) commissionByGuardConfigSecDao.findAll();

		rds.setData(data);

		// set code and message
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("Server Suc");
		return rds;
	}

	@Override
	public ResultDataSet addChannelGuard(String superAccid, int id, int level, int month, long price) {
		ResultDataSet rds = new ResultDataSet();
		if (channelGuardConfigPriDao.findOne(id) != null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("VIP等级已存在");
			return rds;
		}
		ChannelGuardConfig channelGuardConfig = new ChannelGuardConfig();
		channelGuardConfig.setId(id);
		channelGuardConfig.setLevel(level);
		channelGuardConfig.setMonth(month);
		channelGuardConfig.setPrice(price);
		channelGuardConfigPriDao.save(channelGuardConfig);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet updateChannelGuard(String superAccid, int id, int level, int month, long price)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ChannelGuardConfig channelGuardConfig = channelGuardConfigPriDao.findOne(id);
		if (channelGuardConfig == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("VIP等级不存在");
		}
		channelGuardConfig.setLevel(level);
		channelGuardConfig.setMonth(month);
		channelGuardConfig.setPrice(price);
		channelGuardConfigPriDao.save(channelGuardConfig);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet deleteChannelGuard(String superAccid, int id) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		if (!channelGuardConfigPriDao.exists(id)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("VIP等级不存在");
		}
		channelGuardConfigPriDao.delete(id);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryChannelGuardList(int pageSize, int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("asc"), "id");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<ChannelGuardConfig> data = channelGuardConfigSecDao.findAll(pageable);
		rds.setData(MyPageUtils.getMyPage(data));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getChannelGuard(int id) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ChannelGuardConfig data = channelGuardConfigSecDao.findOne(id);
		rds.setData(data);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet refreashChannelGuardCache() {
		hotChannelGuardConfigDao.deleteAll();
		ResultDataSet rds = new ResultDataSet();
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getChannelGuardConfig() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		List<HotChannelGuardConfig> hotChannelGuardConfig = (List<HotChannelGuardConfig>) hotChannelGuardConfigDao
				.findAll();
		if (hotChannelGuardConfig == null || hotChannelGuardConfig.isEmpty()) {
			List<ChannelGuardConfig> data = (List<ChannelGuardConfig>) channelGuardConfigSecDao.findAll();
			for (ChannelGuardConfig config : data) {
				HotChannelGuardConfig hotConfig = new HotChannelGuardConfig();
				hotConfig.setId(config.getId());
				hotConfig.setLevel(config.getLevel());
				hotConfig.setMonth(config.getMonth());
				hotConfig.setPrice(config.getPrice());
				hotChannelGuardConfigDao.save(hotConfig);
			}
			rds.setData(data);
		} else {
			rds.setData(hotChannelGuardConfig);
		}
		// set code and message
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("Server Suc");
		return rds;
	}

	@Override
	public ResultDataSet addGiftType(int id, String name) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ChannelGiftType channelGiftType = new ChannelGiftType();
		channelGiftType.setId(id);
		channelGiftType.setName(name);
		channelGiftTypePriDao.save(channelGiftType);
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("Server Suc");
		return rds;
	}

	@Override
	public ResultDataSet updateGiftType(int id, String name) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ChannelGiftType channelGiftType = channelGiftTypePriDao.findOne(id);
		if (channelGiftType == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该礼物类型");
			return rds;
		}
		channelGiftType.setName(name);
		channelGiftTypePriDao.save(channelGiftType);
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("Server Suc");
		return rds;
	}

	@Override
	public ResultDataSet deleteGiftType(int id) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ChannelGiftType channelGiftType = channelGiftTypePriDao.findOne(id);
		if (channelGiftType == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该礼物类型");
			return rds;
		}
		channelGiftTypePriDao.delete(channelGiftType);
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("Server Suc");
		return rds;
	}

	@Override
	public ResultDataSet getGiftTypes() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Iterable<ChannelGiftType> channelGiftTypes = channelGiftTypeSecDao.findAll();
		rds.setData(channelGiftTypes);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet addAppVersion(String superAccId, int device, int main, int sub, int func, String updateUrl,
			String describe, int appleRelease, int applePayType, String rnVersion) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		AppVersionControl appVersion = new AppVersionControl();
		appVersion.setId("" + device + "_" + main + "_" + sub + "_" + func);
		appVersion.setMainVserion(main);
		appVersion.setSubVersion(sub);
		appVersion.setFuncVersion(func);
		appVersion.setStatus(AppVersionStatus.CURRENT.getValue());
		appVersion.setVersionDescribe(describe);
		appVersion.setDevice(device);
		appVersion.setUpdateUrl(updateUrl);
		String release = AppleReleaseType.values()[appleRelease].getCode();
		appVersion.setAppleRelease(release);
		String pay = ApplePayType.values()[applePayType].getCode();
		appVersion.setApplePayType(pay);
		appVersion.setRnVersion(rnVersion);
		appVersion.setCollectTime(DateUtils.getNowTime());
		appVersionControlPriDao.save(appVersion);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet deleteAppVersion(String superAccId, String id) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		appVersionControlPriDao.delete(id);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet updateAppVersionStatus(String superAccId, String id, int status) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		AppVersionControl version = appVersionControlPriDao.findOne(id);
		if (version == null) {
			rds.setMsg("没有找到该版本");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		version.setStatus(status);
		appVersionControlPriDao.save(version);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet listAppVersion(String superAccId, int pageNum, int pageSize) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "collectTime");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<AppVersionControl> versions = appVersionControlSecDao.findAll(pageable);
		rds.setData(MyPageUtils.getMyPage(versions));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet setWindowsUpdateVersion(String loginVersion, String jsonVersion, String jsonPath,
			String bossZipUrl) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		PlatformConfig loginUpdateConfig = platformConfigPriDao.findOne(Constant.WINDOWS_UPDATE_LOGIN_VERSION);
		if (loginUpdateConfig == null) {
			PlatformConfig newLoginUpdateConfig = new PlatformConfig();
			newLoginUpdateConfig.setcKey(Constant.WINDOWS_UPDATE_LOGIN_VERSION);
			newLoginUpdateConfig.setcValue(loginVersion);
			newLoginUpdateConfig.setcDesc("windows更新登陆程序版本");
			platformConfigPriDao.save(newLoginUpdateConfig);
		} else {
			loginUpdateConfig.setcKey(Constant.WINDOWS_UPDATE_LOGIN_VERSION);
			loginUpdateConfig.setcValue(loginVersion);
			loginUpdateConfig.setcDesc("windows更新登陆程序版本");
			platformConfigPriDao.save(loginUpdateConfig);
		}

		PlatformConfig jsonUpdateConfig = platformConfigPriDao.findOne(Constant.WINDOWS_UPDATE_JSON_VERSION);
		if (jsonUpdateConfig == null) {
			PlatformConfig newJsonUpdateConfig = new PlatformConfig();
			newJsonUpdateConfig.setcKey(Constant.WINDOWS_UPDATE_JSON_VERSION);
			newJsonUpdateConfig.setcValue(jsonVersion);
			newJsonUpdateConfig.setcDesc("windows更新用的json文件md5码");
			platformConfigPriDao.save(newJsonUpdateConfig);
		} else {
			jsonUpdateConfig.setcKey(Constant.WINDOWS_UPDATE_JSON_VERSION);
			jsonUpdateConfig.setcValue(jsonVersion);
			jsonUpdateConfig.setcDesc("windows更新用的json文件md5码");
			platformConfigPriDao.save(jsonUpdateConfig);
		}

		PlatformConfig jsonPathConfig = platformConfigPriDao.findOne(Constant.WINDOWS_UPDATE_JSON_PATH);
		if (jsonPathConfig == null) {
			PlatformConfig newJsonPathConfig = new PlatformConfig();
			newJsonPathConfig.setcKey(Constant.WINDOWS_UPDATE_JSON_PATH);
			newJsonPathConfig.setcValue(jsonPath);
			newJsonPathConfig.setcDesc("windows更新用的json文件下载路径");
			platformConfigPriDao.save(newJsonPathConfig);
		} else {
			jsonPathConfig.setcKey(Constant.WINDOWS_UPDATE_JSON_PATH);
			jsonPathConfig.setcValue(jsonPath);
			jsonPathConfig.setcDesc("windows更新用的json文件下载路径");
			platformConfigPriDao.save(jsonPathConfig);
		}

		PlatformConfig bossZipUrlConfig = platformConfigPriDao.findOne(Constant.WINDOWS_BOSS_UPDATE_ZIP_URL);
		if (bossZipUrlConfig == null) {
			PlatformConfig newBossZipUrlConfig = new PlatformConfig();
			newBossZipUrlConfig.setcKey(Constant.WINDOWS_BOSS_UPDATE_ZIP_URL);
			newBossZipUrlConfig.setcValue(bossZipUrl);
			newBossZipUrlConfig.setcDesc("windows更新用的BossZip文件下载路径");
			platformConfigPriDao.save(newBossZipUrlConfig);
		} else {
			bossZipUrlConfig.setcKey(Constant.WINDOWS_BOSS_UPDATE_ZIP_URL);
			bossZipUrlConfig.setcValue(bossZipUrl);
			bossZipUrlConfig.setcDesc("windows更新用的BossZip文件下载路径");
			platformConfigPriDao.save(bossZipUrlConfig);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet setWindowsUpdateLoginPath(String loginPath) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		PlatformConfig loginPathConfig = platformConfigPriDao.findOne(Constant.WINDOWS_UPDATE_LOGIN_PATH);
		if (loginPathConfig == null) {
			PlatformConfig newLoginPathConfig = new PlatformConfig();
			newLoginPathConfig.setcKey(Constant.WINDOWS_UPDATE_LOGIN_PATH);
			newLoginPathConfig.setcValue(loginPath);
			newLoginPathConfig.setcDesc("windowsLogin更新登陆程序url");
			platformConfigPriDao.save(newLoginPathConfig);
		} else {
			loginPathConfig.setcKey(Constant.WINDOWS_UPDATE_LOGIN_PATH);
			loginPathConfig.setcValue(loginPath);
			loginPathConfig.setcDesc("windows更新登陆程序url");
			platformConfigPriDao.save(loginPathConfig);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet syncWindowsUpdateVersion() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		PlatformConfig loginConfig = platformConfigPriDao.findOne(Constant.WINDOWS_UPDATE_LOGIN_VERSION);
		if (loginConfig == null) {
			rds.setMsg("数据库中没有找到登陆程序版本");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		jedisUtils.set(Constant.WINDOWS_UPDATE_LOGIN_VERSION, loginConfig.getcValue());

		PlatformConfig loginPathConfig = platformConfigPriDao.findOne(Constant.WINDOWS_UPDATE_LOGIN_PATH);
		if (loginPathConfig == null) {
			rds.setMsg("数据库中没有找到登陆程序版本");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		jedisUtils.set(Constant.WINDOWS_UPDATE_LOGIN_PATH, loginPathConfig.getcValue());

		PlatformConfig jsonConfig = platformConfigPriDao.findOne(Constant.WINDOWS_UPDATE_JSON_VERSION);
		if (jsonConfig == null) {
			rds.setMsg("数据库中没有找到Json文件md5");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		jedisUtils.set(Constant.WINDOWS_UPDATE_JSON_VERSION, jsonConfig.getcValue());

		PlatformConfig jsonPath = platformConfigPriDao.findOne(Constant.WINDOWS_UPDATE_JSON_PATH);
		if (jsonPath == null) {
			rds.setMsg("数据库中没有找到Json文件路径");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}

		PlatformConfig bossZipUrl = platformConfigPriDao.findOne(Constant.WINDOWS_BOSS_UPDATE_ZIP_URL);
		if (bossZipUrl == null) {
			rds.setMsg("数据库中没有找到BossZip文件路径");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		jedisUtils.set(Constant.WINDOWS_BOSS_UPDATE_ZIP_URL, bossZipUrl.getcValue());
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet setWindowsWebPageVersion(String homePageVersion, String livePageVersion) {
		ResultDataSet rds = new ResultDataSet();
		PlatformConfig homePageVersionConfig = platformConfigPriDao.findOne(Constant.WINDOWS_BOSS_HOME_PAGE_VERSION);
		if (homePageVersionConfig == null) {
			PlatformConfig newhomePageVersionConfig = new PlatformConfig();
			newhomePageVersionConfig.setcKey(Constant.WINDOWS_BOSS_HOME_PAGE_VERSION);
			newhomePageVersionConfig.setcValue(homePageVersion);
			newhomePageVersionConfig.setcDesc("windows客户端首页版本");
			platformConfigPriDao.save(newhomePageVersionConfig);
		} else {
			homePageVersionConfig.setcKey(Constant.WINDOWS_BOSS_HOME_PAGE_VERSION);
			homePageVersionConfig.setcValue(homePageVersion);
			homePageVersionConfig.setcDesc("windows客户端首页版本");
			platformConfigPriDao.save(homePageVersionConfig);
		}
		PlatformConfig livePageVersionConfig = platformConfigPriDao.findOne(Constant.WINDOWS_BOSS_LIVE_PAGE_VERSION);
		if (livePageVersionConfig == null) {
			PlatformConfig newlivePageVersionConfig = new PlatformConfig();
			newlivePageVersionConfig.setcKey(Constant.WINDOWS_BOSS_LIVE_PAGE_VERSION);
			newlivePageVersionConfig.setcValue(livePageVersion);
			newlivePageVersionConfig.setcDesc("windows客户端内嵌直播间版本");
			platformConfigPriDao.save(newlivePageVersionConfig);
		} else {
			livePageVersionConfig.setcKey(Constant.WINDOWS_BOSS_LIVE_PAGE_VERSION);
			livePageVersionConfig.setcValue(livePageVersion);
			livePageVersionConfig.setcDesc("windows客户端内嵌直播间版本");
			platformConfigPriDao.save(livePageVersionConfig);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getChannelPushConfig() {
		ResultDataSet rds = new ResultDataSet();
		ResponseData response = new ResponseData();
		PlatformConfig pushConfig = platformConfigPriDao.findOne(Constant.PUSH_VIDEO_QUALITY);
		response.put("pushQuality", pushConfig.getcValue());
		PlatformConfig pushConfig2 = platformConfigPriDao.findOne(Constant.PUSH_VIDEO_MIX_MODE);
		response.put("pushMode", pushConfig2.getcValue());
		PlatformConfig pushConfig3 = platformConfigPriDao.findOne(Constant.PUSH_VIDEO_LOGO_MARK);
		response.put("pushMark", pushConfig3.getcValue());
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet setChannelPushConfig(int quality, int mode, int logoMark) {
		ResultDataSet rds = new ResultDataSet();
		PlatformConfig homePageVersionConfig = platformConfigPriDao.findOne(Constant.PUSH_VIDEO_QUALITY);
		if (!ChannelVideoConfig.belongQuality(quality) && ChannelVideoConfig.belongMode(mode)
				&& ChannelVideoConfig.belongLogo(logoMark)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (homePageVersionConfig == null) {
			PlatformConfig newhomePageVersionConfig = new PlatformConfig();
			newhomePageVersionConfig.setcKey(Constant.PUSH_VIDEO_QUALITY);
			newhomePageVersionConfig.setcValue(Integer.toString(quality));
			newhomePageVersionConfig.setcDesc("视频质量");
			platformConfigPriDao.save(newhomePageVersionConfig);
		} else {
			homePageVersionConfig.setcKey(Constant.PUSH_VIDEO_QUALITY);
			homePageVersionConfig.setcValue(Integer.toString(quality));
			homePageVersionConfig.setcDesc("视频质量");
			platformConfigPriDao.save(homePageVersionConfig);
		}

		PlatformConfig mixPageVersionConfig = platformConfigPriDao.findOne(Constant.PUSH_VIDEO_MIX_MODE);
		if (mixPageVersionConfig == null) {

			PlatformConfig newMixPageVersionConfig = new PlatformConfig();
			newMixPageVersionConfig.setcKey(Constant.PUSH_VIDEO_MIX_MODE);
			newMixPageVersionConfig.setcValue(Integer.toString(mode));
			newMixPageVersionConfig.setcDesc("视频混频模式");
			platformConfigPriDao.save(newMixPageVersionConfig);
		} else {
			mixPageVersionConfig.setcKey(Constant.PUSH_VIDEO_MIX_MODE);
			mixPageVersionConfig.setcValue(Integer.toString(mode));
			mixPageVersionConfig.setcDesc("视频混频模式");
			platformConfigPriDao.save(mixPageVersionConfig);
		}

		PlatformConfig markPageVersionConfig = platformConfigPriDao.findOne(Constant.PUSH_VIDEO_LOGO_MARK);
		if (markPageVersionConfig == null) {

			PlatformConfig newMarkPageVersionConfig = new PlatformConfig();
			newMarkPageVersionConfig.setcKey(Constant.PUSH_VIDEO_LOGO_MARK);
			newMarkPageVersionConfig.setcValue(Integer.toString(logoMark));
			newMarkPageVersionConfig.setcDesc("是否添加水印");
			platformConfigPriDao.save(newMarkPageVersionConfig);
		} else {
			markPageVersionConfig.setcKey(Constant.PUSH_VIDEO_LOGO_MARK);
			markPageVersionConfig.setcValue(Integer.toString(logoMark));
			markPageVersionConfig.setcDesc("是否添加水印");
			platformConfigPriDao.save(markPageVersionConfig);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResultDataSet getWindowsWebPageVersion() {
		ResultDataSet rds = new ResultDataSet();
		Map response = new HashMap<>();
		PlatformConfig homePageVersionConfig = platformConfigPriDao.findOne(Constant.WINDOWS_BOSS_HOME_PAGE_VERSION);
		if (homePageVersionConfig != null) {
			response.put("homePage", homePageVersionConfig.getcValue());
		}
		PlatformConfig livePageVersionConfig = platformConfigPriDao.findOne(Constant.WINDOWS_BOSS_LIVE_PAGE_VERSION);
		if (livePageVersionConfig != null) {
			response.put("livePage", livePageVersionConfig.getcValue());
		}
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet syncWindowsWebPageVersion() {
		ResultDataSet rds = new ResultDataSet();
		PlatformConfig homePageConfig = platformConfigPriDao.findOne(Constant.WINDOWS_BOSS_HOME_PAGE_VERSION);
		if (homePageConfig == null) {
			rds.setMsg("数据库中没有找到windows客户端首页版本");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		jedisUtils.set(Constant.WINDOWS_BOSS_HOME_PAGE_VERSION, homePageConfig.getcValue());

		PlatformConfig livePageConfig = platformConfigPriDao.findOne(Constant.WINDOWS_BOSS_LIVE_PAGE_VERSION);
		if (livePageConfig == null) {
			rds.setMsg("数据库中没有找到windows客户端内嵌直播间版本");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		jedisUtils.set(Constant.WINDOWS_BOSS_LIVE_PAGE_VERSION, livePageConfig.getcValue());
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getWindowsUpdateVersion() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Map<String, Object> response = new HashMap<String, Object>();
		PlatformConfig loginConfig = platformConfigPriDao.findOne(Constant.WINDOWS_UPDATE_LOGIN_VERSION);
		if (loginConfig == null) {
			rds.setMsg("数据库中没有找到登陆程序版本");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		PlatformConfig loginPathConfig = platformConfigPriDao.findOne(Constant.WINDOWS_UPDATE_LOGIN_PATH);
		if (loginPathConfig == null) {
			rds.setMsg("数据库中没有找到登陆程序版本");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		PlatformConfig jsonConfig = platformConfigPriDao.findOne(Constant.WINDOWS_UPDATE_JSON_VERSION);
		if (jsonConfig == null) {
			rds.setMsg("数据库中没有找到Json文件md5");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		PlatformConfig jsonPath = platformConfigPriDao.findOne(Constant.WINDOWS_UPDATE_JSON_PATH);
		if (jsonPath == null) {
			rds.setMsg("数据库中没有找到Json文件路径");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		PlatformConfig bossZipUrl = platformConfigPriDao.findOne(Constant.WINDOWS_BOSS_UPDATE_ZIP_URL);
		if (bossZipUrl == null) {
			rds.setMsg("数据库中没有找到BossZip文件路径");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		
		String loginAdvImage = jedisUtils.get(Constant.WINDOWS_LOGIN_ADV_IMAGE_URL);
		response.put("loginAdvImage", loginAdvImage);
		String advRedirectUrl = jedisUtils.get(Constant.WINDOWS_LOGIN_ADV_REDIRECT_URL);
		response.put("advRedirectUrl", advRedirectUrl);
		
		
		response.put("loginVersion", loginConfig.getcValue());
		response.put("loginPath", loginPathConfig.getcValue());
		response.put("jsonMd5", jsonConfig.getcValue());
		response.put("jsonPath", jsonPath.getcValue());
		response.put("bossZipUrl", bossZipUrl.getcValue());
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet setWindowsGameUpdateVersion(String gameJsonMd5, String gameJsonPath, String gameZipUrl)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		PlatformConfig gameJsonMd5Config = platformConfigPriDao.findOne(Constant.WINDOWS_GAME_UPDATE_JSON_VERSION);
		if (gameJsonMd5Config == null) {
			PlatformConfig newGameJsonMd5Config = new PlatformConfig();
			newGameJsonMd5Config.setcKey(Constant.WINDOWS_GAME_UPDATE_JSON_VERSION);
			newGameJsonMd5Config.setcValue(gameJsonMd5);
			newGameJsonMd5Config.setcDesc("windows游戏更新登陆程序版本");
			platformConfigPriDao.save(newGameJsonMd5Config);
		} else {
			gameJsonMd5Config.setcKey(Constant.WINDOWS_GAME_UPDATE_JSON_VERSION);
			gameJsonMd5Config.setcValue(gameJsonMd5);
			gameJsonMd5Config.setcDesc("windows游戏更新登陆程序版本");
			platformConfigPriDao.save(gameJsonMd5Config);
		}

		PlatformConfig gameJsonPathConfig = platformConfigPriDao.findOne(Constant.WINDOWS_GAME_UPDATE_JSON_PATH);
		if (gameJsonPathConfig == null) {
			PlatformConfig newGameJsonPathConfig = new PlatformConfig();
			newGameJsonPathConfig.setcKey(Constant.WINDOWS_GAME_UPDATE_JSON_PATH);
			newGameJsonPathConfig.setcValue(gameJsonPath);
			newGameJsonPathConfig.setcDesc("windows游戏更新用的json文件下载地址");
			platformConfigPriDao.save(newGameJsonPathConfig);
		} else {
			gameJsonPathConfig.setcKey(Constant.WINDOWS_GAME_UPDATE_JSON_PATH);
			gameJsonPathConfig.setcValue(gameJsonPath);
			gameJsonPathConfig.setcDesc("windows游戏更新用的json文件下载地址");
			platformConfigPriDao.save(gameJsonPathConfig);
		}

		PlatformConfig gameZipPathConfig = platformConfigPriDao.findOne(Constant.WINDOWS_GAME_ZIP_PATH);
		if (gameZipPathConfig == null) {
			PlatformConfig newGameZipPathConfig = new PlatformConfig();
			newGameZipPathConfig.setcKey(Constant.WINDOWS_GAME_ZIP_PATH);
			newGameZipPathConfig.setcValue(gameZipUrl);
			newGameZipPathConfig.setcDesc("windows游戏更新用的json文件下载路径");
			platformConfigPriDao.save(newGameZipPathConfig);
		} else {
			gameZipPathConfig.setcKey(Constant.WINDOWS_GAME_ZIP_PATH);
			gameZipPathConfig.setcValue(gameZipUrl);
			gameZipPathConfig.setcDesc("windows游戏更新用的json文件下载路径");
			platformConfigPriDao.save(gameZipPathConfig);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet syncWindowsGameUpdateVersion() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		PlatformConfig jsonMd5Config = platformConfigPriDao.findOne(Constant.WINDOWS_GAME_UPDATE_JSON_VERSION);
		if (jsonMd5Config == null) {
			rds.setMsg("数据库中没有找到游戏Json文件md5");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		jedisUtils.set(Constant.WINDOWS_GAME_UPDATE_JSON_VERSION, jsonMd5Config.getcValue());

		PlatformConfig jsonPathConfig = platformConfigPriDao.findOne(Constant.WINDOWS_GAME_UPDATE_JSON_PATH);
		if (jsonPathConfig == null) {
			rds.setMsg("数据库中没有找到Json文件下载url");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		jedisUtils.set(Constant.WINDOWS_GAME_UPDATE_JSON_PATH, jsonPathConfig.getcValue());

		PlatformConfig gameZipUrl = platformConfigPriDao.findOne(Constant.WINDOWS_GAME_ZIP_PATH);
		if (gameZipUrl == null) {
			rds.setMsg("数据库中没有找到游戏Zip文件路径");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		jedisUtils.set(Constant.WINDOWS_GAME_ZIP_PATH, gameZipUrl.getcValue());
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getWindowsGameUpdateVersion() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Map<String, Object> response = new HashMap<String, Object>();
		PlatformConfig gameJsonMd5 = platformConfigPriDao.findOne(Constant.WINDOWS_GAME_UPDATE_JSON_VERSION);
		if (gameJsonMd5 == null) {
			rds.setMsg("数据库中没有找到游戏json md5");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		PlatformConfig gameJsonPath = platformConfigPriDao.findOne(Constant.WINDOWS_GAME_UPDATE_JSON_PATH);
		if (gameJsonPath == null) {
			rds.setMsg("数据库中没有找到Json文件下载路径");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		PlatformConfig gameZipUrl = platformConfigPriDao.findOne(Constant.WINDOWS_GAME_ZIP_PATH);
		if (gameZipUrl == null) {
			rds.setMsg("数据库中没有找到游戏zip下载路径");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		response.put("gameJsonMd5", gameJsonMd5.getcValue());
		response.put("gameJsonPath", gameJsonPath.getcValue());
		response.put("gameZipUrl", gameZipUrl.getcValue());
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet addFansClubConfig(int month, float discount) {
		ResultDataSet rds = new ResultDataSet();
		FansClubConfig config = fansClubConfigPriDao.findOne(month);
		if (config != null) {
			rds.setMsg("配置已经存在");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}

		config = new FansClubConfig();
		config.setMonth(month);
		config.setDiscount(discount);
		fansClubConfigPriDao.save(config);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet updateFansClubConfig(int month, float discount) {
		ResultDataSet rds = new ResultDataSet();
		FansClubConfig config = fansClubConfigPriDao.findOne(month);
		if (config == null) {
			rds.setMsg("配置不存在");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		config.setDiscount(discount);
		fansClubConfigPriDao.save(config);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet deleteFansClubConfig(int month) {
		ResultDataSet rds = new ResultDataSet();
		FansClubConfig config = fansClubConfigPriDao.findOne(month);
		if (config == null) {
			rds.setMsg("配置不存在");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}

		fansClubConfigPriDao.delete(config);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryFansClubConfigs(int pageSize, int pageNum) {
		ResultDataSet rds = new ResultDataSet();

		Sort sort = new Sort(Direction.fromString("asc"), "month");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<FansClubConfig> data = fansClubConfigSecDao.findAll(pageable);
		rds.setData(MyPageUtils.getMyPage(data));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet freshFansClubsConfig() {
		ResultDataSet rds = new ResultDataSet();
		hotFansClubConfigDao.deleteAll();
		Iterable<FansClubConfig> configs = fansClubConfigPriDao.findAll();
		List<HotFansClubConfig> hotConfigs = new ArrayList<HotFansClubConfig>();
		for (FansClubConfig config : configs) {
			HotFansClubConfig newconfig = new HotFansClubConfig();
			newconfig.setMonth(config.getMonth());
			newconfig.setDiscount(config.getDiscount());
			hotConfigs.add(newconfig);
		}
		hotFansClubConfigDao.save(hotConfigs);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getFansClubByMonth(int month) {
		ResultDataSet rds = new ResultDataSet();
		HotFansClubConfig data = hotFansClubConfigDao.findOne(month);
		rds.setData(data);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet addPresentMountConfig(int type, int level, int mountId, int month) {
		ResultDataSet rds = new ResultDataSet();
		MountPresent mountPresent = mountPresentPriDao.findByTypeAndLevel(type, level);
		if (mountPresent != null) {
			rds.setMsg("配置已经存在");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		mountPresent = new MountPresent();
		mountPresent.setType(type);
		mountPresent.setLevel(level);
		mountPresent.setMountId(mountId);
		mountPresent.setMonth(month);
		mountPresentPriDao.save(mountPresent);

		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet deletePresentMountConfig(int type, int level) {
		ResultDataSet rds = new ResultDataSet();
		MountPresent mountPresent = mountPresentPriDao.findByTypeAndLevel(type, level);
		if (mountPresent == null) {
			rds.setMsg("配置不存在");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		mountPresentPriDao.delete(mountPresent);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet updatePresentMountConfig(int type, int level, int mountId, int month) {
		ResultDataSet rds = new ResultDataSet();
		MountPresent mountPresent = mountPresentPriDao.findByTypeAndLevel(type, level);
		if (mountPresent == null) {
			rds.setMsg("配置不存在存在");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}

		mountPresent.setMountId(mountId);
		mountPresent.setMonth(month);
		mountPresentPriDao.save(mountPresent);

		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getPresentMountConfig(int type, int level) {
		ResultDataSet rds = new ResultDataSet();
		MountPresent mountPresent = mountPresentSecDao.findByTypeAndLevel(type, level);
		rds.setData(mountPresent);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryPresentMountConfig(int pageSize, int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("asc"), "type");
		Sort sort2 = new Sort(Direction.fromString("asc"), "level");
		sort.and(sort2);
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<MountPresent> data = mountPresentSecDao.findAll(pageable);

		rds.setData(MyPageUtils.getMyPage(data));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet addClubTaskConfig(int taskId, long score, String description) {
		ResultDataSet rds = new ResultDataSet();
		ClubTaskConfig config = clubTaskConfigSecDao.findOne(taskId);
		if (config != null) {
			rds.setMsg("配置已经存在");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}

		config = new ClubTaskConfig();
		config.setTaskId(taskId);
		config.setTaskScore(score);
		;
		config.setTaskDesc(description);
		clubTaskConfigSecDao.save(config);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet updateClubTaskConfig(int taskId, long score, String description) {
		ResultDataSet rds = new ResultDataSet();
		ClubTaskConfig config = clubTaskConfigSecDao.findOne(taskId);
		if (config == null) {
			rds.setMsg("配置不存在");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}

		config.setTaskId(taskId);
		config.setTaskScore(score);
		;
		config.setTaskDesc(description);
		clubTaskConfigSecDao.save(config);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet deleteClubTaskConfig(int taskId) {
		ResultDataSet rds = new ResultDataSet();
		ClubTaskConfig config = clubTaskConfigSecDao.findOne(taskId);
		if (config == null) {
			rds.setMsg("配置不存在");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		clubTaskConfigSecDao.delete(config);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getClubTask(int taskId) {
		ResultDataSet rds = new ResultDataSet();
		ClubTaskConfig config = clubTaskConfigPriDao.findOne(taskId);
		if (config == null) {
			rds.setMsg("配置不存在");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		rds.setData(config);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryClubTaskConfigs(int pageSize, int pageNum) {
		ResultDataSet rds = new ResultDataSet();

		Sort sort = new Sort(Direction.fromString("asc"), "taskId");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<ClubTaskConfig> data = clubTaskConfigSecDao.findAll(pageable);
		rds.setData(MyPageUtils.getMyPage(data));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet freshClubTaskConfig() {
		ResultDataSet rds = new ResultDataSet();
		hotClubTaskConfigDao.deleteAll();
		Iterable<ClubTaskConfig> configs = clubTaskConfigPriDao.findAll();
		List<HotClubTaskConfig> hotConfigs = new ArrayList<HotClubTaskConfig>();
		for (ClubTaskConfig config : configs) {
			HotClubTaskConfig newconfig = new HotClubTaskConfig();
			newconfig.setId(config.getTaskId());
			newconfig.setTaskScore(config.getTaskScore());
			newconfig.setTaskDesc(config.getTaskDesc());
			hotConfigs.add(newconfig);
		}
		hotClubTaskConfigDao.save(hotConfigs);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet updateGameExchangeRate(String gameDiamondRate, String gameGoldRate, String lovelinessRate) {
		ResultDataSet rds = new ResultDataSet();
		PlatformConfig config = platformConfigPriDao.findOne(Constant.SQL_PLATFORM_CONFIG_DIAMOND_EXCHARGE_RATE);
		if (config == null) {
			config = new PlatformConfig();
			config.setcKey(Constant.SQL_PLATFORM_CONFIG_DIAMOND_EXCHARGE_RATE);
			config.setcDesc("游戏币兑换钻石");
		}
		config.setcValue(gameDiamondRate);
		platformConfigPriDao.save(config);

		config = null;
		config = platformConfigPriDao.findOne(Constant.SQL_PLATFORM_CONFIG_GAME_GOLD_EXCHARGE_RATE);
		if (config == null) {
			config = new PlatformConfig();
			config.setcKey(Constant.SQL_PLATFORM_CONFIG_GAME_GOLD_EXCHARGE_RATE);
			config.setcDesc("虎币兑换游戏币");
		}
		config.setcValue(gameGoldRate);
		platformConfigPriDao.save(config);

		config = null;
		config = platformConfigPriDao.findOne(Constant.SQL_PLATFORM_CONFIG_LOVELINESS_EXCHARGE_RATE);
		if (config == null) {
			config = new PlatformConfig();
			config.setcKey(Constant.SQL_PLATFORM_CONFIG_LOVELINESS_EXCHARGE_RATE);
			config.setcDesc("魅力值兑换游戏币");
		}
		config.setcValue(lovelinessRate);
		platformConfigPriDao.save(config);

		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryGameExchangeRate() {
		ResultDataSet rds = new ResultDataSet();
		int diamondRate, gameGoldRate, lovelinessRate;
		diamondRate = gameGoldRate = lovelinessRate = 0;

		PlatformConfig config = platformConfigPriDao.findOne(Constant.SQL_PLATFORM_CONFIG_DIAMOND_EXCHARGE_RATE);
		if (config != null) {
			diamondRate = Integer.parseInt(config.getcValue());
		}
		config = platformConfigPriDao.findOne(Constant.SQL_PLATFORM_CONFIG_GAME_GOLD_EXCHARGE_RATE);
		if (config != null) {
			gameGoldRate = Integer.parseInt(config.getcValue());
		}
		config = platformConfigPriDao.findOne(Constant.SQL_PLATFORM_CONFIG_LOVELINESS_EXCHARGE_RATE);
		if (config != null) {
			lovelinessRate = Integer.parseInt(config.getcValue());
		}
		ResponseData rp = new ResponseData();
		rp.put(Constant.SQL_PLATFORM_CONFIG_DIAMOND_EXCHARGE_RATE, diamondRate);
		rp.put(Constant.SQL_PLATFORM_CONFIG_GAME_GOLD_EXCHARGE_RATE, gameGoldRate);
		rp.put(Constant.SQL_PLATFORM_CONFIG_LOVELINESS_EXCHARGE_RATE, lovelinessRate);
		rds.setData(rp);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet setAppShareUrl(String appShareUrl) {
		ResultDataSet rds = new ResultDataSet();
		PlatformConfig config = platformConfigPriDao.findOne(Constant.APP_SOCIAL_SHARE_URL);
		if (config != null) {
			config.setcValue(appShareUrl);
			config.setcDesc("设置app分享url");
			platformConfigPriDao.save(config);
		} else {
			PlatformConfig newConfig = new PlatformConfig();
			newConfig.setcKey(Constant.APP_SOCIAL_SHARE_URL);
			newConfig.setcValue(appShareUrl);
			newConfig.setcDesc("设置app分享url");
			platformConfigPriDao.save(newConfig);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getAppShareUrl() {
		ResultDataSet rds = new ResultDataSet();
		PlatformConfig config = platformConfigSecDao.findOne(Constant.APP_SOCIAL_SHARE_URL);
		rds.setData(config.getcValue());
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet addHomeType(String name, int value, int sortId) {
		ResultDataSet rds = new ResultDataSet();
		ChannelType type = new ChannelType();
		type.setName(name);
		type.setValue(value);
		type.setSortId(sortId);
		channelTypePriDao.save(type);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet deleteHomeType(String name) {
		ResultDataSet rds = new ResultDataSet();
		ChannelType type = channelTypePriDao.findOne(name);
		channelTypePriDao.delete(type);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getHomeType() {
		ResultDataSet rds = new ResultDataSet();
		Iterable<ChannelType> types = channelTypeSecDao.findAll();
		rds.setData(types);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet addPlatformConfig(String key, String desc, String value) {
		ResultDataSet rds = new ResultDataSet();
		PlatformConfig config = platformConfigPriDao.findOne(key);
		if (config != null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该配置已经存在");
			return rds;
		}
		if (key.equals(Constant.VIDEO_CLOUD_SIGN_KEY)){
			try {
				YXResultSet resultSet = Videocloud163.setSignKey(value);
				if (!resultSet.getCode().equals("200")){
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					rds.setMsg(CodeToString.getString(resultSet.getCode()));
					return rds;
				}
			} catch (IOException e) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("云信操作失败");
				return rds;
			}
		}else if (key.equals(Constant.CHANNEL_STATUS_CALLBACK_URL)){
			try {
				YXResultSet resultSet = Videocloud163.setChStatusCallback(value);
				if (!resultSet.getCode().equals("200")){
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					rds.setMsg(CodeToString.getString(resultSet.getCode()));
					return rds;
				}
			} catch (IOException e) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("云信操作失败");
				return rds;
			}
		}
		config = new PlatformConfig();
		config.setcKey(key);
		config.setcDesc(desc);
		config.setcValue(value);
		platformConfigPriDao.save(config);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	@CacheEvict(value = "PlatformConfig", allEntries = true)
	public ResultDataSet clearPlatformConfig() {
		ResultDataSet rds = new ResultDataSet();
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet UpdatePlatformConfig(String key, String value) {
		ResultDataSet rds = new ResultDataSet();
		PlatformConfig config = platformConfigPriDao.findOne(key);
		if (config == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该配置");
			return rds;
		}
		config.setcValue(value);
		platformConfigPriDao.save(config);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet ListPlatformConfig() {
		ResultDataSet rds = new ResultDataSet();
		Iterable<PlatformConfig> config = platformConfigSecDao.findAll();
		rds.setData(config);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getProducts(int device) {
		ResultDataSet rds = new ResultDataSet();
		if (!DeviceCode.contains(device)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("无效设备");
			return rds;
		}
		List<Product> products = productSecDao.findByDeviceOrderByPrice(device);
		rds.setData(products);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet addProducts(long id, long iGold, long price, String productId, int device) {
		ResultDataSet rds = new ResultDataSet();
		Product pt = null;

		if (!DeviceCode.contains(device)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("无效设备");
			return rds;
		}
		if (iGold < 0) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("虎币数不能为负数");
			return rds;
		}
		if (price < 0) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("支付价格不能为负数");
			return rds;
		}

		if (id > 0) {
			pt = productPriDao.findOne(id);
			if (pt == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setData("商品id错误");
				return rds;
			}
			pt.setId(id);
		} else {
			pt = new Product();
		}
		pt.setDevice(device);
		pt.setiGold(iGold);
		pt.setPrice(price);
		pt.setProductId(productId);
		productPriDao.save(pt);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet deleteProduct(long id) {
		ResultDataSet rds = new ResultDataSet();
		productPriDao.delete(id);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet addSound(String name, String path) throws IOException {
		ChannelSound channelSound = new ChannelSound();
		channelSound.setGuid(StringUtils.createUUID());
		channelSound.setName(name);
		channelSound.setPath(path);
		channelSoundPriDao.save(channelSound);
		ResultDataSet rds = new ResultDataSet();
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet deleteSound(String guid) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ChannelSound channelSound = channelSoundPriDao.findOne(guid);
		if (channelSound == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("音效不存在");
		}
		channelSoundPriDao.delete(channelSound);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet querySoundList(int pageSize, int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("asc"), "name");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<ChannelSound> data = channelSoundSecDao.findAll(pageable);
		rds.setData(MyPageUtils.getMyPage(data));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getSound(String guid) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ChannelSound channelSound = channelSoundSecDao.findOne(guid);
		rds.setData(channelSound);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet setWindowsLoginAdvPath(String imagePath, String redirectPath) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		PlatformConfig loginPathConfig = platformConfigPriDao.findOne(Constant.WINDOWS_LOGIN_ADV_IMAGE_URL);
		if (loginPathConfig == null) {
			PlatformConfig newLoginPathConfig = new PlatformConfig();
			newLoginPathConfig.setcKey(Constant.WINDOWS_LOGIN_ADV_IMAGE_URL);
			newLoginPathConfig.setcValue(imagePath);
			newLoginPathConfig.setcDesc("windowsLogin广告url");
			platformConfigPriDao.save(newLoginPathConfig);
		} else {
			loginPathConfig.setcKey(Constant.WINDOWS_LOGIN_ADV_IMAGE_URL);
			loginPathConfig.setcValue(imagePath);
			loginPathConfig.setcDesc("windowsLogin广告url");
			platformConfigPriDao.save(loginPathConfig);
		}
		jedisUtils.set(Constant.WINDOWS_LOGIN_ADV_IMAGE_URL, loginPathConfig.getcValue());

		PlatformConfig loginRedirectPathConfig = platformConfigPriDao.findOne(Constant.WINDOWS_LOGIN_ADV_REDIRECT_URL);
		if (loginRedirectPathConfig == null) {
			PlatformConfig newLoginPathConfig = new PlatformConfig();
			newLoginPathConfig.setcKey(Constant.WINDOWS_LOGIN_ADV_REDIRECT_URL);
			newLoginPathConfig.setcValue(imagePath);
			newLoginPathConfig.setcDesc("windowsLogin广告跳转turl");
			platformConfigPriDao.save(newLoginPathConfig);
		} else {
			loginRedirectPathConfig.setcKey(Constant.WINDOWS_LOGIN_ADV_REDIRECT_URL);
			loginRedirectPathConfig.setcValue(imagePath);
			loginRedirectPathConfig.setcDesc("windowsLogin广告跳转url");
			platformConfigPriDao.save(loginRedirectPathConfig);
		}
		jedisUtils.set(Constant.WINDOWS_LOGIN_ADV_REDIRECT_URL, loginRedirectPathConfig.getcValue());
		
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet setGameVipConfig(int level, int month, long price) {
		ResultDataSet rds = new ResultDataSet();
		GameVipConfig config = gameVipConfigPriDao.findByLevelAndMonth(level, month);
		if (config == null){
			config = new GameVipConfig();
			config.setLevel(level);
			config.setMonth(month);
			config.setPrice(price);
		}else{
			config.setPrice(price);
		}
		gameVipConfigPriDao.save(config);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet deleteGameVipConfig(int level, int month) {
		ResultDataSet rds = new ResultDataSet();
		GameVipConfig config = gameVipConfigPriDao.findByLevelAndMonth(level, month);
		if (config == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有配置");
			return rds;
		}else{
			gameVipConfigPriDao.delete(config);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getGameVipConfigs() {
		ResultDataSet rds = new ResultDataSet();
		List<GameVipConfig> configs = (List<GameVipConfig>) gameVipConfigSecDao.findAll();
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setData(configs);
		return rds;
	}
}
