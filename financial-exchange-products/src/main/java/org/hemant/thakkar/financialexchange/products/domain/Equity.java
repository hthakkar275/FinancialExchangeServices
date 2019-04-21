package org.hemant.thakkar.financialexchange.products.domain;

public class Equity extends BaseProductImpl {


	public String toString() {
		StringBuffer message = new StringBuffer();
		message.append(" id = ").append(this.getId()).append(";");
		message.append(" symbol = ").append(getSymbol());
		return message.toString();
	}
}
