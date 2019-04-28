package org.hemant.thakkar.financialexchange.orderbooks.service;

import org.hemant.thakkar.financialexchange.orderbooks.domain.Trade;

public interface RemoteServices {

	boolean saveTrade(Trade trade);
	String getProductSymbol(long productId);

}
