package org.hemant.thakkar.financialexchange.orders.monitor;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ExecPosMessageSerializer implements Serializer<ExecPosMessage> {

	public void configure(Map<String, ?> configs, boolean isKey) {
		// Nothing to do
	}

	public byte[] serialize(String topic, ExecPosMessage data) {
		byte[] bytes = null;
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		//objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		try {
			bytes = objectMapper.writeValueAsBytes(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bytes;
	}

	public void close() {
		// Nothing to do
	}

}
