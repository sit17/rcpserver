package com.i5i58.push.handler;

import java.util.UUID;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.i5i58.base.handler.IBusinessHandler;
import com.i5i58.base.manager.IRemotPeer;
import com.i5i58.base.util.JedisWrapper;
import com.i5i58.base.util.JsonRequest;
import com.i5i58.base.util.JsonResponse;
import com.i5i58.base.util.NetworkUtil;
import com.i5i58.push.protocol.CustomRequestIds;
import com.i5i58.push.protocol.MsgCmd;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

public class PushBusinessHandler implements IBusinessHandler {
	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	public boolean beforeServerStart() {
		JedisWrapper.init();
		return true;
	}

	@Override
	public void afterServerStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChannelActive(ChannelHandlerContext ctx, IRemotPeer peer) {
		logger.info("channel active " + peer.getSessionId() + ", ip address " + ctx.channel().remoteAddress().toString());
//		JSONObject object = new JSONObject();
//		JSONObject data = new JSONObject();
//		object.put("cmd", "qrcode");
//		data.put("qrtoken", peer.getSessionId());
//		object.put("data", data);
//		peer.send(object);
	}

	@Override
	public void onChannelInactive(ChannelHandlerContext ctx, IRemotPeer peer) {
		logger.info("channel inactive " + peer.getSessionId());
	}

	@Override
	public void onExceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	}

	@Override
	public void onChannelRead0(IRemotPeer peer, String msg) {
		logger.info("channel read " + peer.getSessionId());
		logger.info("channel read msg " + msg);
		JsonRequest jsonRequest = new JsonRequest();
		if (!jsonRequest.parseObject(msg)){
			logger.error("parse json request failed.");
			return;
		}
		
		String cmd = jsonRequest.getCmd();
		if (cmd == null || cmd.isEmpty()){
			logger.error("request cmd is null or empty");
			return;
		}
		if (cmd.equals(MsgCmd.QrTokenRequest)){
			onQrTokenRequest(jsonRequest, peer);
		}
	}
	
	public void onError(IRemotPeer peer, String cmd, String code, String msg){
		JsonResponse jsonResponse = new JsonResponse();
		jsonResponse.setCmd(cmd);
		if (code != null && !code.isEmpty()){			
			jsonResponse.setValue("code", code);
		}
		if (msg != null && !msg.isEmpty()){			
			jsonResponse.setValue("msg", msg);
		}
		peer.sendAndClose(jsonResponse.getResponse());
	}
	
	public void onQrTokenRequest(JsonRequest jsonRequest, IRemotPeer peer){
		if (jsonRequest.getData() == null){
			logger.error("data is null, full msg is " + jsonRequest.getRequest().toJSONString());
			return;
		}
//		InetSocketAddress inetSocketAddress = (InetSocketAddress) peer.getContext().channel().remoteAddress();
		String ip = NetworkUtil.getHostString(peer.getContext());
		int device = jsonRequest.getIntValue("device");
		int version = jsonRequest.getIntValue("version");
		String serialNum = jsonRequest.getString("serialNum");
		String qrToken = UUID.randomUUID().toString();//.replace("-", "");
		
		JedisWrapper jedisUtils = new JedisWrapper();
		jedisUtils.set(qrToken, peer.getSessionId(), "NX", "EX", 600);
		
		JsonResponse jsonResponse = new JsonResponse();
		jsonResponse.setCmd("qrCode");
		jsonResponse.setValue("qrToken", qrToken);
		jsonResponse.setValue("ip", ip);
		jsonResponse.setValue("device", device);
		jsonResponse.setValue("version", version);
		jsonResponse.setValue("serialNum", serialNum);
		
		peer.send(jsonResponse.getResponse());
	}

	@Override
	public void onDefenseCC(ChannelHandlerContext ctx, String msg) {

	}

	public void onQrLogin(IRemotPeer peer, JsonRequest jsonRequest) {
		logger.info("onQrLogin " + peer.getSessionId());
		logger.info("onQrLogin " + jsonRequest.getJSONObject("result").toJSONString());
		JSONObject object = new JSONObject();
		object.put("cmd", MsgCmd.QrLoginResponse);
		object.put("data", jsonRequest.getJSONObject("result"));
		peer.send(object).addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void onCustomRequest(int cmd, IRemotPeer peer, Object request) {
		switch (cmd) {
		case CustomRequestIds.qrLogin:
			if (request instanceof JsonRequest){				
				onQrLogin(peer, (JsonRequest)request);
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
