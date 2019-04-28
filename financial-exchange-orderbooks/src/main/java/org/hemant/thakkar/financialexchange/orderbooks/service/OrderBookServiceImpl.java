package org.hemant.thakkar.financialexchange.orderbooks.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hemant.thakkar.financialexchange.orderbooks.domain.ExchangeException;
import org.hemant.thakkar.financialexchange.orderbooks.domain.OrderBookEntry;
import org.hemant.thakkar.financialexchange.orderbooks.domain.OrderBookItem;
import org.hemant.thakkar.financialexchange.orderbooks.domain.OrderBookItemImpl;
import org.hemant.thakkar.financialexchange.orderbooks.domain.OrderBookState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("orderBookServiceImpl")
public class OrderBookServiceImpl implements OrderBookService {

	@Autowired
	@Qualifier("remoteServicesImpl")
	private RemoteServices remoteServices;
	
	private Map<Long, OrderBook> orderBooks;
	
	public OrderBookServiceImpl() {
		this.orderBooks = new ConcurrentHashMap<Long, OrderBook>();
	}
	
	public OrderBook getOrderBook(long productId) throws ExchangeException {
		OrderBook orderBook = null;
		orderBook = orderBooks.get(productId);
		if (orderBook == null) {
			String productSymbol = remoteServices.getProductSymbol(productId);
			orderBook = new OrderBookImpl(remoteServices, productId, productSymbol);
			orderBooks.put(productId, orderBook);
		}
		return orderBook;
	}
		
	public void deleteOrderBook(long productId) {
		orderBooks.remove(productId);
	}

	public void addOrder(OrderBookEntry orderBookEntry) throws ExchangeException {
		OrderBook orderBook = getOrderBook(orderBookEntry.getProductId());
		OrderBookItem orderBookItem = createOrderBookItem(orderBookEntry);
		orderBook.processOrder(orderBookItem);
	}

	@Override
	public void cancelOrder(long productId, long orderId) throws ExchangeException {
		OrderBook orderBook = getOrderBook(productId);
		orderBook.cancelOrder(orderId);		
	}

	@Override
	public OrderBookState getOrderBookMontage(long productId) {
		// TODO Auto-generated method stub
		return null;
	}

	private OrderBookItem createOrderBookItem(OrderBookEntry orderBookEntry) throws ExchangeException {
		OrderBookItem order = new OrderBookItemImpl();
		order.setOrderId(orderBookEntry.getOrderId());
		order.setProductId(orderBookEntry.getProductId());
		order.setType(orderBookEntry.getType());
		order.setSide(orderBookEntry.getSide());
		order.setQuantity(orderBookEntry.getQuantity());
		order.setPrice(orderBookEntry.getPrice());
		return order;
	}

}

