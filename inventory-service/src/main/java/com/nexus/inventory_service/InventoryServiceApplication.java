package com.nexus.inventory_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"com.nexus.inventory_service", "controller", "services", "mapper", "consumer"})
@org.springframework.boot.autoconfigure.domain.EntityScan("entity")
@org.springframework.data.jpa.repository.config.EnableJpaRepositories("repository")
@EnableDiscoveryClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

}
