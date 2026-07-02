# Fail Safe 🛡️
> **A High-Performance, Resilient Distributed Ingestion & AI Inference Pipeline.**

Fail Safe is a distributed backend system designed to handle massive data streams with absolute reliability. It focuses on the core pillars of modern backend engineering: **Exactly-once delivery**, **Fault tolerance**, and **AI-driven anomaly detection**.

---

## 🏗️ System Architecture

### 1. Ingestion & Messaging (Kafka)
*   **Role**: Acts as the system's "central nervous system."
*   **Communication**: Asynchronous, decoupled messaging.
*   **Feature**: Implements **Exactly-Once Semantics (EOS)** to ensure no data is lost or processed twice, even during network partitions.

### 2. Processing Layer (Spring Boot)
*   **Role**: Orchestrates data flow and business logic.
*   **Communication**: Multi-threaded consumption from Kafka; REST/gRPC to AI services.
*   **Feature**: **Resilience4j** integration for Circuit Breakers to prevent cascading failures.

### 3. AI Inference Engine (FastAPI & PyTorch)
*   **Role**: Real-time intelligence.
*   **Models**: 
    *   *Isolation Forest*: For real-time anomaly detection.
    *   *LSTM (PyTorch)*: For time-series spike prediction.
*   **Performance**: Optimized for **P99 < 180ms**.

### 4. Persistence & Caching (PostgreSQL & Redis)
*   **PostgreSQL**: ACID-compliant storage using **Time-based Table Partitioning** for high-speed historical queries.
*   **Redis**: Serves as a high-speed cache (reducing DB load by 70%) and a **Pub/Sub** broker for live dashboard updates via WebSockets.

---

## 🛠️ Tech Stack

| Layer | Technology |
| :--- | :--- |
| **Language** | Java (Spring Boot), Python (FastAPI) |
| **Message Broker** | Apache Kafka |
| **Database** | PostgreSQL 15+ |
| **In-Memory** | Redis |
| **ML Framework** | PyTorch, Scikit-learn |
| **Resilience** | Resilience4j |
| **DevOps** | Docker, Docker-Compose |

---

## 📅 The 45-Day Roadmap

- [ ] **Phase 1: Distributed Messaging** (Days 1-10) - Kafka, Idempotency, Transactions.
- [ ] **Phase 2: Resilient Processing** (Days 11-20) - Multi-threading, Circuit Breakers.
- [ ] **Phase 3: AI & Inference** (Days 21-30) - FastAPI, Isolation Forest, LSTM.
- [ ] **Phase 4: Persistence & Speed** (Days 31-40) - DB Partitioning, Redis Caching.
- [ ] **Phase 5: Hardening** (Days 41-45) - P99 Tuning, Chaos Engineering.

---

## 🚀 Getting Started

### Prerequisites
* Java 17+
* Docker & Docker Compose
* Maven 3.8+

### Quick Start
1. **Clone the repo**
   ```bash
   git clone https://github.com/YOUR_USERNAME/fail-safe.git
