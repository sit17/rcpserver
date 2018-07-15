package com.i5i58.base.rabbitmq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import com.i5i58.base.util.PropertiesUtil;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


public abstract class EndPoint {

	protected Channel channel;
	protected Connection connection;
	protected String endPointName;
	
	protected String address;
	protected String username;
	protected String password;
	protected String virtualHost;
	protected Address[] arrAddress;
	
	public void loadConfig(){
		address = PropertiesUtil.getProperty("my.rabbitmq.addresses");
		username = PropertiesUtil.getProperty("my.rabbitmq.username");
		password = PropertiesUtil.getProperty("my.rabbitmq.password");
		virtualHost = PropertiesUtil.getProperty("my.rabbitmq.virtualHost");
		if (address == null || address.isEmpty()){
			return;
		}
		String[] addresses = address.split(",");
		ArrayList<Address> listAddr = new ArrayList<>();
		for (String item : addresses){
			String[] subAddr = item.split(":");
			if (subAddr.length < 2)
				continue;
			Address a = new Address(subAddr[0], Integer.parseInt(subAddr[1]));
			listAddr.add(a);
		}
		arrAddress = listAddr.toArray(new Address[0]);
	}

	public EndPoint(String endpointName) {
		this.endPointName = endpointName;
		loadConfig();
		ConnectionFactory factory = new ConnectionFactory();
//		factory.setHost("localhost");
		factory.setUsername(username);
		factory.setPassword(password);
		try {
			connection = factory.newConnection(arrAddress);
			channel = connection.createChannel();
			channel.queueDeclare(endpointName, true, false, false, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() throws IOException, TimeoutException {
		this.channel.close();
		this.connection.close();
	}
}