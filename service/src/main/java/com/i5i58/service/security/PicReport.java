package com.i5i58.service.security;

public class PicReport extends Report{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4701208201908579339L;
	
    private int interval;
    private int count;
    
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
    
}
