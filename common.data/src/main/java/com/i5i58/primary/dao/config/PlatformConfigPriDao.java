package com.i5i58.primary.dao.config;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.config.PlatformConfig;

@Transactional
public interface PlatformConfigPriDao extends CrudRepository<PlatformConfig, String> {

}
