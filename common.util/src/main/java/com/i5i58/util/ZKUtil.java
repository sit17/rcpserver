package com.i5i58.util;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

/**
 * zookeeper�����
 * @author Administrator
 *
 */
public class ZKUtil {

	private static ZkClient zkClient;

	private String hosts;
	private int sessionTimeout;
	private int connectionTimeout;

	public ZKUtil(String hosts, int sessionTimeout, int connectionTimeout){
		this.hosts = hosts;
		this.sessionTimeout = sessionTimeout;
		this.connectionTimeout = connectionTimeout;
	}
	
	public void init() {
		zkClient = new ZkClient(hosts, sessionTimeout, connectionTimeout, new StringSerializer());
	}
	
	public ZkClient getZkClient(){
		return zkClient;
	}
	
	public String getHosts() {
		return hosts;
	}

	public void setHosts(String hosts) {
		this.hosts = hosts;
	}

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public String create(final String path, Object data, final CreateMode mode) {
		if (path == null)
			return null;
		String[] sub = path.split("/");
		String tmpPath = "";
		for (int i=0; i<sub.length; i++){
			if (sub[i].isEmpty())
				continue;
			tmpPath += "/" + sub[i];
			if (zkClient.exists(tmpPath))
				continue;
			if (i < sub.length - 1){
				zkClient.create(tmpPath, null, CreateMode.PERSISTENT);
			}else{				
				zkClient.create(tmpPath, data, mode);
			}
		}
		return path;
	}
	
	public void createEphemeral(final String path, final Object data) {
		create(path, data, CreateMode.EPHEMERAL);
	}
	
	public void createPersistent(final String path, final Object data) {
		create(path, data, CreateMode.PERSISTENT);
	}
	
	public boolean exists(final String path) {
		if (path == null)
			return false;
        return zkClient.exists(path);
    }
	
	public boolean delete(String path){
		if (path == null)
			return false;
		return zkClient.delete(path);
	}
	
	public boolean deleteRecursive(String path) {
		if (path == null)
			return false;
        return zkClient.deleteRecursive(path);
    }

	public List<String> subscribeChildChanges(String path, IZkChildListener childListener)
			throws InterruptedException {
		if (path == null || childListener == null)
			return null;
		List<String> subscribeResult = zkClient.subscribeChildChanges(path, childListener);
		return subscribeResult;
	}
	
	public void unsubscribeChildChanges(String path, IZkChildListener childListener) {
		if (path == null || childListener == null)
			return;
        zkClient.unsubscribeChildChanges(path, childListener);
    }

	public void subscribeDataChanges(String path, IZkDataListener dataListener) {
		if (path == null)
			return;
		zkClient.subscribeDataChanges(path, dataListener);
	}
	
	public void unsubscribeDataChanges(String path, IZkDataListener dataListener) {
		if (path == null || dataListener == null)
			return;
		zkClient.unsubscribeDataChanges(path, dataListener);
    }

	public void subscribeStateChanges(final IZkStateListener listener) {
		if (listener == null)
			return;
		zkClient.subscribeStateChanges(listener);
	}
	
	public void unsubscribeStateChanges(IZkStateListener stateListener){
		if (stateListener == null)
			return;
		zkClient.unsubscribeStateChanges(stateListener);
	}
	
	public void unsubscribeAll(){
		zkClient.unsubscribeAll();
	}
	public void writeData(String path, Object object) {
		if (path == null)
			return;
		zkClient.writeData(path, object);
	}
	public String readData(String path){
		if (path == null)
			return null;
		return zkClient.readData(path);
	}
	public List<String> getChildren(String path){
		if (path == null)
			return null;
		return zkClient.getChildren(path);
	}
}
