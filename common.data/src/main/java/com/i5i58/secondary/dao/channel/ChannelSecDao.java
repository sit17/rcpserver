package com.i5i58.secondary.dao.channel;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.Channel;

@Transactional
public interface ChannelSecDao extends PagingAndSortingRepository<Channel, String> {
	
	public Channel findByOwnerId(String ownerId);

	public Channel findByCId(String cId);
	
	public Channel findByYunXinRId(String yunXinRId);

	public List<Channel> findByGId(String gId);
	public List<Channel> findByGIdAndType(String gId, int type, Pageable pageable);
	
	@Query("select c from Channel c where c.ownerId like %?1% or c.channelId like %?1% or c.channelName like %?1% or c.channelNotice like %?1%")
	public Page<Channel> findByParam(String param, Pageable pageable);

	@Query("select c from Channel c where c.gId in (?1)")
	public List<Channel> findChannelByGIds(String gids, Pageable pageable);
	
	@Query("select c from Channel c where c.nullity=0 and (c.channelId like %?1% or c.channelName like %?1%)")
	public Page<Channel> findByParamWithChannelIdOrChannelName(String param, Pageable pageable);

	public List<Channel> findByOwnerIdNotAndNullity(String notOwnerd, boolean nullity);

	public Page<Channel> findByNullity(boolean nullity, Pageable pageable);
	
	public Page<Channel> findByStatusNot(int status, Pageable pageable);
	public Page<Channel> findByStatusNotAndType(int status, int type, Pageable pageable);
	
	@Query("select c from Channel c where c.cId in (select n.cId from ChannelNotice n where n.accId=?1) and c.status=?2 order by c.statusChanged desc")
	public List<Channel> findNoticeChannels(String accId, int status);
}