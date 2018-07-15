package com.i5i58.redis.all;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.social.HotSongs;

public interface HotSongsDao extends PagingAndSortingRepository<HotSongs, String> {

	List<HotSongs> findByUploadTime(String time);
	
	public HotSongs findByUuId(String uuId);
	
}
