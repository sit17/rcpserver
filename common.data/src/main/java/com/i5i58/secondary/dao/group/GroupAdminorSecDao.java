package com.i5i58.secondary.dao.group;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.group.GroupAdminor;

@Transactional
public interface GroupAdminorSecDao extends CrudRepository<GroupAdminor, Long> {

	List<GroupAdminor> findByAdminId(String accId);
	
	GroupAdminor findByAdminIdAndGId(String accId,String gId);

	List<GroupAdminor> findByGId(String gId);
}
