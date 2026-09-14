package com.nexus.order_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderServiceApplicationTests {
	@org.springframework.beans.factory.annotation.Autowired
	private org.springframework.context.ApplicationContext context;

	@Test
	void contextLoads() {
		org.junit.jupiter.api.Assertions.assertNotNull(context.getBean(controller.OrderController.class));
		org.junit.jupiter.api.Assertions.assertNotNull(context.getBean(impl.OrderServiceImpl.class));
		org.junit.jupiter.api.Assertions.assertNotNull(context.getBean(repository.OrderRepository.class));
		org.junit.jupiter.api.Assertions.assertNotNull(context.getBean(client.InventoryFeignClient.class));
		org.junit.jupiter.api.Assertions.assertNotNull(context.getBean(kafka.OrderEventProducer.class));
	}

}
