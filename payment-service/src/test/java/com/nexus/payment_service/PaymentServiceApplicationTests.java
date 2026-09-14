package com.nexus.payment_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PaymentServiceApplicationTests {
	@org.springframework.beans.factory.annotation.Autowired
	private org.springframework.context.ApplicationContext context;

	@Test
	void contextLoads() {
		org.junit.jupiter.api.Assertions.assertNotNull(context.getBean(controller.PaymentController.class));
		org.junit.jupiter.api.Assertions.assertNotNull(context.getBean(impl.RazorpayWebhookImpl.class));
		org.junit.jupiter.api.Assertions.assertNotNull(context.getBean(repository.PaymentRepository.class));
		org.junit.jupiter.api.Assertions.assertNotNull(context.getBean(client.OrderClient.class));
		org.junit.jupiter.api.Assertions.assertNotNull(context.getBean(client.InventoryClient.class));
	}

}
