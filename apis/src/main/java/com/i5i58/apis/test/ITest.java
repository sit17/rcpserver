package com.i5i58.apis.test;

import com.i5i58.apis.constants.ResultDataSet;

public interface ITest {

	ResultDataSet test(String name, int age,String clientIp,String clientIpYun); 
	
	ResultDataSet testRedis();
	
	ResultDataSet kick();
	
	ResultDataSet channelNotice(String cId);
}
