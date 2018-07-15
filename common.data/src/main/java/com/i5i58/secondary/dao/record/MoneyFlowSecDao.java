package com.i5i58.secondary.dao.record;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.record.MoneyFlow;

public interface MoneyFlowSecDao extends PagingAndSortingRepository<MoneyFlow, Long> {
	Page<MoneyFlow> findByAccId(String accId, Pageable pageable);

	List<MoneyFlow> findByAccIdAndType(String accId, int type);
	
//	@Query("select new com.i5i58.data.anchor.AnchorCommission(SUM(m.targetCommission-m.sourceCommission)) from MoneyFlow m where m.type=?2 and m.accId=?1 and m.dateTime>=?3 and m.dateTime<=?4")
//	AnchorCommission getTotalGiftCommisionByAccId(String accId, int type, long fromTime, long toTime);
//	
	@Query("select m from MoneyFlow m where m.type=?2 and m.accId=?1 and m.dateTime>=?3 and m.dateTime<=?4")
	List<MoneyFlow> getTotalGiftCommission(String accId, int type, long fromTime, long toTime);
}
