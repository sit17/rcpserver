package com.i5i58.primary.dao.account;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.anchor.AnchorAuth;

/**
 * AuthenticationAnchorDao DB Operations
 * 
 * @author xiaoming
 *
 */
@Transactional
public interface AuthAnchorPriDao extends PagingAndSortingRepository<AnchorAuth, String> {

	public AnchorAuth findByAccIdAndAuthed(String accId, int authed);

	public Page<AnchorAuth> findByAuthed(Integer b, Pageable pageable);
}
