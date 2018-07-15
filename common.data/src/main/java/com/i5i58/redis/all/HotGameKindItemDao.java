package com.i5i58.redis.all;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.game.GameKindItem;

public interface HotGameKindItemDao extends PagingAndSortingRepository<GameKindItem, Integer> {

}
