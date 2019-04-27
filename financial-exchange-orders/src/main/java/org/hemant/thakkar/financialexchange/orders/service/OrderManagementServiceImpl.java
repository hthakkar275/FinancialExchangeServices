package org.hemant.thakkar.financialexchange.orders.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.hemant.thakkar.financialexchange.orders.domain.ExchangeException;
import org.hemant.thakkar.financialexchange.orders.domain.Order;
import org.hemant.thakkar.financialexchange.orders.domain.OrderEntry;
import org.hemant.thakkar.financialexchange.orders.domain.OrderImpl;
import org.hemant.thakkar.financialexchange.orders.domain.OrderLongevity;
import org.hemant.thakkar.financialexchange.orders.domain.OrderReport;
import org.hemant.thakkar.financialexchange.orders.domain.OrderStatus;
import org.hemant.thakkar.financialexchange.orders.domain.ResultCode;
import org.hemant.thakkar.financialexchange.orders.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("orderManagementServiceImpl")
public class OrderManagementServiceImpl implements OrderManagementService {

	private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS");
	
	@Autowired
	@Qualifier("orderJpaRepositoryImpl")
	private OrderRepository orderRepository;
	
	@Autowired
	@Qualifier("remoteServicesImpl")
	private RemoteServices remoteServices;
	
	@Override
	public void cancelOrder(long orderId) throws ExchangeException {
		Order order = orderRepository.getOrder(orderId);
		if (order == null) {
			throw new ExchangeException(ResultCode.ORDER_NOT_FOUND);
		}
		if (order.getStatus() == OrderStatus.FILLED) {
			throw new ExchangeException(ResultCode.ORDER_FILLED);
		}
		remoteServices.cancelOrderInBook(orderId);
	}

	@Override
	public OrderReport getOrderStatus(long orderId) throws ExchangeException {
		Order order = orderRepository.getOrder(orderId);
		OrderReport orderReport = createOrderReport(order);
		return orderReport;
	}

	@Override
	public long acceptNewOrder(OrderEntry orderEntry) throws ExchangeException {
		Order order = createOrder(orderEntry);
		long orderId = orderRepository.saveOrder((OrderImpl) order);
		if (orderId < 0) {
			throw new ExchangeException(ResultCode.GENERAL_ERROR);
		}
		orderEntry.setId(orderId);
		remoteServices.addOrderInBook(orderEntry);
		return order.getId();
	}

	private Order createOrder(OrderEntry orderEntry) throws ExchangeException {
		
		if (!remoteServices.isValidProduct(orderEntry.getProductId())) {
			throw new ExchangeException(ResultCode.PRODUCT_NOT_FOUND);
		}
		
		if (!remoteServices.isValidParticipant(orderEntry.getParticipantId())) {
			throw new ExchangeException(ResultCode.PARTICIPANT_NOT_FOUND);
		}
		
		Order order = new OrderImpl();
		order.setEntryTime(LocalDateTime.now());
		order.setProductId(orderEntry.getProductId());
		order.setParticipantId(orderEntry.getParticipantId());
		order.setType(orderEntry.getType());
		order.setLongevity(OrderLongevity.DAY);
		order.setSide(orderEntry.getSide());
		order.setQuantity(orderEntry.getQuantity());
		order.setPrice(orderEntry.getPrice());
		order.setStatus(OrderStatus.NEW);
		return order;
	}

	@Override
	public void updateFromOrderBook(long orderId, OrderEntry orderEntry) throws ExchangeException {
		Order order = createOrder(orderEntry);
		orderRepository.saveOrder((OrderImpl) order);
	}

	private OrderReport createOrderReport(Order order) {
		OrderReport orderReport = new OrderReport();
		orderReport.setBookedQuantity(order.getBookedQuantity());
		orderReport.setEntryTime(timeFormatter.format(order.getEntryTime()));
		orderReport.setId(order.getId());
		orderReport.setParticipantId(order.getParticipantId());
		orderReport.setLongevity(order.getLongevity());
		orderReport.setOriginalQuantity(order.getQuantity());
		orderReport.setProductId(order.getProductId());
		orderReport.setSide(order.getSide());
		orderReport.setStatus(order.getStatus());
		orderReport.setTradedQuantity(order.getTradedQantity());
		orderReport.setType(order.getType());
		orderReport.setPrice(order.getPrice());
		List<Long> trades = remoteServices.getTradesForOrder(order.getId());
		orderReport.setTrades(trades);
		return orderReport;
	}

	@Override
	public List<OrderReport> getOrdersForProduct(long productId) throws ExchangeException {
		List<Order> orders = orderRepository.getOrdersByProduct(productId);
		List<OrderReport> orderReports = orders.stream()
				.map(this::createOrderReport)
				.collect(Collectors.toList());
		return orderReports;
	}

}
