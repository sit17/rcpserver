package com.i5i58.yunxin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.i5i58.util.JsonUtils;
import com.i5i58.yunxin.Utils.CreateTeamResult;
import com.i5i58.yunxin.Utils.GetUserResult;
import com.i5i58.yunxin.Utils.RefreshTokenResult;
import com.i5i58.yunxin.Utils.RegisterAccountResult;
import com.i5i58.yunxin.Utils.UpdateChatRoomMessageResult;
import com.i5i58.yunxin.Utils.YXResultSet;

public class YunxinIM {
	public static final String sYunXinServerHost = "https://api.netease.im/nimserver/";
	public static final String sYunXinUserHost = sYunXinServerHost + "user/";
	public static final String sYunXinMsgHost = sYunXinServerHost + "msg/";
	public static final String sYunXinTeamHost = sYunXinServerHost + "team/";
	public static String appKey = "ba7a0d3dc3914a9e5cbac325d3c6aeab";
	public static String appSecret = "7759313c5a0d";

	public static String httpPost(String url, PostParams... postParams) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(url);

			String nonce = "12345";
			String curTime = String.valueOf((new Date()).getTime() / 1000L);
			String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce, curTime);// 参考

			// 设置请求的header
			httpPost.addHeader("AppKey", appKey);
			httpPost.addHeader("Nonce", nonce);
			httpPost.addHeader("CurTime", curTime);
			httpPost.addHeader("CheckSum", checkSum);
			httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

			// 设置请求的参数
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for (PostParams params : postParams) {
				nvps.add(new BasicNameValuePair(params.getName(), params.getValue()));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

			CloseableHttpResponse response = httpClient.execute(httpPost);

			try {
				// 打印执行结果
				String retString = EntityUtils.toString(response.getEntity(), "utf-8");
				System.out.println(retString);
				System.out.println(response.getStatusLine());
				HttpEntity entity = response.getEntity();
				// do something useful with the response body
				// and ensure it is fully consumed
				EntityUtils.consume(entity);
				return retString;
			} finally {
				response.close();
			}
		} finally {
			httpClient.close();
		}
	}

	/**
	 * httpPostWithArraryParam,改写httpPost，参数改成List类型，防止手动传参数时写错
	 * 
	 * @author songfl
	 * @param url
	 * @param arrayParams
	 * @throws IOException
	 */
	public static String httpPostWithArraryParam(String url, List<NameValuePair> arrayParams) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(url);

			String nonce = "12345";
			String curTime = String.valueOf((new Date()).getTime() / 1000L);
			String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce, curTime);// 参考
																						// 计算CheckSum的java代码

			// 设置请求的header
			httpPost.addHeader("AppKey", appKey);
			httpPost.addHeader("Nonce", nonce);
			httpPost.addHeader("CurTime", curTime);
			httpPost.addHeader("CheckSum", checkSum);
			httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

			httpPost.setEntity(new UrlEncodedFormEntity(arrayParams, "utf-8"));

			CloseableHttpResponse response = httpClient.execute(httpPost);

			try {
				// 打印执行结果
				String retString = EntityUtils.toString(response.getEntity(), "utf-8");
				 System.out.println(retString);
				 System.out.println(response.getStatusLine());
				HttpEntity entity = response.getEntity();
				// do something useful with the response body
				// and ensure it is fully consumed
				EntityUtils.consume(entity);
				return retString;
			} finally {
				response.close();
			}
		} finally {
			httpClient.close();
		}
	}

	public static RegisterAccountResult registerAccount(String id, String nickName, String token) throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/user/create.action", new PostParams("accid", id),
				new PostParams("name", nickName), new PostParams("token", token));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, RegisterAccountResult.class);
	}

	// [start]聊天室
	/**
	 * 
	 * createChatRoom 创建聊天室
	 * 
	 * @author Lee
	 * @param ids
	 *            用户accid（必须）
	 * @param rname
	 *            直播间名字（必须）
	 * @param notic
	 *            直播间公告(长度限制1024个字符)
	 * @param bcurl
	 *            直播间地址(长度限制1024个字符)
	 * @param ewords
	 *            扩展字段(最长4096字节)
	 * @throws IOException
	 */
	public static YXResultSet createChatRoom(String ids, String rname, String notic, String bcurl, String ewords)
			throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/chatroom/create.action", new PostParams("creator", ids),
				new PostParams("name", rname), new PostParams("announcement", notic),
				new PostParams("broadcasturl", bcurl), new PostParams("ext", ewords));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * 
	 * checkChatRoom 查询聊天室信息
	 * 
	 * @author Lee
	 * @param rid
	 *            直播间id（必须）
	 * @param olpeople
	 *            是否需要返回在线人数(boolean;默认false)
	 * @throws IOException
	 */
	public static YXResultSet checkChatRoom(String rid, String olpeople) throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/chatroom/get.action", new PostParams("roomid", rid) // must
				, new PostParams("needOnlineUserCount", olpeople));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * 
	 * updateChatRoomMessage 更新聊天室信息 @author Lee
	 * 
	 * @param rid
	 *            直播间id（必须）
	 * @param rname
	 *            直播间名字
	 * @param notic
	 *            公告
	 * @param bcurl
	 *            直播间地址
	 * @param ewords
	 *            扩展字段
	 * @param notify
	 *            是否需要发送更新通知事件，默认true
	 * @param neworlds
	 *            通知事件扩展字段(长度限制2048)
	 * @throws IOException
	 */
	public static UpdateChatRoomMessageResult updateChatRoomMessage(String rid, String rname, String notic,
			String bcurl, String ewords, String notify, String neworlds) throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/chatroom/update.action", new PostParams("roomid", rid),
				new PostParams("name", rname), new PostParams("announcement", notic),
				new PostParams("broadcasturl", bcurl), new PostParams("ext", ewords),
				new PostParams("needNotify", notify), new PostParams("notifyExt", neworlds));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, UpdateChatRoomMessageResult.class);
	}

	/**
	 * 
	 * chatRoomOpenOrClose 修改聊天室开/关闭状态 @author Lee
	 * 
	 * @param rid
	 *            直播间id（必须）
	 * @param ids
	 *            操作者账号（直播间创建者）（必须）
	 * @param open
	 *            设置是否打开（boolean;false:关闭聊天室；true:打开聊天室）（必须）
	 * @throws IOException
	 */
	public static YXResultSet chatRoomOpenOrClose(String rid, String ids, String open) throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/chatroom/toggleCloseStat.action",
				new PostParams("roomid", rid), new PostParams("operator", ids), new PostParams("valid", open));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * 
	 * setMemberRole 设置聊天室内成员角色
	 * 
	 * @author Lee
	 * @param rid
	 *            直播间id（必须）
	 * @param ids
	 *            操作者账号accid（必须）
	 * @param cids
	 *            被操作者账号accid（必须）
	 * @param lv
	 *            操作（1:设置为管理员，操作者是创建者；2:设置普通等级用户，操作者是创建者或管理员；-1:设为黑名单用户，操作者是创建者或管理员；-2:设为禁言用户，操作者是创建者或管理员）（必须）
	 * @param real
	 *            确认设置（boolean;true:设置；false:取消设置）（必须）
	 * @param neworlds
	 *            通知扩展字段（json格式）
	 * @throws IOException
	 */
	public static YXResultSet setChatRoomMemberRole(String rid, String ids, String cids, String lv, String real,
			String neworlds) throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/chatroom/setMemberRole.action",
				new PostParams("roomid", rid), new PostParams("operator", ids), new PostParams("target", cids),
				new PostParams("opt", lv), new PostParams("optvalue", real), new PostParams("notifyExt", neworlds));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * 
	 * requestChatRoomAddr 请求聊天室地址
	 * 
	 * @author Lee
	 * @param rid
	 *            直播间id（必须）
	 * @param ids
	 *            进入聊天室的账号（必须）
	 * @param type
	 *            1:weblink;2:commonlink,默认1
	 * @throws IOException
	 */
	public static YXResultSet requestChatRoomAddr(String rid, String ids, String type) throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/chatroom/requestAddr.action",
				new PostParams("roomid", rid), new PostParams("accid", ids), new PostParams("clienttype", type));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * sendMsg 发送聊天室消息
	 * 
	 * @author Lee
	 * @param rid
	 *            直播间id（必须）
	 * @param msgid
	 *            客户端消息id（使用uuid等随机串， 相同的消息会被客户端去重）（必须）
	 * @param fids
	 *            消息发出者的账号accid（必须）
	 * @param mtype
	 *            消息类型（0：文本；1：图片；2：语音；3：视频；4：位置；6：文件；10：Tips；100：自定义）（必须）
	 * @param mflag
	 *            重发消息标记(0：非重发消息，1：重发消息，如重发消息会按照msgid检查去重逻辑)
	 * @param context
	 *            消息内容(长度限制2048字节)
	 * @param eworlds
	 *            消息扩展字段（JSON格式;长度限制4096）
	 * @return
	 * @throws IOException
	 */
	public static YXResultSet sendChatRoomMsg(String rid, String msgid, String fids, String mtype, String mflag,
			String context, String eworlds) throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/chatroom/sendMsg.action", new PostParams("roomid", rid),
				new PostParams("msgId", msgid), new PostParams("fromAccid", fids), new PostParams("msgType", mtype),
				new PostParams("resendFlag", mflag), new PostParams("attach", context), new PostParams("ext", eworlds));
		System.out.println(ret);

		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * 
	 * addRobot 往聊天室内添加机器人
	 * 
	 * @author Lee
	 * @param rid
	 *            直播间id（必须）
	 * @param robots
	 *            机器人账号accid列表（JSONArray;账号数量上限100个）（必须）
	 * @param rmsg
	 *            机器人信息扩展字段（json;长度4096字节）
	 * @param reworlds
	 *            机器人进入聊天室通知的扩展字段（json;长度2048字节）
	 * @throws IOException
	 */
	public static YXResultSet addChatRoomRobot(String rid, String robots, String rmsg, String reworlds)
			throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/chatroom/addRobot.action",
				new PostParams("roomid", rid), new PostParams("accids", robots), new PostParams("roleExt", rmsg),
				new PostParams("notifyExt", reworlds));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * 
	 * addRobot 往聊天室内添加机器人
	 * 
	 * @author Lee
	 * @param rid
	 *            直播间id（必须）
	 * @param robots
	 *            机器人账号accid列表（JSONArray;账号数量上限100个）（必须）
	 * @throws IOException
	 */
	public static YXResultSet addChatRoomRobot(String rid, String robots) throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/chatroom/addRobot.action",
				new PostParams("roomid", rid), new PostParams("accids", robots));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * 
	 * removeRobot 从聊天室内删除机器人
	 * 
	 * @author Lee
	 * @param rid
	 *            直播间id（必须）
	 * @param robots
	 *            机器人账号accid列表（JSONArray;账号数量上限100个）（必须）
	 * @throws IOException
	 */
	public static YXResultSet removeChatRoomRobot(String rid, String robots) throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/chatroom/removeRobot.action",
				new PostParams("roomid", rid), new PostParams("accids", robots));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * 
	 * setTemporaryMute 设置临时禁言状态
	 * 
	 * @author Lee
	 * @param roomid
	 *            直播间id（必须）
	 * @param operator
	 *            操作者accid（管理员或创建者）（必须）
	 * @param target
	 *            被禁言的目标账号accid（必须）
	 * @param muteDuration
	 *            状态（0:解除禁言;>0设置禁言的秒数，不能超过2592000秒(30天)）（必须）
	 * @param needNotify
	 *            操作完成后是否需要发广播(Boolean;默认true)
	 * @param notifyExt
	 *            通知广播事件中的扩展字段
	 * @throws IOException
	 */
	public static YXResultSet setChatRoomTemporaryMute(String roomid, String operator, String target,
			String muteDuration, String needNotify, String notifyExt) throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/chatroom/temporaryMute.action",
				new PostParams("roomid", roomid), new PostParams("operator", operator), new PostParams("target", target),
				new PostParams("muteDuration", muteDuration), new PostParams("needNotify", needNotify),
				new PostParams("notifyExt", notifyExt));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * 
	 * addNewOfferToQueue 往聊天室有序队列中新加或更新元素 @author Lee
	 * 
	 * @param rid
	 *            直播间id（必须）
	 * @param ekey
	 *            新元素的elementKey（自定义;长度限制128字节）（必须）
	 * @param evalue
	 *            新元素内容(长度限制4096字节)（必须）
	 * @throws IOException
	 */
	public static YXResultSet addChatRoomNewOfferToQueue(String rid, String ekey, String evalue) throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/chatroom/queueOffer.action",
				new PostParams("roomid", rid), new PostParams("key", ekey), new PostParams("value", evalue));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * 
	 * selectOfferFromQueue 从队列中取出元素 @author Lee
	 * 
	 * @param rid
	 *            直播间id（必须）
	 * @param ekey
	 *            目前元素的elementKey(长度限制128字节，不填表示取出头上的第一个)（必须）
	 * @throws IOException
	 */
	public static YXResultSet selectChatRoomOneOfferFromQueue(String rid, String ekey) throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/chatroom/queuePoll.action",
				new PostParams("roomid", rid), new PostParams("key", ekey));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * 
	 * selectAllOffer 排序列出队列中所有元素
	 * 
	 * @author Lee
	 * @param rid
	 *            直播间id（必须）
	 * @throws IOException
	 */
	public static YXResultSet selectChatRoomAllOffer(String rid) throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/chatroom/queueList.action",
				new PostParams("roomid", rid));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * 
	 * queueDrop 删除清理整个队列
	 * 
	 * @author Lee
	 * @param rid
	 *            直播间id（必须）
	 * @throws IOException
	 */
	public static YXResultSet chatRoomQueueDrop(String rid) throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/chatroom/queueDrop.action",
				new PostParams("roomid", rid));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * 
	 * queueInit 初始化队列
	 * 
	 * @author Lee
	 * @param rid
	 *            直播间id（必须）
	 * @param size
	 *            队列长度限制（0~1000）（必须）
	 * @throws IOException
	 */
	public static YXResultSet chatRoomQueueInit(String rid, String size) throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/chatroom/queueInit.action",
				new PostParams("roomid", rid), new PostParams("sizeLimit", size));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	// [end]

	// [start]历史记录
	/**
	 * 
	 * 
	 * querySingleChatMsg 单聊云端历史消息查询 @author Lee
	 * 
	 * @param fids
	 *            发送者accid（必须）
	 * @param tids
	 *            接收者accid（必须）
	 * @param btime
	 *            开始时间，ms（必须）
	 * @param etime
	 *            截止时间，ms（必须）
	 * @param limit
	 *            本次查询的消息条数上限（0~100）（必须）
	 * @param array
	 *            排序方式（1按时间正序排列；2按时间降序排列）
	 * @throws IOException
	 */
	public static YXResultSet querySingleChatMsg(String fids, String tids, String btime, String etime, String limit,
			String array) throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/history/querySessionMsg.action",
				new PostParams("from", fids), new PostParams("to", tids), new PostParams("begintime", btime),
				new PostParams("endtime", etime), new PostParams("limit", limit), new PostParams("reverse", array));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * 
	 * queryTeamChatMsg 群聊云端历史消息查询
	 * 
	 * @author Lee
	 * @param tid
	 *            群id（必须）
	 * @param ids
	 *            查询用户对应的accid（必须）
	 * @param btime
	 *            开始时间，ms（必须）
	 * @param etime
	 *            截止时间，ms（必须）
	 * @param limit
	 *            本次查询的消息条数上限（0~100）（必须）
	 * @param array
	 *            排序方式（1按时间正序排列；2按时间降序排列）
	 * @return
	 * @throws IOException
	 */

	public static YXResultSet queryTeamChatMsg(String tid, String ids, String btime, String etime, String limit,
			String array) throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/history/queryTeamMsg.action",
				new PostParams("tid", tid), new PostParams("accid", ids), new PostParams("begintime", btime),
				new PostParams("endtime", etime), new PostParams("limit", limit), new PostParams("reverse", array));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}
	// [end]

	// [start]账号

	////////////////////////////////////// 1.云信ID

	/**
	 * 获取用户名片
	 * 
	 * @param ids
	 * @return
	 * @throws IOException
	 */
	public static GetUserResult getUser(String ids) throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/user/getUinfos.action", new PostParams("accids", ids));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, GetUserResult.class);
	}

	/**
	 * 
	 * registerAccount
	 * 
	 * @Description: 创建云信ID
	 * @param id
	 * @param nickName
	 * @param
	 * @return @param @throws IOException 参数说明 @return String 返回类型 @throws
	 */
	// public static String registerAccount(String id, String nickName) throws
	// IOException {
	// return httpPost("https://api.netease.im/nimserver/user/create.action"
	// , new PostParams("accid", id), new PostParams("name", nickName));
	// }

	/**
	 * 
	 * updateAccount
	 * 
	 * @Description: 云信ID更新
	 * @author Xiaoming
	 * @param id
	 *            云信ID
	 * @return Http 请求返回内容
	 * @throws IOException
	 *             参数说明
	 */
	public static String updateAccount(String id) throws IOException {
		// LogUtils.printStack();
		return httpPost("https://api.netease.im/nimserver/user/update.action", new PostParams("accid", id));
	}

	/**
	 * 更新并获取新token
	 * 
	 * @author frank
	 * @param accId
	 * @return
	 * @throws IOException
	 */
	public static RefreshTokenResult refreshTokenAccount(String accId) throws IOException {
		// LogUtils.printStack();
		String ret = httpPost("https://api.netease.im/nimserver/user/refreshToken.action",
				new PostParams("accid", accId));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, RefreshTokenResult.class);
	}

	/**
	 * 
	 * blockAccount
	 * 
	 * @Description: 封禁云信ID
	 * @author Xiaoming
	 * @param id
	 *            云信ID，最大长度32字节，必须保证一个APP内唯一
	 * @param needkick
	 *            是否踢掉被禁用户，默认false
	 * @throws IOException
	 *             参数说明
	 * @return String 返回类型
	 */
	public static YXResultSet blockAccount(String id, Boolean needkick) throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/user/block.action", new PostParams("accid", id),
				new PostParams("needkick", needkick.toString()));
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * 
	 * unblockAccount
	 * 
	 * @Description: 解禁云信ID
	 * @author Xiaoming
	 * @param id
	 * @param @throws
	 *            IOException 参数说明
	 * @return String
	 */
	public static YXResultSet unblockAccount(String id) throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/user/unblock.action", new PostParams("accid", id));
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/////////////////////////////////////////////////// 2.用户名片
	//
	// public static String getUser(String ids) throws IOException {
	// return httpPost("https://api.netease.im/nimserver/user/getUinfos.action"
	// , new PostParams("accids", ids));
	// }

	/**
	 * 
	 * updateUinfoAccount
	 * 
	 * @Description: 更新用户名片
	 * @author Xiaoming
	 * @param id
	 * @param name
	 *            用户昵称，最大长度64字节
	 * @param icon
	 *            用户icon，最大长度1024字节
	 * @param sign
	 *            用户签名，最大长度256字节
	 * @param email
	 *            用户email，最大长度64字节
	 * @param birth
	 *            用户生日，最大长度16字节
	 * @param mobile
	 *            用户mobile，最大长度32字节，只支持国内号码
	 * @param gender
	 *            用户性别，0表示未知，1表示男，2女表示女，其它会报参数错误
	 * @param ex
	 *            用户名片扩展字段，最大长度1024字节，用户可自行扩展，建议封装成JSON字符串
	 * @param @throws
	 *            IOException 参数说明
	 * @return String 返回类型 @throws
	 */
	public static String updateUinfoAccount(String id, String name, String icon, String sign, String email,
			String birth, String mobile, String gender, String ex) throws IOException {
		return httpPost("https://api.netease.im/nimserver/user/updateUinfo.action", new PostParams("accid", id),
				new PostParams("name", name), new PostParams("icon", icon), new PostParams("sign", sign),
				new PostParams("email", email), new PostParams("birth", birth), new PostParams("mobile", mobile),
				new PostParams("gender", gender), new PostParams("ex", ex));
	}

	/**
	 * 更新用户昵称
	 * 
	 * @author frank
	 * @param id
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static YXResultSet updateUinfoAccount(String id, String name) throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/user/updateUinfo.action", new PostParams("accid", id),
				new PostParams("name", name));
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	///////////////////////////////////////////////////// 3.用户设置

	/**
	 * 
	 * setDonnop
	 * 
	 * @Description: 设置桌面端在线时，移动端是否需要推送
	 * @author Xiaoming
	 * @param id
	 *            用户帐号
	 * @param donnopOpen
	 *            桌面端在线时，移动端是否不推送：true:移动端不需要推送，false:移动端需要推送
	 * @param @throws
	 *            IOException 参数说明
	 * @return String 返回类型
	 */
	public static String setDonnop(String id, Boolean donnopOpen) throws IOException {
		return httpPost("https://api.netease.im/nimserver/user/setDonnop.action", new PostParams("accid", id),
				new PostParams("donnopOpen", donnopOpen.toString()));
	}
	// [end]

	// [start]好友
	///////////////////////////////////////////////////// 4.用户关系托管
	/**
	 * 
	 * addFriend
	 * 
	 * @Description: 加好友
	 * @author Xiaoming
	 * @param accid
	 *            加好友发起者accid
	 * @param faccid
	 *            加好友接收者accid
	 * @param type
	 *            1直接加好友，2请求加好友，3同意加好友，4拒绝加好友
	 * @param msg
	 *            加好友对应的请求消息，第三方组装，最长256字节
	 * @throws IOException
	 * @return String
	 */
	public static YXResultSet addFriend(String accid, String faccid, String type, String msg) throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/friend/add.action", new PostParams("accid", accid),
				new PostParams("faccid", faccid), new PostParams("type", type), new PostParams("msg", msg));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * 
	 * updateFriend
	 * 
	 * @Description: 更新好友相关信息
	 * @author Xiaoming
	 * @param accid
	 *            发起者accid
	 * @param faccid
	 *            要修改朋友的accid
	 * @param alias
	 *            给好友增加备注名
	 * @throws IOException
	 *             参数说明
	 * @return String
	 */
	public static YXResultSet updateFriend(String accid, String faccid, String alias) throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/friend/update.action", new PostParams("accid", accid),
				new PostParams("faccid", faccid), new PostParams("alias", alias));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * 
	 * deleteFirend
	 * 
	 * @Description: 删除好友关系
	 * @author Xiaoming
	 * @param accid
	 *            发起者accid
	 * @param faccid
	 *            要删除朋友的accid
	 * @return String 返回类型
	 */
	public static YXResultSet deleteFirend(String accid, String faccid) throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/friend/delete.action", new PostParams("accid", accid),
				new PostParams("faccid", faccid));
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * 
	 * getFirend
	 * 
	 * @Description: 获取好友关系 查询某时间点起到现在有更新的双向好友
	 * @author Xiaoming
	 * @param accid
	 *            发起者accid
	 * @param createtime
	 *            查询的时间点
	 * @throws IOException
	 *             参数说明
	 * @return String 返回类型
	 */
	public static String getFirend(String accid, String createtime) throws IOException {
		return httpPost("https://api.netease.im/nimserver/friend/get.action", new PostParams("accid", accid),
				new PostParams("createtime", createtime));
	}

	/**
	 * 
	 * setSpecialRelation
	 * 
	 * @Description: 设置黑名单/静音
	 * @author Xiaoming
	 * @param accid
	 *            用户帐号，最大长度32字节，必须保证一个APP内唯一
	 * @param targetAcc
	 *            被加黑或加静音的帐号
	 * @param relationType
	 *            本次操作的关系类型,1:黑名单操作，2:静音列表操作
	 * @param value
	 *            操作值，0:取消黑名单或静音，1:加入黑名单或静音
	 * @throws IOException
	 *             参数说明 @return String 返回类型
	 */
	public static YXResultSet setSpecialRelation(String accid, String targetAcc, String relationType, String value)
			throws IOException {
		String ret = httpPost("https://api.netease.im/nimserver/user/setSpecialRelation.action",
				new PostParams("accid", accid), new PostParams("targetAcc", targetAcc),
				new PostParams("relationType", relationType), new PostParams("value", value));
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * 
	 * listBlackAndMuteList
	 * 
	 * @Description: 查看指定用户的黑名单和静音列表
	 * @author Xiaoming
	 * @param accid
	 * @throws IOException
	 *             参数说明
	 * @return String 返回类型
	 */
	public static String listBlackAndMuteList(String accid) throws IOException {
		return httpPost("https://api.netease.im/nimserver/user/listBlackAndMuteList.action",
				new PostParams("accid", accid));
	}
	// [end]

	// [start]消息相关功能
	/****************************************************************************************
	 * 以下是消息相关的功能
	 **************************************************************************************/
	/**
	 * httpPostMessageRequest
	 * 
	 * @author songfl
	 * @Description: 处理聊天消息相关的功能
	 * @param actionType
	 *            http 调用的方法名，为"*.action"形式
	 * @param arrryParams
	 *            http 协议参数,key-value形式的列表
	 * @throws IOException
	 * @return String json格式
	 */
	public static String httpPostMessageRequest(String actionType, List<NameValuePair> arrryParams) throws IOException {
		String url = sYunXinMsgHost + actionType;
		return httpPostWithArraryParam(url, arrryParams);
	}

	/**
	 * sendMessage
	 * 
	 * 给用户或者群发送普通消息，包括文本，图片，语音，视频和地理位置，具体消息参考下面描述
	 * 
	 * @author songfl
	 * @param accid
	 *            Necessary, 发送者accid，用户帐号，最大32字节，
	 * @param ope
	 *            Necessary,0：点对点个人消息，1：群消息，其他返回414
	 * @param to
	 *            Necessary,ope==0是表示accid即用户id，ope==1表示tid即群id
	 * @param type
	 *            Necessary,0 表示文本消息,1 表示图片，2 表示语音，3 表示视频，4 表示地理位置信息，6 表示文件，100
	 *            自定义消息类型
	 * @param body
	 *            Necessary,最大长度5000字节，为一个json串
	 *            ******************************************************************************
	 *            下面的参数不是必备的，可以传空字符串
	 * @param option
	 *            Unnecessary,发消息时特殊指定的行为选项,Json格式，可用于指定消息的漫游，存云端历史，发送方多端同步，推送，消息抄送等特殊行为;option中字段不填时表示默认值
	 *            option示例:
	 *            {"push":false,"roam":true,"history":false,"sendersync":true,"route":false,"badge":false,"needPushNick":true}
	 *            字段说明： 1. roam: 该消息是否需要漫游，默认true(需要app开通漫游消息功能); 2. history:
	 *            该消息是否存云端历史，默认true; 3. sendersync: 该消息是否需要发送方多端同步，默认true; 4.
	 *            push: 该消息是否需要APNS推送或安卓系统通知栏推送，默认true; 5. route:
	 *            该消息是否需要抄送第三方；默认true (需要app开通消息抄送功能); 6.
	 *            badge:该消息是否需要计入到未读计数中，默认true; 7. needPushNick:
	 *            推送文案是否需要带上昵称，不设置该参数时默认true;
	 * @param pushContent
	 *            Unnecessary,ios推送内容，不超过150字节，option选项中允许推送（push=true），此字段可以指定推送内容
	 * @param payload
	 *            Unnecessary,ios 推送对应的payload,必须是JSON,不能超过2k字节
	 * @param ext
	 *            Unnecessary,开发者扩展字段，长度限制1024字节
	 * @param forcePushList
	 *            Unnecessary,发送群消息时的强推（@操作）用户列表，格式为JSONArray，如["accid1","accid2"]。若forcepushall为true，则forcepushlist为除发送者外的所有有效群成员
	 * @param forcePushContent
	 *            Unnecessary,发送群消息时，针对强推（@操作）列表forcepushlist中的用户，强制推送的内容
	 * @param forcePushAll
	 *            Unnecessary,发送群消息时，强推（@操作）列表是否为群里除发送者外的所有有效成员，默认为false
	 * @return
	 * @throws IOException
	 * @return String 返回json,
	 *         {"code":"200"},主要返回码200、403、414、416、431、500 @throws
	 */
	public static YXResultSet sendMessage(String from, String ope, String to, String type, String body, 
			String antispam, String antispamCustom,	String option,
			String pushContent, String payload, String ext, String forcePushList, String forcePushContent,
			boolean forcePushAll) throws IOException {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (!from.isEmpty()) {
			nvps.add(new BasicNameValuePair("from", from));
		}
		if (!ope.isEmpty()) {
			nvps.add(new BasicNameValuePair("ope", ope));
		}
		if (!to.isEmpty()) {
			nvps.add(new BasicNameValuePair("to", to));
		}
		if (!type.isEmpty()) {
			nvps.add(new BasicNameValuePair("type", type));
		}
		if (!body.isEmpty()) {
			nvps.add(new BasicNameValuePair("body", body));
		}
		if (!antispam.isEmpty()) {
			nvps.add(new BasicNameValuePair("antispam", antispam));
		}
		if (!antispamCustom.isEmpty()) {
			nvps.add(new BasicNameValuePair("antispamCustom", antispamCustom));
		}
		if (!option.isEmpty()) {
			nvps.add(new BasicNameValuePair("option", option));
		}
		if (!pushContent.isEmpty()) {
			nvps.add(new BasicNameValuePair("pushcontent", pushContent));
		}
		if (!payload.isEmpty()) {
			nvps.add(new BasicNameValuePair("payload", payload));
		}

		if (!ext.isEmpty()) {
			nvps.add(new BasicNameValuePair("ext", ext));
		}
		if (!forcePushList.isEmpty()) {
			nvps.add(new BasicNameValuePair("forcepushlist", forcePushList));
		}
		if (!forcePushContent.isEmpty()) {
			nvps.add(new BasicNameValuePair("forcepushcontent", forcePushContent));
		}
		if (forcePushAll) {
			nvps.add(new BasicNameValuePair("forcepushall", "true"));
		}
		String ret = sendMessage(nvps);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * sendMessage @author songfl @Description:
	 * 给用户或者群发送普通消息，包括文本，图片，语音，视频和地理位置，具体消息参考下面描述，参数传入key-value形式列表，防止参数的对应位置写错 @param
	 * arrayList key-value形式参数列表 @return @throws IOException @return
	 * String @throws
	 */
	public static String sendMessage(List<NameValuePair> arrayList) throws IOException {
		return httpPostMessageRequest("sendMsg.action", arrayList);
	}

	/**
	 * sendBatchMessage 1.给用户发送点对点普通消息，包括文本，图片，语音，视频，地理位置和自定义消息。
	 * 2.最大限500人，只能针对个人,如果批量提供的帐号中有未注册的帐号，会提示并返回给用户。
	 * 3.此接口受频率控制，一个应用一分钟最多调用120次，超过会返回416状态码，并且被屏蔽一段时间； 具体消息参考下面描述。
	 * 
	 * @param fromAccid
	 *            Necessary,发送者accid，用户帐号，最大32字节，必须保证一个APP内唯一
	 * @param toAccids
	 *            Necessary,["aaa","bbb"]（JSONArray对应的accid，如果解析出错，会报414错误），限500人
	 * @param type
	 *            Necessary, 0 表示文本消息, 1 表示图片， 2 表示语音， 3 表示视频， 4 表示地理位置信息， 6
	 *            表示文件， 100 自定义消息类型
	 * @param body
	 *            请参考下方消息示例说明中对应消息的body字段，最大长度5000字节，为一个json串
	 *            *************************** 下面的参数不是必备的，可以传空字符串
	 * @param option
	 *            {"push":false,"roam":true,"history":false,"sendersync":true,"route":false,"badge":false,"needPushNick":true}字段说明：
	 *            1. roam: 该消息是否需要漫游，默认true（需要app开通漫游消息功能）；
	 *            2.history:该消息是否存云端历史，默认true； 3. sendersync:
	 *            该消息是否需要发送方多端同步，默认true；4.push: 该消息是否需要APNS推送或安卓系统通知栏推送，默认true；
	 *            5. route:该消息是否需要抄送第三方；默认true
	 *            (需要app开通消息抄送功能);6.badge:该消息是否需要计入到未读计数中，默认true; 7.
	 *            needPushNick:推送文案是否需要带上昵称，不设置该参数时默认true;
	 * @param pushContent
	 *            Unnecessary,ios推送内容，不超过150字节，option选项中允许推送（push=true），此字段可以指定推送内容
	 * @param payload
	 *            Unnecessary,ios 推送对应的payload,必须是JSON,不能超过2k字节
	 * @param ext
	 *            Unnecessary,开发者扩展字段，长度限制1024字节
	 * @return String "code":"200","unregister":"["a","b"...]" //未注册的帐号
	 *         },主要的返回码200、403、414、416、431、500
	 * @throws IOException
	 */
	public static String sendBatchMessage(String fromAccid, String toAccids, String type, String body, String option,
			String pushContent, String payload, String ext) throws IOException {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (!fromAccid.isEmpty()) {
			nvps.add(new BasicNameValuePair("fromAccid", fromAccid));
		}
		if (!toAccids.isEmpty()) {
			nvps.add(new BasicNameValuePair("toAccids", toAccids));
		}
		if (!type.isEmpty()) {
			nvps.add(new BasicNameValuePair("type", type));
		}
		if (!body.isEmpty()) {
			nvps.add(new BasicNameValuePair("body", body));
		}
		if (!option.isEmpty()) {
			nvps.add(new BasicNameValuePair("option", option));
		}
		if (!pushContent.isEmpty()) {
			nvps.add(new BasicNameValuePair("pushcontent", pushContent));
		}
		if (!payload.isEmpty()) {
			nvps.add(new BasicNameValuePair("payload", payload));
		}
		if (!ext.isEmpty()) {
			nvps.add(new BasicNameValuePair("ext", ext));
		}

		return sendBatchMessage(nvps);
	}

	/**
	 * sendBatchMessage
	 * 
	 * @author songfl
	 * @Description: 批量发送消息. 参数传入key-value形式列表，防止参数的对应位置写错
	 * @param arrayList
	 *            key-value形式参数列表
	 * @throws IOException
	 * @return String
	 */
	public static String sendBatchMessage(List<NameValuePair> arrayList) throws IOException {
		return httpPostMessageRequest("sendBatchMsg.action", arrayList);
	}

	/**
	 * sendAttachMessage
	 * 
	 * @author songfl
	 * @Description: 1.自定义系统通知区别于普通消息，方便开发者进行业务逻辑的通知；
	 *               2.目前支持两种类型：点对点类型和群类型（仅限高级群），根据msgType有所区别。
	 * @param from
	 *            Necessary,发送者accid，用户帐号，最大32字节，APP内唯一
	 * @param msgType
	 *            Necessary,0：点对点自定义通知，1：群消息自定义通知，其他返回414
	 * @param to
	 *            Necessary,msgtype==0是表示accid即用户id，msgtype==1表示tid即群id
	 * @param attach
	 *            Necessary,自定义通知内容，第三方组装的字符串，建议是JSON串，最大长度4096字节
	 *            ****************************************************下面的参数不是必备的，可以传空字符串
	 * @param pushContent
	 *            Unnecessary,推送内容，第三方自己组装的推送内容,不超过150字节, 可以为空字符串
	 * @param payload
	 *            Unnecessary,iOS推送对应的payload,必须是JSON,不能超过2k字节
	 * @param sound
	 *            Unnecessary,如果有指定推送，此属性指定为客户端本地的声音文件名，长度不要超过30个字节，如果不指定，会使用默认声音
	 * @param save
	 *            Unnecessary,1表示只发在线，2表示会存离线，其他会报414错误。默认会存离线
	 * @param option
	 *            Unnecessary,发消息时特殊指定的行为选项,Json格式，可用于指定消息计数等特殊行为;option中字段不填时表示默认值。
	 *            option示例： {"badge":false,"needPushNick":false,"route":false}
	 *            字段说明： 1. badge:该消息是否需要计入到未读计数中，默认true; 2. needPushNick:
	 *            推送文案是否需要带上昵称，不设置该参数时默认false(ps:注意与sendMsg.action接口有别); 3.
	 *            route: 该消息是否需要抄送第三方；默认true (需要app开通消息抄送功能) @return @throws
	 *            IOException
	 */
	public static YXResultSet sendAttachMessage(String from, String msgType, String to, String attach,
			String pushContent, String payload, String sound, String save, String option) throws IOException {
		// "sendAttachMsg.action";
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (!from.isEmpty()) {
			nvps.add(new BasicNameValuePair("from", from));
		}
		if (!msgType.isEmpty()) {
			nvps.add(new BasicNameValuePair("msgtype", msgType));
		}
		if (!to.isEmpty()) {
			nvps.add(new BasicNameValuePair("to", to));
		}
		if (!attach.isEmpty()) {
			nvps.add(new BasicNameValuePair("attach", attach));
		}
		if (!pushContent.isEmpty()) {
			nvps.add(new BasicNameValuePair("pushcontent", pushContent));
		}
		if (!payload.isEmpty()) {
			nvps.add(new BasicNameValuePair("payload", payload));
		}
		if (!sound.isEmpty()) {
			nvps.add(new BasicNameValuePair("sound", sound));
		}
		if (!save.isEmpty()) {
			nvps.add(new BasicNameValuePair("save", save));
		}
		if (!option.isEmpty()) {
			nvps.add(new BasicNameValuePair("option", option));
		}
		String ret = sendAttachMessage(nvps);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * sendAttachMessage @author songfl @Description: 发送自定义系统消息.
	 * 参数传入key-value形式列表，防止参数的对应位置写错 @param arrayList
	 * key-value形式参数列表 @return @throws IOException @return String @throws
	 */
	public static String sendAttachMessage(List<NameValuePair> arrayList) throws IOException {
		return httpPostMessageRequest("sendAttachMsg.action", arrayList);
	}

	/**
	 * sendBatchAttachMessage
	 * 
	 * @author songfl
	 * @Description: 1.系统通知区别于普通消息，应用接收到直接交给上层处理，客户端可不做展示； 2.目前支持类型：点对点类型；
	 *               3.最大限500人，只能针对个人,如果批量提供的帐号中有未注册的帐号，会提示并返回给用户；
	 *               4.此接口受频率控制，一个应用一分钟最多调用120次，超过会返回416状态码，并且被屏蔽一段时间；
	 * @param fromAccid
	 *            Necessary,发送者accid，用户帐号，最大32字节，APP内唯一
	 * @param toAccids
	 *            Necessary,["aaa","bbb"]（JSONArray对应的accid，如果解析出错，会报414错误），最大限500人
	 * @param attach
	 *            Necessary,自定义通知内容，第三方组装的字符串，建议是JSON串，最大长度4096字节
	 *            *************************************************************************************************
	 *            以下参数不是必须的，可以传空字符串
	 * @param pushcontent
	 *            Unnecessary,iOS推送内容，第三方自己组装的推送内容,不超过150字节
	 * @param payload
	 *            Unnecessary,iOS推送对应的payload,必须是JSON,不能超过2k字节
	 * @param sound
	 *            Unnecessary,如果有指定推送，此属性指定为客户端本地的声音文件名，长度不要超过30个字符，如果不指定，会使用默认声音
	 * @param save
	 *            Unnecessary,1表示只发在线，2表示会存离线，其他会报414错误。默认会存离线
	 * @param option
	 *            Unnecessary,发消息时特殊指定的行为选项,Json格式，可用于指定消息计数等特殊行为;option中字段不填时表示默认值。
	 *            option示例： {"badge":false,"needPushNick":false,"route":false}
	 *            字段说明： 1. badge:该消息是否需要计入到未读计数中，默认true; 2. needPushNick:
	 *            推送文案是否需要带上昵称，不设置该参数时默认false(ps:注意与sendBatchMsg.action接口有别)。 3.
	 *            route: 该消息是否需要抄送第三方；默认true (需要app开通消息抄送功能)
	 * @return
	 * @throws IOException
	 */
	public static YXResultSet sendBatchAttachMessage(String fromAccid, String toAccids, String attach,
			String pushcontent, String payload, String sound, String save, String option) throws IOException {
		// "sendBatchAttachMsg.action";
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (!fromAccid.isEmpty()) {
			nvps.add(new BasicNameValuePair("fromAccid", fromAccid));
		}
		if (!toAccids.isEmpty()) {
			nvps.add(new BasicNameValuePair("toAccids", toAccids));
		}
		if (!attach.isEmpty()) {
			nvps.add(new BasicNameValuePair("attach", attach));
		}
		if (!pushcontent.isEmpty()) {
			nvps.add(new BasicNameValuePair("pushcontent", pushcontent));
		}
		if (!payload.isEmpty()) {
			nvps.add(new BasicNameValuePair("payload", payload));
		}
		if (!sound.isEmpty()) {
			nvps.add(new BasicNameValuePair("sound", sound));
		}
		if (!save.isEmpty()) {
			nvps.add(new BasicNameValuePair("save", save));
		}
		if (!option.isEmpty()) {
			nvps.add(new BasicNameValuePair("option", option));
		}
		String ret = sendBatchAttachMessage(nvps);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * sendBatchAttachMessage
	 * 
	 * @author songfl
	 * @Description: 批量发送自定义系统消息.参数传入key-value形式列表，防止参数的对应位置写错
	 * @param arrayList
	 *            key-value形式参数列表
	 * @throws IOException
	 * @return String
	 */
	public static String sendBatchAttachMessage(List<NameValuePair> arrayList) throws IOException {
		return httpPostMessageRequest("sendBatchAttachMsg.action", arrayList);
	}

	/**
	 * uploadFile
	 * 
	 * @author songfl
	 * @Description: 上传文件
	 * @param content
	 *            Necessary,字节流base64串(Base64.encode(bytes)) ，最大15M的字节流
	 * @param type
	 *            Unnecessary,上传文件类型
	 * @throws IOException
	 * @return String 返回json{"code":"200","url":"xxx"}
	 */
	public static String uploadFile(String content, String type) throws IOException {
		// "upload.action";
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (!content.isEmpty()) {
			nvps.add(new BasicNameValuePair("content", content));
		}
		if (!type.isEmpty()) {
			nvps.add(new BasicNameValuePair("type", type));
		}

		return uploadFile(nvps);
	}

	/**
	 * uploadFile
	 * 
	 * @author songfl
	 * @Description: 上传文件, 参数传入key-value形式列表，防止参数的对应位置写错
	 * @param arrayList
	 *            key-value形式参数列表
	 * @throws IOException
	 * @return String
	 */
	public static String uploadFile(List<NameValuePair> arrayList) throws IOException {
		return httpPostMessageRequest("upload.action", arrayList);
	}

	/**
	 * multipartUploadFile
	 * 
	 * @author songfl
	 * @Description: 上传文件, 暂时不可用
	 * @param content
	 *            Necessary,字节流base64串(Base64.encode(bytes)) ，最大15M的字节流
	 * @param type
	 *            Unnecessary,上传文件类型，不是必须参数
	 * @throws IOException
	 * @return String 返回json{"code":"200","url":"xxx"}
	 */
	public static String multipartUploadFile(String content, String type) throws IOException {
		// "fileUpload.action";
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (!content.isEmpty()) {
			nvps.add(new BasicNameValuePair("content", content));
		}
		if (!type.isEmpty()) {
			nvps.add(new BasicNameValuePair("type", type));
		}

		return multipartUploadFile(nvps);
	}

	/**
	 * multipartUploadFile
	 * 
	 * @author songfl
	 * @Description: 批量上传文件. 参数传入key-value形式列表，防止参数的对应位置写错， 暂时不可用
	 * @param arrayList
	 *            key-value形式参数列表
	 * @throws IOException
	 * @return String
	 */
	public static String multipartUploadFile(List<NameValuePair> arrayList) throws IOException {
		return httpPostMessageRequest("fileUpload.action", arrayList);
	}

	/****************************************************************************************
	 * 消息功能到此结束
	 **************************************************************************************/
	// [end]

	// [start]群组

	/****************************************************************************************
	 * 以下是群组相关的功能
	 **************************************************************************************/

	/**
	 * httpPostTeamRequest
	 * 
	 * @author songfl
	 * @Description: 发送群组相关的请求
	 * @param actionType
	 *            http 调用的方法名，为"*.action"形式
	 * @param arrayParams
	 *            http 协议参数, key-value形式的列表
	 * @throws IOException
	 * @return String
	 */
	public static String httpPostTeamRequest(String actionType, List<NameValuePair> arrayParams) throws IOException {
		String url = sYunXinTeamHost + actionType;
		return httpPostWithArraryParam(url, arrayParams);
	}

	/**
	 * createTeam
	 * 
	 * @author songfl
	 * @Description: 1、创建高级群，以邀请的方式发送给用户； 2、custom
	 *               字段是给第三方的扩展字段，第三方可以基于此字段扩展高级群的功能，构建自己需要的群；
	 *               3、建群成功会返回tid，需要保存，以便于加人与踢人等后续操作； 4、每个用户可创建的群数量有限制，限制值由 IM
	 *               套餐的群组配置决定，可登录管理后台查看。
	 * @param tname
	 *            Necessary，群名称，最大长度64字节
	 * @param owner
	 *            Necessary，群主用户帐号，最大长度32字节
	 * @param members
	 *            Necessary，["aaa","bbb"](JSONArray对应的accid，如果解析出错会报414)，一次最多拉200个成员
	 * @param announcement
	 *            Unnecessary，群公告，最大长度1024字节
	 * @param intro
	 *            Unnecessary，群描述，最大长度512字节 @param msg
	 *            Necessary，邀请发送的文字，最大长度150字节
	 * @param magree
	 *            Necessary，管理后台建群时，0不需要被邀请人同意加入群，1需要被邀请人同意才可以加入群。其它会返回414
	 * @param joinmode
	 *            Necessary，群建好后，sdk操作时，0不用验证，1需要验证,2不允许任何人加入。其它返回414
	 * @param custom
	 *            Unnecessary,自定义高级群扩展属性，第三方可以跟据此属性自定义扩展自己的群属性。（建议为json）,最大长度1024字节
	 * @param icon
	 *            Unnecessary,,群头像，最大长度1024字节
	 * @param beinvitemode
	 *            Unnecessary,被邀请人同意方式，0-需要同意(默认),1-不需要同意。其它返回414
	 * @param invitemode
	 *            Unnecessary,谁可以邀请他人入群，0-管理员(默认),1-所有人。其它返回414
	 * @param uptinfomode
	 *            Unnecessary,谁可以修改群资料，0-管理员(默认),1-所有人。其它返回414
	 * @param upcustommode
	 *            Unnecessary,谁可以更新群自定义属性，0-管理员(默认),1-所有人。其它返回414
	 * @throws IOException
	 * @return String {"code":"200", "tid":"11001"}
	 */
	public static CreateTeamResult createTeam(String tname, String owner, String members, String announcement,
			String intro, String msg, String magree, String joinMode, String custom, String icon, String beinviteMode,
			String inviteMode, String uptinfoMode, String upcustomMode) throws IOException {
		List<NameValuePair> arrayParams = new ArrayList<NameValuePair>();
		if (!tname.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("tname", tname));
		}
		if (!owner.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("owner", owner));
		}
		if (!members.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("members", members));
		}
		if (!announcement.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("announcement", announcement));
		}
		if (!intro.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("intro", intro));
		}
		if (!msg.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("msg", msg));
		}
		if (!magree.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("magree", magree));
		}
		if (!joinMode.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("joinmode", joinMode));
		}
		if (!custom.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("custom", custom));
		}
		if (!icon.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("icon", icon));
		}
		if (!beinviteMode.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("beinvitemode", beinviteMode));
		}
		if (!inviteMode.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("invitemode", inviteMode));
		}
		if (!uptinfoMode.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("uptinfomode", uptinfoMode));
		}
		if (!upcustomMode.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("upcustommode", upcustomMode));
		}

		return createTeam(arrayParams);
	}

	public static CreateTeamResult createTeam(List<NameValuePair> arrayParams) throws IOException {
		String ret = httpPostTeamRequest("create.action", arrayParams);
		System.out.println(ret);
		return new JsonUtils().toObject(ret, CreateTeamResult.class);
	}

	/**
	 * addTeamMembers
	 * 
	 * @author songfl
	 * @Description: 1.可以批量邀请，邀请时需指定群主； 2.当群成员达到上限时，再邀请某人入群返回失败；
	 *               3.当群成员达到上限时，被邀请人“接受邀请"的操作也将返回失败。
	 * @param tid
	 *            Necessary，云信服务器产生，群唯一标识，创建群时会返回，最大长度128字节
	 * @param owner
	 *            Necessary，群主用户帐号，最大长度32字节
	 * @param members
	 *            Necessary，["aaa","bbb"](JSONArray对应的accid，如果解析出错会报414)，一次最多拉200个成员
	 * @param magree
	 *            Necessary，管理后台建群时，0不需要被邀请人同意加入群，1需要被邀请人同意才可以加入群。其它会返回414
	 * @param msg
	 *            Necessary，邀请发送的文字，最大长度150字节
	 * @param attach
	 *            Unnecessary,自定义扩展字段，最大长度512
	 * @throws IOException
	 * @return String
	 */
	public static YXResultSet addTeamMembers(String tid, String owner, String members, String magree, String msg,
			String attach) throws IOException {
		List<NameValuePair> arrayParams = new ArrayList<NameValuePair>();

		if (!tid.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("tid", tid));
		}

		if (!owner.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("owner", owner));
		}

		if (!members.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("members", members));
		}

		if (!magree.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("magree", magree));
		}

		if (!msg.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("msg", msg));
		}

		if (!attach.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("attach", attach));
		}

		return addTeamMembers(arrayParams);
	}

	public static YXResultSet addTeamMembers(List<NameValuePair> arrayParams) throws IOException {
		String ret = httpPostTeamRequest("add.action", arrayParams);
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * kickTeamMemeber
	 * 
	 * @author songfl
	 * @Description: 高级群踢人出群，需要提供群主accid以及要踢除人的accid。
	 * @param tid
	 *            Necessary，云信服务器产生，群唯一标识，创建群时会返回，最大长度128字节
	 * @param owner
	 *            Necessary，群主的accid，用户帐号，最大长度32字节
	 * @param member
	 *            Necessary，被移除人的accid，用户账号，最大长度字节
	 * @param attach
	 *            Unnecessary，自定义扩展字段，最大长度512
	 * @throws IOException
	 * @return String
	 */
	public static YXResultSet kickTeamMemeber(String tid, String owner, String member, String attach)
			throws IOException {
		List<NameValuePair> arrayParams = new ArrayList<NameValuePair>();

		if (!tid.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("tid", tid));
		}

		if (!owner.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("owner", owner));
		}

		if (!member.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("member", member));
		}

		if (!attach.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("attach", attach));
		}
		return kickTeamMemeber(arrayParams);
	}

	public static YXResultSet kickTeamMemeber(List<NameValuePair> arrayParams) throws IOException {
		String ret = httpPostTeamRequest("kick.action", arrayParams);
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * removeTeam
	 * 
	 * @author songfl
	 * @Description: 解散群
	 * @param tid
	 *            云信服务器产生，群唯一标识，创建群时会返回，最大长度128字节
	 * @param owner
	 *            群主用户帐号，最大长度32字节
	 * @throws IOException
	 * @return String
	 */
	public static YXResultSet removeTeam(String tid, String owner) throws IOException {
		List<NameValuePair> arrayParams = new ArrayList<NameValuePair>();

		if (!tid.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("tid", tid));
		}

		if (!owner.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("owner", owner));
		}

		return removeTeam(arrayParams);
	}

	public static YXResultSet removeTeam(List<NameValuePair> arrayParams) throws IOException {
		String ret = httpPostTeamRequest("remove.action", arrayParams);
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * updateTeam
	 * 
	 * @author songfl
	 * @Description: 高级群基本信息修改
	 * @param tid
	 *            Necessary,云信服务器产生，群唯一标识，创建群时会返回
	 * @param tname
	 *            Unnecessary,群名称，最大长度64字节
	 * @param owner
	 *            Necessary,群主用户帐号，最大长度32字节
	 * @param announcement
	 *            Unnecessary,群公告，最大长度1024字节
	 * @param intro
	 *            Unnecessary,群描述，最大长度512字节
	 * @param joinmode
	 *            Unnecessary,群建好后，sdk操作时，0不用验证，1需要验证,2不允许任何人加入。其它返回414
	 * @param custom
	 *            Unnecessary,自定义高级群扩展属性，第三方可以跟据此属性自定义扩展自己的群属性。（建议为json）,最大长度1024字节
	 * @param icon
	 *            Unnecessary,群头像，最大长度1024字节
	 * @param beinvtemode
	 *            Unnecessary,被邀请人同意方式，0-需要同意(默认),1-不需要同意。其它返回414
	 * @param invitemode
	 *            Unnecessary,谁可以邀请他人入群，0-管理员(默认),1-所有人。其它返回414
	 * @param uptinfomode
	 *            Unnecessary,谁可以修改群资料，0-管理员(默认),1-所有人。其它返回414
	 * @param upcustommode
	 *            Unnecessary,谁可以更新群自定义属性，0-管理员(默认),1-所有人。其它返回414
	 * @return
	 * @throws IOException
	 */
	public static YXResultSet updateTeam(String tid, String tname, String owner, String announcement, String intro,
			String joinMode, String custom, String icon, String beinvteMode, String inviteMode, String uptinfoMode,
			String upcustomMode) throws IOException {
		List<NameValuePair> arrayParams = new ArrayList<NameValuePair>();
		if (!tid.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("tid", tid));
		}

		if (!tname.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("tname", tname));
		}

		if (!owner.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("owner", owner));
		}

		if (!announcement.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("announcement", announcement));
		}

		if (!intro.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("intro", intro));
		}

		if (!joinMode.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("joinmode", joinMode));
		}

		if (!custom.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("custom", custom));
		}

		if (!icon.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("icon", icon));
		}

		if (!beinvteMode.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("beinvtemode", beinvteMode));
		}

		if (!inviteMode.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("invitemode", inviteMode));
		}

		if (!uptinfoMode.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("uptinfomode", uptinfoMode));
		}

		if (!upcustomMode.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("upcustommode", upcustomMode));
		}
		return updateTeam(arrayParams);
	}

	public static YXResultSet updateTeam(List<NameValuePair> arrayParams) throws IOException {
		String ret = httpPostTeamRequest("update.action", arrayParams);
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * queryTeam
	 * 
	 * @author songfl
	 * @Description: 1、高级群信息与成员列表查询，一次最多查询30个群相关的信息，跟据ope参数来控制是否带上群成员列表；
	 *               2、查询群成员会稍微慢一些，所以如果不需要群成员列表可以只查群信息；
	 *               3、此接口受频率控制，某个应用一分钟最多查询30次，超过会返回416，并且被屏蔽一段时间；
	 *               4、群成员的群列表信息中增加管理员成员admins的返回。
	 * @param tids
	 *            Necessary,群id列表，如["3083","3084"]
	 * @param ope
	 *            Necessary,1表示带上群成员列表，0表示不带群成员列表，只返回群信息
	 * @return
	 * @throws IOException
	 */
	public static String queryTeam(String tids, String ope) throws IOException {
		List<NameValuePair> arrayParams = new ArrayList<NameValuePair>();
		if (!tids.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("tids", tids));
		}
		if (!ope.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("ope", ope));
		}
		return queryTeam(arrayParams);
	}

	public static String queryTeam(List<NameValuePair> arrayParams) throws IOException {
		return httpPostTeamRequest("query.action", arrayParams);
		// return new JsonUtils().toObject(ret, QueryTeamResult.class);
	}

	/**
	 * changeTeamOwner
	 * 
	 * @author songfl
	 * @Description: 1、转换群主身份； 2、群主可以选择离开此群，还是留下来成为普通成员。
	 * @param tid
	 *            Necessary,云信服务器产生，群唯一标识，创建群时会返回，最大长度128字节
	 * @param owner
	 *            Necessary,群主用户帐号，最大长度32字节
	 * @param newOwner
	 *            Necessary,新群主帐号，最大长度32字节
	 * @param leave
	 *            Necessary,1:群主解除群主后离开群，2：群主解除群主后成为普通成员。其它414
	 * @return
	 * @throws IOException
	 */
	public static YXResultSet changeTeamOwner(String tid, String owner, String newOwner, String leave)
			throws IOException {
		List<NameValuePair> arrayParams = new ArrayList<NameValuePair>();
		if (!tid.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("tid", tid));
		}
		if (!owner.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("owner", owner));
		}
		if (!newOwner.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("newowner", newOwner));
		}
		if (!leave.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("leave", leave));
		}
		return changeTeamOwner(arrayParams);

	}

	public static YXResultSet changeTeamOwner(List<NameValuePair> arrayParams) throws IOException {
		String ret = httpPostTeamRequest("changeOwner.action", arrayParams);
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * addTeamManager
	 * 
	 * @author songfl
	 * @Description: 提升普通成员为群管理员，可以批量，但是最多不超过10个人。
	 * @param tid
	 *            Necessary,云信服务器产生，群唯一标识，创建群时会返回，最大长度128字节
	 * @param owner
	 *            Necessary,群主用户帐号，最大长度32字节
	 * @param members
	 *            Necessary,["aaa","bbb"](JSONArray对应的accid，如果解析出错会报414)，长度最大1024字节（群成员最多10个）
	 * @return
	 * @throws IOException
	 */
	public static YXResultSet addTeamManager(String tid, String owner, String members) throws IOException {
		List<NameValuePair> arrayParams = new ArrayList<NameValuePair>();
		if (!tid.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("tid", tid));
		}
		if (!owner.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("owner", owner));
		}
		if (!members.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("members", members));
		}

		return addTeamManager(arrayParams);

	}

	public static YXResultSet addTeamManager(List<NameValuePair> arrayParams) throws IOException {
		String ret = httpPostTeamRequest("addManager.action", arrayParams);
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * removeTeamManager
	 * 
	 * @author songfl
	 * @Description: 解除管理员身份，可以批量，但是最多不超过10个人
	 * @param tid
	 *            Necessary,云信服务器产生，群唯一标识，创建群时会返回，最大长度128字节
	 * @param owner
	 *            Necessary,群主用户帐号，最大长度32字节
	 * @param members
	 *            Necessary,["aaa","bbb"](JSONArray对应的accid，如果解析出错会报414)，长度最大1024字节（群成员最多10个） @return @throws
	 *            IOException
	 */
	public static YXResultSet removeTeamManager(String tid, String owner, String members) throws IOException {
		List<NameValuePair> arrayParams = new ArrayList<NameValuePair>();
		if (!tid.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("tid", tid));
		}
		if (!owner.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("owner", owner));
		}
		if (!members.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("members", members));
		}
		return removeTeamManager(arrayParams);
	}

	public static YXResultSet removeTeamManager(List<NameValuePair> arrayParams) throws IOException {
		String ret = httpPostTeamRequest("removeManager.action", arrayParams);
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * queryUserJoinedTeamsInfo
	 * 
	 * @author songfl
	 * @Description: 获取某个用户所加入高级群的群信息
	 * @param accid
	 *            Necessary,要查询用户的accid
	 * @throws IOException
	 * @return String
	 */
	public static String queryUserJoinedTeamsInfo(String accid) throws IOException {
		List<NameValuePair> arrayParams = new ArrayList<NameValuePair>();
		if (!accid.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("accid", accid));
		}

		return queryUserJoinedTeamsInfo(arrayParams);

	}

	public static String queryUserJoinedTeamsInfo(List<NameValuePair> arrayParams) throws IOException {
		return httpPostTeamRequest("joinTeams.action", arrayParams);
	}

	/**
	 * updateTeamNick
	 * 
	 * @author songfl @Description: 修改指定账号在群内的昵称
	 * @param tid
	 *            Necessary,群唯一标识，创建群时云信服务器产生并返回
	 * @param owner
	 *            Necessary,群主 accid
	 * @param accid
	 *            Necessary,要修改群昵称的群成员 accid
	 * @param nick
	 *            Necessary,accid 对应的群昵称，最大长度32字节
	 * @return
	 * @throws IOException
	 */
	public static YXResultSet updateTeamNick(String tid, String owner, String accid, String nick) throws IOException {
		List<NameValuePair> arrayParams = new ArrayList<NameValuePair>();
		if (!tid.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("tid", tid));
		}
		if (!owner.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("owner", owner));
		}
		if (!accid.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("accid", accid));
		}
		if (!nick.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("nick", nick));
		}

		return updateTeamNick(arrayParams);
	}

	public static YXResultSet updateTeamNick(List<NameValuePair> arrayParams) throws IOException {
		String ret = httpPostTeamRequest("updateTeamNick.action", arrayParams);
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * updateMuteTeamConfig
	 * 
	 * @author songfl
	 * @Description: 高级群修改消息提醒开关
	 * @param tid
	 *            Necessary,云信服务器产生，群唯一标识，创建群时会返回
	 * @param accid
	 *            Necessary,要操作的群成员accid
	 * @param ope
	 *            Necessary,1：关闭消息提醒，2：打开消息提醒，其他值无效
	 * @return
	 * @throws IOException
	 */
	public static YXResultSet updateMuteTeamConfig(String tid, String accid, String ope) throws IOException {
		List<NameValuePair> arrayParams = new ArrayList<NameValuePair>();
		if (!tid.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("tid", tid));
		}
		if (!accid.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("accid", accid));
		}
		if (!ope.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("ope", ope));
		}
		return updateMuteTeamConfig(arrayParams);
	}

	public static YXResultSet updateMuteTeamConfig(List<NameValuePair> arrayParams) throws IOException {
		String ret = httpPostTeamRequest("muteTeam.action", arrayParams);
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * setTeamMuteList
	 * 
	 * @author songfl
	 * @Description: 高级群禁言群成员
	 * @param tid
	 *            Necessary,云信服务器产生，群唯一标识，创建群时会返回
	 * @param owner
	 *            Necessary,群主accid
	 * @param accid
	 *            Necessary,禁言对象的accid
	 * @param mute
	 *            Necessary,1-禁言，0-解禁
	 * @throws IOException
	 * @return String
	 */
	public static YXResultSet setTeamMuteList(String tid, String owner, String accid, String mute) throws IOException {
		List<NameValuePair> arrayParams = new ArrayList<NameValuePair>();
		if (!tid.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("tid", tid));
		}
		if (!owner.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("owner", owner));
		}
		if (!accid.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("accid", accid));
		}
		if (!mute.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("mute", mute));
		}

		return setTeamMuteList(arrayParams);
	}

	public static YXResultSet setTeamMuteList(List<NameValuePair> arrayParams) throws IOException {
		String ret = httpPostTeamRequest("muteTlist.action", arrayParams);
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * leaveTeam
	 * 
	 * @author songfl
	 * @Description: 高级群主动退群
	 * @param tid
	 *            Necessary,云信服务器产生，群唯一标识，创建群时会返回
	 * @param accid
	 *            Necessary,退群的accid
	 * @throws IOException
	 * @return String
	 */
	public static YXResultSet leaveTeam(String tid, String accid) throws IOException {
		List<NameValuePair> arrayParams = new ArrayList<NameValuePair>();
		if (!tid.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("tid", tid));
		}
		if (!accid.isEmpty()) {
			arrayParams.add(new BasicNameValuePair("accid", accid));
		}

		return leaveTeam(arrayParams);
	}

	public static YXResultSet leaveTeam(List<NameValuePair> arrayParams) throws IOException {
		String ret = httpPostTeamRequest("leave.action", arrayParams);
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/****************************************************************************************
	 * 群组相关功能到此结束
	 * 
	 * @throws IOException
	 **************************************************************************************/
	// [end]

	// [start]短信
	/**
	 * 发送短信验证码,向指定的手机号码发送短信验证码
	 * 
	 * @author frank
	 * @param phoneNo
	 *            必须,目标手机号
	 * @param deviceId
	 *            目标设备号，可选参数
	 * @param templateId
	 *            模板编号(如不指定则使用配置的默认模版)
	 * @return
	 * @throws IOException
	 */
	public static YXResultSet sendSmsCode(String phoneNo, String deviceId, String templateId) throws IOException {
		List<NameValuePair> arrayParams = new ArrayList<NameValuePair>();
		arrayParams.add(new BasicNameValuePair("mobile", phoneNo));
		arrayParams.add(new BasicNameValuePair("deviceId", deviceId));
		arrayParams.add(new BasicNameValuePair("templateid", templateId));
		String ret = httpPostWithArraryParam("https://api.netease.im/sms/sendcode.action", arrayParams);
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * 验证短信验证码,校验指定手机号的验证码是否合法
	 * 
	 * @author frank
	 * @param phoneNo
	 *            必须,目标手机号
	 * @param code
	 *            必须,验证码
	 * @return
	 * @throws IOException
	 */
	public static YXResultSet verifySmscode(String phoneNo, String code) throws IOException {
		List<NameValuePair> arrayParams = new ArrayList<NameValuePair>();
		arrayParams.add(new BasicNameValuePair("mobile", phoneNo));
		arrayParams.add(new BasicNameValuePair("code", code));
		String ret = httpPostWithArraryParam("https://api.netease.im/sms/verifycode.action", arrayParams);
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * 发送模板短信,向手机号发送内容格式预定义的短信，整个短信的内容由模板和变量组成。
	 * 
	 * @param templateid
	 *            必须,模板编号(由客户顾问配置之后告知开发者)
	 * @param mobiles
	 *            必须,接收者号码列表，JSONArray格式,如["186xxxxxxxx","186xxxxxxxx"]，限制接收者号码个数最多为100个
	 * @param params
	 *            模板中若含变量则必须包含此参数,短信参数列表，用于依次填充模板，JSONArray格式，如["xxx","yyy"];对于不包含变量的模板，不填此参数表示模板即短信全文内容
	 * @return
	 * @throws IOException
	 */
	public static YXResultSet sendSmsTemplate(String templateid, String mobiles, String params) throws IOException {
		List<NameValuePair> arrayParams = new ArrayList<NameValuePair>();
		arrayParams.add(new BasicNameValuePair("templateid", templateid));
		arrayParams.add(new BasicNameValuePair("mobiles", mobiles));
		arrayParams.add(new BasicNameValuePair("params", params));
		String ret = httpPostWithArraryParam("https://api.netease.im/sms/sendtemplate.action", arrayParams);
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}

	/**
	 * 查询短信状态,根据短信的sendid(sendtemplate.action接口中的返回值)，查询短信发送结果
	 * 
	 * @author frank
	 * @param sendid
	 *            必须,发送短信的编号sendid
	 * @return
	 * @throws IOException
	 */
	public static YXResultSet querySmsStatus(String sendid) throws IOException {
		List<NameValuePair> arrayParams = new ArrayList<NameValuePair>();
		arrayParams.add(new BasicNameValuePair("sendid", sendid));
		String ret = httpPostWithArraryParam("https://api.netease.im/sms/querystatus.action", arrayParams);
		System.out.println(ret);
		return new JsonUtils().toObject(ret, YXResultSet.class);
	}
	// [end]
}
