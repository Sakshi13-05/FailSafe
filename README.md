# FailSafe 🛡️ 
> **A Resilient, Event-Driven Ingestion Pipeline for High-Availability Systems.**

FailSafe is a production-grade backend system designed to solve the "Synchronous Bottleneck." In traditional architectures, if the database is slow or down, the API crashes. FailSafe utilizes **Temporal Decoupling** via Apache Kafka to ensure that ingestion remains 100% available, even during total storage failure.

---

## 🏗️ System Architecture (Version 1.0)

The system follows a layered, decoupled architecture to ensure that failures in the persistence layer do not propagate to the ingestion layer.

**Client** → **REST Controller** → **Service Layer** → **Kafka Producer** → **Kafka Topic (Aiven)** → **Kafka Consumer** → **Spring Data JPA** → **MySQL (AWS RDS)**

### Key Architectural Pillars:
*   **Ingestion Layer (Spring Boot)**: A non-blocking entry point that validates requests and hands them off to the message broker.
*   **Durable Buffering (Apache Kafka)**: Acts as a "Time Buffer," allowing the system to accept data at high speeds and store it safely on disk during downstream outages.
*   **Reliable Persistence (MySQL)**: Ensures ACID-compliant storage with built-in **Idempotency** to handle network retries.

---

## 🧪 The "Chaos Engineering" Demo 
*The core differentiator of FailSafe is its ability to survive infrastructure failure.*

### The Scenario: Network Partition
1.  **Breaking the Connection**: I intentionally deleted the **AWS Security Group** Inbound Rule (Port 3306) to isolate the database.
2.  **The Result**: The API remained active, returning `201 Created` / `202 Accepted`. Requests were durably stored in Kafka (Aiven) as seen via increasing **Offsets**.
3.  **Recovery & Eventual Consistency**: Upon restoring the Security Group rule, the Kafka Consumer automatically resumed from the last offset and reconciled the state, persisting all buffered data to MySQL without loss.

---

## 🛠️ Tech Stack & Engineering Decisions

| Technology | Role | The "Why" |
| :--- | :--- | :--- |
| **Java/Spring Boot** | Processing | Used for its robust ecosystem and managed connection pooling (HikariCP). |
| **Apache Kafka** | Messaging | Provides **Temporal Decoupling**. The API speed is not limited by DB disk I/O. |
| **Spring Data JPA** | Persistence | Abstracted data access with support for transaction management. |
| **MySQL (AWS RDS)** | Storage | Relational storage for strict data integrity and schema enforcement. |
| **UUIDs** | Tracing | Generated in the Mapper to ensure **Idempotency** and end-to-end traceablity. |

### Engineering "FailSafe" Settings:
*   **Startup Resilience**: Configured `spring.datasource.hikari.initialization-fail-timeout=0` to prevent the Application Context from crashing if the DB is offline at startup.
*   **Distributed Tracing**: Implementation of a unique `eventId` (UUID) to link logs across the Producer, Kafka, and Consumer boundaries.

---

## 🚀 Version 2.0 Roadmap

Currently refactoring the monolith into a **Microservices Architecture**:
- [ ] **Service Splitting**: Separate Ingestion (Producer) and Persistence (Consumer) runtimes.
- [ ] **Circuit Breakers**: Integrating **Resilience4j** to handle cascading failures.
- [ ] **AI Inference Pipeline**: Adding a FastAPI-based anomaly detection service.
- [ ] **Redis Caching**: Implementing a write-through cache to reduce DB read pressure.
- [ ] **Containerization**: Dockerizing services for Kubernetes deployment.

---

## 🛠️ Setup & Installation

### Prerequisites
* Java 17+
* Maven 3.8+
* Active Kafka Broker (Aiven) & MySQL Instance (AWS RDS)

### Configuration
Update `src/main/resources/application.yml` with your cloud credentials:
```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USER}
    password: ${DB_PASSWORD}
  kafka:
    bootstrap-servers: ${KAFKA_URL}
```

### Run
```bash
mvn clean install
mvn spring-boot:run
```

### Learning Philosophy
```bash
"Understand deeply rather than complete quickly."
This project was built to understand the internal workings of distributed systems, specifically how to manage state across asynchronous boundaries and how to build for the "failure-first" reality of production cloud environments.
```

**LinkedIn:** [linkedin.com/in/sakshichavan-dev](https://www.linkedin.com/in/sakshichavan-dev)
