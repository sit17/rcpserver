package com.i5i58.secondary.dao.vendor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.i5i58.data.vendor.VendorUserAction;

@Transactional
public interface VendorUserActionSecDao extends PagingAndSortingRepository<VendorUserAction,String>{
	public Page<VendorUserAction> findByVendorId(String vendorId, Pageable pageable);
	
	public Page<VendorUserAction> findByAdId(String adId, Pageable pageable);
	
	public Page<VendorUserAction> findByActionId(int actionId, Pageable pageable);
	
	@Query(value="select a from VendorUserAction a where a.dateTime>=?1 and a.dateTime<=?2")
	public Page<VendorUserAction> findByTime(long from, long to, Pageable pageable);
	
	@Query(value = "select count(n) from VendorUserAction n where n.vendorId=?1")
	int countByVendorId(String vendorId);

	@Query(value = "select count(n) from VendorUserAction n where n.adId=?1")
	int countByAdId(String adId);
	
	@Query(value = "select count(n) from VendorUserAction n where n.actionId=?1")
	int countByActionId(int actionId);
	
	@Query(value="select count(n) from VendorUserAction n where n.dateTime>= ?1 and n.dateTime<=?2")
	public int countByTime(long from, long to);

}
