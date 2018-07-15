package com.i5i58.connectionService.handler;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.base.handler.IBusinessHandler;
import com.i5i58.base.manager.IRemotPeer;
import com.i5i58.base.util.JedisWrapper;
import com.i5i58.base.util.JsonRequest;
import com.i5i58.base.util.NetworkUtil;
import com.i5i58.connectionService.config.BeanContext;
import com.i5i58.connectionService.protocol.CustomRequestIds;
import com.i5i58.connectionService.protocol.WsResultDataSet;
import com.i5i58.data.channel.HotChannel;
import com.i5i58.util.Constant;
import com.i5i58.util.StringUtils;

import io.netty.channel.ChannelHandlerContext;

public class BusinessHandler implements IBusinessHandler {
	private Logger logger = Logger.getLogger(getClass());

	public static final String CHANNEL_WELCOME_MESSAGE = "胖虎官方倡导绿色直播，对直播内容24小时在线巡查。任何传播违法、违规、低俗、暴力等不良信息将会封停账号。安全提示：聊天中若有涉及财产安全信息，一定要先核实对方身份，谨防受骗！";

	@Override
	public boolean beforeServerStart() {
		return true;
	}

	@Override
	public void afterServerStart() {

	}

	@Override
	public void onChannelActive(ChannelHandlerContext ctx, IRemotPeer peer) {
		System.out.println("channel active " + peer.getSessionId() + ", ip address " + ctx.channel().remoteAddress().toString());
		HashMap<String, Object> response = new HashMap<String, Object>();

		response.put("cmd", "welcome");
		response.put("data", CHANNEL_WELCOME_MESSAGE);
		
		peer.send(response);
	}

	@Override
	public void onChannelInactive(ChannelHandlerContext ctx, IRemotPeer peer) {
		if (peer == null)
			return;
		System.out.println("channel inactive " + peer.getSessionId());
		String sessionId = peer.getSessionId();
		JSONObject userData = peer.getUserData();
		String cId = userData.getString("cId");
		String clientIp = NetworkUtil.getHostString(ctx);
		String accId = userData.getString("accId");
		String token = userData.getString("token");
		
		System.out.println(String.format("[%s]:disconnected [cId:%s] from [%s] by [accId:%s, token:%s]", sessionId,
				cId, clientIp, accId, token));
		if (!StringUtils.StringIsEmptyOrNull(accId)
				&& !StringUtils.StringIsEmptyOrNull(cId)) {
			ResultDataSet rds = BeanContext.getChannelPlay().exitChannel(accId, cId, sessionId);
			System.out.println(rds.getCode());
			System.out.println(rds.getMsg());
		}
	}

	@Override
	public void onExceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	}

	@Override
	public void onChannelRead0(IRemotPeer peer, String msg) {
		if (peer == null)
			return;
		
		System.out.println("来自客户端的消息:" + msg);
		JSONObject request = JSON.parseObject(msg);
		if (request == null){
			logger.error("消息解析错误 " + msg);
			peer.closeGracefully();
			return;
		}
		boolean succeed = false;
		switch (request.getString("cmd")) {
		case "config":
			succeed = onConfig(request.getJSONObject("params"), peer);
			break;
		case "guest":
			succeed = onGuest(request.getJSONObject("params"), peer);
			break;
		case "enter":
			succeed = onEnter(request.getJSONObject("params"), peer);
			break;
		case "reEnter":
			succeed = onReEnter(request.getJSONObject("params"), peer);
			break;
		case "gift":
			succeed = onGift(request.getJSONObject("params"), peer);
			break;
		case "driftComment":
			succeed = onDriftComment(request.getJSONObject("params"), peer);
			break;
		}
		if(!succeed){
			peer.closeGracefully();
		}
	}

	private boolean verifyAuth(String accId, String token) {
		if (!StringUtils.StringIsEmptyOrNull(accId) && !StringUtils.StringIsEmptyOrNull(token)) {
			// 验证token
			// String key = Constant.HOT_ACCOUNT_TOKEN_SET_KEY + accId;
			String key = Constant.HOT_ACCOUNT_TOKEN_SET_KEY + accId;
			if (new JedisWrapper().exist(key) && new JedisWrapper().get(key).equals(token)) {
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
	
	public boolean onConfig(JSONObject params, IRemotPeer peer){
		int configDevice = params.getIntValue("device");
		String configGiftVersion = params.getString("giftVersion");
		String configMountVersion = params.getString("mountVersion");
		String configAnimationVersion = params.getString("animationVersion");
		ResultDataSet rds = BeanContext.getChannelPlay().getChannelConfig(configDevice, configGiftVersion, configMountVersion,
				configAnimationVersion);
		if (rds.getCode().equals(ResultCode.SUCCESS.getCode())) {
			WsResultDataSet wrds = new WsResultDataSet(rds);
			wrds.setCmd("config");
			peer.send(wrds);
			return true;
		} else {
			System.out.println("service : code:" + rds.getCode() + " msg:" + rds.getMsg());
			return false;
		}
	}

	public boolean onGuest(JSONObject params, IRemotPeer peer){
		JSONObject userData = peer.getUserData();
		int guestDevice = params.getIntValue("device");
		String guestCId = params.getString("cId");
		ResultDataSet rds = BeanContext.getChannelPlay().enterChannelNoAcc(guestDevice, guestCId);
		System.out.println(rds.getCode());
		System.out.println(rds.getData());
		if (rds.getCode().equals(ResultCode.SUCCESS.getCode())) {
			userData.put("device", guestDevice);
			userData.put("cId", guestCId);
			WsResultDataSet wrds = new WsResultDataSet(rds);
			wrds.setCmd("guest");
			peer.send(wrds);
			return true;
		} else {
			System.out.println("service : code:" + rds.getCode() + " msg:" + rds.getMsg());
			return false;
		}
	}
	
	public boolean onEnter(JSONObject params, IRemotPeer peer){
		JSONObject userData = peer.getUserData();
		String accId = params.getString("accId");
		String token = params.getString("token");
		int device = params.getIntValue("device");
		boolean authed = verifyAuth(accId, token);
		if (!authed) {
			return false;
		}
		userData.put("authed", !authed);
		System.out.println("verifyAuth :" + authed);
		String cId = params.getString("cId");
		if (StringUtils.StringIsEmptyOrNull(cId)) {
			logger.error(String.format("enter: cId is null:accId:%s, device:%s", accId, device));
			return false;
		}

		ResultDataSet rds = BeanContext.getChannelPlay().enterChannel(accId, device, cId, peer.getSessionId());
		System.out.println(rds.getCode());
		System.out.println(rds.getData());
		if (rds.getCode().equals(ResultCode.SUCCESS.getCode())) {
			userData.put("accId", accId);
			userData.put("token", token);
			userData.put("device", device);
			userData.put("cId", cId);
			WsResultDataSet wrds = new WsResultDataSet(rds);
			wrds.setCmd("enter");
			peer.send(wrds);
			
			if (device == 2) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> res = (HashMap<String, Object>) rds.getData();
				ResultDataSet rds1 = BeanContext.getChannelPlay()
						.getChatRoomAddr(((HotChannel) res.get("channel")).getYunXinRId(), accId);
				WsResultDataSet wrds1 = new WsResultDataSet(rds1);
				wrds1.setCmd("chatAddr");
				peer.send(wrds1);
			}
			return true;
		} else {
			System.out.println("service : code:" + rds.getCode() + " msg:" + rds.getMsg());
		}
		
		return false;
	}
	
	public boolean onReEnter(JSONObject params, IRemotPeer peer){
		JSONObject userData = peer.getUserData();
		String reAccId = params.getString("accId");
		String reToken = params.getString("token");
		int reDevice = params.getIntValue("device");
		boolean authed = verifyAuth(reAccId, reToken);
		if (!authed) {
			return false;
		}
		
		userData.put("authed", authed);
		
		System.out.println("verifyAuth :" + authed);
		String reCId = params.getString("cId");
		if (StringUtils.StringIsEmptyOrNull(reCId)) {
			logger.error("enter: cId is null");
			return false;
		}
		ResultDataSet rds = BeanContext.getChannelPlay().reEnterChannel(reAccId, reDevice, reCId, peer.getSessionId());
		System.out.println(rds.getCode());
		System.out.println(rds.getData());
		if (rds.getCode().equals(ResultCode.SUCCESS.getCode())) {
			userData.put("accId", reAccId);
			userData.put("token", reToken);
			userData.put("device", reDevice);
			userData.put("cId", reCId);
			WsResultDataSet wrds = new WsResultDataSet(rds);
			wrds.setCmd("reEnter");
			peer.send(wrds);
			if (reDevice == 2) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> res = (HashMap<String, Object>) rds.getData();
				ResultDataSet rds1 = BeanContext.getChannelPlay()
						.getChatRoomAddr(((HotChannel) res.get("channel")).getYunXinRId(), reAccId);
				WsResultDataSet wrds1 = new WsResultDataSet(rds1);
				wrds1.setCmd("chatAddr");
				peer.send(wrds1);
			}
			return true;
		} else {
			System.out.println("service : code:" + rds.getCode() + " msg:" + rds.getMsg());
		}
		
		return false;
	}
	
	public boolean onGift(JSONObject params, IRemotPeer peer){
		JSONObject userData = peer.getUserData();
		boolean authed = userData.getBooleanValue("authed");
		if (!authed){
			return false;
		}
		String accId = userData.getString("accId");
		String token = userData.getString("token");
		int device = userData.getIntValue("device");
		String cId = userData.getString("cId");
		String clientIp = NetworkUtil.getHostString(peer.getContext());
		
		ResultDataSet rds = BeanContext.getChannelPlay().giveChannelGift(accId, device,
				cId, params.getIntValue("id"),
				params.getIntValue("ct"),
				params.getIntValue("ctis"),
				clientIp);
		WsResultDataSet wrds = new WsResultDataSet(rds);
		wrds.setCmd("gift");
		peer.send(wrds);
		return true;
	}
	
	public boolean onDriftComment(JSONObject params, IRemotPeer peer){
		JSONObject userData = peer.getUserData();
		boolean authed = userData.getBooleanValue("authed");
		if(!authed){
			return false;
		}
		String accId = userData.getString("accId");
		String token = userData.getString("token");
		int device = userData.getIntValue("device");
		String cId = userData.getString("cId");
		String clientIp = NetworkUtil.getHostString(peer.getContext());
		ResultDataSet rds = BeanContext.getChannelPlay().driftComment(accId, device, cId,
				params.getString("content"), clientIp);
		WsResultDataSet wrds = new WsResultDataSet(rds);
		wrds.setCmd("driftComment");
		peer.send(wrds);
		return true;
	}
	
	@Override
	public void onDefenseCC(ChannelHandlerContext ctx, String msg) {

	}


	@Override
	public void onCustomRequest(int cmd, IRemotPeer peer, Object request) {
		switch (cmd) {
		case CustomRequestIds.qrLogin:
			if (request instanceof JsonRequest){		
				
			}else{
				logger.error("request type unknown " + request.getClass());
			}
			break;

		default:
			logger.error("unknown request cmd = " + cmd);
			break;
		}
	}
}
