package com.i5i58.secondary.dao.channel;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.ChannelGift;

@Transactional
public interface ChannelGiftSecDao extends PagingAndSortingRepository<ChannelGift, Integer> {

	List<ChannelGift> findByNullity(boolean nullity);

	@Query("select g from ChannelGift g where g.name like %?1%")
	List<ChannelGift> queryByName(String name);

	List<ChannelGift> findByNullity(boolean nullity, Sort sort);

}
