package com.i5i58.util;

/**
 * zookeeper���ýӿ�
 * @author songfeilong
 *
 */
public interface IZkConfig {
	public String getHosts();
	public void setHosts(String hosts);
	public int getSessionTimeout();
	public void setSessionTimeout(int sessionTimeout);
	public int getConnectionTimeout();
	public void setConnectionTimeout(int connectionTimeout);
}
