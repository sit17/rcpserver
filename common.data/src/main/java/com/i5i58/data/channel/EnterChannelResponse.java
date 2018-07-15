package com.i5i58.data.channel;

import java.io.Serializable;

import com.i5i58.data.account.HotAccount;

public class EnterChannelResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4663943076487681913L;

	private HotAccount owner;
	
	private HotChannel channel;
	
	private boolean attention;
	
	private boolean giftUpdate;

	public HotAccount getOwner() {
		return owner;
	}

	public void setOwner(HotAccount owner) {
		this.owner = owner;
	}

	public HotChannel getChannel() {
		return channel;
	}

	public void setChannel(HotChannel channel) {
		this.channel = channel;
	}

	public boolean isAttention() {
		return attention;
	}

	public void setAttention(boolean attention) {
		this.attention = attention;
	}

	public boolean isGiftUpdate() {
		return giftUpdate;
	}

	public void setGiftUpdate(boolean giftUpdate) {
		this.giftUpdate = giftUpdate;
	}
	
}
