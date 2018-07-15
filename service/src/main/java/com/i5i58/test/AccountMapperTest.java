package com.i5i58.test;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.i5i58.data.account.Account;
import com.i5i58.primary.mapper.test.AccountPriMapper;
import com.i5i58.secondary.mapper.test.AccountSecMapper;
import com.i5i58.util.JsonUtils;

@Component
public class AccountMapperTest {

	@Autowired
	private AccountPriMapper accountPriMapper;
	
	@Autowired
	private AccountSecMapper accountSecMapper;
	
	public void testQuery(){
		Account account = accountPriMapper.selectAccountByPhoneNo("13738049078");
		try {
			System.out.println(new JsonUtils().toJson(account));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		accountPriMapper.updateNickName("myBatis primary test", "13738049078");
		
//		accountSecMapper.updateNickName("myBatis secondary test", "13738049078");
	}
}
