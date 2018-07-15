package com.i5i58.primary.dao.game;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.i5i58.data.game.GameVipConfig;

@Transactional
public interface GameVipConfigPriDao extends PagingAndSortingRepository<GameVipConfig, Long> {

	GameVipConfig findByLevelAndMonth(int level, int month);
}
