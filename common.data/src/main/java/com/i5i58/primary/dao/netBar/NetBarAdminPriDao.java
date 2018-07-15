package com.i5i58.primary.dao.netBar;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.netBar.NetBarAdmin;

public interface NetBarAdminPriDao extends PagingAndSortingRepository<NetBarAdmin, String> {

	NetBarAdmin findByAccIdAndNetIp(String accId, String clientIP);

}
