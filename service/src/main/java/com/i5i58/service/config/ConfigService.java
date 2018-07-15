package com.i5i58.service.config;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.config.IConfig;
import com.i5i58.apis.constants.ResponseData;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.data.channel.ChannelType;
import com.i5i58.data.config.AppVersionControl;
import com.i5i58.data.config.AppVersionStatus;
import com.i5i58.data.config.ApplePayType;
import com.i5i58.data.config.AppleReleaseType;
import com.i5i58.data.config.ReactNativeConfig;
import com.i5i58.data.config.ReactNativeVersionConfig;
import com.i5i58.primary.dao.channel.ChannelTypePriDao;
import com.i5i58.primary.dao.config.AppVersionControlPriDao;
import com.i5i58.primary.dao.config.PlatformConfigPriDao;
import com.i5i58.primary.dao.config.ReactNativeConfigPriDao;
import com.i5i58.primary.dao.config.ReactNativeVersionConfigPriDao;
import com.i5i58.util.ConfigUtils;
import com.i5i58.util.Constant;
import com.i5i58.util.DeviceCode;
import com.i5i58.util.JedisUtils;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.StringUtils;

@Service(protocol = "dubbo")
public class ConfigService implements IConfig {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	JedisUtils jedisUtils;

	@Autowired
	PlatformConfigPriDao platformConfigDao;

	@Autowired
	ReactNativeVersionConfigPriDao reactNativeVersionConfigDao;

	@Autowired
	ReactNativeConfigPriDao reactNativeConfigDao;

	@Autowired
	AppVersionControlPriDao appVersionControlDao;

	@Autowired
	ChannelTypePriDao channelTypeDao;

	@Autowired
	ConfigUtils configUtils;

	@Override
	// @Cacheable(value = "RNConfig", key = "'RNConfig_'+#version")
	public ResultDataSet checkRn(String rnVersion, int device, int main, int sub, int func, String deviceVersion,
			String version) {
		ResultDataSet rds = new ResultDataSet();
		String cacheKey = "rnconfigcache_" + version;

		if (jedisUtils.exist(cacheKey)) {
			String value = jedisUtils.get(cacheKey);
			if (!StringUtils.StringIsEmptyOrNull(value)) {
				ResponseData data = null;
				try {
					data = new JsonUtils().toObject(value, ResponseData.class);
				} catch (IOException e) {
					logger.error(e);
				}
				// set code and message
				if (data != null) {
					rds.setData(data);
					rds.setCode(ResultCode.SUCCESS.getCode());
					return rds;
				}
			}
		}

		AppVersionControl appVersion = appVersionControlDao.findOne(deviceVersion);
		if (appVersion == null) {
			rds.setData("no_app_version");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		ReactNativeVersionConfig rnVersionConfig = reactNativeVersionConfigDao.findOne(appVersion.getRnVersion());
		if (rnVersionConfig == null) {
			rds.setData("no_rn_version");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (rnVersion.equals(rnVersionConfig.getRnZipUrl())) {
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		}
		List<ReactNativeConfig> rnConfig = reactNativeConfigDao.findByVersion(rnVersionConfig.getVersion());

		ResponseData response = new ResponseData();
		response.put("react", rnConfig);
		response.put("rnZip", rnVersionConfig.getRnZipUrl());
		response.put("rnConfigVersion", rnVersionConfig.getRnZipUrl());

		String cacheValue = null;
		try {
			cacheValue = new JsonUtils().toJson(response);
			jedisUtils.set(cacheKey, cacheValue);
			jedisUtils.expire(cacheKey, 10);
		} catch (IOException e) {
			logger.error(e);
		}

		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	@Cacheable(value = "PlatformConfig", key = "'PlatformConfig_'+#version")
	public ResultDataSet getPlatformConfig(int device, int main, int sub, int func, String version) {
		ResultDataSet rds = new ResultDataSet();
		HashMap<String, Object> response = new HashMap<String, Object>();
		Sort sort = new Sort(Direction.fromString("asc"), "sortId");
		Iterable<ChannelType> channelTypes = channelTypeDao.findAll(sort);

		String shareUrl = "";
		try {
			shareUrl = configUtils.getPlatformConfig(Constant.APP_SOCIAL_SHARE_URL);
		} catch (Exception e) {
			logger.error("", e);
		}
		response.put("homeType", channelTypes);
		response.put("shareUrl", shareUrl);// "https://www.gg78.com/wap/gethotpage.html?cId=");
		AppVersionControl currAppVersion = appVersionControlDao
				.findByStatusAndDevice(AppVersionStatus.CURRENT.getValue(), device);
		if (currAppVersion == null) {
			rds.setMsg("版本丢失");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		AppVersionControl appVersion = appVersionControlDao.findOne(version);
		if (appVersion == null) {// 非法版本
			rds.setMsg("版本已过时");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		if (appVersion.getStatus() == AppVersionStatus.EXAMINE.getValue()) {
			response.put("appUpdate", "noUpdate");
			response.put("release", AppleReleaseType.APPLE.getCode());
			response.put("payType", ApplePayType.APPLE_PAY.getCode());
		} else if (appVersion.getStatus() == AppVersionStatus.DISCARD.getValue()) {
			response.put("appUpdate", "forceUpdate");
			response.put("updateUrl", currAppVersion.getUpdateUrl());
			response.put("release", AppleReleaseType.APPLE.getCode());
			response.put("payType", ApplePayType.APPLE_PAY.getCode());
		} else {
			response.put("release", appVersion.getAppleRelease());
			response.put("payType", appVersion.getApplePayType());
			if (main != currAppVersion.getMainVserion()) {
				response.put("appUpdate", "forceUpdate");
				response.put("updateUrl", currAppVersion.getUpdateUrl());
			} else {
				if (sub != currAppVersion.getSubVersion()) {
					response.put("appUpdate", "forceUpdate");
					response.put("updateUrl", currAppVersion.getUpdateUrl());
				} else {
					if (func > currAppVersion.getFuncVersion()) {// 非法版本
						response.put("appUpdate", "forceUpdate");
						response.put("updateUrl", currAppVersion.getUpdateUrl());
					} else if (func < currAppVersion.getFuncVersion()) {
						response.put("appUpdate", "enableUpdate");
						response.put("updateUrl", currAppVersion.getUpdateUrl());
					} else {
						response.put("appUpdate", "noUpdate");
					}
				}
			}
		}
		byte[] RTMP_SIGN_KEY = new byte[] { (byte) 0x3f, (byte) 0x5c, (byte) 0x13, (byte) 0x4c, (byte) 0x45,
				(byte) 0x10, (byte) 0x6a, (byte) 0xad, (byte) 0x24, (byte) 0x14, (byte) 0xcc, (byte) 0xff, (byte) 0x9b,
				(byte) 0xee, (byte) 0x1c, (byte) 0x1b, (byte) 0xa1, (byte) 0xbf, (byte) 0x05, (byte) 0xde, (byte) 0x04,
				(byte) 0x5b, (byte) 0xac, (byte) 0xad, (byte) 0x4a, (byte) 0xf4, (byte) 0xc4, (byte) 0xdf, (byte) 0x74,
				(byte) 0x20, (byte) 0x60, (byte) 0x41 };
		String rtmpSignKey = Base64.getEncoder().encodeToString(RTMP_SIGN_KEY);
		if (DeviceCode.AndroidLive == device) {
			response.put("umengKey", "58660e541061d22cf9000384");
			response.put("yunXinKey", "ba7a0d3dc3914a9e5cbac325d3c6aeab");
			response.put("ossKey", "78xKgz5yZPGMjdtv");
			response.put("ossSecret", "PzsRMoiU4XsLFVURtNmi3jaxVeFoLT");
			response.put("wechatKey", "wx880fa74e302e49c6");
			response.put("wechatSecret", "ebd78ceb6555d6856379370780417123");
			response.put("qqKey", "1105831799");
			response.put("qqSecret", "KEYDN8AnxhE3FBXL3zZ");
			response.put("weiBoKey", "1374374554");
			response.put("weiBoSecret", "f006e60c140f7721e8b2aa4e1dcd7283");
			response.put("weiBoCallBackUrl", "http://www.panglaohu.com");
			response.put("zegoKey", "1989373496");
			response.put("zegoRtmpSignKey", rtmpSignKey);
			response.put("wechatPayKey", "wx880fa74e302e49c6");

		} else if (DeviceCode.IOSLive == device) {
			response.put("umengKey", "5866258c8f4a9d47cc0016ff");
			response.put("yunXinKey", "ba7a0d3dc3914a9e5cbac325d3c6aeab");
			response.put("ossKey", "78xKgz5yZPGMjdtv");
			response.put("ossSecret", "PzsRMoiU4XsLFVURtNmi3jaxVeFoLT");
			response.put("wechatKey", "wx880fa74e302e49c6");
			response.put("wechatSecret", "ebd78ceb6555d6856379370780417123");
			response.put("qqKey", "1105831799");
			response.put("qqSecret", "KEYDN8AnxhE3FBXL3zZ");
			response.put("weiBoKey", "1374374554");
			response.put("weiBoSecret", "f006e60c140f7721e8b2aa4e1dcd7283");
			response.put("weiBoCallBackUrl", "http://www.panglaohu.com");
			response.put("zegoKey", "1989373496");
			response.put("zegoRtmpSignKey", rtmpSignKey);
			response.put("wechatPayKey", "wx880fa74e302e49c6");

		}
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	// @Cacheable(value = "WindowsUpdateConfig", key =
	// "'WindowsUpdateConfig_'+#allVersion")
	public ResultDataSet getWindowsUpdateConfig(String loginVersion, String jsonVersion, String allVersion) {
		ResultDataSet rds = new ResultDataSet();
		Map<Object, Object> response = new HashMap<Object, Object>();
		String newLoginVersion = jedisUtils.get(Constant.WINDOWS_UPDATE_LOGIN_VERSION);
		if (!newLoginVersion.equals(loginVersion)) {
			String loginPath = jedisUtils.get(Constant.WINDOWS_UPDATE_LOGIN_PATH);
			response.put("login", loginPath);
		}
		String newJsonVersion = jedisUtils.get(Constant.WINDOWS_UPDATE_JSON_VERSION);
		if (!newJsonVersion.equals(jsonVersion)) {
			String jsonPath = jedisUtils.get(Constant.WINDOWS_UPDATE_JSON_PATH);
			response.put("json", jsonPath);
		}
		String bossZipUrl = jedisUtils.get(Constant.WINDOWS_BOSS_UPDATE_ZIP_URL);
		response.put("bossZipUrl", bossZipUrl);
		String homePage = jedisUtils.get(Constant.WINDOWS_BOSS_HOME_PAGE_VERSION);
		response.put("homePage", homePage);
		String livePage = jedisUtils.get(Constant.WINDOWS_BOSS_LIVE_PAGE_VERSION);
		response.put("livePage", livePage);
		String loginAdvImage = jedisUtils.get(Constant.WINDOWS_LOGIN_ADV_IMAGE_URL);
		response.put("loginAdvImage", loginAdvImage);
		String advRedirectUrl = jedisUtils.get(Constant.WINDOWS_LOGIN_ADV_REDIRECT_URL);
		response.put("advRedirectUrl", advRedirectUrl);
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	// @Cacheable(value = "WindowsGameUpdateConfig", key =
	// "'WindowsGameUpdateConfig'+#gameJsonVersion")
	public ResultDataSet getWindowsGameUpdateConfig(String gameJsonVersion) {
		ResultDataSet rds = new ResultDataSet();
		Map<Object, Object> response = new HashMap<Object, Object>();
		String newJsonVersion = jedisUtils.get(Constant.WINDOWS_GAME_UPDATE_JSON_VERSION);
		if (!newJsonVersion.equals(gameJsonVersion)) {
			String gameJsonPath = jedisUtils.get(Constant.WINDOWS_GAME_UPDATE_JSON_PATH);
			response.put("gameJson", gameJsonPath);
		}
		String gameZipUrl = jedisUtils.get(Constant.WINDOWS_GAME_ZIP_PATH);
		response.put("gameZip", gameZipUrl);
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getWindowsBossConfig(String version) {
		ResultDataSet rds = new ResultDataSet();
		boolean over360 = false;
		try {
			over360 = Boolean.parseBoolean(configUtils.getPlatformConfig(Constant.WINDOWS_BOSS_CONFIG + version));
		} catch (Exception e) {
			logger.error("", e);
		}
		rds.setData(over360);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getOSSConfig() {
		ResultDataSet rds = new ResultDataSet();
		Map<Object, Object> response = new HashMap<Object, Object>();

		String OSSEndPoint = jedisUtils.get(Constant.OSS_ENDPOINT);
		String OSSAccessKeyId = jedisUtils.get(Constant.OSS_ACCESS_KEY_ID);
		String OSSAccessKeySecret = jedisUtils.get(Constant.OSS_ACCESS_KEY_SECRET);
		response.put("OSSEndPoint", OSSEndPoint);
		response.put("OSSAccessKeyId", OSSAccessKeyId);
		response.put("OSSAccessKeySecret", OSSAccessKeySecret);
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}
}
