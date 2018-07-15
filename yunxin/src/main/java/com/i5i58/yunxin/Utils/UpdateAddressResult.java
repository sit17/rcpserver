package com.i5i58.yunxin.Utils;

import java.io.IOException;
import java.util.Map;

public class UpdateAddressResult extends YXResultSet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2462649488763830098L;

	public UpdateAddressResultInfo getRet() throws IOException {
		Map<?, ?> ret = this.getMap("ret");
		UpdateAddressResultInfo uari = new UpdateAddressResultInfo();
		uari.setName(ret.get("name").toString());
		uari.setHlsPullUrl(ret.get("hlsPullUrl").toString());
		uari.setHttpPullUrl(ret.get("httpPullUrl").toString());
		uari.setPushUrl(ret.get("pushUrl").toString());
		uari.setRtmpPullUrl(ret.get("rtmpPullUrl").toString());
		return uari;
	}
}