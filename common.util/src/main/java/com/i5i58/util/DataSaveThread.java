package com.i5i58.util;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

public class DataSaveThread<T, TID extends Serializable> implements Runnable {

	private T data;

	private CrudRepository<T, TID> dataDao;
	
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public CrudRepository<T, TID> getDataDao() {
		return dataDao;
	}

	public void setDataDao(CrudRepository<T, TID> dataDao) {
		this.dataDao = dataDao;
	}

	public DataSaveThread(T data, CrudRepository<T, TID> dataDao) {
		super();
		this.data = data;
		this.dataDao = dataDao;
	}

	@Override
	public void run() {
		dataDao.save(data);
	}

	public void Save()
	{
		dataDao.save(data);
	}
}
