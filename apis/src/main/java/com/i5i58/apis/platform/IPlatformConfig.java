package com.i5i58.apis.platform;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

public interface IPlatformConfig {

	/**
	 * 添加礼物
	 * 
	 * @author frank
	 * @param superAccid
	 * @param id
	 * @param price
	 * @param anchorPrice
	 * @param name
	 * @param nullity
	 * @param isForGuard
	 * @param isForVip
	 * @param unit
	 * @param maxCount
	 * @param function
	 * @param path
	 * @return
	 * @throws IOException
	 */
	ResultDataSet addGift(String superAccid, int id, long price, long anchorPrice, String name, boolean nullity,
			boolean isForGuard, boolean isForVip, String unit, int maxCount, String function, int condition,
			String path, boolean broadcast, int sortId, int node, String flashPath) throws IOException;

	/**
	 * 更新礼物
	 * 
	 * @author frank
	 * @param superAccid
	 * @param id
	 * @param price
	 * @param anchorPrice
	 * @param name
	 * @param nullity
	 * @param isForGuard
	 * @param isForVip
	 * @param unit
	 * @param maxCount
	 * @param function
	 * @param path
	 * @return
	 * @throws IOException
	 */
	ResultDataSet updateGift(String superAccid, int id, long price, long anchorPrice, String name, boolean nullity,
			boolean isForGuard, boolean isForVip, String unit, int maxCount, String function, int condition,
			String path, boolean broadcast, int sortId, int node, String flashPath) throws IOException;

	/**
	 * 删除礼物
	 * 
	 * @author frank
	 * @param superAccid
	 * @param id
	 * @return
	 * @throws IOException
	 */
	ResultDataSet deleteGift(String superAccid, int id) throws IOException;

	/**
	 * 查询礼物列表
	 * 
	 * @author frank
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws IOException
	 */
	ResultDataSet queryGiftList(int pageSize, int pageNum) throws IOException;

	/**
	 * 查询礼物列表
	 * 
	 * @author frank
	 * @param name
	 * @return
	 * @throws IOException
	 */
	ResultDataSet queryGift(String name) throws IOException;

	/**
	 * 获取礼物
	 * 
	 * @author frank
	 * @param id
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getGift(int id) throws IOException;

	/**
	 * 清除礼物设置缓存
	 * 
	 * @return
	 */
	public ResultDataSet refreashChannelGiftConfigCache();

	/**
	 * 获取礼物设置
	 * 
	 * @throws IOException
	 */
	public ResultDataSet getChannelGiftConfig() throws IOException;

	/**
	 * 新版礼物上线
	 * 
	 * @return
	 */
	public ResultDataSet newGiftVersion();

	/**
	 * 生成uuid
	 * 
	 * @return
	 */
	public ResultDataSet createUUID();

	/**
	 * 添加坐骑
	 * 
	 * @author frank
	 * @param superAccid
	 * @param id
	 * @param price
	 * @param name
	 * @param sImgMd5
	 * @param bImgMd5
	 * @param aniamtion
	 * @param nullity
	 * @param isForGuard
	 * @param isForVip
	 * @param isForFansClubs
	 * @param deadline
	 * @return
	 * @throws IOException
	 */
	ResultDataSet addMount(String superAccid, int id, long price, String name, boolean nullity, boolean isForGuard,
			boolean isForVip, String function, String path, int deadline, boolean isForFansClubs, int level)
			throws IOException;

	/**
	 * 更新坐骑
	 * 
	 * @author frank
	 * @param superAccid
	 * @param id
	 * @param price
	 * @param name
	 * @param sImgMd5
	 * @param bImgMd5
	 * @param aniamtion
	 * @param nullity
	 * @param isForGuard
	 * @param isForVip
	 * @param isForFansClubs
	 * @param deadline
	 * @return
	 * @throws IOException
	 */
	ResultDataSet updateMount(String superAccid, int id, long price, String name, boolean nullity, boolean isForGuard,
			boolean isForVip, String function, String path, int validity, boolean isForFansClubs, int level)
			throws IOException;

	/**
	 * 删除坐骑
	 * 
	 * @author frank
	 * @param superAccid
	 * @param id
	 * @return
	 * @throws IOException
	 */
	ResultDataSet deleteMount(String superAccid, int id) throws IOException;

	/**
	 * 查询礼物列表
	 * 
	 * @author frank
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws IOException
	 */
	ResultDataSet queryMountList(int pageSize, int pageNum) throws IOException;

	/**
	 * 获取礼物
	 * 
	 * @author frank
	 * @param id
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getMount(int id) throws IOException;

	/**
	 * 清除礼物设置缓存
	 * 
	 * @return
	 */
	public ResultDataSet refreashChannelMountConfigCache();

	/**
	 * 获取礼物设置
	 * 
	 * @throws IOException
	 */
	public ResultDataSet getChannelMountConfig() throws IOException;

	/**
	 * 新版礼物上线
	 * 
	 * @return
	 */
	public ResultDataSet newMountVersion();

	/**
	 * 设置移动端最小坐骑显示ID
	 * 
	 * @param mobileMountMin
	 * @return
	 */
	public ResultDataSet setMobileMountMin(int mobileMountMin);

	/**
	 * 获取移动端最小坐骑显示ID
	 * 
	 * @param mobileMountMin
	 * @return
	 */
	public ResultDataSet getMobileMountMin();

	/**
	 * 获取频道html资源
	 * 
	 * @return
	 */
	public ResultDataSet getAnimationConfig();

	/**
	 * 清空频道html资源缓存
	 * 
	 * @return
	 */
	public ResultDataSet refreashAnimationConfigCache();

	/**
	 * 保存动画配置
	 * 
	 * @author frank
	 * @param zipUrl
	 * @param resUrl
	 * @return
	 */
	public ResultDataSet saveAnimationConfig(String zipUrl, String resUrl);

	/**
	 * 新版频道动画上线
	 * 
	 * @return
	 */
	public ResultDataSet newAnimationVersion();

	/**
	 * 添加 React native
	 * 
	 * @author frank
	 * @param superAccid
	 * @param id
	 * @param node
	 * @param name
	 * @param icon
	 * @param module
	 * @param rnZip
	 * @param version
	 * @return
	 * @throws IOException
	 */
	ResultDataSet addRn(String superAccid, String id, String node, String name, String icon, String module,
			String rnZip, String type, int section, String version) throws IOException;

	/**
	 * 获取react native
	 * 
	 * @author frank
	 * @param id
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getRn(String id, String version) throws IOException;

	/**
	 * 更新 React native
	 * 
	 * @author frank
	 * @param superAccid
	 * @param id
	 * @param node
	 * @param name
	 * @param icon
	 * @param module
	 * @param rnZip
	 * @param version
	 * @return
	 * @throws IOException
	 */
	ResultDataSet updateRn(String superAccid, String id, String node, String name, String icon, String module,
			String rnZip, String type, int section, String version) throws IOException;

	/**
	 * 添加react native zip url
	 * 
	 * @author HL
	 * @param superAccid
	 * @param rnZip
	 * @return
	 * @throws IOException
	 */
	ResultDataSet addRnVersion(String superAccid, String version, String rnZipUrl, String rnDescribe)
			throws IOException;

	/**
	 * 删除react native zip url
	 * 
	 * @author HL
	 * @param superAccid
	 * @param iosVersion
	 * @param rnZip
	 * @return
	 * @throws IOException
	 */
	ResultDataSet deleteRnVersion(String superAccid, String version) throws IOException;

	/**
	 * 分页获取rn版本
	 * 
	 * @param superAccid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws IOException
	 */
	ResultDataSet queryRnVersion(String superAccid, int pageNum, int pageSize) throws IOException;

	/**
	 * 删除 React native
	 * 
	 * @author frank
	 * @param superAccid
	 * @param id
	 * @return
	 * @throws IOException
	 */
	ResultDataSet deleteRn(String superAccid, String id, String version) throws IOException;

	/**
	 * 查询 React native
	 * 
	 * @author frank
	 * @param params
	 * @return
	 * @throws IOException
	 */
	ResultDataSet queryRn(String params) throws IOException;

	/**
	 * 查询 React native列表
	 * 
	 * @author frank
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws IOException
	 */
	ResultDataSet queryRnList(int pageSize, int pageNum) throws IOException;

	/**
	 * 清空react native缓存
	 * 
	 * @return
	 */
	public ResultDataSet clearRnChache();

	/**
	 * 增加VIP配置 CW
	 * 
	 * @param superAccid
	 * @param id
	 * @param level
	 * @param month
	 * @param price
	 * @return
	 * @throws IOException
	 */
	ResultDataSet addAccountVip(String superAccid, int id, int level, int month, long price) throws IOException;

	/**
	 * 更新VIP配置 CW
	 * 
	 * @param superAccid
	 * @param id
	 * @param level
	 * @param month
	 * @param price
	 * @return
	 * @throws IOException
	 */
	ResultDataSet updateAccountVip(String superAccid, int id, int level, int month, long price) throws IOException;

	/**
	 * 删除VIP配置 CW
	 * 
	 * @param superAccid
	 * @param id
	 * @return
	 * @throws IOException
	 */
	ResultDataSet deleteAccountVip(String superAccid, int id) throws IOException;

	/**
	 * 查询VIP配置列表 CW
	 * 
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws IOException
	 */
	ResultDataSet queryAccountVipList(int pageSize, int pageNum) throws IOException;

	/**
	 * 获取VIP配置 CW
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getAccountVip(int id) throws IOException;

	/**
	 * 刷新VIP配置缓存 CW
	 * 
	 * @return
	 */
	public ResultDataSet refreashAccountVipCache();

	/**
	 * 获取VIP配置列表 CW
	 * 
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet getAccountVipConfig() throws IOException;

	/**
	 * 增加售出骑士获得佣金配置 HL
	 * 
	 * @param superAccid
	 * @param id
	 * @param guardLevel
	 * @param moneyOneMonth
	 * @return
	 * @throws IOException
	 */
	ResultDataSet addGuardCommissionConfig(String superAccid, int id, int guardLevel, long moneyOneMonth)
			throws IOException;

	/**
	 * 更新售出骑士获得佣金配置 HL
	 * 
	 * @param superAccid
	 * @param id
	 * @param level
	 * @param month
	 * @param price
	 * @return
	 * @throws IOException
	 */
	ResultDataSet updateGuardCommissionConfig(String superAccid, int id, int guardLevel, long moneyOneMonth)
			throws IOException;

	/**
	 * 删除售出骑士获得佣金配置HL
	 * 
	 * @param superAccid
	 * @param id
	 * @return
	 * @throws IOException
	 */
	ResultDataSet deleteGuardCommissionConfig(String superAccid, int id) throws IOException;

	/**
	 * 查询售出骑士获得佣金配置列表HL
	 * 
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws IOException
	 */
	ResultDataSet queryGuardCommissionConfigList(int pageSize, int pageNum) throws IOException;

	/**
	 * 获取售出骑士获得佣金配置 HL
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getGuardCommissionConfig(int id) throws IOException;

	/**
	 * 获取售出骑士获得佣金配置列表 HL
	 * 
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet getGuardCommissionConfigList() throws IOException;

	/**
	 * 增加频道守护配置 CW
	 * 
	 * @param superAccid
	 * @param id
	 * @param level
	 * @param month
	 * @param price
	 * @return
	 * @throws IOException
	 */
	ResultDataSet addChannelGuard(String superAccid, int id, int level, int month, long price) throws IOException;

	/**
	 * 更新频道守护配置 CW
	 * 
	 * @param superAccid
	 * @param id
	 * @param level
	 * @param month
	 * @param price
	 * @return
	 * @throws IOException
	 */
	ResultDataSet updateChannelGuard(String superAccid, int id, int level, int month, long price) throws IOException;

	/**
	 * 删除频道守护配置 CW
	 * 
	 * @param superAccid
	 * @param id
	 * @return
	 * @throws IOException
	 */
	ResultDataSet deleteChannelGuard(String superAccid, int id) throws IOException;

	/**
	 * 查询频道守护配置列表 CW
	 * 
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws IOException
	 */
	ResultDataSet queryChannelGuardList(int pageSize, int pageNum) throws IOException;

	/**
	 * 获取指定频道守护配置 CW
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getChannelGuard(int id) throws IOException;

	/**
	 * 刷新频道守护配置缓存 CW
	 * 
	 * @return
	 */
	public ResultDataSet refreashChannelGuardCache();

	/**
	 * 获取频道守护配置 CW
	 * 
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet getChannelGuardConfig() throws IOException;

	/**
	 * 添加礼物节点
	 * 
	 * @author frank
	 * @param id
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet addGiftType(int id, String name) throws IOException;

	/**
	 * 更新礼物节点
	 * 
	 * @author frank
	 * @param id
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet updateGiftType(int id, String name) throws IOException;

	/**
	 * 删除礼物节点
	 * 
	 * @author frank
	 * @param id
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet deleteGiftType(int id) throws IOException;

	/**
	 * 获取礼物节点
	 * 
	 * @author frank
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet getGiftTypes() throws IOException;

	/**
	 * 添加app版本
	 * 
	 * @author frank
	 * @param superAccId
	 * @param device
	 * @param main
	 * @param sub
	 * @param func
	 * @param updateUrl
	 * @param describe
	 * @param appleRelease
	 *            设置苹果配置，0:审核 or 1:运营
	 * @param applePayType
	 *            设置苹果支付类型，0:苹果支付，1:第三方支付，2:苹果支付与第三方支付
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet addAppVersion(String superAccId, int device, int main, int sub, int func, String updateUrl,
			String describe, int appleRelease, int applePayType, String rnVersion) throws IOException;

	/**
	 * 删除app版本
	 * 
	 * @author frank
	 * @param superAccId
	 * @param id
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet deleteAppVersion(String superAccId, String id) throws IOException;

	/**
	 * 更新app版本状态
	 * 
	 * @author frank
	 * @param superAccId
	 * @param id
	 * @param status
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet updateAppVersionStatus(String superAccId, String id, int status) throws IOException;

	/**
	 * 列出app版本
	 * 
	 * @author frank
	 * @param superAccId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet listAppVersion(String superAccId, int pageNum, int pageSize) throws IOException;

	/*
	 * 设置windows更新信息
	 * 
	 * @author frank
	 * 
	 * @param loginVersion
	 * 
	 * @param jsonVersion
	 * 
	 * @param jsonPath
	 * 
	 * @return
	 * 
	 * @throws IOException
	 */
	public ResultDataSet setWindowsUpdateVersion(String loginVersion, String jsonVersion, String jsonPath,
			String bossZipUrl) throws IOException;

	/**
	 * 设置windowslogin更新包下载地址
	 * 
	 * @param loginPath
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet setWindowsUpdateLoginPath(String loginPath) throws IOException;

	/**
	 * 设置windows客户端首页、内嵌直播间网页版本（触发清空缓存）
	 * 
	 * @author frank
	 * @param homePageVersion
	 * @param livePageVersion
	 * @return
	 */
	public ResultDataSet setWindowsWebPageVersion(String homePageVersion, String livePageVersion);

	/**
	 * 获取windows客户端首页、内嵌直播间网页版本
	 * 
	 * @author frank
	 * @return
	 */
	public ResultDataSet getWindowsWebPageVersion();

	/**
	 * 同步至rediswindows客户端首页、内嵌直播间网页版本（触发清空缓存）
	 * 
	 * @author frank
	 * @return
	 */
	public ResultDataSet syncWindowsWebPageVersion();

	/*
	 * 同步windows更新json文件版本到redis
	 * 
	 * @author frank
	 */
	public ResultDataSet syncWindowsUpdateVersion() throws IOException;

	/**
	 * 获取windows版本信息
	 * 
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet getWindowsUpdateVersion() throws IOException;

	/**
	 * 设置windows游戏版本
	 * 
	 * @param gameJsonVersion
	 * @param gameJsonPath
	 * @param gameZipUrl
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet setWindowsGameUpdateVersion(String gameJsonMd5, String gameJsonPath, String gameZipUrl)
			throws IOException;

	/**
	 * 同步windows游戏更新版本到redis
	 * 
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet syncWindowsGameUpdateVersion() throws IOException;

	/**
	 * 获取windows游戏版本信息
	 * 
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet getWindowsGameUpdateVersion() throws IOException;

	/**
	 * 添加粉丝团配置项
	 * 
	 * @author songfl
	 * @param month
	 * @param price
	 */
	public ResultDataSet addFansClubConfig(int month, float discount);

	/**
	 * 更新粉丝团配置项
	 * 
	 * @author songfl
	 * @param month
	 * @param price
	 */
	public ResultDataSet updateFansClubConfig(int month, float discount);

	/**
	 * 删除粉丝团配置项
	 * 
	 * @author songfl
	 * @param month
	 */
	public ResultDataSet deleteFansClubConfig(int month);

	/**
	 * 查询粉丝团配置项
	 * 
	 * @author songfl
	 * @param pageSize
	 * @param pageNum
	 */
	public ResultDataSet queryFansClubConfigs(int pageSize, int pageNum);

	/**
	 * 刷新粉丝团配置项
	 * 
	 * @author songfl
	 */
	public ResultDataSet freshFansClubsConfig();

	/**
	 * 刷新粉丝团配置项
	 * 
	 * @author wangsentao
	 * @param month
	 */
	public ResultDataSet getFansClubByMonth(int month);

	/**
	 * 添加赠送坐骑的配置
	 * 
	 * @author songfl
	 * @param type
	 * @param level
	 * @param mountId
	 * @param month
	 * @return
	 */
	public ResultDataSet addPresentMountConfig(int type, int level, int mountId, int month);

	/**
	 * 删除赠送坐骑的配置
	 * 
	 * @author songfl
	 * @param type
	 * @param level
	 * @return
	 */
	public ResultDataSet deletePresentMountConfig(int type, int level);

	/**
	 * 修改赠送坐骑的配置
	 * 
	 * @author songfl
	 * @param type
	 * @param level
	 * @param mountId
	 * @param month
	 * @return
	 */
	public ResultDataSet updatePresentMountConfig(int type, int level, int mountId, int month);

	/**
	 * 获取指定的赠送坐骑配置
	 * 
	 * @author songfl
	 * @param type
	 * @param level
	 * @return
	 */
	public ResultDataSet getPresentMountConfig(int type, int level);

	/**
	 * 查询赠送坐骑配置
	 * 
	 * @author songfl
	 * @param pageSize
	 * @param pageNum
	 */
	public ResultDataSet queryPresentMountConfig(int pageSize, int pageNum);

	/**
	 * 添加粉丝团任务配置
	 * 
	 * @author songfl
	 * @param taskId
	 * @param score
	 * @param description
	 */
	public ResultDataSet addClubTaskConfig(int taskId, long score, String description);

	/**
	 * 更新粉丝团任务配置项
	 * 
	 * @author songfl
	 * @param taskId
	 */
	public ResultDataSet updateClubTaskConfig(int taskId, long score, String description);

	/**
	 * 删除粉丝团任务配置项
	 * 
	 * @author songfl
	 * @param taskId
	 */
	public ResultDataSet deleteClubTaskConfig(int taskId);

	/**
	 * 刷新粉丝团配置项
	 * 
	 * @author songfl
	 * @param taskId
	 */
	public ResultDataSet getClubTask(int taskId);

	/**
	 * 查询粉丝团任务配置项
	 * 
	 * @author songfl
	 * @param pageSize
	 * @param pageNum
	 */
	public ResultDataSet queryClubTaskConfigs(int pageSize, int pageNum);

	/**
	 * 刷新粉丝团配置项
	 * 
	 * @author songfl
	 */
	public ResultDataSet freshClubTaskConfig();

	/**
	 * 更新游戏币兑换比率
	 * 
	 * @author songfl
	 * @param gameDiamondRate
	 * @param gameGoldRate
	 * @param lovelinessRate
	 */
	public ResultDataSet updateGameExchangeRate(String gameDiamondRate, String gameGoldRate, String lovelinessRate);

	/**
	 * 获取游戏币兑换比率
	 * 
	 * @author songfl
	 * @return
	 */
	public ResultDataSet queryGameExchangeRate();

	/**
	 * 设置app分享url
	 * 
	 * @author frank
	 * @param appShareUrl
	 * @return
	 */
	public ResultDataSet setAppShareUrl(String appShareUrl);

	/**
	 * 获取app分享url
	 * 
	 * @author frank
	 * @return
	 */
	public ResultDataSet getAppShareUrl();

	/**
	 * 添加首页频道类型
	 * 
	 * @author frank
	 * @param name
	 * @param value
	 * @return
	 */
	public ResultDataSet addHomeType(String name, int value, int sortId);

	/**
	 * 添加首页频道类型
	 * 
	 * @author frank
	 * @param name
	 * @param value
	 * @return
	 */
	public ResultDataSet deleteHomeType(String name);

	/**
	 * 获取首页频道类型
	 * 
	 * @author frank
	 * @return
	 */
	public ResultDataSet getHomeType();

	/**
	 * 添加平台配置
	 * 
	 * @author songfl
	 * @param key
	 * @param value
	 * @return
	 */
	public ResultDataSet addPlatformConfig(String key, String desc, String value);

	/**
	 * 清楚平台配置
	 * 
	 * @return
	 */
	public ResultDataSet clearPlatformConfig();

	/**
	 * 添加/更新平台配置
	 * 
	 * @author frank
	 * @param key
	 * @param value
	 * @return
	 */
	public ResultDataSet UpdatePlatformConfig(String key, String value);

	/**
	 * 列出平台配置
	 * 
	 * @author frank
	 * @return
	 */
	public ResultDataSet ListPlatformConfig();

	/**
	 * 获取商品列表
	 * 
	 * @author songfl
	 * @param device
	 *            {0:none，1:pc，2:web，3:ios, 4:android} {PC游戏:10, IOS游戏:11,
	 *            android:12}
	 * @return
	 */
	public ResultDataSet getProducts(int device);

	/**
	 * 添加/修改商品
	 * 
	 * @author songfl
	 * @param id
	 * @param iGold
	 *            虎币数x100，和数据库保持一致
	 * @param price
	 *            精确到分
	 * @param productId
	 * @param device
	 *            {0:none，1:pc，2:web，3:ios, 4:android} {PC游戏:10, IOS游戏:11,
	 *            android:12}
	 * @return
	 */
	public ResultDataSet addProducts(long id, long iGold, long price, String productId, int device);

	/**
	 * 删除商品
	 * 
	 * @author songfl
	 * @param id
	 */
	public ResultDataSet deleteProduct(long id);

	/**
	 * 添加音效
	 * 
	 * @author songfl
	 * @param superAccid
	 * @param name
	 * @param path
	 * @throws IOException
	 */
	ResultDataSet addSound(String name, String path) throws IOException;

	/**
	 * 删除音效
	 * 
	 * @author songfl
	 * @param guid
	 * @return
	 * @throws IOException
	 */
	ResultDataSet deleteSound(String guid) throws IOException;

	/**
	 * 查询音效列表
	 * 
	 * @author songfl
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws IOException
	 */
	ResultDataSet querySoundList(int pageSize, int pageNum) throws IOException;

	/**
	 * 获取唯一音效
	 * 
	 * @author songfl
	 * @param guid
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getSound(String guid) throws IOException;

	/**
	 * 设置视频推流设置
	 * 
	 * @author hl
	 * @param quality
	 * @return
	 * @throws IOException
	 */
	ResultDataSet setChannelPushConfig(int quality, int mode, int logoMark);

	/**
	 * 获取视频推流设置
	 * 
	 * @author hl
	 */
	ResultDataSet getChannelPushConfig();

	/**
	 * 设置windowslogin更新包下载地址
	 * 
	 * @param loginPath
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet setWindowsLoginAdvPath(String imagePath, String redirectPath) throws IOException;
	
	/**
	 * 添加游戏会员配置
	 * @author songfl 
	 * 
	 * */
	public ResultDataSet setGameVipConfig(int level, int month, long price);
	
	/**
	 * 删除游戏会员配置
	 * @author songfl 
	 * 
	 * */
	public ResultDataSet deleteGameVipConfig(int level, int month);
	
	/**
	 * 获取游戏会员配置
	 * @author songfl 
	 * 
	 * */
	public ResultDataSet getGameVipConfigs();
}
