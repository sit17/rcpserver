package com.i5i58.primary.dao.channel;

import java.util.List;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.Channel;

@Transactional
public interface ChannelPriDao extends PagingAndSortingRepository<Channel, String> {
	
	public Channel findByOwnerId(String ownerId);

	public Channel findByCId(String cId);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select c from Channel c where c.cId = ?1")
	public Channel findByCIdPessimisticLock(String cId);
	
	public Channel findByYunXinRId(String yunXinRId);
	public Channel findByYunXinCId(String yunXinCId);

	public List<Channel> findByGId(String gId);

	@Query("select c from Channel c where c.ownerId like %?1% or c.channelId like %?1% or c.channelName like %?1% or c.channelNotice like %?1%")
	public Page<Channel> findByParam(String param, Pageable pageable);

	@Query("select c from Channel c where c.gId in (?1)")
	public List<Channel> findChannelByGIds(String gids, Pageable pageable);

	@Query("select c from Channel c where c.nullity=0 and (c.channelId like %?1% or c.channelName like %?1%) and c.ownerId !=''")
	public Page<Channel> findByParamWithChannelIdOrChannelName(String param, Pageable pageable);

	public List<Channel> findByOwnerIdNotAndNullity(String notOwnerd, boolean nullity);

	public Page<Channel> findByNullity(boolean nullity, Pageable pageable);

	public Page<Channel> findByStatusNotAndTypeNotAndTypeNotAndNullityAndOwnerIdNot(int status, int type1, int type2, boolean nullity,
			String ownerId, Pageable pageable);

	public Page<Channel> findByStatusNotAndTypeAndNullityAndOwnerIdNot(int status, int type, boolean nullity,
			String ownerId, Pageable pageable);
	
	@Modifying
	@Query(value = "update Channel c set c.zegoStreamId = ?2 where c.cId = ?1")
	public void updateZegoStreamId(String cId, String zegoStreamId);
//	
//	@Query("select c from Channel c where c.gId = ?! ORDER by rand() LIMIT 1")
//	public Channel findRandomByGId(String gId);
//	
//	@Query("select c from Channel c where c.gId = ?! and c.type = ?2 ORDER by rand() LIMIT 1")
//	public Channel findRandomByGIdAndType(String gId, int type);
	@Query("select c from Channel c where c.gId in (?1) and (c.ownerId='' or c.ownerId is null) and c.type=?2")
	public List<Channel> findFreeChannelByGIdsAndType(String gids, int type);
	
	@Query("select c from Channel c where c.gId in (?1) and (c.ownerId='' or c.ownerId is null)")
	public List<Channel> findFreeChannelByGIds(String gids);
}