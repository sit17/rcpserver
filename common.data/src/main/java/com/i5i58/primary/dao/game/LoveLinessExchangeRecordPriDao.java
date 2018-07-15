package com.i5i58.primary.dao.game;

import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.game.LoveLinessExchangeRecord;


public interface LoveLinessExchangeRecordPriDao extends CrudRepository<LoveLinessExchangeRecord, String> {

	LoveLinessExchangeRecord findByAccIdAndDate(String accId, long date);
}
