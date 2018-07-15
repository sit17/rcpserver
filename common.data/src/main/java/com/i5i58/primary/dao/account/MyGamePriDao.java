package com.i5i58.primary.dao.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.account.MyGame;

public interface MyGamePriDao extends PagingAndSortingRepository<MyGame, Long> {

	MyGame findByAccIdAndKindId(String accId, int kindId);

	Page<MyGame> findByAccId(String accId, Pageable pageable);

}
