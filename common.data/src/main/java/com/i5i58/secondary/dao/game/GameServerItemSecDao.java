package com.i5i58.secondary.dao.game;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.game.GameServerItem;

public interface GameServerItemSecDao extends PagingAndSortingRepository<GameServerItem, Integer> {

}
