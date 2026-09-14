package com.nexus.order_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.nexus.order_service", "controller", "impl", "mapper", "kafka", "config"})
@org.springframework.boot.autoconfigure.domain.EntityScan("entity")
@org.springframework.data.jpa.repository.config.EnableJpaRepositories("repository")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "client")
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

}
