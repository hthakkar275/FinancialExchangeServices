package org.hemant.thakkar.financialexchange.orderbooks.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.hemant.thakkar.financialexchange.orderbooks.domain.APIDataResponse;
import org.hemant.thakkar.financialexchange.orderbooks.domain.APIResponse;
import org.hemant.thakkar.financialexchange.orderbooks.domain.OrderActivity;
import org.hemant.thakkar.financialexchange.orderbooks.domain.OrderActivityEntry;
import org.hemant.thakkar.financialexchange.orderbooks.domain.ResultCode;
import org.hemant.thakkar.financialexchange.orderbooks.domain.TradeEntry;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("remoteServicesImpl")
public class RemoteServicesImpl implements RemoteServices {

	private String baseUrl;
	private boolean useNonStandardPort;
	private Map<String, Integer> servicesPorts;
	
	public RemoteServicesImpl() {
		baseUrl = System.getProperty("remote.services.baseurl", "http://localhost");
		String useNonStanardPortStr = System.getProperty("remote.services.useNonStandardPort", "false");
		try {
			useNonStandardPort = Boolean.parseBoolean(useNonStanardPortStr);
		} catch (Exception e) {
			useNonStandardPort = true;
		}
		servicesPorts = new HashMap<>();
		servicesPorts.put("product.service.port", 
				Integer.parseInt(System.getProperty("product.service.port", "8080")));
		servicesPorts.put("participant.service.port", 
				Integer.parseInt(System.getProperty("participant.service.port", "8081")));
		servicesPorts.put("order.service.port", 
				Integer.parseInt(System.getProperty("order.service.port", "8082")));
		servicesPorts.put("orderbook.service.port", 
				Integer.parseInt(System.getProperty("orderbook.service.port", "8083")));
		servicesPorts.put("trade.service.port", 
				Integer.parseInt(System.getProperty("trade.service.port", "8084")));
	}
		
	@SuppressWarnings("rawtypes")
	@Override
	public boolean saveTrade(TradeEntry tradeEntry) {
		boolean updatedTrade = false;
		try {
			StringBuffer stringBuffer = new StringBuffer(baseUrl);
			if (useNonStandardPort) {
				stringBuffer.append(":").append(servicesPorts.get("trade.service.port"));
			}
			stringBuffer.append("/trade/");
			String serviceUrl = stringBuffer.toString();
			System.out.println("saveTrade service url: " + serviceUrl);
			
			RestTemplate restTemplate = new RestTemplate(); 
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<TradeEntry> entity = new HttpEntity<TradeEntry>(tradeEntry, headers);
			
			ResponseEntity<APIDataResponse> response = restTemplate.exchange(serviceUrl, HttpMethod.POST, entity, APIDataResponse.class);
			updatedTrade = response.getBody().getResponseCode() == ResultCode.TRADE_ACCEPTED.getCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updatedTrade;
	}

	@Override
	public String getProductSymbol(long productId) {
		String symbol = null;
		try {
			StringBuffer stringBuffer = new StringBuffer(baseUrl);
			if (useNonStandardPort) {
				stringBuffer.append(":").append(servicesPorts.get("product.service.port"));
			}
			stringBuffer.append("/product/equity/");
			stringBuffer.append(productId);
			String serviceUrl = stringBuffer.toString();
			System.out.println("getProductSymbol service url: " + serviceUrl);

			RestTemplate restTemplate = new RestTemplate(); 
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			ResponseEntity<String> response = restTemplate.exchange(serviceUrl, HttpMethod.GET, entity, String.class);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(response.getBody());
			JsonNode product = root.get("data");
			symbol = product.get("symbol").asText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return symbol;
	}

	@Override
	public boolean updateOrders(TradeEntry tradeEntry) {
		boolean updatedOrders = false;
		try {
			boolean buySideOrderUpdated = orderTradedQuantity(tradeEntry.getBuyTradableId(), 
					tradeEntry.getTradeTime(), tradeEntry.getQuantity());
			boolean sellSideOrderUpdated = orderTradedQuantity(tradeEntry.getSellTradableId(), 
					tradeEntry.getTradeTime(), tradeEntry.getQuantity());
			updatedOrders = buySideOrderUpdated && sellSideOrderUpdated;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updatedOrders;
	}
	
	@Override
	public boolean orderTradedQuantity(long orderId, LocalDateTime tradeTime, int tradedQuantity) {
		boolean updatedOrder = false;
		try {
			StringBuffer stringBuffer = new StringBuffer(baseUrl);
			if (useNonStandardPort) {
				stringBuffer.append(":").append(servicesPorts.get("order.service.port"));
			}
			stringBuffer.append("/order/activity/");
			stringBuffer.append(orderId);
			String serviceUrl = stringBuffer.toString();
			System.out.println("orderTradedQuantity service url: " + serviceUrl);
			
			OrderActivityEntry orderActivityEntry = new OrderActivityEntry();
			orderActivityEntry.setOrderId(orderId);
			orderActivityEntry.setTradedQuantity(tradedQuantity);
			orderActivityEntry.setOrderActivity(OrderActivity.TRADED);
			orderActivityEntry.setActivityTime(tradeTime);
			
			RestTemplate restTemplate = new RestTemplate(); 
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<OrderActivityEntry> entity = new HttpEntity<OrderActivityEntry>(orderActivityEntry, headers);
			
			ResponseEntity<APIResponse> response = restTemplate.exchange(serviceUrl, HttpMethod.PUT, entity, APIResponse.class);
			updatedOrder = response.getBody().getResponseCode() == ResultCode.ORDER_UPDATED.getCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updatedOrder;
	}
	
	@Override
	public boolean orderBookedQuantity(long orderId, int bookedQuantity, LocalDateTime bookedTime) {
		boolean updatedOrder = false;
		try {
			StringBuffer stringBuffer = new StringBuffer(baseUrl);
			if (useNonStandardPort) {
				stringBuffer.append(":").append(servicesPorts.get("order.service.port"));
			}
			stringBuffer.append("/order/activity/");
			stringBuffer.append(orderId);
			String serviceUrl = stringBuffer.toString();
			System.out.println("orderBookedQuantity service url: " + serviceUrl);

			OrderActivityEntry orderActivityEntry = new OrderActivityEntry();
			orderActivityEntry.setOrderId(orderId);
			orderActivityEntry.setBookedQuantity(bookedQuantity);
			orderActivityEntry.setOrderActivity(OrderActivity.BOOKED);
			orderActivityEntry.setActivityTime(bookedTime);
			
			RestTemplate restTemplate = new RestTemplate(); 
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<OrderActivityEntry> entity = new HttpEntity<OrderActivityEntry>(orderActivityEntry, headers);
			
			ResponseEntity<APIResponse> response = restTemplate.exchange(serviceUrl, HttpMethod.PUT, entity, APIResponse.class);
			updatedOrder = response.getBody().getResponseCode() == ResultCode.ORDER_UPDATED.getCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updatedOrder;
	}

	@Override
	public boolean orderCancelledQuantity(long orderId, int cancelledQuantity, LocalDateTime cancelledTime) {
		boolean updatedOrder = false;
		try {
			StringBuffer stringBuffer = new StringBuffer(baseUrl);
			if (useNonStandardPort) {
				stringBuffer.append(":").append(servicesPorts.get("order.service.port"));
			}
			stringBuffer.append("/order/activity/");
			stringBuffer.append(orderId);
			String serviceUrl = stringBuffer.toString();
			System.out.println("orderCancelledQuantity service url: " + serviceUrl);
			
			OrderActivityEntry orderActivityEntry = new OrderActivityEntry();
			orderActivityEntry.setOrderId(orderId);
			orderActivityEntry.setBookedQuantity(cancelledQuantity);
			orderActivityEntry.setOrderActivity(OrderActivity.CANCELLED);
			orderActivityEntry.setActivityTime(cancelledTime);
			
			RestTemplate restTemplate = new RestTemplate(); 
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<OrderActivityEntry> entity = new HttpEntity<OrderActivityEntry>(orderActivityEntry, headers);
			
			ResponseEntity<APIResponse> response = restTemplate.exchange(serviceUrl, HttpMethod.PUT, entity, APIResponse.class);
			updatedOrder = response.getBody().getResponseCode() == ResultCode.ORDER_UPDATED.getCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updatedOrder;
	}

}
