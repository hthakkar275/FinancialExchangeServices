package org.hemant.thakkar.financialexchange.orderbooks.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface Trade {

	public long getId();
	public void setId(long id);
	public LocalDateTime getTradeTime();
	public void setTradeTime(LocalDateTime tradeTime);
	public long getBuyTradableId();
	public void setBuyTradableId(long tradableId);
	public long getSellTradableId();
	public void setSellTradableId(long tradableId);
	public BigDecimal getPrice();
	public void setPrice(BigDecimal price);
	public int getQuantity();
	public void setQuantity(int quantity);
}
