package com.i5i58.secondary.dao.group;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.group.ChannelGroup;

@Transactional
public interface ChannelGroupSecDao extends PagingAndSortingRepository<ChannelGroup, String> {

	public List<ChannelGroup> findByOwnerId(String ownerId);

	public ChannelGroup findByGId(String gId);

	public List<ChannelGroup> findByGIdOrParentId(String gId, String parentId);

	public List<ChannelGroup> findByParentId(String gId);

	public List<ChannelGroup> findByProfileId(String profileId);

	@Query("select g from ChannelGroup g where g.profileId=?1 and (g.parentId is null or g.parentId='')")
	public List<ChannelGroup> findTopGroupByFId(String fId);

	@Query("select g from ChannelGroup g where g.gId=?1 and (g.parentId is null or g.parentId='')")
	public ChannelGroup findTopGroupByGId(String gId);

	// @Query("select g from ChannelGroup g where g.groupId like %?1% or
	// g.ownerId in(select a.accId from Account a where a.openId like %?1%) or
	// g.name like %?1%")
	// public Page<ChannelGroup> findByParam(String param, Pageable pageable);

	@Query("select g from ChannelGroup g where (g.groupId like %?1% or g.ownerId in(select a.accId from Account a where a.openId like %?1%) or g.name like %?1%) and (g.parentId is null or g.parentId='')")
	public Page<ChannelGroup> findTopGroupByParam(String param, Pageable pageable);
}
