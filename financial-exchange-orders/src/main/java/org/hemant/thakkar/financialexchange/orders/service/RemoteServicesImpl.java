package org.hemant.thakkar.financialexchange.orders.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hemant.thakkar.financialexchange.orders.domain.APIResponse;
import org.hemant.thakkar.financialexchange.orders.domain.Order;
import org.hemant.thakkar.financialexchange.orders.domain.OrderBookEntry;
import org.hemant.thakkar.financialexchange.orders.domain.ResultCode;
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
	private Map<String, Integer> servicesPorts;
	
	public RemoteServicesImpl() {
		baseUrl = System.getProperty("remote.services.baseurl", "http://localhost");
		servicesPorts = new HashMap<>();
		servicesPorts.put("products.port", 
				Integer.parseInt(System.getProperty("products.port", "8080")));
		servicesPorts.put("participants.port", 
				Integer.parseInt(System.getProperty("participants.port", "8081")));
		servicesPorts.put("orders.port", 
				Integer.parseInt(System.getProperty("orders.port", "8082")));
		servicesPorts.put("orderbooks.port", 
				Integer.parseInt(System.getProperty("orderbooks.port", "8083")));
		servicesPorts.put("trades.port", 
				Integer.parseInt(System.getProperty("trades.port", "8084")));
	}
	
	@Override
	public boolean isValidProduct(long productId) {
		boolean validProduct = false;
		try {
			String serviceUrl = baseUrl + ":" + servicesPorts.get("products.port") 
					+ "/product/equity/" + productId;
			RestTemplate restTemplate = new RestTemplate(); 
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			ResponseEntity<String> response = restTemplate.exchange(serviceUrl, HttpMethod.GET, entity, String.class);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(response.getBody());
			int responseCode = root.get("responseCode").asInt();
			validProduct = responseCode == ResultCode.PRODUCT_FOUND.getCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return validProduct;
	}

	@Override
	public boolean isValidParticipant(long participantId) {
		boolean validParticipant = false;
		try {
			String serviceUrl = baseUrl + ":" + servicesPorts.get("participants.port") 
					+ "/participant/broker/" + participantId;
			RestTemplate restTemplate = new RestTemplate(); 
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			ResponseEntity<String> response = restTemplate.exchange(serviceUrl, HttpMethod.GET, entity, String.class);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(response.getBody());
			int responseCode = root.get("responseCode").asInt();
			validParticipant = responseCode == ResultCode.PARTICIPANT_FOUND.getCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return validParticipant;
	}

	@Override
	public boolean cancelOrderInBook(long orderId) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean addOrderInBook(Order order) {
		boolean addedToBook = false;
		try {
			String serviceUrl = baseUrl + ":" + servicesPorts.get("orderbooks.port") 
					+ "/orderBook/order/";
			
			OrderBookEntry orderBookEntry = new OrderBookEntry();
			orderBookEntry.setEntryTime(order.getEntryTime());
			orderBookEntry.setOrderId(order.getId());
			orderBookEntry.setPrice(order.getPrice());
			orderBookEntry.setProductId(order.getProductId());
			orderBookEntry.setQuantity(order.getQuantity());
			orderBookEntry.setSide(order.getSide());
			orderBookEntry.setType(order.getType());
			
			RestTemplate restTemplate = new RestTemplate(); 
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<OrderBookEntry> entity = new HttpEntity<OrderBookEntry>(orderBookEntry, headers);
			
			ResponseEntity<APIResponse> response = restTemplate.exchange(serviceUrl, HttpMethod.POST, entity, APIResponse.class);
			addedToBook = response.getBody().getResponseCode() == ResultCode.ORDER_ACCEPTED.getCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return addedToBook;
	}

	@Override
	public List<Long> getTradesForOrder(long orderId) {
		// TODO Auto-generated method stub
		return new ArrayList<Long>();
	}

}
