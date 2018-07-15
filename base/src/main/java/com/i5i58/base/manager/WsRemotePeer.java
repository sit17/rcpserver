package com.i5i58.base.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.i5i58.async.threading.TaskQueue;
import com.i5i58.base.util.NetworkUtil;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WsRemotePeer implements IRemotPeer {

	protected String sessionId;
	protected ChannelHandlerContext ctx;
	protected JSONObject userData;
	protected TaskQueue taskQueue;

	public WsRemotePeer(String sessionId, ChannelHandlerContext ctx, TaskQueue taskQueue) {
		this.sessionId = sessionId;
		this.ctx = ctx;
		this.taskQueue = taskQueue;
		userData = new JSONObject();
	}

	@Override
	public ChannelFuture send(Object object) {
		String json;
		if(object instanceof String){
			json = (String) object;
		}else if(object instanceof JSONObject){
			json = ((JSONObject)object).toJSONString();
		}else{
			json = JSON.toJSONString(object);
		}
		return ctx.writeAndFlush(new TextWebSocketFrame(json)); 
	}

	@Override
	public ChannelFuture sendAndClose(Object object) {
		String json;
		if(object instanceof String){
			json = (String) object;
		}else if(object instanceof JSONObject){
			json = ((JSONObject)object).toJSONString();
		}else{
			json = JSON.toJSONString(object);
		}
		return ctx.writeAndFlush(new TextWebSocketFrame(json)).addListener(ChannelFutureListener.CLOSE); 
	}

	@Override
	public JSONObject getUserData() {
		return userData;
	}
	
	@Override
	public TaskQueue getTaskQueue() {
		return taskQueue;
	}

	@Override
	public void setTaskQueue(TaskQueue taskQueue) {
		this.taskQueue = taskQueue;
	}

	@Override
	public String getSessionId() {
		return sessionId;
	}
	
	@Override
	public void close() {
		ctx.close();
	}

	@Override
	public void closeGracefully() {
		NetworkUtil.closeGracefully(ctx);
	}

	@Override
	public ChannelHandlerContext getContext() {
		return ctx;
	}
}
