package com.i5i58.primary.dao.record;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.record.MoneyFlow;

@Transactional
public interface MoneyFlowPriDao extends PagingAndSortingRepository<MoneyFlow, Long> {

	@Query(value = "select SUM(m.targetIGold-m.sourceIGold) from MoneyFlow m where m.type=11 and m.IpAddress=?3 and FROM_UNIXTIME(m.dateTime/1000,'%Y%m')=?4 and m.accId not in (?1,?2)")
	long CountPayRebateByAccIdAndIp(String accId, String otherAccId, String ip, String searchMonth);	

	@Query(value = "select SUM(m.sourceIGold-m.targetIGold) from MoneyFlow m where m.type=1 and m.IpAddress=?3 and FROM_UNIXTIME(m.dateTime/1000,'%Y%m')=?4")
	long CountExChangeScoreRebateByAccIdAndIp(String ip, String searchMonth);

	@Query(value = "select SUM(m.sourceIGold-m.targetIGold) from MoneyFlow m where m.type=2 and m.IpAddress=?3 and FROM_UNIXTIME(m.dateTime/1000,'%Y%m')=?4 and m.accId not in (?1,?2)")
	long CountGivenGiftRebateByAccIdAndIp(String accId, String otherAccId, String ip, String searchMonth);
}
