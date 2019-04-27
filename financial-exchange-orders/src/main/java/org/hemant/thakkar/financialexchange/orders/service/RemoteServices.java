package org.hemant.thakkar.financialexchange.orders.service;

import java.util.List;

import org.hemant.thakkar.financialexchange.orders.domain.OrderEntry;

public interface RemoteServices {

	boolean isValidProduct(long productId);
	boolean isValidParticipant(long participantId);
	boolean cancelOrderInBook(long orderId);
	boolean addOrderInBook(OrderEntry order);
	List<Long> getTradesForOrder(long orderId);
}
