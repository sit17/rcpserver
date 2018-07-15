package com.i5i58.primary.dao.vendor;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.i5i58.data.vendor.VendorUserAction;

@Transactional
public interface VendorUserActionPriDao extends PagingAndSortingRepository<VendorUserAction,String>{
	
}
