package com.i5i58.base.manager;

import com.alibaba.fastjson.JSONObject;
import com.i5i58.async.threading.TaskQueue;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

public interface IRemotPeer {

	ChannelFuture send(Object object);
	ChannelFuture sendAndClose(Object object);
	
	TaskQueue getTaskQueue();

	void setTaskQueue(TaskQueue taskQueue);

	String getSessionId();
	
	void close();
	
	void closeGracefully();

	ChannelHandlerContext getContext();

	JSONObject getUserData();
}
