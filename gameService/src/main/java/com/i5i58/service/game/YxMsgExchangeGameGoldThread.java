package com.i5i58.service.game;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.i5i58.data.game.ExChangeGameGoldResponse;
import com.i5i58.data.im.YxCustomMsg;
import com.i5i58.util.JsonUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CodeToString;
import com.i5i58.yunxin.Utils.YXResultSet;

public class YxMsgExchangeGameGoldThread implements Runnable {

	private Logger logger = Logger.getLogger(getClass());

	private String accId;

	private ExChangeGameGoldResponse msg;

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public YxMsgExchangeGameGoldThread(String accId, ExChangeGameGoldResponse msg) {
		super();
		this.accId = accId;
		this.msg = msg;
	}

	@Override
	public void run() {

		YXResultSet resultR;
		try {
			YxCustomMsg yxMsg = new YxCustomMsg();
			yxMsg.setCmd("exchangeGameGold");
			yxMsg.setData(this.msg);
			String jsonMsg = new JsonUtils().toJson(yxMsg);
			resultR = YunxinIM.sendAttachMessage(accId, "0", msg.getAccId(), jsonMsg, "兑换成功", jsonMsg, "", "2", "");// new
																													// JsonUtils().toJson(yxChatMsg));
			if (!"200".equals(resultR.getCode())) {
				System.out.println(CodeToString.getString(resultR.getCode()));
			}
		} catch (IOException e) {
			logger.error("", e);
		}
	}

}
