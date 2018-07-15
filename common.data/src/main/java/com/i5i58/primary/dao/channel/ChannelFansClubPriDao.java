package com.i5i58.primary.dao.channel;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.ChannelFansClub;

@Transactional
public interface ChannelFansClubPriDao  extends PagingAndSortingRepository<ChannelFansClub, String> {

	ChannelFansClub findByCIdAndAccId(String cId, String accId);
	
	ChannelFansClub findByCIdAndAccIdAndEndDateGreaterThan(String cId, String accId, long endDate);

//	@Query("select c from ChannelFansClub c where c.cId=?1 and c.endDate >= ?2")
//	List<ChannelFansClub> findByCIdAndEndDate(String cId, long endData);
//	Page<ChannelFansClub> findByCIdAndEndDateGreaterThan(String cId, long endDate, Pageable pageable);
	
	@Query("select Count(c) from ChannelFansClub c where c.cId=?1 and c.endDate > ?2")
	int findCountByCIdAndEndDate(String cId, long endData);
	
//	List<ChannelFansClub> findByAccId(String accId);
//	
//	Page<ChannelFansClub> findByAccId(String accId, Pageable pageable);
}
