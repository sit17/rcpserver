package com.i5i58.service.channel;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.i5i58.apis.channel.IGateServerSelect;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.util.Constant;
import com.i5i58.util.JedisUtils;
import com.i5i58.zookpeeper.ZookeeperService;

@Service(protocol = "dubbo")
public class GateServerSelectService implements IGateServerSelect {

	@Autowired
	JedisUtils jedisUtils;
	
	@Override
	public ResultDataSet selectGateServer() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		List<JSONObject> gateServers = ZookeeperService.getInstance().getGateServers();
		int minUserCount = -1;
		int minIndex = -1;
		
		for (int i=0; i<gateServers.size(); ++i){
			String host = gateServers.get(i).getString("host");
			int tcpPort = gateServers.get(i).getIntValue("tcpPort");
			int wsPort = gateServers.get(i).getIntValue("wsPort");
			String gateServerKey = host + "_" + tcpPort + "_" + wsPort;
			String gateServerValue = jedisUtils.hget(Constant.HOT_GATESERVER_INFO, gateServerKey);
			JSONObject object = JSON.parseObject(gateServerValue);
			if (object == null)
				continue;
			int userCount = object.getIntValue("onlineCount");
			if (userCount < minUserCount || minUserCount == -1){
				minUserCount = userCount;
				minIndex = i;
			}
		}
		if (minIndex >= 0){
			String data = gateServers.get(minIndex).toJSONString();
			rds.setCode(ResultCode.SUCCESS.getCode());
			rds.setData(data);
		}else{
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setData("没有找到可用的网关服务器");
		}
		return rds;
	}

}
