package com.i5i58.primary.dao.account;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.account.MountStore;

@Transactional
public interface MountStorePriDao extends PagingAndSortingRepository<MountStore, String> {

	public Page<MountStore> findByAccId(String accId, Pageable pageable);

	public List<MountStore> findByAccId(String accId);

	public MountStore findByAccIdAndMountsId(String accId, int mountId);

	public MountStore findByAccIdAndMountsIdAndCId(String accId, int mountId, String cId);

	public List<MountStore> findByAccIdAndCId(String accId, String cId);
	
	

}
