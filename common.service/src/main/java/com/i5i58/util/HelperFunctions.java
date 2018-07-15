package com.i5i58.util;

import com.i5i58.data.account.Wallet;
import com.i5i58.data.record.MoneyFlow;
import com.i5i58.data.record.MoneyFlowType;

public class HelperFunctions {
	public static void setMoneyFlowSource(Wallet wallet, MoneyFlow moneyFlow){
		moneyFlow.setSourceIGold(wallet.getiGold());
		moneyFlow.setSourceDiamond(wallet.getDiamond());
		moneyFlow.setSourceRedDiamond(wallet.getRedDiamond());
		moneyFlow.setSourceCommission(wallet.getCommission());
		moneyFlow.setSourceGiftTicket(wallet.getGiftTicket());
	}
	
	public static void setMoneyFlowTarget(Wallet wallet, MoneyFlow moneyFlow){
		moneyFlow.setTargetIGold(wallet.getiGold());
		moneyFlow.setTargetDiamond(wallet.getDiamond());
		moneyFlow.setTargetRedDiamond(wallet.getRedDiamond());
		moneyFlow.setTargetCommission(wallet.getCommission());
		moneyFlow.setTargetGiftTicket(wallet.getGiftTicket());
	}
	
	public static void setMoneyFlowType(MoneyFlowType flowType,MoneyFlow moneyFlow){
		moneyFlow.setType(flowType.getValue());
		moneyFlow.setDescription(flowType.getDesc());
	}
}
