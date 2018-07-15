package com.i5i58.secondary.dao.channel;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.MountPresent;


@Transactional
public interface MountPresentSecDao extends PagingAndSortingRepository<MountPresent, Integer> {

	public MountPresent findByTypeAndLevel(int type, int level);
}
