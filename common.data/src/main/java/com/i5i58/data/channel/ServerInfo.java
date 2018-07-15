package com.i5i58.data.channel;

/**
 * ��������Ϣ
 * @author frank
 *
 */
public class ServerInfo {
	private String nodePath="";		// server node path in zookeeper
	private String connectorName="";
	private String serverKey="";				// server id, each server instance have one id
	private String host="";				// server host, doman or ip ?
	private int connectorPort;		// local tpc port
	private int directTcpPort;			// remote tcp port
	private int directWebsocketPort;			// remote tcp port
	private int httpPort;			//http port for intranet-network
	private String business="";			//ҵ������(ֱ������Ϸ)
	private boolean multiConnect;		//������̨ҵ�񹲴棨����ֱ����ֻ�ܵ�����������Ϸ���Զ࿪���䣩
	
	public ServerInfo(){
		
	}
	public ServerInfo(ServerInfo info){
		this.nodePath 				= info.nodePath;
		this.connectorName 			= info.connectorName;
		this.serverKey 				= info.serverKey;
		this.host 					= info.host;
		this.connectorPort 			= info.connectorPort;
		this.directTcpPort			= info.directTcpPort;
		this.directWebsocketPort 	= info.directWebsocketPort;
		this.httpPort 				= info.httpPort;
		this.business 				= info.business;
		this.multiConnect 			= info.multiConnect;
	}
	public String getNodePath() {
		return nodePath;
	}
	public void setNodePath(String nodePath) {
		this.nodePath = nodePath;
	}
	public String getConnectorName() {
		return connectorName;
	}
	public void setConnectorName(String connectorName) {
		this.connectorName = connectorName;
	}
	public String getServerKey() {
		return serverKey;
	}
	public void setServerKey(String serverKey) {
		this.serverKey = serverKey;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getConnectorPort() {
		return connectorPort;
	}
	public void setConnectorPort(int connectorPort) {
		this.connectorPort = connectorPort;
	}
	public int getDirectTcpPort() {
		return directTcpPort;
	}
	public void setDirectTcpPort(int directTcpPort) {
		this.directTcpPort = directTcpPort;
	}
	public int getDirectWebsocketPort() {
		return directWebsocketPort;
	}
	public void setDirectWebsocketPort(int directWebsocketPort) {
		this.directWebsocketPort = directWebsocketPort;
	}
	public String getBusiness() {
		return business;
	}
	public void setBusiness(String business) {
		this.business = business;
	}
	public boolean isMultiConnect() {
		return multiConnect;
	}
	public void setMultiConnect(boolean multiConnect) {
		this.multiConnect = multiConnect;
	}
	public int getHttpPort() {
		return httpPort;
	}
	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}
}
