package org.hemant.thakkar.financialexchange.trades.controller;

import java.util.List;

import org.hemant.thakkar.financialexchange.trades.domain.APIDataResponse;
import org.hemant.thakkar.financialexchange.trades.domain.APIResponse;
import org.hemant.thakkar.financialexchange.trades.domain.ExchangeException;
import org.hemant.thakkar.financialexchange.trades.domain.ResultCode;
import org.hemant.thakkar.financialexchange.trades.domain.TradeEntry;
import org.hemant.thakkar.financialexchange.trades.domain.TradeReport;
import org.hemant.thakkar.financialexchange.trades.service.TradeManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TradeManagementController {

	@Autowired
	@Qualifier("tradeManagementServiceImpl")
	private TradeManagementService tradeManagmentService;
	
	@PostMapping(value = "/trade", produces = "application/json", consumes = "application/json")
	public APIDataResponse<Long> addEquity(@RequestBody TradeEntry tradeEntry)  {
		APIDataResponse<Long> response = new APIDataResponse<>();
		try {
			long tradeId = tradeManagmentService.acceptTrade(tradeEntry);
			response.setSuccess(true);
			response.setInfoMessage(ResultCode.TRADE_ACCEPTED.getMessage());
			response.setData(tradeId);
			response.setResponseCode(ResultCode.TRADE_ACCEPTED.getCode());
		} catch (ExchangeException ee) {
			response.setErrorMessage(ee.getMessage());
			response.setResponseCode(ee.getErrorCode());
		} catch (Throwable t) {
			response.setErrorMessage("Unexpected error. Please contact customer service");
			response.setResponseCode(ResultCode.GENERAL_ERROR.getCode());
		}
		return response;
	}
	 
	@GetMapping(value = "/trade/{tradeId}", produces = "application/json", consumes = "application/json")
	public APIDataResponse<TradeReport> getTrade(@PathVariable("tradeId") long tradeId)  {
		APIDataResponse<TradeReport> response = new APIDataResponse<>();
		try {
			TradeReport tradeReport = tradeManagmentService.getTrade(tradeId);
			response.setSuccess(true);
			response.setInfoMessage(ResultCode.TRADE_FOUND.getMessage());
			response.setData(tradeReport);
			response.setResponseCode(ResultCode.TRADE_FOUND.getCode());
		} catch (ExchangeException ee) {
			response.setErrorMessage(ee.getMessage());
			response.setResponseCode(ee.getErrorCode());
		} catch (Throwable t) {
			response.setErrorMessage("Unexpected error. Please contact customer service");
			response.setResponseCode(ResultCode.GENERAL_ERROR.getCode());
		}
		return response;
	}

	@GetMapping(value = "/tradesForOrder/{orderId}", produces = "application/json", consumes = "application/json")
	public APIDataResponse<List<TradeReport>> getTradesForOrder(@PathVariable("orderId") long orderId)  {
		APIDataResponse<List<TradeReport>> response = new APIDataResponse<>();
		try {
			List<TradeReport> trades = tradeManagmentService.getTradesForOrder(orderId);
			response.setSuccess(true);
			response.setInfoMessage(ResultCode.TRADE_FOUND.getMessage());
			response.setData(trades);
			response.setResponseCode(ResultCode.TRADE_FOUND.getCode());
		} catch (ExchangeException ee) {
			response.setErrorMessage(ee.getMessage());
			response.setResponseCode(ee.getErrorCode());
		} catch (Throwable t) {
			response.setErrorMessage("Unexpected error. Please contact customer service");
			response.setResponseCode(ResultCode.GENERAL_ERROR.getCode());
		}
		return response;
	}

	@DeleteMapping(value = "/trade/{tradeId}", produces = "application/json", consumes = "application/json")
	public APIResponse bustTrade(@PathVariable("tradeId") long tradeId)  {
		APIResponse response = new APIResponse();
		try {
			tradeManagmentService.bustTrade(tradeId);
			response.setSuccess(true);
			response.setInfoMessage(ResultCode.TRADE_BUSTED.getMessage());
			response.setResponseCode(ResultCode.TRADE_BUSTED.getCode());
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
