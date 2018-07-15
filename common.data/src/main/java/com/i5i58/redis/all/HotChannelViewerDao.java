package com.i5i58.redis.all;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.HotChannelViewer;

public interface HotChannelViewerDao extends PagingAndSortingRepository<HotChannelViewer, String> {

	public List<HotChannelViewer> findByCId(String cId);

	public Page<HotChannelViewer> findByIsAndroid(boolean isAndroid, Pageable pageable);
}
