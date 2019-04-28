package org.hemant.thakkar.financialexchange.orderbooks.service;

import java.util.HashMap;
import java.util.Map;

import org.hemant.thakkar.financialexchange.orderbooks.domain.Trade;
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
	public boolean saveTrade(Trade trade) {
		System.out.println(trade);
		return true;
	}

	@Override
	public String getProductSymbol(long productId) {
		String symbol = null;
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
			JsonNode product = root.get("data");
			symbol = product.get("symbol").asText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return symbol;
	}

}
