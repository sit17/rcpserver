package com.i5i58.primary.dao.channel;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.WSSessionCache;

@Transactional
public interface WSSessionCachePriDao extends PagingAndSortingRepository<WSSessionCache, String> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
    public	WSSessionCache findByCacheId(String cacheId);
}
