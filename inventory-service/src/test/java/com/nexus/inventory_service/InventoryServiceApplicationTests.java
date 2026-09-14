package com.nexus.inventory_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InventoryServiceApplicationTests {
	@org.springframework.beans.factory.annotation.Autowired
	private org.springframework.context.ApplicationContext context;

	@Test
	void contextLoads() {
		org.junit.jupiter.api.Assertions.assertNotNull(context.getBean(controller.InventoryController.class));
		org.junit.jupiter.api.Assertions.assertNotNull(context.getBean(services.InventoryService.class));
		org.junit.jupiter.api.Assertions.assertNotNull(context.getBean(repository.InventoryRepository.class));
		org.junit.jupiter.api.Assertions.assertNotNull(context.getBean(repository.ProcessedOrderRepository.class));
		org.junit.jupiter.api.Assertions.assertNotNull(context.getBean(consumer.OrderEventConsumer.class));
	}

}
