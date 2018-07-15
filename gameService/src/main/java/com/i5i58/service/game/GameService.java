package com.i5i58.service.game;

import java.io.IOException;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.constants.ResponseData;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.game.IGame;
import com.i5i58.config.MyThreadPool;
import com.i5i58.config.SqlserverConfig;
import com.i5i58.data.account.Account;
import com.i5i58.data.account.Wallet;
import com.i5i58.data.config.AppVersionControl;
import com.i5i58.data.config.AppVersionStatus;
import com.i5i58.data.config.PlatformConfig;
import com.i5i58.data.game.ExChangeDiamondResponse;
import com.i5i58.data.game.ExChangeGameGoldResponse;
import com.i5i58.data.game.GameKindItem;
import com.i5i58.data.game.GameServerItem;
import com.i5i58.data.game.GameVipConfig;
import com.i5i58.data.game.LoveLinessExchangeRecord;
import com.i5i58.data.netBar.NetBarAccount;
import com.i5i58.data.record.MoneyFlow;
import com.i5i58.data.record.MoneyFlowType;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.WalletPriDao;
import com.i5i58.primary.dao.config.PlatformConfigPriDao;
import com.i5i58.primary.dao.game.LoveLinessExchangeRecordPriDao;
import com.i5i58.primary.dao.netBar.NetBarAccountPriDao;
import com.i5i58.primary.dao.record.MoneyFlowPriDao;
import com.i5i58.redis.all.HotGameKindItemDao;
import com.i5i58.redis.all.HotGameServerItemDao;
import com.i5i58.secondary.dao.account.WalletSecDao;
import com.i5i58.secondary.dao.config.AppVersionControlSecDao;
import com.i5i58.secondary.dao.game.GameKindItemSecDao;
import com.i5i58.secondary.dao.game.GameServerItemSecDao;
import com.i5i58.secondary.dao.game.GameVipConfigSecDao;
import com.i5i58.secondary.dao.game.LoveLinessExchangeRecordSecDao;
import com.i5i58.util.AccountUtils;
import com.i5i58.util.AppKeySecretPair;
import com.i5i58.util.ConfigUtils;
import com.i5i58.util.Constant;
import com.i5i58.util.DateUtils;
import com.i5i58.util.DeviceCode;
import com.i5i58.util.HelperFunctions;
import com.i5i58.util.ServerCode;
import com.i5i58.util.StringUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CodeToString;
import com.i5i58.yunxin.Utils.RefreshTokenResult;
import com.i5i58.yunxin.Utils.YXResultSet;

/**
 * 游戏服务
 * 
 * @author Administrator
 *
 */
@Service(protocol = "dubbo")
public class GameService implements IGame {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	AccountPriDao accountPriDao;

	@Autowired
	AccountUtils accountUtils;

	@Autowired
	PlatformConfigPriDao platformConfigDao;

	@Autowired
	WalletPriDao walletPriDao;

	@Autowired
	WalletSecDao walletSecDao;

	@Autowired
	MyThreadPool myThreadPool;

	@Autowired
	HotGameServerItemDao gameServerItemPriDao;

	@Autowired
	GameServerItemSecDao gameServerItemSecDao;

	@Autowired
	HotGameKindItemDao gameKindItemPriDao;

	@Autowired
	GameKindItemSecDao gameKindItemSecDao;

	@Autowired
	LoveLinessExchangeRecordPriDao loveLinessExchangeRecordPriDao;

	@Autowired
	LoveLinessExchangeRecordSecDao loveLinessExchangeRecordSecDao;

	@Autowired
	SqlserverConfig sqlserverConfig;

	@Autowired
	MoneyFlowPriDao moneyFlowPriDao;

	@Autowired
	AppVersionControlSecDao appVersionControlSecDao;

	@Autowired
	GameVipConfigSecDao gameVipConfigSecDao;

	@Autowired
	NetBarAccountPriDao netBarAccountPriDao;

	@Autowired
	ConfigUtils configUtils;

	@Override
	public ResultDataSet loginByOpenIdForGame(String openId, String password, String appKey, String clientIP,
			int device, String serialNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findByOpenIdAndPassword(openId, password);
		if (null != acc) {
			if (acc.isNullity()) {
				rds.setCode(ResultCode.AUTH.getCode());
				rds.setMsg("账号被禁用");
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
			// accountUtils.loadHotAccount(acc);
			acc.setPhoneNo(StringUtils.addMask(acc.getPhoneNo(), '*', 2, 2));
			acc.setEmailAddress(StringUtils.addEmailMask(acc.getEmailAddress(), '*', 2, 2));
			ResponseData response = new ResponseData();
			response.put("acc", acc);
			response.put("token", token);
			rds.setData(response);
			rds.setCode(ResultCode.SUCCESS.getCode());
		} else {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账户或密码错误，忘记密码请关注【胖虎娱乐】公众号找回密码！");
		}
		return rds;
	}

	@Override
	public ResultDataSet loginByPhoneNoForGame(String phoneNo, String password, String appKey, String clientIP,
			int device, String serialNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findByPhoneNoAndPassword(phoneNo, password);
		if (null != acc) {
			if (acc.isNullity()) {
				rds.setCode(ResultCode.AUTH.getCode());
				rds.setMsg("账号被禁用");
				return rds;
			}
			String token = accountUtils.getToken(acc.getAccId());
			if (token == null) {
				RefreshTokenResult refreshTokenResult = YunxinIM.refreshTokenAccount(acc.getAccId());
				if (refreshTokenResult == null) {
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					rds.setMsg("yx_exception:null");
					return rds;
				}
				if (!refreshTokenResult.getCode().equals("200")) {
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					rds.setMsg("yx_exception:" + CodeToString.getString(refreshTokenResult.getCode()) + " desc:"
							+ refreshTokenResult.getString("desc"));
					return rds;
				}
				System.out.println(refreshTokenResult.getInfo().getAccid());
				System.out.println(acc.getAccId());
				if (!refreshTokenResult.getInfo().getAccid().equals(acc.getAccId())) {
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					rds.setMsg("yx_exception:accid dif");
					return rds;
				}
				token = refreshTokenResult.getInfo().getToken();
				accountUtils.setToken(acc.getAccId(), token);
			}
			// accountUtils.loadHotAccount(acc);
			acc.setPhoneNo(StringUtils.addMask(acc.getPhoneNo(), '*', 2, 2));
			acc.setEmailAddress(StringUtils.addEmailMask(acc.getEmailAddress(), '*', 2, 2));
			ResponseData response = new ResponseData();
			response.put("acc", acc);
			response.put("token", token);
			rds.setData(response);
			rds.setCode(ResultCode.SUCCESS.getCode());
		} else {
			Account existAcc = accountPriDao.findByPhoneNo(phoneNo);
			if (existAcc == null) {
				List<Account> bindAcc = accountPriDao.findByBindMobile(phoneNo);
				if (bindAcc != null && bindAcc.size() > 0) {
					rds.setCode(ResultCode.PARAM_INVALID.getCode());
					rds.setMsg("账户不存在，使用微信、QQ、微博登陆【胖虎直播】的玩家请使用直播ID登陆游戏！");
					return rds;
				}
			}
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账户或密码错误，忘记密码请关注【胖虎娱乐】公众号找回密码！");
		}
		return rds;
	}

	@Override
	public ResultDataSet loginByTokenForGame(String accId, String appKey, String clientIP, int device, String serialNum)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(accId);
		if (null != acc) {
			if (acc.isNullity()) {
				rds.setCode(ResultCode.AUTH.getCode());
				rds.setMsg("账号被禁用");
				return rds;
			}
			// accountUtils.loadHotAccount(acc);
			acc.setPhoneNo(StringUtils.addMask(acc.getPhoneNo(), '*', 2, 2));
			acc.setEmailAddress(StringUtils.addEmailMask(acc.getEmailAddress(), '*', 2, 2));
			rds.setData(acc);
			rds.setCode(ResultCode.SUCCESS.getCode());
		} else {
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
		}
		return rds;
	}

	@Override
	public ResultDataSet exchangeGameGold(String accId, String appKey, long iGold, long gameGold, String clientIP,
			int device, String serialNum, String insurePass) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		GameDataHelper dbHelper = new GameDataHelper(sqlserverConfig);
		Wallet wallet = walletPriDao.findOne(accId);
		if (wallet == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_WALLET.getCode());
			return rds;
		}
		long orgIGold = wallet.getiGold();
		long costIGold = iGold * 100;
		if (orgIGold < costIGold) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.IGOLD_NOT_ENOUGH.getCode());
			return rds;
		}
		PlatformConfig platformConfig = platformConfigDao.findOne(Constant.SQL_PLATFORM_CONFIG_GAME_GOLD_EXCHARGE_RATE);
		if (platformConfig == null) {
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("数据异常：x000657456,没有配置");
		} else {
			String rateStr = platformConfig.getcValue();
			if (StringUtils.StringIsEmptyOrNull(rateStr)) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("数据异常：x000657456,配置值为空");
			} else {
				try {
					ExChangeGameGoldResponse response = new ExChangeGameGoldResponse();
					response.setAccId(accId);
					response.setOrgIGold(orgIGold);
					response.setOrgGameGold(gameGold);
					response.setExIGold(costIGold);

					long dateTime = DateUtils.getNowTime();
					MoneyFlow moneyFlow = new MoneyFlow();
					moneyFlow.setAccId(accId);
					moneyFlow.setDateTime(dateTime);
					moneyFlow.setIpAddress(clientIP);
					HelperFunctions.setMoneyFlowType(MoneyFlowType.ExchangeGameGold, moneyFlow);
					HelperFunctions.setMoneyFlowSource(wallet, moneyFlow);

					long rate = Long.parseLong(rateStr);
					long exChargeGameGold = iGold * rate;
					wallet.setiGold(wallet.getiGold() - costIGold);
					walletPriDao.save(wallet);

					HelperFunctions.setMoneyFlowTarget(wallet, moneyFlow);
					moneyFlowPriDao.save(moneyFlow);

					SQLResultData sd = dbHelper.exchangeGameGold(accId, exChargeGameGold, iGold, Byte.parseByte("0"),
							clientIP, serialNum, "T币兑换游戏币", insurePass);
					if (sd.getReutrnCode() != 0) {
						rds.setCode(ResultCode.SERVICE_ERROR.getCode());
						rds.setMsg(sd.getDescribe());
						return rds;
					}
					response.setRate(rate);
					response.setCurrGameGold(gameGold + exChargeGameGold);
					response.setExTime(dateTime);
					YxMsgExchangeGameGoldThread yxExchangeGameGoldThread = new YxMsgExchangeGameGoldThread(
							response.getAccId(), response);
					myThreadPool.getYunxinPool().execute(yxExchangeGameGoldThread);
					rds.setCode(ResultCode.SUCCESS.getCode());
					rds.setData(response);
				} catch (NumberFormatException e) {
					logger.error("", e);
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					rds.setMsg("数据异常：x000657456");
				}
			}
		}
		return rds;
	}

	@Override
	public ResultDataSet exchangeDiamond(String accId, String appKey, long diamond, long gameGold, String clientIP,
			int device, String serialNum, String insurePass) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		GameDataHelper dbHelper = new GameDataHelper(sqlserverConfig);

		Wallet wallet = walletPriDao.findOne(accId);
		if (wallet == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_WALLET.getCode());
			return rds;
		}
		PlatformConfig platformConfig = platformConfigDao.findOne(Constant.SQL_PLATFORM_CONFIG_DIAMOND_EXCHARGE_RATE);
		if (platformConfig == null) {
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("数据异常：x000657456,没有配置");
		} else {
			String rateStr = platformConfig.getcValue();
			if (StringUtils.StringIsEmptyOrNull(rateStr)) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("数据异常：x000657456,配置值为空");
			} else {
				try {
					long rate = Long.parseLong(rateStr);
					long exChargeGameGold = diamond * rate;
					if (exChargeGameGold > gameGold) {
						rds.setCode(ResultCode.PARAM_INVALID.getCode());
						rds.setMsg("游戏币不足");
						return rds;
					}

					SQLResultData sd = dbHelper.exchangeGameGold(accId, exChargeGameGold, diamond, Byte.parseByte("1"),
							clientIP, serialNum, "游戏币兑换钻石", insurePass);
					if (sd.getReutrnCode() != 0) {
						rds.setCode(ResultCode.SERVICE_ERROR.getCode());
						rds.setMsg(sd.getDescribe());
						return rds;
					}
					ExChangeDiamondResponse response = new ExChangeDiamondResponse();
					response.setAccId(accId);
					response.setOrgDiamond(wallet.getRedDiamond());
					response.setOrgGameGold(gameGold);
					response.setExDiamond(diamond);

					long dateTime = DateUtils.getNowTime();
					MoneyFlow moneyFlow = new MoneyFlow();
					moneyFlow.setAccId(accId);
					moneyFlow.setDateTime(dateTime);
					HelperFunctions.setMoneyFlowType(MoneyFlowType.ExchangeDiamond, moneyFlow);
					HelperFunctions.setMoneyFlowSource(wallet, moneyFlow);

					// wallet.setRedDiamond(wallet.getRedDiamond() + diamond);
					wallet.setDiamond(wallet.getDiamond() + diamond);
					walletPriDao.save(wallet);

					HelperFunctions.setMoneyFlowTarget(wallet, moneyFlow);
					moneyFlowPriDao.save(moneyFlow);

					response.setRate(rate);
					response.setCurrGameGold(gameGold - exChargeGameGold);
					response.setExTime(dateTime);
					YxMsgExchangeDiamondThread yxChatGiftThread = new YxMsgExchangeDiamondThread(response.getAccId(),
							response);
					myThreadPool.getYunxinPool().execute(yxChatGiftThread);
					rds.setCode(ResultCode.SUCCESS.getCode());
					rds.setData(response);

				} catch (NumberFormatException e) {
					logger.error("", e);
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					rds.setMsg("数据异常：x000657456");
				}
			}
		}
		return rds;
	}

	@Override
	public ResultDataSet getIGold(String accId, String appKey) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Wallet wallet = walletSecDao.findOne(accId);
		if (wallet == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_WALLET.getCode());
			return rds;
		}
		rds.setData(wallet);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getIGoldByOpenId(String openId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account account = accountPriDao.findByOpenId(openId);
		if (account == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		Wallet wallet = walletSecDao.findOne(account.getAccId());
		if (wallet == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_WALLET.getCode());
			return rds;
		}
		rds.setData(wallet);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet PlayerAction(String accId, String appKey, byte query, String text, String action, String ext)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultDataSet getGameList(String appKey) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Iterable<GameKindItem> gameKindItem = gameKindItemSecDao.findAll();
		Iterable<GameServerItem> gameServerItem = gameServerItemSecDao.findAll();
		HashMap<String, Object> response = new HashMap<String, Object>();
		response.put("kind", gameKindItem);
		response.put("room", gameServerItem);
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet UpdateGameItem(String appKey, String secret, int serverId, int kindId, int nodeId, int sortId,
			int serverKind, int serverType, int serverLevel, int serverPort, long cellScore, long enterScore,
			int serverRule, int lineCount, int androidCount, int fullCount, String serverAddr, String serverName)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		if (!AppKeySecretPair.GameServer.match(appKey, secret)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("appKey不匹配");
			return rds;
		}
		GameServerItem item = gameServerItemPriDao.findOne(serverId);
		if (item != null) {
			item.setServerId(serverId);
			item.setKindId(kindId);
			item.setNodeId(nodeId);
			item.setSortId(sortId);
			item.setServerKind(serverKind);
			item.setServerType(serverType);
			item.setServerLevel(serverLevel);
			item.setServerPort(serverPort);
			item.setCellScore(cellScore);
			item.setEnterScore(enterScore);
			item.setServerRule(serverRule);
			item.setLineCount(lineCount);
			item.setAndroidCount(androidCount);
			item.setFullCount(fullCount);
			item.setServerAddr(serverAddr);
			item.setServerName(serverName);
			gameServerItemPriDao.save(item);
		} else {
			GameServerItem gameServerItem = new GameServerItem();
			gameServerItem.setServerId(serverId);
			gameServerItem.setKindId(kindId);
			gameServerItem.setNodeId(nodeId);
			gameServerItem.setSortId(sortId);
			gameServerItem.setServerKind(serverKind);
			gameServerItem.setServerType(serverType);
			gameServerItem.setServerLevel(serverLevel);
			gameServerItem.setServerPort(serverPort);
			gameServerItem.setCellScore(cellScore);
			gameServerItem.setEnterScore(enterScore);
			gameServerItem.setServerRule(serverRule);
			gameServerItem.setLineCount(lineCount);
			gameServerItem.setAndroidCount(androidCount);
			gameServerItem.setFullCount(fullCount);
			gameServerItem.setServerAddr(serverAddr);
			gameServerItem.setServerName(serverName);
			gameServerItemPriDao.save(gameServerItem);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet deleteGameItem(String appKey, String secret, int serverId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		if (!AppKeySecretPair.GameServer.match(appKey, secret)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("appKey不匹配");
			return rds;
		}
		StringBuilder builder = new StringBuilder();
		builder.append("appKey ").append(appKey).append(", secret ").append(secret)
				.append(", delete game item, serverId ").append(serverId);
		logger.info(builder.toString());
		GameServerItem item = gameServerItemPriDao.findOne(serverId);
		if (item != null) {
			gameServerItemPriDao.delete(item);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet updateGameLineCount(String appKey, String secret, int serverId, int lineCount)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		if (!AppKeySecretPair.GameServer.match(appKey, secret)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("appKey不匹配");
			return rds;
		}
		GameServerItem item = gameServerItemPriDao.findOne(serverId);
		if (item != null) {
			item.setLineCount(lineCount);
			gameServerItemPriDao.save(item);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet UpdateGameKindItem(String appKey, String secret, int kindId, int gameId, int typeId,
			int joinId, int sortId, String kindName, String processName, boolean supportMobile, String gameRuleUrl,
			String downLoadUrl) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		if (!AppKeySecretPair.GameTool.match(appKey, secret)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("appKey不匹配");
			return rds;
		}
		GameKindItem item = gameKindItemPriDao.findOne(kindId);
		if (item != null) {
			item.setKindId(kindId);
			item.setGameId(gameId);
			item.setTypeId(typeId);
			item.setJoinId(joinId);
			item.setSortId(sortId);
			item.setKindName(kindName);
			item.setProcessName(processName);
			item.setSupportMobile(supportMobile);
			item.setGameRuleUrl(gameRuleUrl);
			item.setDownLoadUrl(downLoadUrl);
			gameKindItemPriDao.save(item);
		} else {
			GameKindItem gameKindItem = new GameKindItem();
			gameKindItem.setKindId(kindId);
			gameKindItem.setGameId(gameId);
			gameKindItem.setTypeId(typeId);
			gameKindItem.setJoinId(joinId);
			gameKindItem.setSortId(sortId);
			gameKindItem.setKindName(kindName);
			gameKindItem.setProcessName(processName);
			gameKindItem.setSupportMobile(supportMobile);
			gameKindItem.setGameRuleUrl(gameRuleUrl);
			gameKindItem.setDownLoadUrl(downLoadUrl);
			gameKindItemPriDao.save(gameKindItem);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet deleteAllGameKind(String appKey, String secret) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		if (!AppKeySecretPair.GameTool.match(appKey, secret)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("appKey不匹配");
			return rds;
		}
		gameKindItemPriDao.deleteAll();
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getBank(String accId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			HashMap<String, Object> response = new HashMap<String, Object>();
			GameDataHelper dbHelper = new GameDataHelper(sqlserverConfig);
			ResultSet ret = dbHelper.getBank(accId);
			if (ret.next()) {
				response.put("score", ret.getLong(2));
				response.put("insureScore", ret.getLong(4));
				response.put("LoveLiness", ret.getLong(5));
				response.put("LoveLinessPresent", ret.getLong(6));
			} else {
				response.put("score", 0L);
				response.put("insureScore", 0L);
				response.put("LoveLiness", 0L);
				response.put("LoveLinessPresent", 0L);
			}
			rds.setData(response);
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (Exception e) {
			logger.error("", e);
			rds.setMsg("0x2353_exception");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
	}

	@Override
	public ResultDataSet UserEnableInsure(String accId, String strPassword, String strInsurePass, String clientIP,
			String MachineID, Integer GameID, Short Gender, String MobilePhone) {
		ResultDataSet rds = new ResultDataSet();
		try {
			GameDataHelper dbHelper = new GameDataHelper(sqlserverConfig);
			SQLResultData ret = dbHelper.UserEnableInsure(accId, strPassword, strInsurePass, clientIP, MachineID,
					GameID, Gender, MobilePhone);
			if (ret != null) {
				System.out.println("not null");
				if (ret.getReutrnCode() == 0) {
					rds.setCode(ResultCode.SUCCESS.getCode());
					rds.setMsg(ret.getDescribe());
					return rds;
				}
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg(ret.getDescribe());
				return rds;
			}
		} catch (Exception e) {
			logger.error("", e);
			rds.setMsg("0x2353_exception");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		return rds;
	}

	@Override
	public ResultDataSet isBankEnabled(String accId) {
		ResultDataSet rds = new ResultDataSet();
		GameDataHelper dbHelper = new GameDataHelper(sqlserverConfig);
		try {
			boolean isEnabled = false;
			String insurePass = dbHelper.getBankPassword(accId);
			if (insurePass == null || insurePass.isEmpty()) {
				isEnabled = false;
			} else {
				isEnabled = true;
			}
			ResponseData rp = new ResponseData();
			rp.put("isEnabled", isEnabled);
			rds.setData(rp);
			rds.setCode(ResultCode.SUCCESS.getCode());
		} catch (Exception e) {
			logger.error("", e);
			rds.setMsg("数据查询失败");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
		}
		return rds;
	}

	@Override
	public ResultDataSet checkBankPass(String accId, String bankPass) {
		ResultDataSet rds = new ResultDataSet();
		GameDataHelper dbHelper = new GameDataHelper(sqlserverConfig);
		try {
			boolean checked = false;
			String insurePass = dbHelper.getBankPassword(accId);
			if (insurePass != null && bankPass.equals(insurePass)) {
				checked = true;
			} else {
				checked = false;
			}
			ResponseData rp = new ResponseData();
			rp.put("checked", checked);
			rds.setData(rp);
			rds.setCode(ResultCode.SUCCESS.getCode());
		} catch (Exception e) {
			logger.error("", e);
			rds.setMsg("数据查询失败");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
		}
		return rds;
	}

	@Override
	public ResultDataSet SaveScore(String accId, long ExchangeScore, String clientIP, String MachineID) {
		ResultDataSet rds = new ResultDataSet();
		try {
			GameDataHelper dbHelper = new GameDataHelper(sqlserverConfig);
			SQLResultData ret = dbHelper.SaveScore(accId, ExchangeScore, clientIP, MachineID);
			if (ret != null) {
				System.out.println("not null");
				if (ret.getReutrnCode() == 0) {
					rds.setCode(ResultCode.SUCCESS.getCode());
					rds.setMsg(ret.getDescribe());
					return rds;
				}
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg(ret.getDescribe());
				return rds;
			}
		} catch (Exception e) {
			logger.error("", e);
			rds.setMsg("0x2353_exception");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		return rds;
	}

	@Override
	public ResultDataSet TakeScore(String accId, String strPassword, long ExchangeScore, String clientIP,
			String MachineID) {
		ResultDataSet rds = new ResultDataSet();

		try {
			GameDataHelper dbHelper = new GameDataHelper(sqlserverConfig);
			SQLResultData ret = dbHelper.TakeScore(accId, strPassword, ExchangeScore, clientIP, MachineID);

			if (ret != null) {
				if (ret.getReutrnCode() == 0) {
					rds.setCode(ResultCode.SUCCESS.getCode());
					rds.setMsg(ret.getDescribe());
					return rds;
				}
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg(ret.getDescribe());
				return rds;
			}
		} catch (Exception e) {
			logger.error("", e);
			rds.setMsg("0x2353_exception");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		return rds;
	}

	@Override
	public ResultDataSet ModifyInsurePassword(String accId, String strPassword, String strNewPassword,
			String clientIp) {
		ResultDataSet rds = new ResultDataSet();
		try {
			GameDataHelper dbHelper = new GameDataHelper(sqlserverConfig);
			SQLResultData ret = dbHelper.ModifyInsurePassword(accId, strPassword, strNewPassword, clientIp);

			if (ret.getReutrnCode() == 0) {
				rds.setCode(ResultCode.SUCCESS.getCode());
				rds.setMsg(ret.getDescribe());
				return rds;
			}
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ret.getDescribe());
			return rds;

		} catch (Exception e) {
			logger.error("", e);
			rds.setMsg("0x2353_exception");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
	}

	@Override
	public ResultDataSet resetInsurePassword(String accId, String verifyCode, String password, String clientIp) {
		ResultDataSet rds = new ResultDataSet();
		try {
			Account account = accountPriDao.findOne(accId);
			if (account == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setData(ServerCode.NO_ACCOUNT.getCode());
				return rds;
			}
			String phoneNo = account.getBindMobile();
			if (StringUtils.StringIsEmptyOrNull(phoneNo)) {
				rds.setMsg("请先到胖虎直播绑定手机号");
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				return rds;
			}

			YXResultSet rs = YunxinIM.verifySmscode(phoneNo, verifyCode);
			if (!rs.getCode().equals("200")) {
				System.out.println(rs.getError());
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("验证码错误");
				return rds;
			}
			password = StringUtils.getMd5(password);
			GameDataHelper dbHelper = new GameDataHelper(sqlserverConfig);
			SQLResultData ret = dbHelper.ResetInsurePassword(accId, password, clientIp);

			if (ret.getReutrnCode() == 0) {
				rds.setCode(ResultCode.SUCCESS.getCode());
				rds.setMsg(ret.getDescribe());
				return rds;
			}
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ret.getDescribe());
			return rds;

		} catch (Exception e) {
			logger.error("", e);
			rds.setMsg("0x2353_exception");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
	}

	@Override
	public ResultDataSet sendSMSForResetInsurePassword(String accId) {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(accId);
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		String bindMobile = acc.getBindMobile();
		if (StringUtils.StringIsEmptyOrNull(bindMobile)) {
			rds.setMsg("请先到胖虎直播绑定手机号");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}

		YXResultSet rs;
		try {
			rs = YunxinIM.sendSmsCode(bindMobile, bindMobile, "3056623");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("验证码发送失败");
			return rds;
		}
		if (!rs.getCode().equals("200")) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(rs.getError());
			return rds;
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getBankRecord(String accId, String startTime, String endTime, int pageSize, int pageIndex) {
		ResultDataSet rds = new ResultDataSet();
		try {
			GameDataHelper dbHelper = new GameDataHelper(sqlserverConfig);
			ResultSet rs = dbHelper.getBankRecord(accId, startTime, endTime, pageSize, pageIndex);
			if (rs == null) {
				rds.setMsg("数据库查询错误");
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				return rds;
			}

			List<HashMap<String, String>> columns = new ArrayList<HashMap<String, String>>();
			while (rs.next()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("RecordID", rs.getString("RecordID"));
				map.put("SourceUserID", rs.getString("SourceUserID"));
				map.put("SourceNickName", rs.getString("SourceNickName"));
				map.put("SourceGold", rs.getString("SourceGold"));
				map.put("SourceBank", rs.getString("SourceBank"));
				map.put("TargetUserID", rs.getString("TargetUserID"));
				map.put("TargetNickName", rs.getString("TargetNickName"));
				map.put("TargetGold", rs.getString("TargetGold"));
				map.put("TargetBank", rs.getString("TargetBank"));
				map.put("SwapScore", rs.getString("SwapScore"));
				map.put("Revenue", rs.getString("Revenue"));
				map.put("TradeType", rs.getString("TradeType"));
				map.put("CollectDate", rs.getString("CollectDate"));
				columns.add(map);
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
			rds.setData(columns);
			return rds;
		} catch (Exception e) {
			logger.error("", e);
			rds.setMsg("0x2353_exception");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
	}

	@Override
	public ResultDataSet getCurExchangedLoveLiness(String accId) {
		ResultDataSet rds = new ResultDataSet();
		long date;
		try {
			date = DateUtils.getNowDate();
		} catch (ParseException e) {
			logger.error(e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("date parse error");
			return rds;
		}
		LoveLinessExchangeRecord record = loveLinessExchangeRecordSecDao.findByAccIdAndDate(accId, date);
		rds.setData(record);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet exchagneLiveliness(String accId, long lovelinessExchanged, String clientIp) {
		ResultDataSet rds = new ResultDataSet();
		long convertRate = 1;
		PlatformConfig platformConfig = platformConfigDao
				.findOne(Constant.SQL_PLATFORM_CONFIG_LOVELINESS_EXCHARGE_RATE);
		if (platformConfig != null) {
			String cvalue = platformConfig.getcValue();
			if (!StringUtils.StringIsEmptyOrNull(cvalue)) {
				try {
					convertRate = Long.parseLong(cvalue);
				} catch (NumberFormatException e) {
					logger.error("", e);
					convertRate = 1;
				}
			}
		}

		int dailyLimit = 10000;
		PlatformConfig dailyLimitConfig = platformConfigDao
				.findOne(Constant.SQL_PLATFORM_CONFIG_LOVELINESS_EXCHARGE_DAILYLIMIT);
		if (dailyLimitConfig != null) {
			String cvalue = dailyLimitConfig.getcValue();
			if (!StringUtils.StringIsEmptyOrNull(cvalue)) {
				dailyLimit = Integer.parseInt(cvalue);
			}
		}

		long date;
		try {
			date = DateUtils.getNowDate();
		} catch (ParseException e) {
			logger.error(e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("date parse error");
			return rds;
		}
		LoveLinessExchangeRecord record = loveLinessExchangeRecordPriDao.findByAccIdAndDate(accId, date);
		if (record != null) {
			if (record.getExchanged() >= dailyLimit) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("已达到当日最多兑换上限");
				return rds;
			}
		} else {
			record = new LoveLinessExchangeRecord();
			record.setAccId(accId);
			record.setDate(date);
			record.setExchanged(0);
		}
		if (record.getExchanged() + lovelinessExchanged > dailyLimit) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("当前数额超过剩余可兑换数");
			return rds;
		}

		GameDataHelper dbHelper = new GameDataHelper(sqlserverConfig);
		SQLResultData sqlResultData = dbHelper.exchangeLoveliness(accId, lovelinessExchanged, clientIp, convertRate);
		if (sqlResultData.getReutrnCode() == 0) {
			record.setExchanged(record.getExchanged() + lovelinessExchanged);
			loveLinessExchangeRecordPriDao.save(record);
			rds.setCode(ResultCode.SUCCESS.getCode());
		} else {
			rds.setMsg(sqlResultData.getDescribe());
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
		}

		return rds;
	}

	@Override
	public ResultDataSet queryGameExchangeRate() {
		ResultDataSet rds = new ResultDataSet();
		int diamondRate, gameGoldRate, lovelinessRate;
		diamondRate = gameGoldRate = lovelinessRate = 0;

		PlatformConfig config = platformConfigDao.findOne(Constant.SQL_PLATFORM_CONFIG_DIAMOND_EXCHARGE_RATE);
		if (config != null) {
			diamondRate = Integer.parseInt(config.getcValue());
		}
		config = platformConfigDao.findOne(Constant.SQL_PLATFORM_CONFIG_GAME_GOLD_EXCHARGE_RATE);
		if (config != null) {
			gameGoldRate = Integer.parseInt(config.getcValue());
		}
		config = platformConfigDao.findOne(Constant.SQL_PLATFORM_CONFIG_LOVELINESS_EXCHARGE_RATE);
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
	public ResultDataSet modifyGameNickName(String accId, String nickName, String clientIp) {
		ResultDataSet rds = new ResultDataSet();

		GameDataHelper dbHelper = new GameDataHelper(sqlserverConfig);
		SQLResultData sqlResultData = dbHelper.modifyUserNickName(accId, nickName, clientIp);
		if (sqlResultData.getReutrnCode() == 0) {
			rds.setCode(ResultCode.SUCCESS.getCode());
		} else {
			rds.setMsg(sqlResultData.getDescribe());
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
		}
		return rds;
	}

	@Override
	public ResultDataSet getGameNickName(String accId) {
		ResultDataSet rds = new ResultDataSet();
		GameDataHelper dbHelper = new GameDataHelper(sqlserverConfig);
		String nickName = dbHelper.getGameNick(accId);
		ResponseData rp = new ResponseData();
		rp.put("nickName", nickName);
		rds.setData(rp);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getRankList(String accId, int rankFirst, int rankLast) {
		ResultDataSet rds = new ResultDataSet();
		GameDataHelper dbHelper = new GameDataHelper(sqlserverConfig);
		ResponseData rp = dbHelper.getRankList(accId, rankFirst, rankLast);
		rds.setData(rp);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getGameVersion(int device) {
		ResultDataSet rds = new ResultDataSet();
		ResponseData rp = new ResponseData();
		AppVersionControl appVersionControl = appVersionControlSecDao
				.findByStatusAndDevice(AppVersionStatus.CURRENT.getValue(), device);

		String version = "";
		String downloadUrl = "";
		if (appVersionControl != null) {
			version = appVersionControl.getMainVserion() + "." + appVersionControl.getSubVersion() + "."
					+ appVersionControl.getFuncVersion();
			downloadUrl = appVersionControl.getUpdateUrl();
		}

		rp.put("version", version);
		rp.put("downloadUrl", downloadUrl);
		rds.setData(rp);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getPayOption(int device) {
		ResultDataSet rds = new ResultDataSet();
		ResponseData rp = new ResponseData();
		int payOption = 0;
		if (DeviceCode.IOSGame == device || DeviceCode.IOSGame_V2 == device) {
			PlatformConfig payOptionConfig = platformConfigDao.findOne(Constant.IOS_Game_Pay_Option);
			String value = "";
			if (payOptionConfig != null) {
				value = payOptionConfig.getcValue();
				payOption = Integer.parseInt(value);
			}
		}
		rp.put("payOption", payOption);
		rds.setData(rp);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getGameOption(int device) {
		ResultDataSet rds = new ResultDataSet();
		ResponseData rp = new ResponseData();
		String PHLiveLogon = "false";
		String DiamondExch = "false";
		if (DeviceCode.IOSGame == device || DeviceCode.IOSGame_V2 == device) {
			PlatformConfig PHLiveLogonConfig = platformConfigDao.findOne(Constant.IOS_Game_PHLive_Logon);
			if (PHLiveLogonConfig != null) {
				PHLiveLogon = PHLiveLogonConfig.getcValue();
			}
			PlatformConfig DiamondExchConfig = platformConfigDao.findOne(Constant.IOS_Game_DiamondExchanged_Enable);
			if (DiamondExchConfig != null) {
				DiamondExch = DiamondExchConfig.getcValue();
			}
		}
		rp.put("PHLiveLogon", PHLiveLogon);
		rp.put("DiamondExch", DiamondExch);
		rds.setData(rp);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet commissionExchangeGameGold(String accId, long commission, String clientIp) {
		ResultDataSet rds = new ResultDataSet();
		if (commission < 0) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("兑换值不能为负");
			return rds;
		}
		long gameGold = commission * 200;
		Wallet wallet = walletPriDao.findByAccId(accId);
		if (wallet.getCommission() < commission) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("您的俸禄不够");
			return rds;
		}
		GameDataHelper dbHelper = new GameDataHelper(sqlserverConfig);
		SQLResultData res = dbHelper.commissionExchangeGameGold(accId, commission, gameGold, clientIp);
		if (res.getReutrnCode() != 0) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("兑换欢乐豆失败");
			return rds;
		}
		long dateTime = DateUtils.getNowTime();
		MoneyFlow moneyFlow = new MoneyFlow();
		moneyFlow.setAccId(accId);
		moneyFlow.setDateTime(dateTime);
		moneyFlow.setIpAddress(clientIp);
		HelperFunctions.setMoneyFlowType(MoneyFlowType.CommissionToGameGold, moneyFlow);
		HelperFunctions.setMoneyFlowSource(wallet, moneyFlow);

		wallet.setCommission(wallet.getCommission() - commission);
		walletPriDao.save(wallet);

		HelperFunctions.setMoneyFlowTarget(wallet, moneyFlow);
		moneyFlowPriDao.save(moneyFlow);

		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet TransferScore(String accId, long TransferScore, String InsurePass, String TargetAccId,
			String TransRemark, String clientIP, String MachineID) {
		// TODO Auto-generated method stub
		ResultDataSet rds = new ResultDataSet();

		try {
			GameDataHelper dbHelper = new GameDataHelper(sqlserverConfig);
			SQLResultData ret = dbHelper.TransferScore(accId, TransferScore, InsurePass, TargetAccId, TransRemark,
					clientIP, MachineID);

			if (ret != null) {
				if (ret.getReutrnCode() == 0) {
					rds.setCode(ResultCode.SUCCESS.getCode());
					rds.setData(ret.getDescribe());
					return rds;
				}
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg(ret.getDescribe());
				return rds;
			}
		} catch (Exception e) {
			logger.error("", e);
			rds.setMsg("0x2353_exception");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		return rds;
	}

	@Override
	public ResultDataSet getBankRecordByTradeType(String accId, String startTime, String endTime, int pageSize,
			int pageIndex) {
		ResultDataSet rds = new ResultDataSet();
		try {
			GameDataHelper dbHelper = new GameDataHelper(sqlserverConfig);
			ResultSet rs = dbHelper.getBankRecordByTradeType(accId, startTime, endTime, pageSize, pageIndex);
			if (rs == null) {
				rds.setMsg("数据库查询错误");
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				return rds;
			}

			List<HashMap<String, String>> columns = new ArrayList<HashMap<String, String>>();
			while (rs.next()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("RecordID", rs.getString("RecordID"));
				map.put("SourceUserID", rs.getString("SourceUserID"));
				map.put("SourceNickName", rs.getString("SourceNickName"));
				map.put("SourceGold", rs.getString("SourceGold"));
				map.put("SourceBank", rs.getString("SourceBank"));
				map.put("TargetUserID", rs.getString("TargetUserID"));
				map.put("TargetNickName", rs.getString("TargetNickName"));
				map.put("TargetGold", rs.getString("TargetGold"));
				map.put("TargetBank", rs.getString("TargetBank"));
				map.put("SwapScore", rs.getString("SwapScore"));
				map.put("Revenue", rs.getString("Revenue"));
				map.put("TradeType", rs.getString("TradeType"));
				map.put("CollectDate", rs.getString("CollectDate"));
				columns.add(map);
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
			rds.setData(columns);
			return rds;
		} catch (Exception e) {
			logger.error("", e);
			rds.setMsg("0x2353_exception");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
	}

	@Override
	public ResultDataSet getGameInfo(String gameId) {
		ResultDataSet rds = new ResultDataSet();
		GameDataHelper dbHelper = new GameDataHelper(sqlserverConfig);
		Map<String, Object> res = dbHelper.getGameInfoByGameId(gameId);
		if (res.size() == 0) {
			rds.setMsg("数据库查询错误");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		ResponseData rp = new ResponseData();
		rp.putAll(res);
		rds.setData(rp);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet exchangeGameGoldForWeb(String accId, String appKey, long iGold, long gameGold, String clientIP,
			int device, String serialNum, String insurePass) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		GameDataHelper dbHelper = new GameDataHelper(sqlserverConfig);
		Wallet wallet = walletPriDao.findOne(accId);
		if (wallet == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_WALLET.getCode());
			return rds;
		}
		long orgIGold = wallet.getiGold();
		long costIGold = iGold;
		if (orgIGold < costIGold) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.IGOLD_NOT_ENOUGH.getCode());
			return rds;
		}
		PlatformConfig platformConfig = platformConfigDao.findOne(Constant.SQL_PLATFORM_CONFIG_GAME_GOLD_EXCHARGE_RATE);
		if (platformConfig == null) {
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("数据异常：SQL_PLATFORM_CONFIG_GAME_GOLD_EXCHARGE_RATE,未配置");
			return rds;
		}
		String rateStr = platformConfig.getcValue();
		if (StringUtils.StringIsEmptyOrNull(rateStr)) {
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("数据异常：SQL_PLATFORM_CONFIG_GAME_GOLD_EXCHARGE_RATE,配置值为空");
			return rds;
		}
		try {
			ExChangeGameGoldResponse response = new ExChangeGameGoldResponse();
			response.setAccId(accId);
			response.setOrgIGold(orgIGold);
			response.setOrgGameGold(gameGold);
			response.setExIGold(costIGold);

			long dateTime = DateUtils.getNowTime();
			MoneyFlow moneyFlow = new MoneyFlow();
			moneyFlow.setAccId(accId);
			moneyFlow.setDateTime(dateTime);
			moneyFlow.setIpAddress(clientIP);
			HelperFunctions.setMoneyFlowType(MoneyFlowType.ExchangeGameGold, moneyFlow);
			HelperFunctions.setMoneyFlowSource(wallet, moneyFlow);

			long rate = Long.parseLong(rateStr);
			long exChargeGameGold = iGold * rate / 100;
			logger.error(String.format("exchangeGameGoldForWeb:{clientIP:[%s], accId:[%s]}", clientIP, accId));
			NetBarAccount nba = netBarAccountPriDao.findByNetIpAndOwnerIdAndNullity(clientIP, accId, false);
			if (nba != null) {
				PlatformConfig netBarRateConfig = platformConfigDao.findOne(Constant.NET_BAR_EXCHANGE_GAME_SCORE_RATE);
				if (netBarRateConfig == null) {
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					rds.setMsg("数据异常：NET_BAR_EXCHANGE_GAME_SCORE_RATE,未配置");
					return rds;
				}
				String netBarRateStr = netBarRateConfig.getcValue();
				if (StringUtils.StringIsEmptyOrNull(netBarRateStr)) {
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					rds.setMsg("数据异常：NET_BAR_EXCHANGE_GAME_SCORE_RATE,配置值为空");
					return rds;
				}
				long netBarRate = Long.parseLong(netBarRateStr);
				exChargeGameGold = exChargeGameGold + exChargeGameGold * netBarRate / 100;
			}
			wallet.setiGold(wallet.getiGold() - costIGold);
			walletPriDao.save(wallet);

			HelperFunctions.setMoneyFlowTarget(wallet, moneyFlow);
			moneyFlowPriDao.save(moneyFlow);

			SQLResultData sd = dbHelper.exchangeGameGold(accId, exChargeGameGold, iGold, Byte.parseByte("0"), clientIP,
					serialNum, "T币兑换游戏币", insurePass);
			if (sd.getReutrnCode() != 0) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg(sd.getDescribe());
				return rds;
			}
			response.setRate(rate);
			response.setCurrGameGold(gameGold + exChargeGameGold);
			response.setExTime(dateTime);
			YxMsgExchangeGameGoldThread yxExchangeGameGoldThread = new YxMsgExchangeGameGoldThread(response.getAccId(),
					response);
			myThreadPool.getYunxinPool().execute(yxExchangeGameGoldThread);
			rds.setCode(ResultCode.SUCCESS.getCode());
			rds.setData(response);
			return rds;
		} catch (NumberFormatException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("数据异常：x000657456");
			return rds;
		}
	}

	@Override
	public ResultDataSet buyGameVip(String accId, String tarAccId, int level, int month, String clientIp,
			String serialNum) {
		ResultDataSet rds = new ResultDataSet();
		GameVipConfig config = gameVipConfigSecDao.findByLevelAndMonth(level, month);
		if (config == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有配置");
			return rds;
		}
		Wallet wallet = walletPriDao.findByAccId(accId);
		if (wallet == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_WALLET.getCode());
			return rds;
		}
		if (config.getPrice() > wallet.getiGold()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.IGOLD_NOT_ENOUGH.getCode());
			return rds;
		}

		if (!StringUtils.StringIsEmptyOrNull(tarAccId)) {
			if (accId != tarAccId) {
				Account TarAcc = accountPriDao.findOne(tarAccId);
				if (TarAcc == null) {
					rds.setCode(ResultCode.PARAM_INVALID.getCode());
					rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
					return rds;
				}
			}
		}

		GameDataHelper dbHelper = new GameDataHelper(sqlserverConfig);

		SQLResultData srd = dbHelper.purchaseMemberVip(accId, tarAccId, level, month, clientIp, serialNum);
		if (srd.getReutrnCode() != 0) {

			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg(srd.getDescribe());
			return rds;
		}

		MoneyFlow moneyFlow = new MoneyFlow();
		moneyFlow.setAccId(accId);
		moneyFlow.setDateTime(DateUtils.getNowTime());
		moneyFlow.setIpAddress(clientIp);

		HelperFunctions.setMoneyFlowType(MoneyFlowType.BuyGameVip, moneyFlow);
		HelperFunctions.setMoneyFlowSource(wallet, moneyFlow);

		wallet.setiGold(wallet.getiGold() - config.getPrice());
		walletPriDao.save(wallet);

		HelperFunctions.setMoneyFlowTarget(wallet, moneyFlow);
		moneyFlowPriDao.save(moneyFlow);

		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getGameVipInfo(String accId) {
		ResultDataSet rds = new ResultDataSet();
		GameDataHelper dbHelper = new GameDataHelper(sqlserverConfig);
		GameVipInfo vipInfo = dbHelper.getGameVip(accId);
		rds.setData(vipInfo);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getGameVipConfig() {
		ResultDataSet rds = new ResultDataSet();
		Iterable<GameVipConfig> config = gameVipConfigSecDao.findAll();
		if (config == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有这个配置");
			return rds;
		}
		rds.setData(config);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;

	}

	@Override
	public ResultDataSet getGameScoreByIp(String accId, long starTime, long endTime) {
		ResultDataSet rds = new ResultDataSet();
		try {
			GameDataHelper dbHelper = new GameDataHelper(sqlserverConfig);

			NetBarAccount nbAcc = netBarAccountPriDao.findByNullityAndOwnerId(false, accId);

			String countRebate;

			countRebate = dbHelper.getGameScoreByIp(DateUtils.getTimeString(starTime, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.getTimeString(endTime, "yyyy-MM-dd HH:mm:ss"), nbAcc.getNetIp());

			String num;
			num = configUtils.getPlatformConfig(Constant.NETBAR_INGAMECONSUMEREBATE_RATE);

			ResponseData response = new ResponseData();
			response.put("rebate", countRebate);
			response.put("rate", num);
			rds.setData(response);
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (Exception e) {
			logger.error("", e);
			rds.setMsg("0x2353_exception");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
	}

	@Override
	public ResultDataSet getGamePlayTimes(String accId, long starTime, long endTime) {
		ResultDataSet rds = new ResultDataSet();
		try {
			GameDataHelper dbHelper = new GameDataHelper(sqlserverConfig);

			NetBarAccount nbAcc = netBarAccountPriDao.findByNullityAndOwnerId(false, accId);

			ResultSet ret = dbHelper.getGamePlayTimes(DateUtils.getTimeString(starTime, "yyyy-MM-dd HH:mm:ss"),
					DateUtils.getTimeString(endTime, "yyyy-MM-dd HH:mm:ss"), nbAcc.getNetIp());
			if (ret == null) {
				rds.setMsg("数据库查询错误");
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				return rds;
			}
			List<HashMap<String, String>> columns = new ArrayList<HashMap<String, String>>();
			while (ret.next()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("GameID", ret.getString("GameID"));
				map.put("NickName", ret.getString("NickName"));
				map.put("playcount", ret.getString("playcount"));
				columns.add(map);
			}
			rds.setData(columns);
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (Exception e) {
			logger.error("", e);
			rds.setMsg("0x2353_exception");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
	}

}
