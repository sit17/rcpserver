package com.i5i58.primary.dao.channel;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.i5i58.data.channel.ZegoClk;

@Transactional
public interface ZegoClkPriDao extends CrudRepository<ZegoClk, Long> {

}
