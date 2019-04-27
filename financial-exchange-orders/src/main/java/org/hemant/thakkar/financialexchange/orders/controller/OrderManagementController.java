package org.hemant.thakkar.financialexchange.orders.controller;

import org.hemant.thakkar.financialexchange.orders.domain.APIDataResponse;
import org.hemant.thakkar.financialexchange.orders.domain.APIResponse;
import org.hemant.thakkar.financialexchange.orders.domain.ExchangeException;
import org.hemant.thakkar.financialexchange.orders.domain.OrderEntry;
import org.hemant.thakkar.financialexchange.orders.domain.OrderReport;
import org.hemant.thakkar.financialexchange.orders.domain.ResultCode;
import org.hemant.thakkar.financialexchange.orders.service.OrderManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderManagementController {

	@Autowired
	@Qualifier("orderManagementServiceImpl")
	private OrderManagementService orderManagementService;
	
	@PostMapping(value = "/order", produces = "application/json", consumes = "application/json")
	public APIDataResponse<Long> acceptNewOrder(@RequestBody OrderEntry orderEntry) {
		APIDataResponse<Long> response = new APIDataResponse<Long>();
		try {
			long orderId = orderManagementService.acceptNewOrder(orderEntry);
			response.setSuccess(true);
			response.setInfoMessage(ResultCode.ORDER_ACCEPTED.getMessage());
			response.setResponseCode(ResultCode.ORDER_ACCEPTED.getCode());
			response.setData(orderId);
		} catch (ExchangeException ee) {
			response.setErrorMessage(ee.getMessage());
			response.setResponseCode(ee.getErrorCode());
		} catch (Throwable t) {
			response.setErrorMessage("Unexpected error. Please contact customer service");
			response.setResponseCode(ResultCode.GENERAL_ERROR.getCode());
		}
		return response;
	} 
	
	@PutMapping(value = "/order/{orderId}", produces = "application/json", consumes = "application/json")
	public APIResponse updateOrderFromOrderBook(@PathVariable("orderId") long orderId,
			@RequestBody OrderEntry orderEntry) {
		APIResponse response = new APIResponse();
		try {
			orderManagementService.updateFromOrderBook(orderId, orderEntry);
			response.setSuccess(true);
			response.setInfoMessage(ResultCode.ORDER_UPDATED.getMessage());
			response.setResponseCode(ResultCode.ORDER_UPDATED.getCode());
		} catch (ExchangeException ee) {
			response.setErrorMessage(ee.getMessage());
			response.setResponseCode(ee.getErrorCode());
		} catch (Throwable t) {
			response.setErrorMessage("Unexpected error. Please contact customer service");
			response.setResponseCode(ResultCode.GENERAL_ERROR.getCode());
		}
		return response;

	} 

	@GetMapping(value = "/order/{orderId}", produces = "application/json", consumes = "application/json")
	public APIDataResponse<OrderReport> getOrderStatus(@PathVariable("orderId") long orderId) {
		APIDataResponse<OrderReport> response = new APIDataResponse<>();
		try {
			OrderReport orderReport = orderManagementService.getOrderStatus(orderId);
			response.setSuccess(true);
			response.setInfoMessage(ResultCode.ORDER_FOUND.getMessage());
			response.setData(orderReport);
			response.setResponseCode(ResultCode.ORDER_FOUND.getCode());
		} catch (ExchangeException ee) {
			response.setErrorMessage(ee.getMessage());
			response.setResponseCode(ee.getErrorCode());
		} catch (Throwable t) {
			response.setErrorMessage("Unexpected error. Please contact customer service");
			response.setResponseCode(ResultCode.GENERAL_ERROR.getCode());
		}
		return response;

	} 

	@DeleteMapping(value = "/order/{orderId}", produces = "application/json", consumes = "application/json")
	public APIResponse deleteOrder(@PathVariable("orderId") long orderId) {
		APIResponse response = new APIResponse();
		try {
			orderManagementService.cancelOrder(orderId);
			response.setSuccess(true);
			response.setInfoMessage(ResultCode.ORDER_CANCELLED.getMessage());
			response.setResponseCode(ResultCode.ORDER_CANCELLED.getCode());
		} catch (ExchangeException ee) {
			response.setErrorMessage(ee.getMessage());
			response.setResponseCode(ee.getErrorCode());
		} catch (Throwable t) {
			response.setErrorMessage("Unexpected error. Please contact customer service");
			response.setResponseCode(ResultCode.GENERAL_ERROR.getCode());
		}
		return response;
	} 
}