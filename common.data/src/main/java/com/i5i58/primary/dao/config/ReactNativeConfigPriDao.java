package com.i5i58.primary.dao.config;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.config.ReactNativeConfig;

@Transactional
public interface ReactNativeConfigPriDao extends PagingAndSortingRepository<ReactNativeConfig, String> {

	@Query("select r from ReactNativeConfig r where r.id like %?1% or r.node like %?1% or r.name like %?1% or r.module like %?1%")
	List<ReactNativeConfig> findByParams(String params);

	List<ReactNativeConfig> findByVersion(String version);

	ReactNativeConfig findByIdAndVersion(String id, String version);

}
