package com.i5i58.primary.dao.channel;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.MountPresent;


@Transactional
public interface MountPresentPriDao extends PagingAndSortingRepository<MountPresent, Integer> {

	public MountPresent findByTypeAndLevel(int type, int level);
}
