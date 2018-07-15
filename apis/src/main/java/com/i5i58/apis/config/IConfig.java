package com.i5i58.apis.config;

import com.i5i58.apis.constants.ResultDataSet;

public interface IConfig {
	public ResultDataSet checkRn(String rnVersion, int device, int main, int sub ,int func, String deviceVersion, String version);

	public ResultDataSet getPlatformConfig(int device, int main, int sub, int func, String version);

	public ResultDataSet getWindowsUpdateConfig(String loginVersion, String version,String allversion);

	public ResultDataSet getWindowsGameUpdateConfig(String version);

	public ResultDataSet getWindowsBossConfig(String version);
	
	public ResultDataSet getOSSConfig();
}
