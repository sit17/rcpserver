package com.i5i58.push.handler;

import org.apache.log4j.Logger;

import com.i5i58.async.threading.ITaskPool;
import com.i5i58.async.threading.TaskQueue;
import com.i5i58.base.handler.IBusinessHandler;
import com.i5i58.base.manager.IRemotPeer;
import com.i5i58.base.manager.RemotePeerManager;
import com.i5i58.base.manager.WsRemotePeer;
import com.i5i58.base.util.NetworkUtil;
import com.i5i58.push.threading.ChannelActiveTask;
import com.i5i58.push.threading.ChannelInactiveTask;
import com.i5i58.push.threading.ChannelReadTask;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

@io.netty.channel.ChannelHandler.Sharable
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	Logger logger = Logger.getLogger(getClass());

	private IBusinessHandler businessHandler;

	private ITaskPool taskPool;
//
//	private boolean isGate;
//
//	private boolean useBusinessTask;

	public TextWebSocketFrameHandler(IBusinessHandler businessHandler, ITaskPool taskPool) {
		this.businessHandler = businessHandler;
		this.taskPool = taskPool;
//		this.isGate = isGate;
//		this.useBusinessTask = useBusinessTask;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		String sessionId = ctx.channel().id().asLongText();
		TaskQueue queue = taskPool.getMatchWorker();
		queue.incrementPeerCount();
		IRemotPeer peer = new WsRemotePeer(sessionId, ctx, queue);
		RemotePeerManager.put(sessionId, peer);
		ChannelActiveTask task = new ChannelActiveTask(businessHandler, ctx, peer);
		queue.addTask(task);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		String sessionId = ctx.channel().id().asLongText();
		IRemotPeer peer = RemotePeerManager.getClient(sessionId);
		if (peer != null) {
			TaskQueue queue = peer.getTaskQueue();
			if (queue != null) {
				ChannelInactiveTask task = new ChannelInactiveTask(businessHandler, ctx, peer);
				queue.addTask(task);
			}else{
				RemotePeerManager.removeClient(sessionId);
				NetworkUtil.closeGracefully(ctx);
			}
		}else{
			NetworkUtil.closeGracefully(ctx);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		businessHandler.onExceptionCaught(ctx, cause);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		String clientMsg = msg.text();
		logger.info(clientMsg);
		String sessionId = ctx.channel().id().asLongText();
		IRemotPeer peer = RemotePeerManager.getClient(sessionId);
		if (peer == null){
			NetworkUtil.closeGracefully(ctx);
		}else{
			TaskQueue queue = peer.getTaskQueue();
			if (queue == null) {
				RemotePeerManager.removeClient(sessionId);
				NetworkUtil.closeGracefully(ctx);
			}else{
				ChannelReadTask task = new ChannelReadTask(businessHandler, clientMsg, peer);
				queue.addTask(task);
			}
		}
	}
}
