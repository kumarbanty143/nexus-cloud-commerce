# Nexus Cloud Commerce

A tutorial-guided Java / Spring Boot e-commerce microservices learning project.
This repository is being improved with regression tests and clearer service contracts.
It is **not a production-ready checkout system**, and the payment flow is not yet complete end to end.

## Repository map

| Module | Current scope |
| --- | --- |
| `user-service` | Registration, login, password hashing and JWT authentication |
| `api-gateway` | Routing and JWT/role filtering; authorization coverage still needs review |
| `product-service` | Product and category APIs |
| `order-service` | Inventory lookup, order persistence and Kafka order events |
| `inventory-service` | Stock APIs and order-event consumption |
| `payment-service` | Payment records, Razorpay adapter and webhook handling; integration incomplete |
| `notification-service` | Notification-service code; not covered by the core correctness tests |
| `eureka-server` | Service discovery |
| `cart-service`, `shipping-service` | Application scaffolds, not complete business features |
| `kafka-infra` | Local-development Kafka, MySQL and Zipkin Compose configuration |

## Stack

Java 21, Spring Boot, Spring Cloud / Eureka / OpenFeign, Spring Data JPA,
MySQL, Kafka, Spring Security, JWT, Razorpay, and Maven wrappers.
Modules currently use different Spring Boot versions; dependency alignment remains future work.

## Run the core correctness tests

Install JDK 21. Each module has its own Maven wrapper; there is no root Maven aggregator.
From the repository root:

```sh
(cd order-service && sh ./mvnw verify)
(cd inventory-service && sh ./mvnw verify)
(cd payment-service && sh ./mvnw verify)
```

These tests use an in-memory H2 database, disable service discovery, configuration-server access,
Kafka listener startup and tracing, and mock remote business calls. They do not require MySQL,
Kafka or real Razorpay credentials. These commands can also be used in CI.

Coverage includes:

- Spring registration of controllers, services, repositories and Feign clients.
- Correct order-event quantity and identity, insufficient stock and invalid quantities.
- Processing new inventory events, skipping completed orders and rejecting invalid quantities.
- Signed `payment.captured` and `payment.failed` webhook dispatch, payment persistence,
  sequential duplicate handling, stale failure protection and invalid-signature rejection.

## Running services locally

Review each module's `src/main/resources/application.yml` and `application.properties` first.
The Compose file under `kafka-infra` uses local ports including 3306, 9092 and 9411, and development-only
database credentials. Do not start it against an existing work environment or expose it publicly.
Use dedicated local infrastructure and supply your own test credentials through environment variables.
Once the required infrastructure is configured, a module can be started with `sh ./mvnw spring-boot:run`.

## Known limitations / next milestones

1. **Payment orchestration:** payment creation currently generates a local transaction ID rather than
   persisting a Razorpay order ID. Wire the gateway into payment creation and validate the amount against
   server-side order data before relying on webhook correlation.
2. **Missing callbacks:** the payment clients call order confirm/fail and inventory commit/rollback
   endpoints that are not implemented. The order client also uses `/api/order`, while the current order
   controller uses `/api/orders`. Define and implement a reservation/payment state machine together.
3. **Distributed consistency:** order persistence and Kafka publication are not atomic. Add a transactional
   outbox, retry handling and dead-letter processing. An accepted order is not a guaranteed stock reservation.
4. **Concurrency:** inventory read-modify-write and webhook duplicate checks need locking/atomic transitions.
   Current duplicate tests cover sequential delivery, not concurrent or crash-recovery behavior.
5. **Authorization and validation:** review gateway role naming and route coverage, internal-service access,
   payment-status updates, and ownership checks before deploying beyond local development.
6. **End-to-end verification:** add MySQL/Kafka integration tests and a Razorpay test-mode checkout demo.
   Passing H2/unit tests does not establish payment-provider or full-system correctness.

## Learning provenance

This project began with tutorial-guided implementation. The original tutorial link still needs to be
recorded; no original tutorial author or production usage is claimed here. Improvements and verification
are recorded in the repository's commit history.
