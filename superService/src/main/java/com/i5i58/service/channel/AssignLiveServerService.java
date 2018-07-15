package com.i5i58.service.channel;

import java.io.IOException;
import java.util.List;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.platform.ILiveServerAssign;
import com.i5i58.data.channel.ServerInfo;
import com.i5i58.superService.zookpeeper.ZookeeperService;

@Service(protocol = "dubbo")
public class AssignLiveServerService implements ILiveServerAssign {

	@Override
	public ResultDataSet assignLiveServer(String accId, String cId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ServerInfo serverInfo = ZookeeperService.getInstance().getAnyFreeLiveServerInfo();
		if (serverInfo == null){
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("分配直播服务器失败,没有空闲服务器");
			return rds;
		}
		
		serverInfo.setServerKey(cId);
		if (ZookeeperService.getInstance().saveLiveServer(serverInfo)){
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		}else{
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("分配直播服务器失败");
			return rds;
		}
	}

	@Override
	public ResultDataSet getAllFreeLiveServer() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		List<ServerInfo> allFree = ZookeeperService.getInstance().getAllFreeServerInfo();
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setData(allFree);
		return rds;
	}

	@Override
	public ResultDataSet getAllActiveLiveServer() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		List<ServerInfo> allActive = ZookeeperService.getInstance().getAllActiveServerInfo();
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setData(allActive);
		return rds;
	}

}
