package org.hemant.thakkar.financialexchange.orders.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

	private static final Log logger = LogFactory.getLog(RemoteServicesImpl.class);

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
	
	@Override
	public boolean isValidProduct(long productId) {
		boolean validProduct = false;
		try {
			StringBuffer stringBuffer = new StringBuffer(baseUrl);
			if (useNonStandardPort) {
				stringBuffer.append(":").append(servicesPorts.get("product.service.port"));
			}
			stringBuffer.append("/product/equity/");
			stringBuffer.append(productId);
			String serviceUrl = stringBuffer.toString();
			System.out.println("isValidProduct service url: " + serviceUrl);
			
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
			StringBuffer stringBuffer = new StringBuffer(baseUrl);
			if (useNonStandardPort) {
				stringBuffer.append(":").append(servicesPorts.get("participant.service.port"));
			}
			stringBuffer.append("/participant/broker/");
			stringBuffer.append(participantId);
			String serviceUrl = stringBuffer.toString();
			System.out.println("isValidParticipant service url: " + serviceUrl);

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
		logger.trace("Entering addOrderInBook: " + order);
		boolean addedToBook = false;
		try {
			StringBuffer stringBuffer = new StringBuffer(baseUrl);
			if (useNonStandardPort) {
				stringBuffer.append(":").append(servicesPorts.get("orderbook.service.port"));
			}
			stringBuffer.append("/orderBook/order/");
			String serviceUrl = stringBuffer.toString();
			System.out.println("addOrderInBook service url: " + serviceUrl);

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
			logger.error("Error during service call to orderbook service for order: " + order);
		}
		logger.trace("Exiting addOrderInBook: " + order);
		return addedToBook;
	}

	@Override
	public List<Long> getTradesForOrder(long orderId) {
		// TODO Auto-generated method stub
		return new ArrayList<Long>();
	}

}