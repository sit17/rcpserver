package com.i5i58.push.config;

import com.i5i58.async.threading.ITaskPool;
import com.i5i58.async.threading.TaskPool;
import com.i5i58.async.threading.TaskQueue;

public class PushTaskPool extends TaskPool implements ITaskPool {
	public PushTaskPool(int workerCount, int threadCount) {
		super(workerCount, threadCount);
	}
	@Override
	public TaskQueue getMatchWorker() {
		int workerId = 0;
		int peerCount = workers[0].getPeerCount();
		for (int i = 0; i < workers.length; i++) {
			int peer = workers[i].getPeerCount();
			if (peer < peerCount) {
				workerId = i;
				peerCount = peer;
			}
		}
		return workers[workerId];
	}

}
