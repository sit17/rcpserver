package com.i5i58.service.pay.async;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.i5i58.data.im.YxCustomMsg;
import com.i5i58.util.JsonUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CodeToString;
import com.i5i58.yunxin.Utils.YXResultSet;

public class TaskYxPay implements Runnable {

	private Logger logger = Logger.getLogger(getClass());

	private String from;

	private String to;

	private MsgYxPay msg;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public TaskYxPay(String from, String to, String accId, long applyDate, long completeDate, int device, int discount,
			long iGold, String ipAddress, long orderAmount, String orderId, int orderStatus, long payAmount,
			String serialNum, int shareId, String toAccId) {
		super();

		this.from = from;
		this.to = to;
		this.msg = new MsgYxPay();
		this.msg.setAccId(accId);
		this.msg.setApplyDate(applyDate);
		this.msg.setCompleteDate(completeDate);
		this.msg.setDevice(device);
		this.msg.setDiscount(discount);
		this.msg.setiGold(iGold);
		this.msg.setIpAddress(ipAddress);
		this.msg.setOrderAmount(orderAmount);
		this.msg.setOrderId(orderId);
		this.msg.setOrderStatus(orderStatus);
		this.msg.setPayAmount(payAmount);
		this.msg.setSerialNum(serialNum);
		this.msg.setShareId(shareId);
		this.msg.setToAccId(toAccId);
	}

	@Override
	public void run() {

		YXResultSet resultR;
		try {
			YxCustomMsg yxChatMsg = new YxCustomMsg();
			yxChatMsg.setCmd("pay");
			yxChatMsg.setData(this.msg);
			resultR = YunxinIM.sendAttachMessage(from, "0", to, new JsonUtils().toJson(yxChatMsg), "充值成功", "", "", "2", "");
//					(from, "0", to, "100", "", "", "", "", new JsonUtils().toJson(yxChatMsg), "",
//					"", false);
			if (!"200".equals(resultR.getCode())) {
				logger.error(CodeToString.getString(resultR.getCode()));
			}
		} catch (IOException e) {
			logger.error("", e);
		}
	}

}
