package com.i5i58.util;

import org.springframework.stereotype.Component;

import com.i5i58.util.KVCode;

@Component
public class AuthVerify<T extends Enum<?> & KVCode> {

	/**
	 * verify
	 * 
	 * @param authorty
	 * @param value
	 * @return
	 */
	public boolean Verify(T authorty, int value) {
		return (value & authorty.getValue()) == authorty.getValue();
	}

	/**
	 * add permission
	 * 
	 * @author frank
	 * @param authorty
	 * @param value
	 * @return
	 */
	public int addPermission(T authorty, int value) {
		return value |= authorty.getValue();
	}

	public int addPermissions(int value, @SuppressWarnings("unchecked") T... authortys) {
		for (T cac : authortys) {
			value |= cac.getValue();
		}
		return value;
	}

	public int getPermission(@SuppressWarnings("unchecked") T... authortys) {
		int ret = 0;
		for (T cac : authortys) {
			ret |= cac.getValue();
		}
		return ret;
	}

	/**
	 * permission
	 * 
	 * @author frank
	 * @param authorty
	 * @param value
	 * @return
	 */
	public int removePermission(T authorty, int value) {
		return value &= ~authorty.getValue();
	}
}
