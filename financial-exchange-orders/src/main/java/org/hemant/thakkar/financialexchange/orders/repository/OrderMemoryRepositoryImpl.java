package org.hemant.thakkar.financialexchange.orders.repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.hemant.thakkar.financialexchange.orders.domain.Order;
import org.hemant.thakkar.financialexchange.orders.domain.OrderImpl;
import org.springframework.stereotype.Service;

@Service("orderMemoryRepositoryImpl")
public class OrderMemoryRepositoryImpl implements OrderRepository {

	private static AtomicLong idGenerator = new AtomicLong(1);
	Map<Long, Order> orders;
	
	public OrderMemoryRepositoryImpl() {
		this.orders = new ConcurrentHashMap<>();
	}
	
	@Override
	public long saveOrder(OrderImpl order) {
		if (order.getId() == null) {
			order.setId(idGenerator.getAndIncrement());
		}
		orders.put(order.getId(), order);
		return order.getId();
	}

	@Override
	public boolean deleteOrder(long orderId) {
		Order order = orders.remove(orderId);
		return order != null;
	}

	@Override
	public Order getOrder(long orderId) {
		return orders.get(orderId);
	}

	@Override
	public List<Order> getOrdersByProduct(long productId) {
		List<Order> ordersByProduct = 
				orders.values().stream()
					.parallel()
					.filter(o -> o.getProductId() == productId)
					.collect(Collectors.toList());
		return ordersByProduct;
	}

	@Override
	public List<Order> getOrdersByParticipant(long participantId) {
		List<Order> ordersByParticipant = 
				orders.values().stream()
				.parallel()
				.filter(o -> o.getParticipantId() == participantId)
				.collect(Collectors.toList());
		return ordersByParticipant;
	}

	@Override
	public int getCount() {
		return orders.size();
	}
	
}
