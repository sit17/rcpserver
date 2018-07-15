package com.i5i58.primary.dao.netBar;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.Query;

@Mapper
public interface NetBarIncomeMapper {

//	@Select("select IFNULL(sum(amount), 0) from channel_records where ip_address = #{0} and collect_date >= #{1} and collect_date <= #{2}")
//	long selectSumGiftByIp(String ip, long stratTime, long endTime);

	@Select("select IFNULL(sum(amount), 0) from channel_records where ip_address = #{ip} and collect_date >= #{stratTime} and collect_date <= #{endTime}")
	long selectSumGiftByIp(@Param("ip") String ip, @Param("stratTime") long stratTime, @Param("endTime") long endTime);
	
	@Select(value = "select IFNULL(SUM(targetigold-sourceigold),0) from money_flow where type=11 and ip_address=#{ip} and FROM_UNIXTIME(date_time/1000,'%Y%m')=#{searchMonth} and acc_id not in (#{accId},#{otherAccId})")
	long CountPayRebateByAccIdAndIp(@Param("accId") String accId, @Param("otherAccId") String otherAccId, @Param("ip") String ip, @Param("searchMonth") String searchMonth);	

	@Select(value = "select IFNULL(SUM(sourceigold-targetigold),0) from money_flow where type=1 and ip_address=#{ip} and FROM_UNIXTIME(date_time/1000,'%Y%m')=#{searchMonth}")
	long CountExChangeScoreRebateByAccIdAndIp(@Param("ip") String ip, @Param("searchMonth") String searchMonth);

	@Select(value = "select IFNULL(SUM(sourceigold-targetigold),0) from money_flow where type=2 and ip_address=#{ip} and FROM_UNIXTIME(date_time/1000,'%Y%m')=#{searchMonth} and acc_id not in (#{accId},#{otherAccId})")
	long CountGivenGiftRebateByAccIdAndIp(@Param("accId") String accId, @Param("otherAccId") String otherAccId, @Param("ip") String ip, @Param("searchMonth") String searchMonth);	
}
