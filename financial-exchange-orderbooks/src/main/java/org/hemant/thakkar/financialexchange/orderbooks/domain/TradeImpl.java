package org.hemant.thakkar.financialexchange.orderbooks.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TradeImpl implements Trade {

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS");

	private long id;
	private LocalDateTime tradeTime;
	private long buyTradableId;
	private long sellTradableId;
	private int quantity;
	private BigDecimal price;
	
	public TradeImpl() {
		this.setTradeTime(LocalDateTime.now());
	}
	
	@Override
	public long getId() {
		return this.id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public LocalDateTime getTradeTime() {
		return this.tradeTime;
	}

	@Override
	public void setTradeTime(LocalDateTime tradeTime) {
		this.tradeTime = tradeTime;
	}

	public long getBuyTradableId() {
		return buyTradableId;
	}

	public void setBuyTradableId(long buyTradableId) {
		this.buyTradableId = buyTradableId;
	}

	public long getSellTradableId() {
		return sellTradableId;
	}

	public void setSellTradableId(long sellTradableId) {
		this.sellTradableId = sellTradableId;
	}

	@Override
	public BigDecimal getPrice() {
		return this.price;
	}

	@Override
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public int getQuantity() {
		return this.quantity;
	}

	@Override
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String toString() {
		StringBuffer message = new StringBuffer();
		message.append("-------- Trade ----------\n");
		message.append("ID: ").append(this.getId()).append("\n");
		message.append("Time: " + formatter.format(LocalDateTime.now()) + "\n");
		message.append("Qty: ").append(this.getQuantity()).append("\n");
		message.append("Price: ").append(this.getPrice()).append("\n");
		message.append("Buy Side Tradable Id: ").append(this.getBuyTradableId()).append("  ");
		message.append("Sell Side Tradable Id: ").append(this.getSellTradableId());
		return message.toString();

	}
	
}
