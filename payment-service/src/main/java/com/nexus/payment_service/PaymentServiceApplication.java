package com.nexus.payment_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.nexus.payment_service", "controller", "impl", "config", "exception"})
@org.springframework.boot.autoconfigure.domain.EntityScan("entity")
@org.springframework.data.jpa.repository.config.EnableJpaRepositories("repository")
@org.springframework.cloud.openfeign.EnableFeignClients(basePackages = "client")
public class PaymentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentServiceApplication.class, args);
	}

}
