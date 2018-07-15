package com.i5i58.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.alibaba.dubbo.common.serialize.support.SerializationOptimizer;
import com.i5i58.data.account.Account;
import com.i5i58.data.channel.MyChannels;

public class SerializationOptimizerImpl implements SerializationOptimizer {
	@SuppressWarnings("rawtypes")
	public Collection<Class> getSerializableClasses() {
		List<Class> classes = new LinkedList<Class>();
		classes.add(Account.class);
		classes.add(MyChannels.class);
		return classes;
	}
}