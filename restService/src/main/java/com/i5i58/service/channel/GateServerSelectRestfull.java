package com.i5i58.service.channel;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.channel.IGateServerSelect;
import com.i5i58.apis.constants.ResultDataSet;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "网关服务-选取网关")
@RestController
@RequestMapping("${rpc.controller.version}")
public class GateServerSelectRestfull {

//	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private IGateServerSelect gateServerSelect;
	
	@ApiOperation(value = "选取网关", notes = "")
	@RequestMapping(value = "/channel/selectGateServer", method = RequestMethod.GET)
	public ResultDataSet selectGateServer() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = gateServerSelect.selectGateServer();
		return rds;
	}
}
