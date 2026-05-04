# 🎉 Event Management System

A **production-ready RESTful Event Management API** built with Spring Boot 3.x, featuring event and venue management, real-time search, caching, security, and AI-powered recommendations.

> **Progressive Learning Journey**: Building from monolith → microservices architecture across 5 phases, learning the entire Spring ecosystem.

---

## 📋 Table of Contents

- [✨ Features](#-features)
- [🏗️ Architecture & Tech Stack](#️-architecture--tech-stack)
- [📊 Project Phases](#-project-phases)
- [🚀 Getting Started](#-getting-started)
- [🔐 Authentication & Security](#-authentication--security)
- [⚙️ Configuration](#️-configuration)
- [📚 API Documentation](#-api-documentation)
- [🛠️ Development](#️-development)
- [📝 Contributing](#-contributing)
- [📄 License](#-license)

---

### **Wiki Documentation**
[GitHub Wiki](https://github.com/Shubhamkale1/Event-Manager/wiki)

---

## ✨ Features

### 🎫 Event Management
- ✅ **Full CRUD Operations** — Create, read, update, delete events
- ✅ **Advanced Filtering** — Filter by date, category, capacity
- ✅ **Event Categories** — Organize events by type
- ✅ **Capacity Management** — Track attendee limits
- ✅ **Timestamps** — Auto-tracked creation/modification times
- ✅ **Soft Delete Support** — Archive events without data loss

### 📍 Venue Management
- ✅ **Venue CRUD** — Create and manage event venues
- ✅ **Geocoding Integration** — Automatic latitude/longitude lookup from address
- ✅ **Google Maps Links** — Generate shareable venue maps
- ✅ **Venue Filtering** — Search by city or minimum capacity
- ✅ **Event-Venue Integration** — Assign venues to events with capacity validation
- ✅ **Rate Limiting** — Protected geocoding API calls

### 🔍 Search & Indexing
- ✅ **Elasticsearch Integration** — Full-text search on events
- ✅ **Advanced Queries** — Search by title, description, location
- ✅ **Real-time Indexing** — Automatic index updates on changes
- ✅ **Relevance Scoring** — Ranked search results

### ⚡ Performance & Caching
- ✅ **Redis Caching** — Cache frequently accessed data
- ✅ **TTL Configuration** — Configurable cache expiration
- ✅ **Smart Invalidation** — Cache cleared on updates
- ✅ **Response Compression** — Gzip content encoding

### 🔐 Security & Authentication
- ✅ **Spring Security** — Role-based access control (RBAC)
- ✅ **JWT Tokens** — Secure stateless authentication
- ✅ **OAuth2 Integration** — Google OAuth2 login support
- ✅ **Password Hashing** — BCrypt password encoding
- ✅ **Admin Dashboard** — User management endpoints
- ✅ **JSON Error Responses** — Standardized API error format

### 📧 Notifications
- ✅ **Email Service** — Async email sending
- ✅ **Event Notifications** — Alert attendees of changes
- ✅ **Template Support** — Customizable email templates
- ✅ **Mailtrap Integration** — Development email testing

### 🧠 AI Features
- ✅ **Spring AI Integration** — LLM-powered recommendations
- ✅ **Event Suggestions** — AI-generated event recommendations
- ✅ **Natural Language Support** — Groq & OpenAI models

### 📖 API Documentation
- ✅ **OpenAPI 3.0 / Swagger** — Interactive API explorer
- ✅ **Endpoint Documentation** — Auto-generated from annotations
- ✅ **Schema Definitions** — Request/response models
- ✅ **Try-it-out Support** — Test endpoints directly from docs

---

## 🏗️ Architecture & Tech Stack

### **Backend Stack**
| Layer | Technology |
|-------|-----------|
| **Framework** | Spring Boot 3.4.1 |
| **Language** | Java 17 |
| **REST API** | Spring Web MVC |
| **ORM** | Spring Data JPA + Hibernate |
| **Database** | MySQL 8.0 |
| **Migrations** | Flyway |
| **Validation** | Jakarta Bean Validation |
| **Mapping** | MapStruct (DTO ↔ Entity) |
| **Security** | Spring Security + JWT + OAuth2 |
| **Search** | Elasticsearch 8.x |
| **Cache** | Redis (Spring Data Redis) |
| **Email** | Spring Mail (SMTP) |
| **AI** | Spring AI + Groq/OpenAI |
| **Logging** | SLF4J + Logback |
| **Build** | Maven 3.x |
| **Testing** | JUnit 5 + Mockito |

### **External Services**
- 🗺️ **Geocoding API** — `geocode.maps.co` for address-to-coordinates conversion
- 📧 **Email Service** — Mailtrap SMTP for development/production
- 🤖 **LLM Services** — Groq, OpenAI, or Anthropic Claude
- 🔐 **OAuth2 Provider** — Google Cloud Console

---

## 📊 Project Phases

| Phase | Focus | Status | Features |
|-------|-------|--------|----------|
| **Phase 1** | Core REST API | ✅ Complete | Events CRUD, JPA, H2 Testing |
| **Phase 2** | Persistence & Logic | ✅ Complete | MySQL, Flyway, Validation, MapStruct, JUnit |
| **Phase 3** | Speed & Search | ✅ Complete | Redis Caching, Elasticsearch, Swagger/OpenAPI |
| **Phase 4** | Security & Venues | 🚀 **In Progress** | Spring Security, OAuth2, Venue Management, Geocoding, AI |
| **Phase 5** | Cloud & Microservices | 🔜 Planned | Docker, Kubernetes, Kafka, GitHub Actions, CI/CD |

---

## 🚀 Getting Started

### **Prerequisites**

- **Java 17+** — Download from [oracle.com](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.8+** — Download from [maven.apache.org](https://maven.apache.org/)
- **MySQL 8.0+** — Server running locally or in Docker
- **Redis** (optional) — For caching features
- **Elasticsearch** (optional) — For full-text search
- **Git** — For cloning the repository

### **1️⃣ Clone the Repository**

```bash
git clone https://github.com/Shubhamkale1/Event-Manager.git
cd Event-Manager
```

### **2️⃣ Set Up Database**

**Start MySQL server:**
```bash
# macOS (Homebrew)
brew services start mysql

# Linux (Ubuntu/Debian)
sudo systemctl start mysql

# Docker
docker run -d \
  --name mysql \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=eventdb \
  -p 3306:3306 \
  mysql:8.0
```



## 🔐 Authentication & Security

### **JWT Tokens**
- **Type**: Bearer token in `Authorization` header
- **Format**: `Authorization: Bearer <token>`
- **Expiration**: 24 hours (configurable)
- **Algorithm**: HS256

### **OAuth2 Integration**
- **Provider**: Google Cloud
- **Flow**: Authorization Code with PKCE
- **Scope**: `email`, `profile`
- **Redirect**: `/login/oauth2/code/google`

### **Password Security**
- **Hashing**: BCrypt with 10 salt rounds
- **Validation**: Minimum 8 characters, special chars recommended

### **Role-Based Access Control (RBAC)**
```
USER   — Can read events/venues, create events, manage own profile
ADMIN  — Full access to all endpoints, user management
```

### **CORS Configuration**
```java
// Configured for localhost:3000 (frontend)
// Update in SecurityConfig for production domains
```

---

## ⚙️ Configuration

### **Application Profiles**

#### Development (`application-dev.properties`)
```properties
spring.profiles.active=dev
spring.jpa.show-sql=true
spring.elasticsearch.uris=http://localhost:9200
```

#### Production (`application-prod.properties`)
```properties
spring.profiles.active=prod
spring.jpa.show-sql=false
spring.jpa.open-in-view=false
# Environment variables for sensitive data
```

### **Configurable Properties**

| Property | Default | Purpose |
|----------|---------|---------|
| `app.jwt.secret` | — | JWT signing secret |
| `app.jwt.expiration` | `86400000` | Token lifetime (ms) |
| `app.geocoding.api-key` | — | Geocoding service API key |
| `app.cache.ttl` | `300` | Redis cache TTL (seconds) |
| `server.port` | `8081` | Application port |

---


## 📚 API Documentation

### **Interactive Swagger UI**
```
http://localhost:8081/swagger-ui.html
```

### **OpenAPI JSON Schema**
```
http://localhost:8081/api-docs
```

## 🛠️ Development

### **Running Tests**

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=EventServiceTest

# Run with coverage
mvn test jacoco:report
```

### **Building for Production**

```bash
# Build JAR
mvn clean package -DskipTests

# Run JAR
java -jar target/event-manager-0.0.1-SNAPSHOT.jar
```

### **Docker Support** (Phase 5)

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/event-manager-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
```

```bash
docker build -t event-manager:latest .
docker run -d -p 8081:8081 event-manager:latest
```

---

## 🤝 Contributing

We welcome contributions! Here's how to get started:

### **1. Fork & Clone**
```bash
git clone https://github.com/YOUR_USERNAME/Event-Manager.git
cd Event-Manager
```

### **2. Create Feature Branch**
```bash
git checkout -b feature/amazing-feature
```

### **3. Make Changes & Commit**
```bash
git add .
git commit -m "Add amazing feature"
```

### **4. Push to GitHub**
```bash
git push origin feature/amazing-feature
```

### **5. Open Pull Request**
- Describe changes clearly
- Link related issues
- Ensure tests pass

### **Code Standards**
- Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Write unit tests for new features
- Update README for API changes
- Keep commits atomic and descriptive

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2025 Shubham Kale

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
```

---

## 📞 Support & Questions

- 💬 **Issues**: [GitHub Issues](https://github.com/Shubhamkale1/Event-Manager/issues)
- 📖 **Wiki**: [Project Wiki](https://github.com/Shubhamkale1/Event-Manager/wiki)
- 📧 **Email**: [Contact via GitHub](https://github.com/Shubhamkale1)

---

## 🙏 Acknowledgments

- **Spring Boot Team** — Incredible framework
- **Spring Community** — Great ecosystem
- **Developers** — Who contribute and provide feedback

---

**Happy coding! 🚀**

Made with ❤️ by [Shubham Kale](https://github.com/Shubhamkale1)
