package com.i5i58.util;

public enum AppKeySecretPair {
	GameServer("4CAD73C9E8C143CE954D392BC92544D3", "1A799FF80C9148DE8F17034DC1C50293"),
	GameTool("7fee3ba91337459d9c9c8f7342449596","252090846ee04921b51244b34177f57e");
	
	String key;
	String secret;
	AppKeySecretPair(String key, String secret){
		this.key = key;
		this.secret = secret;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public boolean match(String key, String secret){
		return this.key.equalsIgnoreCase(key) && this.secret.equalsIgnoreCase(secret);
	}
}
