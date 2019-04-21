package org.hemant.thakkar.financialexchange.participants.domain;

public class Broker extends BaseParticipantImpl {


	public String toString() {
		StringBuffer message = new StringBuffer();
		message.append("id = ").append(getId()).append(";");
		message.append("name = ").append(getName());
		return message.toString();
	}
	
}
