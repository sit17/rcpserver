package com.i5i58.secondary.dao.account;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.account.MountStore;

@Transactional
public interface MountStoreSecDao extends PagingAndSortingRepository<MountStore, String> {

	public Page<MountStore> findByAccId(String accId, Pageable pageable);

	public MountStore findByAccIdAndMountsId(String accId, int mountId);

	public MountStore findByAccIdAndMountsIdAndCId(String accId, int mountId, String cId);

}
