package com.i5i58.service.android.channel.async;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.i5i58.data.im.YxCustomMsg;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.StringUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CodeToString;
import com.i5i58.yunxin.Utils.YXResultSet;

public class TaskYxChatDriftComment implements Runnable {

	private Logger logger = Logger.getLogger(getClass());

	private String roomId;
	private String accId;

	private MsgYxChatDriftComment msg;

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public TaskYxChatDriftComment(String roomId, String accId, String fromName, String faceSmallUrl,
			int vip, int guard, long richScore, int fansClub, String clubName, String content) {
		super();
		this.roomId = roomId;
		this.accId = accId;
		this.msg = new MsgYxChatDriftComment();
		this.msg.setAccId(accId);
		this.msg.setName(fromName);
		this.msg.setFace(faceSmallUrl);
		this.msg.setVip(vip);
		this.msg.setGuard(guard);
		this.msg.setRichScore(richScore);
		this.msg.setFansClub(fansClub);
		this.msg.setClubName(clubName);
		this.msg.setContent(content);
	}

	@Override
	public void run() {

		String uuid = StringUtils.createUUID();
		YXResultSet resultR;
		try {
			YxCustomMsg yxChatMsg = new YxCustomMsg();
			yxChatMsg.setCmd("driftComment");
			yxChatMsg.setData(this.msg);
			resultR = YunxinIM.sendChatRoomMsg(roomId, uuid, accId, "100", "0", "", new JsonUtils().toJson(yxChatMsg));
			if (!"200".equals(resultR.getCode())) {
				System.out.println(CodeToString.getString(resultR.getCode()));
			}
		} catch (IOException e) {
			logger.error("", e);
		}
	}

}
