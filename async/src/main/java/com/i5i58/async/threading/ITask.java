package com.i5i58.async.threading;

public interface ITask extends Runnable {

	void setWorkerId(int workerId);
	
	void setPool(TaskPool pool);
	
	int getWorkerId();
}
