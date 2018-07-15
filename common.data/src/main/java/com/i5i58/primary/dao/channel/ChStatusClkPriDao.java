package com.i5i58.primary.dao.channel;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.ChStatusClk;

@Transactional
public interface ChStatusClkPriDao extends PagingAndSortingRepository<ChStatusClk, Long> {

}
