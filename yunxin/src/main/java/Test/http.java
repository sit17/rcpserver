package Test;

import java.util.HashMap;
import java.util.Map;

public class http {

	public static String sendVerifCodeToPhone(String phoneNo) {
		Net net = new Net();
		net.setUrl("http://192.168.1.104:8082/api/sendVerifCodeToPhone");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phoneNo", phoneNo);
		net.setMap(map);
		return HttpUtils.doPost(net);
	}

	public static String registerAccount(String phoneNo, String password, String verifCode) {
		Net net = new Net();
		net.setUrl("http://192.168.1.104:8082/api/registerAccount");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phoneNo", phoneNo);
		map.put("password", password);
		map.put("verifCode", verifCode);
		net.setMap(map);
		return HttpUtils.doPost(net);
	}

	public static String loginByOpenId(String openId, String password, String version) {
		Net net = new Net();
		net.setUrl("http://192.168.1.104:8082/api/loginByOpenId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("openId", openId);
		map.put("password", password);
		map.put("version", version);
		net.setMap(map);
		return HttpUtils.doPost(net);
	}

	public static String loginByToken(String version) {
		Net net = new Net();
		net.setUrl("http://192.168.1.104:6970/api/loginByToken");
		net.setAccid("31a86926bc7e47c988987aa75f473ccc");
		net.setToken("f2b4cfc0bc0f4f2fb6fbd29512d96767");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("version", version);
		net.setMap(map);
		return HttpUtils.doPost(net);
	}

	public static String getAddress() {
		Net net = new Net();
		net.setUrl("http://192.168.1.104:6970/api/getLiveAddress");
		net.setAccid("31a86926bc7e47c988987aa75f473ccc");
		net.setToken("f2b4cfc0bc0f4f2fb6fbd29512d96767");
		return HttpUtils.doPost(net);
	}

	public static String createRoom() {
		Net net = new Net();
		net.setUrl("http://192.168.1.104:6970/api/setBirth");
		net.setAccid("31a86926bc7e47c988987aa75f473ccc");
		net.setToken("f2b4cfc0bc0f4f2fb6fbd29512d96767");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("brith", "2016-09-15");
		net.setMap(map);
		return HttpUtils.doPost(net);
	}

	public static void main(String[] args) {

		System.out.println(loginByToken("1"));

		// System.out.println(loginByToken("1"));

		// System.out.println(sendVerifCodeToPhone("012345"));
		// {"code":"success","msg":null,"data":null}

		// System.out.println(registerAccount("012345","123","888888"));
		// {"code":"success","msg":null,"data":"{\"accId\":\"95bc287f67784c218444beace134e5a0\",\"openId\":\"19846\",\"phoneNo\":\"012345\",\"emailAddress\":\"\",\"nickName\":\"19846\",\"headUrlSmall\":\"\",\"headUrlOrigin\":\"\",\"stageName\":\"\",\"gender\":0,\"birthDate\":\"\",\"location\":\"\",\"signature\":\"\",\"personalBrief\":\"\",\"version\":1,\"isAnchor\":false,\"score\":0,\"vip\":0,\"vipDeadline\":\"\",\"vipPurchase\":0,\"focusCount\":0,\"fansCount\":0,\"authenticated\":false}"}

		// System.out.println(loginByOpenId("19846","123","0"));
		// {"code":"success","msg":null,"data":"{\"accId\":\"95bc287f67784c218444beace134e5a0\",\"openId\":\"19846\",\"phoneNo\":\"01**45\",\"emailAddress\":\"\",\"nickName\":\"19846\",\"headUrlSmall\":\"\",\"headUrlOrigin\":\"\",\"stageName\":\"\",\"gender\":0,\"birthDate\":\"\",\"location\":\"\",\"signature\":\"\",\"personalBrief\":\"\",\"version\":1,\"isAnchor\":false,\"score\":0,\"vip\":0,\"vipDeadline\":\"\",\"vipPurchase\":0,\"focusCount\":0,\"fansCount\":0,\"authenticated\":false,\"token\":\"3d172308fa851e3def922bf2c47e91a4\"}"}

		// 3d172308fa851e3def922bf2c47e91a4
		// System.out.println(loginByToken("0"));
		// {"code":"success","msg":null,"data":"{\"accId\":\"95bc287f67784c218444beace134e5a0\",\"openId\":\"19846\",\"phoneNo\":\"01**45\",\"emailAddress\":\"\",\"nickName\":\"19846\",\"headUrlSmall\":\"\",\"headUrlOrigin\":\"\",\"stageName\":\"\",\"gender\":0,\"birthDate\":\"\",\"location\":\"\",\"signature\":\"\",\"personalBrief\":\"\",\"version\":1,\"isAnchor\":false,\"score\":0,\"vip\":0,\"vipDeadline\":\"\",\"vipPurchase\":0,\"focusCount\":0,\"fansCount\":0,\"authenticated\":false,\"token\":\"3d172308fa851e3def922bf2c47e91a4\"}"}

	}

}
