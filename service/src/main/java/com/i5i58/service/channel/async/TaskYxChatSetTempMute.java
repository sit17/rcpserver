package com.i5i58.service.channel.async;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.i5i58.data.im.YxCustomMsg;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.StringUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CodeToString;
import com.i5i58.yunxin.Utils.YXResultSet;

public class TaskYxChatSetTempMute implements Runnable {
	private Logger logger = Logger.getLogger(getClass());

	private String roomId;
	private String accId;
	private JsonUtils jsonUtil;

	private MsgYxChatSetMute msg;

	public TaskYxChatSetTempMute(String roomId, String admin, String accId, String toName, Long duration, JsonUtils jsonUtils) {
		super();
		jsonUtil = jsonUtils;
		this.roomId = roomId;
		this.accId = admin;
		this.msg = new MsgYxChatSetMute();
		
		if (duration > 0){
			this.msg.setContent(toName + " 被 管理员 禁言");
			this.msg.setMuted("true");
		}else{
			this.msg.setContent(toName + " 被 管理员 解除禁言");
			this.msg.setMuted("false");
		}
		this.msg.setAccId(accId);
	}

	@Override
	public void run() {
		String uuid = StringUtils.createUUID();
		YXResultSet resultR;
		try {
			YxCustomMsg yxChatMsg = new YxCustomMsg();
			yxChatMsg.setCmd("setMute");
			yxChatMsg.setData(this.msg); 
			resultR = YunxinIM.sendChatRoomMsg(roomId, uuid, accId, "100", "0", "", jsonUtil.toJson(yxChatMsg));
			if (!"200".equals(resultR.getCode())) {
				System.out.println(CodeToString.getString(resultR.getCode()));
			}
		} catch (IOException e) {
			logger.error("", e);
		}
	}

}
