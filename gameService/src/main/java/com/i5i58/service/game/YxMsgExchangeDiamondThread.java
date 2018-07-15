package com.i5i58.service.game;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.i5i58.data.game.ExChangeDiamondResponse;
import com.i5i58.data.im.YxCustomMsg;
import com.i5i58.util.JsonUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CodeToString;
import com.i5i58.yunxin.Utils.YXResultSet;

public class YxMsgExchangeDiamondThread implements Runnable {

	private Logger logger = Logger.getLogger(getClass());

	private String accId;

	private ExChangeDiamondResponse msg;

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public YxMsgExchangeDiamondThread(String accId, ExChangeDiamondResponse msg) {
		super();
		this.accId = accId;
		this.msg = msg;
	}

	@Override
	public void run() {

		YXResultSet resultR;
		try {
			YxCustomMsg yxMsg = new YxCustomMsg();
			yxMsg.setCmd("exchangeDiamond");
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
