package com.i5i58.service.android.channel.async;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.i5i58.data.im.YxCustomMsg;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.StringUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CodeToString;
import com.i5i58.yunxin.Utils.YXResultSet;

public class TaskYxChatOpenGuard implements Runnable {

	private Logger logger = Logger.getLogger(getClass());

	private String roomId;
	private String accId;

	private MsgYxChatOpenGuard msg;

	public TaskYxChatOpenGuard(String roomId, String accId, String name, int vip, long vipDeadLine, int guard,
			long guardDeadLine, long richScore, int fansClub, String clubName) {
		super();
		this.roomId = roomId;
		this.accId = accId;
		msg = new MsgYxChatOpenGuard();
		this.msg.setAccId(accId);
		msg.setName(name);
		msg.setVip(vip);
		msg.setVipDeadLine(vipDeadLine);
		msg.setGuardDeadLine(guardDeadLine);
		msg.setGuard(guard);
		msg.setRichScore(richScore);
		switch (guard) {
		case 1:
			msg.setContent("成为 骑士");
			break;
		case 2:
			msg.setContent("成为 大骑士");
			break;
		case 3:
			msg.setContent("成为 圣骑士");
			break;
		}
		this.msg.setFansClub(fansClub);
		this.msg.setClubName(clubName);
	}

	@Override
	public void run() {
		String uuid = StringUtils.createUUID();
		YXResultSet resultR;
		try {
			YxCustomMsg yxChatMsg = new YxCustomMsg();
			yxChatMsg.setCmd("openGuard");
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
