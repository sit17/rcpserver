package com.i5i58.yunxin.Utils;

import java.io.IOException;
import java.util.Map;

public class CreateChannelResult extends YXResultSet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5319230474757431700L;

	public CreateChannelResultInfo getRet() throws IOException {
		Map<?, ?> ret = this.getMap("ret");
		CreateChannelResultInfo ccri = new CreateChannelResultInfo();
		ccri.setCid(ret.get("cid").toString());
		ccri.setCtime((Long) ret.get("ctime"));
		ccri.setHlsPullUrl(ret.get("hlsPullUrl").toString());
		ccri.setHttpPullUrl(ret.get("httpPullUrl").toString());
		ccri.setName(ret.get("name").toString());
		ccri.setPushUrl(ret.get("pushUrl").toString());
		ccri.setRtmpPullUrl(ret.get("rtmpPullUrl").toString());
		return ccri;
	}

}
