package com.i5i58.service.android.channel.async;

import com.i5i58.data.account.Wallet;
import com.i5i58.data.channel.ChannelRecord;
import com.i5i58.primary.dao.account.WalletPriDao;
import com.i5i58.util.DataSaveThread;

public class TaskDataBaseDriftComment implements Runnable {

	private String accId;

	private WalletPriDao walletPriDao;

	private long amount;
	
	private DataSaveThread<ChannelRecord, Long> dataSaveThread;

	public TaskDataBaseDriftComment(String accId, WalletPriDao walletPriDao, long amount,
			DataSaveThread<ChannelRecord, Long> dataSaveThread) {
		super();
		this.accId = accId;
		this.walletPriDao = walletPriDao;
		this.amount = amount;
		this.dataSaveThread = dataSaveThread;
	}

	@Override
	public void run() {
		Wallet wallet = walletPriDao.findByAccId(accId);
		wallet.setiGold(wallet.getiGold() - amount);
		walletPriDao.save(wallet);
		this.dataSaveThread.Save();
	}

}
