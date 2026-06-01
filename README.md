<div align="center">

# 🎟️ Event Management System — REST API

[![CI Tests](https://img.shields.io/github/actions/workflow/status/Shubhamkale1/event-manager/ci.yml?label=CI%20Tests&style=for-the-badge&logo=github-actions&logoColor=white)](https://github.com/Shubhamkale1/event-manager/actions)
[![Docker Build](https://img.shields.io/github/actions/workflow/status/Shubhamkale1/event-manager/docker-build.yml?label=Docker%20Build&style=for-the-badge&logo=docker&logoColor=white)](https://github.com/Shubhamkale1/event-manager/actions)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.1-6DB33F?style=for-the-badge&logo=spring-boot)](https://spring.io/projects/spring-boot)
[![Elasticsearch](https://img.shields.io/badge/Elasticsearch-8.x-005571?style=for-the-badge&logo=elasticsearch&logoColor=white)](https://www.elastic.co/elasticsearch)
[![Redis](https://img.shields.io/badge/Redis-Cache-DC382D?style=for-the-badge&logo=redis&logoColor=white)](https://redis.io/)
[![Apache Kafka](https://img.shields.io/badge/Apache_Kafka-3.x-231F20?style=for-the-badge&logo=apache-kafka)](https://kafka.apache.org/)
[![Actuator](https://img.shields.io/badge/Spring_Actuator-Observed-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
[![Grafana](https://img.shields.io/badge/Grafana-Metrics-F46800?style=for-the-badge&logo=grafana&logoColor=white)](https://grafana.com/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker)](https://www.docker.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)](LICENSE)

**A production-grade, microservices-based Event Management REST API built progressively across 5 phases — from a simple monolith to a cloud-ready distributed system with Kafka messaging, Docker containerization, and real-time monitoring.**

*Inspired by real-world platforms like Eventbrite, BookMyShow, and Meetup.*

[📖 API Docs](https://github.com/Shubhamkale1/event-manager/wiki) •
[🚀 Quick Start](#-quick-start) •
[🏗️ System Architecture](#️-system-architecture) •
[📡 API Endpoints](#-api-endpoints-summary)

</div>

---

## 📖 What Is This Project?

This is not a simple CRUD application. It is a real backend system designed and built the way companies actually evolve software — starting with a working monolith, adding technologies only when they solve real problems, and ending with a cloud-ready microservices architecture.

Every technology was added at the right moment. Redis came when the database was being hit unnecessarily. Elasticsearch came when LIKE queries weren't good enough. Kafka came when synchronous email delivery was blocking API responses. Each phase solved a problem the previous phase created.

---

## 🎯 Highlights

- 🏗️ Production-Grade Event Management Platform
- 🔐 JWT Authentication + Google OAuth2 Login
- ⚡ Redis Distributed Caching for High Performance
- 🔍 Elasticsearch Full-Text Search with Fuzzy Matching
- 📨 Apache Kafka Event-Driven Messaging Architecture
- 🐳 Dockerized Multi-Service Deployment
- 🚀 CI/CD Pipeline with GitHub Actions
- 📊 Prometheus & Grafana Monitoring Stack
- 🧩 Microservices-Based Email Notification Service
- 🗄️ Flyway Database Version Control & Migrations
- 🔄 Rate Limiting with Bucket4j
- 🤖 AI-Powered Event Recommendations using Spring AI

## 🏗️ System Architecture

<p align="center">
  <img src="docs/architecture.png" alt="Event Management System Architecture" width="1000">
</p>

---

## ✨ Features

### Core Platform
- Full Event CRUD with lifecycle state machine (DRAFT → PUBLISHED → CANCELLED/COMPLETED)
- Venue management with automatic GPS geocoding via OpenStreetMap API
- Organization management (groups like "Pune Java User Group") with follower system
- Category tagging system with Elasticsearch-powered filtered search
- Event registration with **atomic SQL concurrency control** — zero overselling under any load

### Security
- JWT stateless authentication (HS256, 24-hour expiry)
- BCrypt password hashing with adaptive cost factor
- OAuth2 Google Social Login
- Role-based access control (USER / ADMIN)
- Per-IP rate limiting via Bucket4j token bucket algorithm

### Performance
- Redis caching with `@Cacheable` / `@CacheEvict` — eliminates redundant database hits
- Elasticsearch fuzzy search with `fuzziness: AUTO` — finds results despite typos
- HikariCP connection pooling for efficient database connections
- Async email via `@Async` — API never blocks on email delivery

### Engagement
- Real-time in-app notification system (EVENT_CANCELLED, REGISTRATION_CONFIRMED, NEW_FOLLOWER)
- Reviews and ratings with multi-layer validation (only attendees of completed events)
- Bookmarks / wishlist for events
- Waitlist with automatic promotion on cancellation

### Event-Driven Architecture
- **Apache Kafka** message bus between main app and Email microservice
- Event cancellations publish to `event.cancelled` topic — email service consumes asynchronously
- Registration confirmations publish to `event.registered` topic
- Zero coupling between services — each evolves independently

### DevOps & Observability
- **Docker** multi-stage builds — final image 220MB vs 720MB without multi-stage
- **Docker Compose** — one command starts all 8 services
- **GitHub Actions CI/CD** — tests run on every push, Docker image built on merge to main
- **Prometheus + Grafana** — real-time dashboard (request rate, P95 latency, JVM memory, DB connections)
- Custom `GET /api/system/health` endpoint checks MySQL, Redis, Elasticsearch

### Analytics & Export
- Organizer dashboard with registration rate, turnout metrics
- Platform-wide admin analytics
- CSV and Excel (`.xlsx`) attendee export via Apache POI

---

## 🗺️ Project Phases

| Phase | Focus | Key Technologies | Status |
|-------|-------|-----------------|--------|
| **Phase 1** | Core REST API | Spring Boot, JPA, H2, Lombok | ✅ Complete |
| **Phase 2** | Persistence & Validation | MySQL, Flyway, MapStruct, JUnit 5 | ✅ Complete |
| **Phase 3** | Performance & Search | Redis, Elasticsearch, Swagger, Email | ✅ Complete |
| **Phase 4** | Security & Features | JWT, OAuth2, Kafka, Rate Limiting, Spring AI | ✅ Complete |
| **Phase 5** | Cloud & Microservices | Docker, GitHub Actions, Kafka, Prometheus, Grafana | ✅ Complete |

---

## 🛠️ Tech Stack

| Layer | Technology | Version |
|-------|-----------|---------|
| Language | Java | 17 |
| Framework | Spring Boot | 3.4.1 |
| ORM | Spring Data JPA / Hibernate | 6.6.4 |
| Database | MySQL | 8.0 |
| Cache | Redis | 7.x |
| Search | Elasticsearch | 8.x |
| Messaging | Apache Kafka | 3.x |
| Migrations | Flyway | 10.x |
| Security | Spring Security + JWT | jjwt 0.12.3 |
| OAuth2 | Google Login | Spring Security OAuth2 |
| Rate Limiting | Bucket4j | 8.7.0 |
| Mapping | MapStruct | 1.5.5 |
| API Docs | Springdoc OpenAPI | 2.7.0 |
| AI | Spring AI (Groq LLaMA 3) | 1.0.0-M6 |
| Export | Apache POI | 5.2.5 |
| Monitoring | Prometheus + Grafana | 2.48 / 10.2 |
| Containerization | Docker + Compose | 24.x / 2.x |
| CI/CD | GitHub Actions | — |
| Build Tool | Maven | 3.x |

---

## 🗄️ Database Schema

**14 Flyway Migrations (V1–V14)**

| Migration | Description |
|-----------|-------------|
| V1 | Create events table |
| V2 | Add created_at to events |
| V3 | Create users table |
| V4 | Seed admin user (BCrypt password) |
| V5 | Create venues table with geocoding fields |
| V6 | Add venue_id FK to events |
| V7 | Add bio, phone, city, updated_at to users |
| V8 | Create organizations + organization_followers |
| V9 | Create registrations table + registrations_count |
| V10 | Add event status + lifecycle timestamps |
| V11 | Create notifications table with indexes |
| V12 | Create reviews table with rating constraint |
| V13 | Create bookmarks junction table |
| V14 | Create waitlist table with position ordering |

---

## ⚙️ Prerequisites

| Service | Port | Verify |
|---------|------|--------|
| Docker Desktop | — | `docker --version` |
| Java 17+ | — | `java -version` |
| Maven | — | `mvn --version` |

> All other services (MySQL, Redis, Elasticsearch, Kafka, Prometheus, Grafana) are started automatically by Docker Compose.

---

## 🚀 Quick Start

### Option 1 — Docker Compose (Recommended)

```bash
# 1. Clone the repository
git clone https://github.com/Shubhamkale1/event-manager.git
cd event-manager

# 2. Create environment file
cp .env.example .env
# Edit .env with your values

# 3. Start everything
docker-compose up --build

# 4. Verify all services are running
docker ps
```

**Services available after startup:**

| Service | URL | Credentials |
|---------|-----|-------------|
| API | http://localhost:8081 | — |
| Swagger UI | http://localhost:8081/swagger-ui/index.html | — |
| System Health | http://localhost:8081/api/system/health | — |
| Kafka UI | http://localhost:8090 | — |
| Prometheus | http://localhost:9090 | — |
| Grafana | http://localhost:3000 | admin / admin123 |
| MySQL | localhost:3307 | root / (from .env) |
| Redis | localhost:6380 | — |
| Elasticsearch | localhost:9201 | — |

### Option 2 — Local Development

```bash
# Start infrastructure (MySQL, Redis, Elasticsearch)
# Then run the application with dev profile:
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Environment Variables (`.env`)

```properties
DB_PASSWORD=your_mysql_password
JWT_SECRET=your-jwt-secret-minimum-32-characters
MAIL_USERNAME=your_mailtrap_username
MAIL_PASSWORD=your_mailtrap_password
GEOCODING_API_KEY=your_geocode_api_key
AI_API_KEY=your_groq_api_key
```

## 🔐 Authentication

All protected endpoints require a JWT Bearer token.

**Step 1 — Register**
```
POST /api/auth/register
```

**Step 2 — Copy token from response and use in every request**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**Default admin credentials** (seeded by Flyway V4)
```
Email:    admin@events.com
Password: admin123
```

## 📡 API Endpoints (Summary)

| Category | Count | Auth Required |
|----------|-------|---------------|
| Authentication | 4 | No |
| Events (CRUD + Search + Lifecycle) | 12 | Partial |
| Venues | 7 | Partial |
| Organizations | 9 | Partial |
| User Profile | 4 | Yes |
| Categories | 6 | Partial |
| Registration | 6 | Yes |
| Waitlist | 4 | Yes |
| Reviews & Ratings | 6 | Partial |
| Bookmarks | 4 | Yes |
| Notifications | 6 | Yes |
| Dashboard & Analytics | 3 | Yes |
| Export | 1 | Yes |
| Admin | 3 | ADMIN role |
| AI Recommendations | 1 | Yes |
| System Health | 2 | No |

> **Total: 88 REST endpoints**
> Full documentation with request/response examples: [Wiki →](https://github.com/Shubhamkale1/event-manager/wiki)

---

## 🔄 CI/CD Pipeline

```
Developer pushes code
        ↓
GitHub Actions triggers (ci.yml)
        ↓
Ubuntu VM spins up
MySQL + Redis start as service containers
        ↓
Java 17 configured
Maven dependency cache restored
        ↓
mvn clean test (full test suite)
        ↓
Tests pass? → merge to main
        ↓
docker-build.yml triggers
        ↓
Tests run again (verify main branch)
        ↓
Multi-stage Docker image built
Tagged: latest, sha-{commit}, main
        ↓
Image pushed to Docker Hub
```

---

## 📊 Monitoring

**Prometheus** scrapes `/actuator/prometheus` every 15 seconds.

**Grafana Dashboard** (auto-provisioned) shows:
- HTTP Request Rate (requests/sec)
- HTTP Error Rate (5xx responses/sec)
- P95 Response Time (95th percentile latency)
- JVM Heap Memory Usage
- HikariCP Active Database Connections
- Request distribution by endpoint and status

---

## 🎯 Key Technical Decisions

| Decision | Choice | Why |
|----------|--------|-----|
| Cache | Redis | Eliminates database hits on unchanged data |
| Search | Elasticsearch | Fuzzy matching impossible with SQL LIKE |
| Migrations | Flyway | Versioned, trackable schema changes — never ddl-auto=update |
| Messaging | Kafka | Decouples email delivery from API response time |
| Auth | Stateless JWT | Scales horizontally — no server-side sessions |
| Concurrency | Atomic SQL UPDATE | Thread-safe capacity checks without application locks |
| Mapping | MapStruct | Compile-time generation — zero runtime overhead |
| Images | Multi-stage Docker | 220MB final image vs 720MB with full JDK |

---

## 🧪 Running Tests

The test suite runs against real MySQL and Redis instances using GitHub Actions service containers. Elasticsearch indexing is bypassed during test phases to maximize build execution speed.

```bash
# Run the complete test suite
mvn clean test

# Run a single targeted test class
mvn test -Dtest=EventServiceTest

# Run tests and generate an absolute code coverage report
mvn test jacoco:report
```

---

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines to maintain code quality and consistency.

### 1. Fork & Clone

```bash
git clone https://github.com/YOUR_USERNAME/event-manager.git
cd event-manager
```

### 2. Create a Feature Branch

```bash
git checkout -b feature/your-feature-name
```

### 3. Make Changes & Commit

- Follow the existing project structure.
- Write clean, maintainable code.
- Add unit tests for any new business logic.
- Use descriptive commit messages.

```bash
git add .
git commit -m "Add event waitlist notification feature"
```

### 4. Push Changes

```bash
git push origin feature/your-feature-name
```

### 5. Open a Pull Request

Provide a clear description of:
- What was changed
- Why it was changed
- Any architectural decisions involved

### Coding Standards

- Follow the Google Java Style Guide.
- Keep commits atomic and meaningful.
- Ensure all tests pass before submitting.
- Update documentation when introducing new features.

---

## 📄 License

MIT License — Copyright (c) 2026 Shubham Kale

---

<div align="center">

⭐ Star this repository if you found it helpful

**Shubham Kale** • Pune, India

</div>
