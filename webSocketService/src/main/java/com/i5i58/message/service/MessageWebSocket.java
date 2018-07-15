package com.i5i58.message.service;

import java.io.IOException;
import java.util.HashMap;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.data.channel.HotChannel;
import com.i5i58.message.config.BeanContext;
import com.i5i58.util.Constant;
import com.i5i58.util.JedisUtils;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.StringUtils;
import com.i5i58.util.web.HttpUtils;

@ServerEndpoint("/websocket")
@Component
public class MessageWebSocket {

	private Logger logger = Logger.getLogger(getClass());

	// private static int onlineCount = 0;

	// private static CopyOnWriteArraySet<MessageWebSocket> webSocketSet = new
	// CopyOnWriteArraySet<>();

	private static HashMap<String, Player> players = new HashMap<String, Player>();

	/**
	 * 频道欢迎词
	 */
	public static final String CHANNEL_WELCOME_MESSAGE = "胖虎官方倡导绿色直播，对直播内容24小时在线巡查。任何传播违法、违规、低俗、暴力等不良信息将会封停账号。安全提示：聊天中若有涉及财产安全信息，一定要先核实对方身份，谨防受骗！";

	// @Reference
	// private IChannelPlay channelPlay;

	@OnOpen
	public void onOpen(Session session) {
		String sessionId = session.getRequestParameterMap().get("httpRequest").get(0);
		System.out.println("sessionId = " + sessionId);
		if (players.containsKey(sessionId)) {
			try {
				session.close();
			} catch (IOException e) {
				logger.error("", e);
			}
			return;
		}
		players.put(sessionId, new Player(session));

		// if (channelPlay == null) {
		// channelPlay = (IChannelPlay)
		// BeanContext.getDubboContext().getBean("channelPlay");
		// }
		try {
			HashMap<String, Object> response = new HashMap<String, Object>();

			response.put("cmd", "welcome");
			response.put("data", CHANNEL_WELCOME_MESSAGE);
			sendMessage(session, new JsonUtils().toJson(response));
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {
		System.out.println("seesion closed.");
		String sessionId = session.getRequestParameterMap().get("httpRequest").get(0);
		if (players.containsKey(sessionId)) {
			System.out.println(String.format("[%s]:disconnected [cId:%s] from [%s] by [accId:%s, token:%s]", sessionId,
					players.get(sessionId).getcId(), players.get(sessionId).getRemoteAddress(),
					players.get(sessionId).getAccId(), players.get(sessionId).getToken()));
			if (!StringUtils.StringIsEmptyOrNull(sessionId) && players.containsKey(sessionId)
					&& !StringUtils.StringIsEmptyOrNull(players.get(sessionId).getAccId())
					&& !StringUtils.StringIsEmptyOrNull(players.get(sessionId).getcId())) {
				ResultDataSet rds = BeanContext.getChannelPlay().exitChannel(players.get(sessionId).getAccId(),
						players.get(sessionId).getcId(), sessionId);
				System.out.println(rds.getCode());
				System.out.println(rds.getMsg());
			}
			players.remove(sessionId);
		}
	}

	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		String sessionId = session.getRequestParameterMap().get("httpRequest").get(0);
		if (!players.containsKey(sessionId)) {
			return;
		}
		Player player = players.get(sessionId);
		System.out.println("来自客户端的消息:" + message);
		RequestMessage request = new JsonUtils().toObject(message, RequestMessage.class);
		ResultDataSet rds = new ResultDataSet();
		switch (request.getCmd()) {
		case "config":
			int configDevice = Integer.parseInt(request.getParams().get("device"));
			String configGiftVersion = request.getParams().get("giftVersion");
			String configMountVersion = request.getParams().get("mountVersion");
			String configAnimationVersion = request.getParams().get("animationVersion");
			rds = BeanContext.getChannelPlay().getChannelConfig(configDevice, configGiftVersion, configMountVersion,
					configAnimationVersion);
			if (rds.getCode().equals(ResultCode.SUCCESS.getCode())) {
				WsResultDataSet wrds = new WsResultDataSet(rds);
				wrds.setCmd("config");
				sendMessage(session, new JsonUtils().toJson(wrds));
			} else {
				System.out.println("service : code:" + rds.getCode() + " msg:" + rds.getMsg());
				session.close();
				return;
			}
			break;
		case "guest":
			int guestDevice = Integer.parseInt(request.getParams().get("device"));
			String guestCId = request.getParams().get("cId");
			rds = BeanContext.getChannelPlay().enterChannelNoAcc(guestDevice, guestCId);
			System.out.println(rds.getCode());
			System.out.println(rds.getData());
			if (rds.getCode().equals(ResultCode.SUCCESS.getCode())) {
				player.setDevice(guestDevice);
				player.setcId(guestCId);
				WsResultDataSet wrds = new WsResultDataSet(rds);
				wrds.setCmd("guest");
				sendMessage(session, new JsonUtils().toJson(wrds));
			} else {
				System.out.println("service : code:" + rds.getCode() + " msg:" + rds.getMsg());
				session.close();
				return;
			}
			break;
		case "enter":
			String accId = request.getParams().get("accId");
			String token = request.getParams().get("token");
			int device = Integer.parseInt(request.getParams().get("device"));
			if (verifyAuth(accId, token)) {
				player.setAuthed(true);
			}
			System.out.println("verifyAuth :" + player.isAuthed());
			String cId = request.getParams().get("cId");
			if (StringUtils.StringIsEmptyOrNull(cId)) {
				logger.error(String.format("enter: cId is null:accId:%s, device:%s", accId, device));
				session.close();
				return;
			}
			if (player.isAuthed()) {
				rds = BeanContext.getChannelPlay().enterChannel(accId, device, cId, sessionId);
				System.out.println(rds.getCode());
				System.out.println(rds.getData());
				if (rds.getCode().equals(ResultCode.SUCCESS.getCode())) {
					player.setAccId(accId);
					player.setToken(token);
					player.setDevice(device);
					player.setcId(cId);
					WsResultDataSet wrds = new WsResultDataSet(rds);
					wrds.setCmd("enter");
					sendMessage(player.getSession(), new JsonUtils().toJson(wrds));
					if (device == 2) {
						@SuppressWarnings("unchecked")
						HashMap<String, Object> res = (HashMap<String, Object>) rds.getData();
						ResultDataSet rds1 = BeanContext.getChannelPlay()
								.getChatRoomAddr(((HotChannel) res.get("channel")).getYunXinRId(), accId);
						WsResultDataSet wrds1 = new WsResultDataSet(rds1);
						wrds1.setCmd("chatAddr");
						sendMessage(player.getSession(), new JsonUtils().toJson(wrds1));
					}
				} else {
					System.out.println("service : code:" + rds.getCode() + " msg:" + rds.getMsg());
					session.close();
					return;
				}
			} else {
				session.close();
				return;
			}
			break;
		case "reEnter":
			String reAccId = request.getParams().get("accId");
			String reToken = request.getParams().get("token");
			int reDevice = Integer.parseInt(request.getParams().get("device"));
			if (verifyAuth(reAccId, reToken)) {
				player.setAuthed(true);
			}
			System.out.println("verifyAuth :" + player.isAuthed());
			String reCId = request.getParams().get("cId");
			if (StringUtils.StringIsEmptyOrNull(reCId)) {
				logger.error("enter: cId is null");
				session.close();
				return;
			}
			if (player.isAuthed()) {
				rds = BeanContext.getChannelPlay().reEnterChannel(reAccId, reDevice, reCId, sessionId);
				System.out.println(rds.getCode());
				System.out.println(rds.getData());
				if (rds.getCode().equals(ResultCode.SUCCESS.getCode())) {
					player.setAccId(reAccId);
					player.setToken(reToken);
					player.setDevice(reDevice);
					player.setcId(reCId);
					WsResultDataSet wrds = new WsResultDataSet(rds);
					wrds.setCmd("reEnter");
					sendMessage(player.getSession(), new JsonUtils().toJson(wrds));
					if (reDevice == 2) {
						@SuppressWarnings("unchecked")
						HashMap<String, Object> res = (HashMap<String, Object>) rds.getData();
						ResultDataSet rds1 = BeanContext.getChannelPlay()
								.getChatRoomAddr(((HotChannel) res.get("channel")).getYunXinRId(), reAccId);
						WsResultDataSet wrds1 = new WsResultDataSet(rds1);
						wrds1.setCmd("chatAddr");
						sendMessage(player.getSession(), new JsonUtils().toJson(wrds1));
					}
				} else {
					System.out.println("service : code:" + rds.getCode() + " msg:" + rds.getMsg());
					session.close();
					return;
				}
			} else {
				session.close();
				return;
			}
			break;
		case "gift":
			if (player.isAuthed()) {
				rds = BeanContext.getChannelPlay().giveChannelGift(player.getAccId(), player.getDevice(),
						player.getcId(), Integer.parseInt(request.getParams().get("id")),
						Integer.parseInt(request.getParams().get("ct")),
						Integer.parseInt(request.getParams().get("ctis")),
						player.getRemoteAddress());
				WsResultDataSet wrds = new WsResultDataSet(rds);
				wrds.setCmd("gift");
				sendMessage(player.getSession(), new JsonUtils().toJson(wrds));
			} else {
				session.close();
				return;
			}
			break;
		case "driftComment":
			if (player.isAuthed()) {
				rds = BeanContext.getChannelPlay().driftComment(player.getAccId(), player.getDevice(), player.getcId(),
						request.getParams().get("content"),player.getRemoteAddress());
				WsResultDataSet wrds = new WsResultDataSet(rds);
				wrds.setCmd("driftComment");
				sendMessage(player.getSession(), new JsonUtils().toJson(wrds));
			} else {
				session.close();
				return;
			}
			break;
		}
		// session.getBasicRemote().sendText(message);
		// 群发消息
		/*
		 * for (MessageWebSocket item : webSocketSet) {
		 * item.sendMessage(message); }
		 */
	}

	private boolean verifyAuth(String accId, String token) {
		if (!StringUtils.StringIsEmptyOrNull(accId) && !StringUtils.StringIsEmptyOrNull(token)) {
			// 验证token
			// String key = Constant.HOT_ACCOUNT_TOKEN_SET_KEY + accId;
			String key = Constant.HOT_ACCOUNT_TOKEN_SET_KEY + accId;
			if (new JedisUtils().exist(key) && new JedisUtils().get(key).equals(token)) {
				// 如果token验证成功，将token对应的用户id存在request中，便于之后注入
				return true;
			} else {
				System.out.println("token failed");
				return false;
			}
		} else {
			System.out.println("token failed:params null");
			return false;
		}
	}

	@OnError
	public void onError(Session session, Throwable t) throws Throwable {
		logger.error("", t);
	}

	public void sendMessage(Session session, String message) throws IOException {
		if (session != null && session.isOpen())
			session.getAsyncRemote().sendText(message);
	}

	/*
	 * public static synchronized int getOnlineCount() { return
	 * MessageWebSocket.onlineCount; }
	 * 
	 * public static synchronized void addOnlineCount() {
	 * MessageWebSocket.onlineCount++; }
	 * 
	 * public static synchronized void subOnlineCount() {
	 * MessageWebSocket.onlineCount--; }
	 */

}