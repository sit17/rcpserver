package com.i5i58.service.test;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.test.ISuperTest;
import com.i5i58.apis.test.ITest;

@Service(protocol = "dubbo")
public class TestSuperService implements ISuperTest {

	@Override
	public ResultDataSet test(String name, int age,String clientIp,String clientIpYun) {
		ResultDataSet rds = new ResultDataSet();
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}
}
