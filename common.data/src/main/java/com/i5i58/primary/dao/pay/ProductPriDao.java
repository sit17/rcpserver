package com.i5i58.primary.dao.pay;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.pay.Product;

public interface ProductPriDao extends CrudRepository<Product, Long> {
	public List<Product> findByDeviceOrderByPrice(int device);
}
