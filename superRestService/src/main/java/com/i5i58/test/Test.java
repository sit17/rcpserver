package com.i5i58.test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.test.ISuperTest;
import com.i5i58.apis.test.ITest;
import com.i5i58.data.superAdmin.SuperAdminAuth;
import com.i5i58.security.SuperAuthorization;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "平台管理服务")
@RestController
public class Test {

	@Reference
	ISuperTest test;

	@ApiOperation(value = "test", notes = "")
	@RequestMapping(value = "/api/test", method = RequestMethod.POST)
	@SuperAuthorization(value=SuperAdminAuth.OPERATION_ADMIN_AUTH)
	public ResultDataSet test(@RequestParam(value = "name") String name, @RequestParam(value = "age") int age) {
		ResultDataSet rds = new ResultDataSet();
		rds = test.test(name, age,"hahha","test");
		return rds;
	}

}