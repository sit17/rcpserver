package com.i5i58.data.account;

import java.io.Serializable;
import java.util.Map;

public class MountSelectRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1455378285044190401L;

	private int fansVipNorMount;
	
	private Map<String, Integer> guardMounts;

	public int getFansVipNorMount() {
		return fansVipNorMount;
	}

	public void setFansVipNorMount(int fansVipNorMount) {
		this.fansVipNorMount = fansVipNorMount;
	}

	public Map<String, Integer> getGuardMounts() {
		return guardMounts;
	}

	public void setGuardMounts(Map<String, Integer> guardMounts) {
		this.guardMounts = guardMounts;
	}

}
