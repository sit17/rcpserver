package com.i5i58.yunxin.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YXResultSet extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7280158027895227546L;

	public String getCode() {
		if (containsKey("code")) {
			return get("code").toString();
		} else {
			return null;
		}
	}

	public Map<?, ?> getMap(String key) {
		if (containsKey(key)) {
			return (Map<?, ?>) get(key);
		} else {
			return null;
		}
	}

	public String getString(String key) {
		if (containsKey(key)) {
			return get(key).toString();
		} else {
			return null;
		}
	}
	
	public List<?> getList(String key)
	{
		if (containsKey(key)) {
			return (List<?>)get(key);
		} else {
			return null;
		}
	}

	public String getError() {
		return CodeToString.getString(getCode());
	}
}
