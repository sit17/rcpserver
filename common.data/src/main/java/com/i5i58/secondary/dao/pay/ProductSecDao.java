package com.i5i58.secondary.dao.pay;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.pay.Product;

public interface ProductSecDao extends CrudRepository<Product, Long> {
	public List<Product> findByDeviceOrderByPrice(int device);
}
