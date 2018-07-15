package com.i5i58.zookpeeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.I0Itec.zkclient.IZkChildListener;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.i5i58.data.channel.ServerInfo;
import com.i5i58.util.IZkConfig;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.ZKUtil;


@Component
public class ZookeeperService implements IZkChildListener {
	
	private static Logger logger = Logger.getLogger(ZookeeperService.class);
	
	private static ZookeeperService zookeeperService = null;

	public static final String activeRoomServer = "/ActiveLiveServer";
	public static final String gateServer = "/GateServer";

	private ZKUtil zkUtil;
	
	private Map<String, ServerInfo> liveServerInfos = new HashMap<String, ServerInfo>();
	private ReadWriteLock liveServerInfoLock = new ReentrantReadWriteLock(true);
	
	private List<JSONObject> gateServerInfos = new ArrayList<JSONObject>();
	private ReadWriteLock gateServerInfoLock = new ReentrantReadWriteLock(true);
	
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
//		if (!zkUtil.exists(activeRoomServer)){
//			zkUtil.createPersistent(activeRoomServer, null);
//		}
//		zkUtil.subscribeChildChanges(activeRoomServer, this);
		
		if (!zkUtil.exists(gateServer)){
			zkUtil.createPersistent(gateServer, null);
		}
		zkUtil.subscribeChildChanges(gateServer, this);
		
		List<String> currentChilds = zkUtil.getChildren(gateServer);
		resetGateServers(gateServer, currentChilds);
		
//		currentChilds = zkUtil.getChildren(activeRoomServer);
//		resetRoomServers(activeRoomServer, currentChilds);
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
		if (parentPath.equals(activeRoomServer)){			
			resetRoomServers(parentPath, currentChilds);
		}else if (parentPath.equals(gateServer)){
			resetGateServers(parentPath, currentChilds);
		}
	}
	
	public void resetRoomServers(String parentPath, List<String> currentChilds){		
		liveServerInfoLock.writeLock().lock();
		
		JsonUtils jsonUtils = new JsonUtils();
		liveServerInfos.clear();
		for (String c : currentChilds){
			try {
				String data = zkUtil.readData(parentPath + "/" + c);
				if (data == null || data.isEmpty()){
					// delete server
					continue;
				}
				ServerInfo info = jsonUtils.toObject(data, ServerInfo.class);
				liveServerInfos.put(info.getServerKey(), info);
			} catch (Exception e) {

			}
		}
		liveServerInfoLock.writeLock().unlock();
	}
	
	public void resetGateServers(String parentPath, List<String> currentChilds){
		gateServerInfoLock.writeLock().lock();
		
		gateServerInfos.clear();
		for (String c : currentChilds){
			try {
				String data = zkUtil.readData(parentPath + "/" + c);
				if (data == null || data.isEmpty()){
					// delete server
					continue;
				}
				JSONObject object = JSON.parseObject(data);
				gateServerInfos.add(object);
			} catch (Exception e) {

			}
		}
		gateServerInfoLock.writeLock().unlock();
	}

	public ServerInfo getRoomServerInfo(String serverKey){
		liveServerInfoLock.readLock().lock();
		ServerInfo info = liveServerInfos.get(serverKey);
		liveServerInfoLock.readLock().unlock();
		return info;
	}
	
	public List<JSONObject> getGateServers(){
		gateServerInfoLock.readLock().lock();
//		JSONObject[] infos = new JSONObject[gateServerInfos.size()];
		List<JSONObject> infos = new ArrayList<JSONObject>();
		for (JSONObject object : gateServerInfos){
			infos.add((JSONObject) object.clone());
		}
		gateServerInfoLock.readLock().unlock();
		return infos;
	}
	
	public String getRequestUri(String serverKey){
		String uri = null;
		liveServerInfoLock.readLock().lock();
		do {
			ServerInfo info = liveServerInfos.get(serverKey);
			if (info == null)
				break;
			if (info.getHost() == null)
				break;
			if (info.getHttpPort() == 0)
				break;
			uri = "http://" + info.getHost() + ":" + info.getHttpPort();
		} while (false);
		liveServerInfoLock.readLock().unlock();
		return uri;
	}
}
