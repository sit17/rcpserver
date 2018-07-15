package com.i5i58.secondary.mapper.test;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.i5i58.data.account.Account;

@Mapper
public interface AccountSecMapper {

	@Select("select * from accounts where phone_no = #{phoneNo}")
	@Results({
        @Result(property = "accId",  column = "acc_id"),
        @Result(property = "openId",  column = "open_Id"),
        @Result(property = "nickName", column = "nick_name")
    })
	Account selectAccountByPhoneNo(String phoneNo);
	
	@Update("update accounts set nick_name=#{0} where phone_no = #{1}")
	void updateNickName(String nickName, String phoneNo);
}
