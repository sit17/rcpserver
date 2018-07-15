package com.i5i58.service.channel.async;

import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.i5i58.data.im.YxCustomMsg;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.StringUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CodeToString;
import com.i5i58.yunxin.Utils.YXResultSet;

public class TaskYxZegoCreateCallback implements Runnable {
	private Logger logger = Logger.getLogger(getClass());

	private String roomId;
	private String accId;
	private String rtmpUrl;
	private String hdlUrl;
	private String hlsUrl;
	private String streamAlias;
	private JsonUtils jsonUtil;

	public TaskYxZegoCreateCallback(String roomId, String accId, String rtmpUrl, 
			String hdlUrl, String hlsUrl, String streamAlias, JsonUtils jsonUtils) {
		super();
		jsonUtil = jsonUtils;
		this.roomId = roomId;
		this.accId = accId;
		this.rtmpUrl = rtmpUrl;
		this.hdlUrl = hdlUrl;
		this.hlsUrl = hlsUrl;
		this.streamAlias = streamAlias;
	}

	@Override
	public void run() {
		String uuid = StringUtils.createUUID();
		YXResultSet resultR;
		try {
			HashMap<String, Object> data = new HashMap<>();
			data.put("rtmpUrl", rtmpUrl);
			data.put("hdlUrl", hdlUrl);
			data.put("hlsUrl", hlsUrl);
			data.put("streamAlias", streamAlias);
			
			YxCustomMsg yxChatMsg = new YxCustomMsg();
			yxChatMsg.setCmd("zegoCreateCb");
			yxChatMsg.setData(data);
			
			resultR = YunxinIM.sendChatRoomMsg(roomId, uuid, accId, "100", "0", "", jsonUtil.toJson(yxChatMsg));
			if (!"200".equals(resultR.getCode())) {
				System.out.println(CodeToString.getString(resultR.getCode()));
			}
		} catch (IOException e) {
			logger.error("", e);
		}
	}
}
