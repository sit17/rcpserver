package com.i5i58.service.channel.async;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.i5i58.data.im.YxCustomMsg;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.StringUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CodeToString;
import com.i5i58.yunxin.Utils.YXResultSet;

public class TaskYxChatKick implements Runnable {

	private Logger logger = Logger.getLogger(getClass());

	private String roomId;
	private String adminId;
	private JsonUtils jsonUtil;

	private MsgYxChatIdentity msg;

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public TaskYxChatKick(String roomId, String adminId, String accId, String name, String face, long richScore, long score, int vip,
			long vipDeadLine, int guard, long guardDeadLine, int fansClub, String clubName, int clubLevel,
			long fansClubDeadLine, boolean superUser, JsonUtils jsonUtils) {
		super();
		jsonUtil = jsonUtils;
		this.roomId = roomId;
		this.adminId = adminId;
		this.msg = new MsgYxChatIdentity();
		this.msg.setAccId(accId);
		this.msg.setName(name);
		this.msg.setFace(face);
		this.msg.setVip(vip);
		this.msg.setVipDeadLine(vipDeadLine);
		this.msg.setGuard(guard);
		this.msg.setGuardDeadLine(guardDeadLine);
		this.msg.setRichScore(richScore);
		this.msg.setScore(score);
		this.msg.setFansClub(fansClub);
		this.msg.setClubName(clubName);
		this.msg.setClubLevel(clubLevel);
		this.msg.setFansClubDeadLine(fansClubDeadLine);
		this.msg.setSuperUser(superUser);
	}

	@Override
	public void run() {

		String uuid = StringUtils.createUUID();
		YXResultSet resultR;
		try {
			YxCustomMsg yxChatMsg = new YxCustomMsg();
			yxChatMsg.setCmd("kick");
			yxChatMsg.setData(this.msg);
			resultR = YunxinIM.sendChatRoomMsg(roomId, uuid, adminId, "100", "0", "", jsonUtil.toJson(yxChatMsg));
			if (!"200".equals(resultR.getCode())) {
				System.out.println(CodeToString.getString(resultR.getCode()));
			}
		} catch (IOException e) {
			logger.error("", e);
		}
	}

}
