package org.hemant.thakkar.financialexchange.orderbooks.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hemant.thakkar.financialexchange.orderbooks.domain.APIDataResponse;
import org.hemant.thakkar.financialexchange.orderbooks.domain.APIResponse;
import org.hemant.thakkar.financialexchange.orderbooks.domain.OrderBookEntry;
import org.hemant.thakkar.financialexchange.orderbooks.domain.OrderBookState;
import org.hemant.thakkar.financialexchange.orderbooks.domain.ResultCode;
import org.hemant.thakkar.financialexchange.orderbooks.service.OrderBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderBookManagementController {

	private static final Log logger = LogFactory.getLog(OrderBookManagementController.class);

	@Autowired
	@Qualifier("orderBookServiceImpl")
	private OrderBookService orderBookService;
	
	@PostMapping(value = "/orderBook/order", produces = "application/json", consumes = "application/json")
	public APIResponse acceptNewOrder(@RequestBody OrderBookEntry orderBookEntry) {
		logger.trace("Entering acceptNewOrder: " + orderBookEntry);
		APIResponse response = new APIResponse();
		try {
			orderBookService.addOrder(orderBookEntry);
			response.setSuccess(true);
			response.setInfoMessage(ResultCode.ORDER_ACCEPTED.getMessage());
			response.setResponseCode(ResultCode.ORDER_ACCEPTED.getCode());
		} catch (Throwable t) {
			logger.error("Unexpected error: " + orderBookEntry, t);
			response.setErrorMessage("Unexpected error. Please contact customer service");
			response.setResponseCode(ResultCode.GENERAL_ERROR.getCode());
		}
		return response;

	} 
	
	@GetMapping(value = "/orderBook/{productId}", produces = "application/json", consumes = "application/json")
	public APIDataResponse<OrderBookState> getOrderBook(@PathVariable("productId") long productId) {
		APIDataResponse<OrderBookState> response = new APIDataResponse<>();
		try {
			OrderBookState orderReport = orderBookService.getOrderBookMontage(productId);
			response.setSuccess(true);
			response.setInfoMessage(ResultCode.ORDER_FOUND.getMessage());
			response.setData(orderReport);
			response.setResponseCode(ResultCode.ORDER_FOUND.getCode());
		} catch (Throwable t) {
			response.setErrorMessage("Unexpected error. Please contact customer service");
			response.setResponseCode(ResultCode.GENERAL_ERROR.getCode());
		}
		return response;

	} 

	@DeleteMapping(value = "/orderBook/{orderId}", produces = "application/json", consumes = "application/json")
	public APIResponse deleteOrder(@PathVariable("orderId") long orderId, @RequestParam long productId) {
		APIResponse response = new APIResponse();
		try {
			orderBookService.cancelOrder(productId, orderId);
			response.setSuccess(true);
			response.setInfoMessage(ResultCode.ORDER_CANCELLED.getMessage());
			response.setResponseCode(ResultCode.ORDER_CANCELLED.getCode());
		} catch (Throwable t) {
			response.setErrorMessage("Unexpected error. Please contact customer service");
			response.setResponseCode(ResultCode.GENERAL_ERROR.getCode());
		}
		return response;
	} 

}
