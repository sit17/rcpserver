package com.i5i58.superService.zookpeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.I0Itec.zkclient.IZkChildListener;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.i5i58.data.channel.ServerInfo;
import com.i5i58.util.IZkConfig;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.ZKUtil;


@Component
public class ZookeeperService implements IZkChildListener {
	
	private static Logger logger = Logger.getLogger(ZookeeperService.class);
	
	private static ZookeeperService zookeeperService = null;

	public static final String activeRoomServer = "/ActiveLiveServer";
	public static final String freeRoomServer = "/FreeLiveServer";

	private ZKUtil zkUtil;
	
	private Map<String, ServerInfo> freeLiveServerInfos = new HashMap<String, ServerInfo>();
	private ReadWriteLock freeLiveServerInfoLock = new ReentrantReadWriteLock(true);

	private Map<String, ServerInfo> activeLiveServerInfos = new HashMap<String, ServerInfo>();
	private ReadWriteLock activeLiveServerInfoLock = new ReentrantReadWriteLock(true);
	
	public static ZookeeperService getInstance(){
		return zookeeperService;
	}
	
	public ZookeeperService(){
		if (zookeeperService != null){
			assert false : "zookeeperService 重复实例化！！！！！！！！！！！！";
		}
		zookeeperService = this;
	}
	
	public void init(IZkConfig zkConfig) throws InterruptedException {
		zkUtil = new ZKUtil(zkConfig.getHosts(), zkConfig.getSessionTimeout(), zkConfig.getConnectionTimeout());
		zkUtil.init();
		// free
		if (!zkUtil.exists(freeRoomServer)){
			zkUtil.createPersistent(freeRoomServer, null);
		}
		zkUtil.subscribeChildChanges(freeRoomServer, this);

		List<String> currentChilds = zkUtil.getChildren(freeRoomServer);
		resetFreeRoomServers(freeRoomServer, currentChilds);
		
		// active
		if (!zkUtil.exists(activeRoomServer)) {
			zkUtil.createPersistent(activeRoomServer, null);
		}
		zkUtil.subscribeChildChanges(activeRoomServer, this);
		currentChilds = zkUtil.getChildren(activeRoomServer);
		resetActiveLiveServers(activeRoomServer, currentChilds);
	}

	/**
	 * inherit from IZkChildListener
	 */
	@Override
	public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
		if (currentChilds == null){
			return;
		}	
		logger.debug("zookeeper handleChildChange changed parent: " + parentPath + ", children: "
				+ currentChilds.toString());
		if (parentPath.equals(freeRoomServer)){			
			resetFreeRoomServers(parentPath, currentChilds);
		}else if (parentPath.equals(activeRoomServer)){
			resetActiveLiveServers(parentPath, currentChilds);
		}
	}
	
	public void resetFreeRoomServers(String parentPath, List<String> currentChilds){		
		freeLiveServerInfoLock.writeLock().lock();
		
		JsonUtils jsonUtils = new JsonUtils();
		freeLiveServerInfos.clear();
		for (String c : currentChilds){
			try {
				String data = zkUtil.readData(parentPath + "/" + c);
				if (data == null || data.isEmpty()){
					// delete server
					continue;
				}
				ServerInfo info = jsonUtils.toObject(data, ServerInfo.class);
				freeLiveServerInfos.put(info.getServerKey(), info);
			} catch (Exception e) {

			}
		}
		freeLiveServerInfoLock.writeLock().unlock();
	}
	
	
	
	public void resetActiveLiveServers(String parentPath, List<String> currentChilds){ 
		activeLiveServerInfoLock.writeLock().lock();
		
		JsonUtils jsonUtils = new JsonUtils();
		activeLiveServerInfos.clear();
		for (String c : currentChilds){
			try {
				String data = zkUtil.readData(parentPath + "/" + c);
				if (data == null || data.isEmpty()){
					// delete server
					continue;
				}
				ServerInfo info = jsonUtils.toObject(data, ServerInfo.class);
				activeLiveServerInfos.put(info.getServerKey(), info);
			} catch (Exception e) {

			}
		}
		activeLiveServerInfoLock.writeLock().unlock();
	}
	
	public List<ServerInfo> getAllFreeServerInfo(){
		List<ServerInfo> allFreeServers = new ArrayList<>();
		freeLiveServerInfoLock.readLock().lock();
		ServerInfo info = null;
		for (Map.Entry<String, ServerInfo> entry : freeLiveServerInfos.entrySet()){
			info = entry.getValue();
			ServerInfo serverInfo = new ServerInfo(info);
			allFreeServers.add(serverInfo);
		}
		freeLiveServerInfoLock.readLock().unlock();
		return allFreeServers;
	}
	
	public List<ServerInfo> getAllActiveServerInfo(){
		List<ServerInfo> allActiveServers = new ArrayList<>();
		activeLiveServerInfoLock.readLock().lock();
		ServerInfo info = null;
		for (Map.Entry<String, ServerInfo> entry : activeLiveServerInfos.entrySet()){
			info = entry.getValue();
			ServerInfo serverInfo = new ServerInfo(info);
			allActiveServers.add(serverInfo);
		}
		activeLiveServerInfoLock.readLock().unlock();
		return allActiveServers;
	}
	
	public ServerInfo getAnyFreeLiveServerInfo(){
		freeLiveServerInfoLock.readLock().lock();
		ServerInfo info = null;
		for (Map.Entry<String, ServerInfo> entry : freeLiveServerInfos.entrySet()){
			info = new ServerInfo(entry.getValue());
			break;
		}
		freeLiveServerInfoLock.readLock().unlock();
		return info;
	}
	
	public boolean saveLiveServer(ServerInfo info) throws IOException{
		JsonUtils jsonUtils = new JsonUtils();
		String newData = jsonUtils.toJson(info);
		if (zkUtil.exists(info.getNodePath())){
			zkUtil.writeData(info.getNodePath(), newData);
			return true;
		}else{
			logger.error("server node not exist " + newData);
			return false;
		}
	}
}
