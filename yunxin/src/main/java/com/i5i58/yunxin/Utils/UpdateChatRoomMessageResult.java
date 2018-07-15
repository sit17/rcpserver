package com.i5i58.yunxin.Utils;

import java.io.IOException;

import com.i5i58.util.JsonUtils;

public class UpdateChatRoomMessageResult extends YXResultSet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6131796391638160817L;

	public UpdateChatRoomMessageResultInfo getChatroom() throws IOException {
		return new JsonUtils().toObject("chatroom", UpdateChatRoomMessageResultInfo.class);
	}

}
