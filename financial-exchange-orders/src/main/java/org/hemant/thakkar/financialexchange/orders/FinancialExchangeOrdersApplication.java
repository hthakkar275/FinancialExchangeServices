package org.hemant.thakkar.financialexchange.orders;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FinancialExchangeOrdersApplication {

	private static final Log logger = LogFactory.getLog(FinancialExchangeOrdersApplication.class);

	public static void main(String[] args) {
		String databaseUrl = System.getProperty("database.url", "jdbc:postgresql://127.0.0.1/finex-database");
		String databaseUsernmae = System.getProperty("database.username", "postgres");
		String databasePassword = System.getProperty("database.password", "password1");
		
		System.setProperty("spring.datasource.url", databaseUrl);
		System.setProperty("spring.datasource.username", databaseUsernmae);
		System.setProperty("spring.datasource.password", databasePassword);
		System.setProperty("spring.jpa.generate", "true");
		System.setProperty("spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation", "true");
		System.setProperty("spring.jpa.database-platform", "org.hibernate.dialect.PostgreSQL9Dialect");

		SpringApplication.run(FinancialExchangeOrdersApplication.class, args);
		logger.debug("Orders service started");
	}

}
