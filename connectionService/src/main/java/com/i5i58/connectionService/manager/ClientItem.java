//package com.i5i58.connectionService.manager;
//
//import com.i5i58.async.threading.TaskQueue;
//import com.i5i58.base.manager.WsRemotePeer;
//import com.i5i58.base.util.JsonResponse;
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
//		Object rep;
//		if(object instanceof JsonResponse){
//			rep = ((JsonResponse) object).getResponse();
//		}else{
//			rep = object;
//		}
//		return super.send(rep);
//	}
//
//	@Override
//	public ChannelFuture sendAndClose(Object object) {
//		Object rep;
//		if(object instanceof JsonResponse){
//			rep = ((JsonResponse) object).getResponse();
//		}else{
//			rep = object;
//		}
//		return super.sendAndClose(rep);
//	}
//}
