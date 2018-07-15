package com.i5i58.secondary.dao.home;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.home.Carousel;

/**
 * DB Operations
 * 
 * @author frank
 *
 */
@Transactional
public interface CarouselSecDao extends CrudRepository<Carousel, Long> {

	@Query(value = "select c from Carousel c where c.device=?1 and c.startTime<=?2 and c.endTime>=?2")
	List<Carousel> findByDeviceNow(int device, long time);

	@Query(value = "select c from Carousel c where c.device=?1 and ((c.startTime<=?3 and c.startTime>=?2) or (c.endTime<=?3 and c.endTime>=?2))")
	List<Carousel> findByDeviceInTime(int device, long startTime, long endTime);
}
