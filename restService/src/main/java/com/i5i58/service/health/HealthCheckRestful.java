package com.i5i58.service.health;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.i5i58.apis.constants.ResultDataSet;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "健康检查")
@RestController
public class HealthCheckRestful{

	@ApiOperation(value = "健康检查", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/health/check", method = RequestMethod.GET)
	public ResultDataSet check() {
		ResultDataSet rds = new ResultDataSet();
		return rds;
	}

}
