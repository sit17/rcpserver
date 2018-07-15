package Test;

import java.io.IOException;

import com.i5i58.Videocloud163.Videocloud163;
import com.i5i58.util.AppKeySecretPair;
import com.i5i58.util.JsonUtils;
import com.i5i58.yunxin.CheckSumBuilder;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.YXResultSet;

public class YunxinTest {
	void getChatRoom() {
		try {
			YXResultSet ys = YunxinIM.checkChatRoom("7119942", "true");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void setMute() {
		try {
			String creator = "9c8ae1c323574da7b9190d1a177f0b95";
			String accId = "137d308769e94bbb9fa20d28f36b709f"; // 郭
																// ;//"826e958280d545549faf6982dda0744f"
																// // 李刚;
			YXResultSet yxResultSet = YunxinIM.setChatRoomMemberRole("9600831", creator, accId, "2", "true", "");
			System.out.println(new JsonUtils().toJson(yxResultSet));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void setTempMute(){
		try {
			String creator = "9c8ae1c323574da7b9190d1a177f0b95";
			String accId = "137d308769e94bbb9fa20d28f36b709f"; 
			YXResultSet yxResultSet = YunxinIM.setChatRoomTemporaryMute("9600831", creator, accId, "0", "", "");
			System.out.println(new JsonUtils().toJson(yxResultSet));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * String fromAccid, String toAccids, String type, String body, String
	 * option, String pushContent, String payload, String ext
	 */
	void sendBatchMsg() {
		String ret = "";
		try {
			ret = YunxinIM.sendBatchMessage("ffee0e03c774414b8898222bdf7a0c91",
					"[\"826e958280d545549faf6982dda0744f\",\"ffe34d6874464a028565f9778d9fb445\"]", "0",
					"{\"cmd\":\"test batch msg\"}", "", "", "", "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(ret);
	}

	void sendAttachMsg() {
		YXResultSet ret = null;
		try {
			String payloadString = "{\"aps\":{\"alert\":\"Testing.. (20)\",\"badge\":5,\"sound\":\"default\",\"alertBodys\":\"jgoiaerjofeihjo\"}}";
			System.out.println(payloadString);
			ret = YunxinIM.sendAttachMessage("7d830c41d6fb4135b81d3828b9d782a6", "0",
					"9b149c15447d4bdeb7ae1bc79084bffc", "{\"msg\":\"this is a test attach msg\"}", 
					"this is push content, must be less then 150 bytes", 
					payloadString, 
					"", 
					"2", 
					"{\"badge\":false,\"needPushNick\":false,\"route\":false}");
			System.out.println(new JsonUtils().toJson(ret));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void sendBatchAttachMsg(){
		YXResultSet ret = null;
		try {
			//String payloadString = "{\"aps\":{\"alert\":\"Testing.. (20)\",\"badge\":5,\"sound\":\"default\",\"alertBodys\":\"jgoiaerjofeihjo\"}}";
			String payloadString = "{}";
			System.out.println(payloadString);
			ret = YunxinIM.sendBatchAttachMessage("7d830c41d6fb4135b81d3828b9d782a6", "[\"9b149c15447d4bdeb7ae1bc79084bffc\"]", 
					"{\"msg\":\"this is a test batch attach msg\"}", 
					"this is push content, must be less then 150 bytes", 
					payloadString, 
					"", 
					"1", 
					"");
			System.out.println(new JsonUtils().toJson(ret));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void sendMsg() {
		YXResultSet ret;
		try {
			ret = YunxinIM.sendMessage("7d830c41d6fb4135b81d3828b9d782a6", "0", "826e958280d545549faf6982dda0744f", "0",
					"{\"msg\":\"this is a test msg\"}", "", "", "", "", "", "", "", "", false);
			System.out.println(new JsonUtils().toJson(ret));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void channelStatus() {
		try {
			YXResultSet ret = Videocloud163.channelstatsChannel("19ca168df90a454c884f610549e28d47");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void setSignKey(String signKey){
		try {
			YXResultSet resultSet = Videocloud163.setSignKey(signKey);
			System.out.println(new JsonUtils().toJson(resultSet));
		} catch (IOException e) {
			
		}
	}
	
	void setChStatusCallback(String chStatusClk){
		try {
			YXResultSet ret = Videocloud163.setChStatusCallback(chStatusClk);
			System.out.println(new JsonUtils().toJson(ret));
		} catch (IOException e) {
			
		}
	}
	
	void testSignCheck(){
//cid = 621977362d9a44edb41141e0735d1f52, status = 0, time = 1500876445954, sign = 5410d115c59713256c49a5415bf0b99d
//signKey = phlive_vcloud
//checkResult = F8FC19511F1900D408D16465579DDDE8
		String cid = "621977362d9a44edb41141e0735d1f52";
		int status = 1;
		long time = 1500881175013L;
//		String sign = "47bb8da6cc95253e4d1d81cc310a52e8";
		String signKey = "phlive_vcloud";
		String params = "cid="+cid+"&status="+status+"&time="+time+signKey;
		String md5 = CheckSumBuilder.getMD5(params);
		
		System.out.println(params);
		System.out.println(md5);
	}
	
	public static void main(String[] args) {
		YunxinTest yunxinTest = new YunxinTest();
		// yunxinTest.getChatRoom();
		 //yunxinTest.setMute();
		 yunxinTest.setTempMute();
//		 yunxinTest.sendBatchMsg();
//		yunxinTest.sendAttachMsg();
//		yunxinTest.sendBatchAttachMsg();
		// yunxinTest.sendMsg();
//		yunxinTest.channelStatus();
//		yunxinTest.setSignKey("phlive_vcloud");
		//yunxinTest.testSignCheck();
//		yunxinTest.setChStatusCallback("http://callback.i5i58.com:6970/channel/chStatusCallabck");
	}
}
