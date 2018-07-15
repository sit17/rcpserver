package com.i5i58.service.channel.async;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.i5i58.apis.constants.ResponseData;
import com.i5i58.data.channel.HotChannelViewer;
import com.i5i58.data.im.YxCustomMsg;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.StringUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CodeToString;
import com.i5i58.yunxin.Utils.YXResultSet;

public class TaskYxChatGuardKick implements Runnable {
	private Logger logger = Logger.getLogger(getClass());

	private String roomId;
	private String adminId;
	private JsonUtils jsonUtil;

	private MsgYxChatIdentity guard;
	private MsgYxChatIdentity kicked;

	public TaskYxChatGuardKick(String roomId, String adminId, JsonUtils jsonUtils){
		this.roomId = roomId;
		this.adminId = adminId;
		this.jsonUtil = jsonUtils;
	}

	public void setGuard(HotChannelViewer guardViewer) {
		guard = new MsgYxChatIdentity();
		guard.setAccId(guardViewer.getAccId());
		guard.setName(guardViewer.getName());
		guard.setFace(guardViewer.getFaceSmallUrl());
		guard.setVip(guardViewer.getVip());
		guard.setVipDeadLine(guardViewer.getVipDeadLine());
		guard.setGuard(guardViewer.getGuardLevel());
		guard.setGuardDeadLine(guardViewer.getGuardDeadLine());
		guard.setRichScore(guardViewer.getRichScore());
		guard.setScore(guardViewer.getScore());
		guard.setFansClub(guardViewer.getFansClub());
		guard.setClubName(guardViewer.getClubName());
		guard.setClubLevel(guardViewer.getClubLevel());
		guard.setFansClubDeadLine(guardViewer.getClubDeadLine());
		guard.setSuperUser(guardViewer.isSuperUser());
	}

	public void setKicked(HotChannelViewer kickedViewer) {
		kicked = new MsgYxChatIdentity();
		kicked.setAccId(kickedViewer.getAccId());
		kicked.setName(kickedViewer.getName());
		kicked.setFace(kickedViewer.getFaceSmallUrl());
		kicked.setVip(kickedViewer.getVip());
		kicked.setVipDeadLine(kickedViewer.getVipDeadLine());
		kicked.setGuard(kickedViewer.getGuardLevel());
		kicked.setGuardDeadLine(kickedViewer.getGuardDeadLine());
		kicked.setRichScore(kickedViewer.getRichScore());
		kicked.setScore(kickedViewer.getScore());
		kicked.setFansClub(kickedViewer.getFansClub());
		kicked.setClubName(kickedViewer.getClubName());
		kicked.setClubLevel(kickedViewer.getClubLevel());
		kicked.setFansClubDeadLine(kickedViewer.getClubDeadLine());
		kicked.setSuperUser(kickedViewer.isSuperUser());
	}
	
	@Override
	public void run() {
		String uuid = StringUtils.createUUID();
		YXResultSet resultR;
		try {
			YxCustomMsg yxChatMsg = new YxCustomMsg();
			ResponseData rp = new ResponseData();
			rp.put("guard", guard);
			rp.put("kicked", kicked);
			yxChatMsg.setCmd("guardKick");
			yxChatMsg.setData(rp);
			resultR = YunxinIM.sendChatRoomMsg(roomId, uuid, adminId, "100", "0", "", jsonUtil.toJson(yxChatMsg));
			if (!"200".equals(resultR.getCode())) {
				System.out.println(CodeToString.getString(resultR.getCode()));
			}
		} catch (IOException e) {
			logger.error("", e);
		}
	}

}
