//package com.i5i58.push.manager;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.i5i58.async.threading.TaskQueue;
//import com.i5i58.base.manager.WsRemotePeer;
//
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelHandlerContext;
//
//public class ClientItem extends WsRemotePeer {
//
//	public ClientItem(String sessionId, ChannelHandlerContext ctx, TaskQueue taskQueue) {
//		super(sessionId, ctx, taskQueue);
//	}
//	
//	@Override
//	public ChannelFuture send(Object object) {
//		String json;
//		if(object instanceof String){
//			json = (String) object;
//		}else if(object instanceof JSONObject){
//			json = ((JSONObject)object).toJSONString();
//		}else{
//			json = JSON.toJSONString(object);
//		}
//		return super.send(json);
//	}
//
//	@Override
//	public ChannelFuture sendAndClose(Object object) {
//		String json;
//		if(object instanceof String){
//			json = (String) object;
//		}else if(object instanceof JSONObject){
//			json = ((JSONObject)object).toJSONString();
//		}else{
//			json = JSON.toJSONString(object);
//		}
//		return super.sendAndClose(json);
//	}
//}
