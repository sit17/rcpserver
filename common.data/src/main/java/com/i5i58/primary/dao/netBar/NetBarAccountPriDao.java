package com.i5i58.primary.dao.netBar;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.netBar.NetBarAccount;

public interface NetBarAccountPriDao extends PagingAndSortingRepository<NetBarAccount, String> {

	NetBarAccount findByNetIp(String ipAddress);

	Page<NetBarAccount> findByNullity(boolean nullity, Pageable pageable);

	NetBarAccount findByOwnerId(String accId);
	
	NetBarAccount findByNullityAndOwnerId(boolean b, String accId);

	Page<NetBarAccount> findByAgId(String agId, Pageable pageable);

	Page<NetBarAccount> findByNullityAndAgId(boolean b, String agId, Pageable pageable);

	NetBarAccount findByNetIpAndOwnerIdAndNullity(String clientIP, String accId, boolean nullity);

}
