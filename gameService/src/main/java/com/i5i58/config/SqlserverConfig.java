package com.i5i58.config;

import java.sql.Connection;
import java.sql.DriverManager;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "my.sqlserver")
public class SqlserverConfig {
	private static String driver;
	private static String username;
	private static String password;
	private static String accountdbname;
	private static String treasuredbname;
	private static String host;
	private static int port;

	private static Connection _ACCOUNTCONN = null;
	private static Connection _TREASURECONN = null;

	public static String getUsername() {
		return username;
	}

	public static String getAccountdbname() {
		return accountdbname;
	}

	public static void setAccountdbname(String accountdbname) {
		SqlserverConfig.accountdbname = accountdbname;
	}

	public static String getTreasuredbname() {
		return treasuredbname;
	}

	public static void setTreasuredbname(String treasuredbname) {
		SqlserverConfig.treasuredbname = treasuredbname;
	}

	public static void setUsername(String username) {
		SqlserverConfig.username = username;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		SqlserverConfig.password = password;
	}

	public static String getHost() {
		return host;
	}

	public static void setHost(String host) {
		SqlserverConfig.host = host;
	}

	public static int getPort() {
		return port;
	}

	public static void setPort(int port) {
		SqlserverConfig.port = port;
	}

	public static String getDriver() {
		return driver;
	}

	public static void setDriver(String driver) {
		SqlserverConfig.driver = driver;
	}

	public static void init() {
		try {
			if (_ACCOUNTCONN != null && !_ACCOUNTCONN.isClosed()) {
			} else {
				String sDriverName = driver;
				String sDBUrl = "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + accountdbname;
				Class.forName(sDriverName);
				_ACCOUNTCONN = DriverManager.getConnection(sDBUrl, username, password);
			}
			if (_TREASURECONN != null && !_TREASURECONN.isClosed()) {
			} else {
				String sDriverName = driver;
				String sDBUrl = "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + treasuredbname;
				Class.forName(sDriverName);
				_TREASURECONN = DriverManager.getConnection(sDBUrl, username, password);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
	}

	@Bean
	public Connection GetAccountConn() {

		try {
			if (_ACCOUNTCONN != null && !_ACCOUNTCONN.isClosed()) {
				return _ACCOUNTCONN;
			} else {
				String sDriverName = driver;
				String sDBUrl = "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + accountdbname;

				Class.forName(sDriverName);
				_ACCOUNTCONN = DriverManager.getConnection(sDBUrl, username, password);
			}
			return _ACCOUNTCONN;

		} catch (Exception ex) {
			// ex.printStackTrace();
			System.out.println(ex.getMessage());
			return null;
		}
	}

	@Bean
	public Connection GetTreasureConn() {

		try {
			if (_TREASURECONN != null && !_TREASURECONN.isClosed()) {
				return _TREASURECONN;
			} else {
				String sDriverName = driver;
				String sDBUrl = "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + treasuredbname;

				Class.forName(sDriverName);
				_TREASURECONN = DriverManager.getConnection(sDBUrl, username, password);
			}
			return _TREASURECONN;

		} catch (Exception ex) {
			// ex.printStackTrace();
			System.out.println(ex.getMessage());
			return null;
		}
	}
}
