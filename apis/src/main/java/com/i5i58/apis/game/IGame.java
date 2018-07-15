package com.i5i58.apis.game;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

/**
 * 游戏接口
 * 
 * @author Administrator
 *
 */
public interface IGame {

	/**
	 * ID登陆
	 * 
	 * @author frank
	 * @param openId
	 * @param password
	 * @param accountVersion
	 * @param clientIP
	 * @return
	 * @throws IOException
	 */
	ResultDataSet loginByOpenIdForGame(String openId, String password, String appKey, String clientIP, int device,
			String serialNum) throws IOException;

	/**
	 * 手机号码登陆
	 * 
	 * @author frank
	 * @param phoneNo
	 * @param password
	 * @param accountVersion
	 * @param clientIP
	 * @return
	 * @throws IOException
	 */
	ResultDataSet loginByPhoneNoForGame(String phoneNo, String password, String appKey, String clientIP, int device,
			String serialNum) throws IOException;

	/**
	 * accId与token游戏登陆
	 * 
	 * @author frank
	 * @param accId
	 * @param appKey
	 * @param clientIP
	 * @param device
	 * @param serialNum
	 * @return
	 * @throws IOException
	 */
	ResultDataSet loginByTokenForGame(String accId, String appKey, String clientIP, int device, String serialNum)
			throws IOException;

	/**
	 * 兑换游戏币（消耗i币）
	 * 
	 * @author frank
	 * @param accId
	 * @param appKey
	 * @param iGold
	 * @param clientIP
	 * @param device
	 * @param serialNum
	 * @return
	 * @throws IOException
	 */
	ResultDataSet exchangeGameGold(String accId, String appKey, long iGold, long gameGold, String clientIP, int device,
			String serialNum, String insurePass) throws IOException;

	/**
	 * 兑换游戏币（消耗i币）
	 * 
	 * @author frank
	 * @param accId
	 * @param appKey
	 * @param iGold
	 * @param clientIP
	 * @param device
	 * @param serialNum
	 * @return
	 * @throws IOException
	 */
	ResultDataSet exchangeGameGoldForWeb(String accId, String appKey, long iGold, long gameGold, String clientIP,
			int device, String serialNum, String insurePass) throws IOException;

	/**
	 * 获取银行
	 * 
	 * @author frank
	 * @param accId
	 * @return
	 */
	ResultDataSet getBank(String accId);

	/**
	 * 兑换钻石
	 * 
	 * @author frank
	 * @param accId
	 * @param appKey
	 * @param Diamond
	 * @param clientIP
	 * @param device
	 * @param serialNum
	 * @return
	 * @throws IOException
	 */
	ResultDataSet exchangeDiamond(String accId, String appKey, long Diamond, long gameGold, String clientIP, int device,
			String serialNum, String insurePass) throws IOException;

	/**
	 * 查询IGold余额
	 * 
	 * @author frank
	 * @param accId
	 * @param appKey
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getIGold(String accId, String appKey) throws IOException;

	/**
	 * 查询IGold余额
	 * 
	 * @author songfl
	 * @param openId
	 */
	ResultDataSet getIGoldByOpenId(String openId) throws IOException;

	/**
	 * 缓存玩家状态
	 * 
	 * @author frank
	 * @param accId
	 * @param appKey
	 * @param query
	 *            0表示更新缓存，不存在该玩家缓存时则插入；1表示删除该玩家缓存
	 * @param text
	 *            显示文本，格式例子:"TA正在[{gameKind}][{gameRoom}][{gameTable}]，快去看看吧！"
	 * @param action
	 *            json格式，对应显示文本{...}关键字的HashMap,key或value中的#表示关键字,客户端需要功能支持,
	 *            例:#all显示文本整体，action:打开app内部浏览器跳转玩家在105游戏中的信息,
	 *            gameKind的值是"欢乐斗地主"对应显示文本中的{gameKind}，有下划线，颜色值是100,100,100，点击事件是打开内部浏览器跳转105的游戏介绍,
	 *            gameRoom没有下划线,
	 *            gameTable点击事件：进入该玩家游戏进行旁观，打开app{appKey}/调用app的方法lookOn，?后面便是参数以&分隔的键值对,ui表示该字段底部居中有悬浮提示“点击旁观”
	 *            {#all:{"action":"ii.inWeb/https://game.i5i58.com/player/105"}
	 *            "gameKind":{value:"欢乐斗地主","underLine":true,"color":"100,100,100","action":"ii.inWeb/https://game.i5i58.com/game/role/105"}，
	 *            "gameRoom":{value:"高手房","color":"100,100,100"},
	 *            "gameTable":{value:"5号桌","underLine":true,"color":"100,100,100","ui":{"value":"tip","layout":"bottom/center","text":"点击旁观"},
	 *            "action":"ii.openApp{appKey}/lookOn?gameKind=123&gameRoom=456&gameTable=5&gameChair=3"}}
	 * @param ext
	 *            扩展字段
	 * @return
	 * @throws IOException
	 */
	ResultDataSet PlayerAction(String accId, String appKey, byte query, String text, String action, String ext)
			throws IOException;

	/**
	 * 获取游戏列表
	 * 
	 * @author frank
	 * @param appKey
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getGameList(String appKey) throws IOException;

	/**
	 * 更新游戏类型
	 * 
	 * @param appKey
	 * @param secret
	 * @param kindId
	 * @param gameId
	 * @param typeId
	 * @param joinId
	 * @param sortId
	 * @param kindName
	 * @param processName
	 * @param supportMobile
	 * @param gameRuleUrl
	 * @param downLoadUrl
	 * @return
	 * @throws IOException
	 */
	ResultDataSet UpdateGameKindItem(String appKey, String secret, int kindId, int gameId, int typeId, int joinId,
			int sortId, String kindName, String processName, boolean supportMobile, String gameRuleUrl,
			String downLoadUrl) throws IOException;

	/**
	 * 删除所有游戏类型
	 * 
	 * @author frank
	 * @param appKey
	 * @param secret
	 * @return
	 * @throws IOException
	 */
	ResultDataSet deleteAllGameKind(String appKey, String secret) throws IOException;

	/**
	 * 更新游戏房间列表，不存在则添加
	 * 
	 * @author frank
	 * @param appKey
	 * @param secret
	 * @param serverId
	 * @param kindId
	 * @param nodeId
	 * @param sortId
	 * @param serverKind
	 * @param serverType
	 * @param serverLevel
	 * @param serverPort
	 * @param cellScore
	 * @param enterScore
	 * @param serverRule
	 * @param lineCount
	 * @param androidCount
	 * @param fullCount
	 * @param serverAddr
	 * @param serverName
	 * @return
	 * @throws IOException
	 */
	ResultDataSet UpdateGameItem(String appKey, String secret, int serverId, int kindId, int nodeId, int sortId,
			int serverKind, int serverType, int serverLevel, int serverPort, long cellScore, long enterScore,
			int serverRule, int lineCount, int androidCount, int fullCount, String serverAddr, String serverName)
			throws IOException;

	/**
	 * 删除游戏房间列表
	 * 
	 * @author frank *
	 * @param appKey
	 * @param secret
	 * @param serverId
	 * @return
	 * @throws IOException
	 */
	ResultDataSet deleteGameItem(String appKey, String secret, int serverId) throws IOException;

	/**
	 * 删除游戏房间列表
	 * 
	 * @author frank
	 * @param appKey
	 * @param secret
	 * @param serverId
	 * @param lineCount
	 * @return
	 * @throws IOException
	 */
	ResultDataSet updateGameLineCount(String appKey, String secret, int serverId, int lineCount) throws IOException;

	/**
	 * 用户首次开能银行服务
	 * 
	 * @author cc
	 * @param accId
	 * @param strPassword
	 * @param strInsurePass
	 * @param clientIP
	 * @param MachineID
	 * @return
	 */
	ResultDataSet UserEnableInsure(String accId, String strPassword, String strInsurePass, String clientIP,
			String MachineID, Integer GameID, Short Gender, String MobilePhone);

	/**
	 * @author songfl 查询是否开通银行
	 */
	ResultDataSet isBankEnabled(String accId);

	/**
	 * @author songfl 检查银行密码
	 */
	ResultDataSet checkBankPass(String accId, String bankPass);

	/**
	 * 存游戏币
	 * 
	 * @author cc
	 * @param ExchangeScore
	 * @param clientIP
	 * @param MachineID
	 * @return
	 */
	ResultDataSet SaveScore(String accId, long ExchangeScore, String clientIP, String MachineID);

	/**
	 * 取游戏币
	 * 
	 * @author cc
	 * @param accId
	 * @param strPassword
	 * @param ExchangeScore
	 * @param clientIP
	 * @param MachineID
	 * @return
	 */
	ResultDataSet TakeScore(String accId, String strPassword, long ExchangeScore, String clientIP, String MachineID);

	/**
	 * 修改银行密码
	 * 
	 * @author cc
	 * @param strPassword
	 * @param strNewPassword
	 * @param clientIP
	 * @return
	 */
	ResultDataSet ModifyInsurePassword(String accId, String strPassword, String strNewPassword, String clientIP);

	/**
	 * 重置银行密码
	 * 
	 * @author songfl
	 * @param accId
	 * @param phoneNo
	 * @param strPassword
	 * @param verifyCode
	 * @param clientIP
	 * @return
	 */
	ResultDataSet resetInsurePassword(String accId, String verifyCode, String strPassword, String clientIp);

	/**
	 * 发送验证码
	 * 
	 * @param accId
	 * @param phoneNo
	 */
	ResultDataSet sendSMSForResetInsurePassword(String accId);

	/**
	 * 获取银行记录
	 * 
	 * @author cc
	 * @param accId
	 * @param startTime
	 * @param endTime
	 * @param pageSize
	 * @param pageIndex
	 * @param pageCount
	 * @param recordCount
	 * @return
	 */
	ResultDataSet getBankRecord(String accId, String startTime, String endTime, int pageSize, int pageIndex);

	/**
	 * 获取银行记录
	 * 
	 * @author cc
	 * @param accId
	 * @param startTime
	 * @param endTime
	 * @param pageSize
	 * @param pageIndex
	 * @param pageCount
	 * @param recordCount
	 * @return
	 */
	ResultDataSet getBankRecordByTradeType(String accId, String startTime, String endTime, int pageSize, int pageIndex);

	/**
	 * 获取当日已赠送的魅力值
	 * 
	 * @author songfl
	 * @param accId
	 */
	ResultDataSet getCurExchangedLoveLiness(String accId);

	/**
	 * 魅力值兑换
	 * 
	 * @author songfl
	 * @param accId
	 * @param lovelinessExchanged
	 * @param clientIp
	 * @return
	 */
	ResultDataSet exchagneLiveliness(String accId, long lovelinessExchanged, String clientIp);

	/**
	 * 查询游戏币兑换比率
	 * 
	 * @author songfl
	 * @return
	 */
	ResultDataSet queryGameExchangeRate();

	/**
	 * 修改游戏昵称
	 * 
	 * @author songfl
	 * @return
	 */
	ResultDataSet modifyGameNickName(String accId, String nickName, String clientIp);

	/**
	 * 获取游戏昵称
	 * 
	 * @author songfl
	 * @return
	 */
	ResultDataSet getGameNickName(String accId);

	/**
	 * 获取游戏昵称
	 * 
	 * @author songfl
	 * @return
	 */
	ResultDataSet getGameInfo(String gameId);

	/**
	 * 获取游戏排名
	 * 
	 * @author songfl
	 * @param rankFirst
	 * @param rankLast
	 */
	ResultDataSet getRankList(String accId, int rankFirst, int rankLast);

	/**
	 * 获取游戏版本号
	 * 
	 * @author songfl
	 * @device {PC游戏:10, IOS游戏:11, android:12}
	 */
	ResultDataSet getGameVersion(int device);

	/**
	 * 获取游戏支付选项
	 * 
	 * @author songfl
	 * @device {PC游戏:10, IOS游戏:11, android:12}
	 */
	ResultDataSet getPayOption(int device);

	/**
	 * 获取游戏选项，用于app审核阶段
	 * 
	 * @author songfl
	 * @device {PC游戏:10, IOS游戏:11, android:12}
	 */
	ResultDataSet getGameOption(int device);

	/**
	 * 主播佣金兑换游戏欢乐豆
	 * 
	 * @author songfl
	 */
	ResultDataSet commissionExchangeGameGold(String accId, long commission, String clientIp);

	/**
	 * 转账游戏币
	 * 
	 * @param accId
	 * @param TransferScore
	 * @param InsurePass
	 * @param TargetAccId
	 * @param TransRemark
	 * @param clientIP
	 * @param MachineID
	 * @return
	 */
	ResultDataSet TransferScore(String accId, long TransferScore, String InsurePass, String TargetAccId,
			String TransRemark, String clientIP, String MachineID);

	/**
	 * 购买游戏会员
	 * 
	 * @author songfl
	 * @param accId
	 * @param level
	 * @param month
	 * @param clientIp
	 * @param serialNum
	 * @return
	 */
	ResultDataSet buyGameVip(String accId, String tarAccId, int level, int month, String clientIp, String serialNum);

	/**
	 * 查询游戏会员
	 * 
	 * @author songfl
	 * @param accId
	 * @return
	 */
	ResultDataSet getGameVipInfo(String accId);

	/**
	 * 查询游戏VIP配置
	 * 
	 * @author cy
	 * 
	 * 
	 */
	ResultDataSet getGameVipConfig();

	/**
	 * 获取银行
	 * 
	 * @author frank
	 * @param accId
	 * @return
	 */
	ResultDataSet getGameScoreByIp(String accId, long starTime, long endTime);

	/**
	 * 获取游戏时间统计
	 * 
	 * @author frank
	 * @param accId
	 * @return
	 */
	ResultDataSet getGamePlayTimes(String accId, long starTime, long endTime);

}
