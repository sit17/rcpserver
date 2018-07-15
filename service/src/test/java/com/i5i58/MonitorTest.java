package com.i5i58;

import java.io.IOException;

import com.i5i58.data.im.YxCustomMsg;
import com.i5i58.service.security.PicReport;
import com.i5i58.service.security.Report;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.StringUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CodeToString;
import com.i5i58.yunxin.Utils.YXResultSet;

public class MonitorTest {

	void getPic() {
		String accId = "eb5aaeb82053473ea0169221b60413d7";
		String openId = "10419914";
		YXResultSet resultR;
		PicReport report = new PicReport();
		report.setEndpoint("http://oss-cn-hangzhou.aliyuncs.com");
		report.setAccessKeyId("LTAI2rZVmmXL7k3s");
		report.setAccessKeySecret("yvb5tXceTa9eORKGlXZpccqPEeb2vj");
		report.setBucketName("gg78live");
		report.setKey(String.format("Monitor/%s/%s", openId, StringUtils.createUUID()));
		report.setInterval(500);
		report.setCount(5);
		try {
			YxCustomMsg yxChatMsg = new YxCustomMsg();
			yxChatMsg.setCmd("getPic");
			yxChatMsg.setData(report);
			resultR = YunxinIM.sendAttachMessage(accId, "0", accId, new JsonUtils().toJson(yxChatMsg), "", "", "", "1",
					"");
			// (from, "0", to, "100", "", "", "", "", new
			// JsonUtils().toJson(yxChatMsg), "",
			// "", false);
			if (!"200".equals(resultR.getCode())) {
				System.out.println(CodeToString.getString(resultR.getCode()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void getProc() {
		String accId = "eb5aaeb82053473ea0169221b60413d7";
		String openId = "10419914";
		YXResultSet resultR;
		Report report = new Report();
		report.setEndpoint("http://oss-cn-hangzhou.aliyuncs.com");
		report.setAccessKeyId("LTAI2rZVmmXL7k3s");
		report.setAccessKeySecret("yvb5tXceTa9eORKGlXZpccqPEeb2vj");
		report.setBucketName("gg78live");
		report.setKey(String.format("Monitor/%s/%s.txt", openId, StringUtils.createUUID()));
		try {
			YxCustomMsg yxChatMsg = new YxCustomMsg();
			yxChatMsg.setCmd("getProc");
			yxChatMsg.setData(report);
			resultR = YunxinIM.sendAttachMessage(accId, "0", accId, new JsonUtils().toJson(yxChatMsg), "", "", "", "1",
					"");
			// (from, "0", to, "100", "", "", "", "", new
			// JsonUtils().toJson(yxChatMsg), "",
			// "", false);
			if (!"200".equals(resultR.getCode())) {
				System.out.println(CodeToString.getString(resultR.getCode()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		MonitorTest monitor = new MonitorTest();
		monitor.getPic();
	}
}
