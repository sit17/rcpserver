package com.i5i58.service.android.channel.async;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.i5i58.data.im.YxCustomMsg;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.StringUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CodeToString;
import com.i5i58.yunxin.Utils.YXResultSet;

public class TaskYxChatExit implements Runnable {

	private Logger logger = Logger.getLogger(getClass());

	private String roomId;
	private String accId;

	private MsgYxChatIdentity msg;

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

	public TaskYxChatExit(String roomId, String accId, String name, String face, long richScore, long score, int vip,
			long vipDeadLine, int guard, long guardDeadLine, int fansClub, String clubName, long fansClubDeadLine) {
		super();
		this.roomId = roomId;
		this.accId = accId;
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
		this.msg.setFansClubDeadLine(fansClubDeadLine);
	}

	@Override
	public void run() {
		try {
			String accIds = "[\"" + accId + "\"]";
			YXResultSet yxrs = YunxinIM.removeChatRoomRobot(roomId, accIds);
			if (!"200".equals(yxrs.getCode())) {
				logger.error(yxrs.getError());
			}
		} catch (IOException e) {
			logger.error("", e);
		}
	}

}
