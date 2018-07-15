package com.i5i58.redis.all;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.game.GameServerItem;

public interface HotGameServerItemDao extends PagingAndSortingRepository<GameServerItem, Integer> {

}
