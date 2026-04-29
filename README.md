# Event Management System 

A RESTful Event Management API built with Spring Boot 3.x as part of a progressive learning journey from monolith to microservices.

---

## API Documentation

- [https://github.com/Shubhamkale1/Event-Manager/wiki](https://github.com/Shubhamkale1/Event-Manager/wiki)

***

## About This Project

This is **Phase 1** of a 5-phase project designed to learn the entire Spring ecosystem by building a real application. Each phase adds new technologies on top of a working foundation.

| Phase | Focus | Status |
|-------|-------|--------|
| Phase 1 | Core REST API — Spring Boot, JPA, H2, Lombok | ✅ Complete |
| Phase 2 | Persistence & Logic — MySQL, Flyway, Validation, MapStruct, JUnit | ✅ Complete |
| Phase 3 | Speed & Search — Redis, Elasticsearch, Swagger | ✅ Complete |
| Phase 4 | Security — Spring Security, OAuth2, Spring AI | 🔜 Planned |
| Phase 5 | Cloud & Microservices — Docker, Kafka, GitHub Actions | 🔜 Planned |

---



## API Endpoints

Base URL: `http://localhost:8080/api/events`

| Method | Endpoint | Description | Status Code |
|--------|----------|-------------|-------------|
| GET | `/api/events` | Get all events | 200 OK |
| GET | `/api/events/{id}` | Get event by ID | 200 OK |
| POST | `/api/events` | Create new event | 201 Created |
| PUT | `/api/events/{id}` | Update existing event | 200 OK |
| DELETE | `/api/events/{id}` | Delete event | 204 No Content |

---


## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.x
- IntelliJ IDEA (recommended) or any IDE
- Postman (for API testing)

### Run the Application

**1. Clone the repository**
```bash
git clone https://github.com/yourusername/event-management.git
cd event-management
```

**2. Build the project**
```bash
mvn clean install
```

**3. Run the application**
```bash
mvn spring-boot:run
```

The application starts on `http://localhost:8080`

---

### H2 Database Console

While the app is running, visit the H2 web console to inspect your data live:

```
URL:      http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:eventsdb
Username: sa
Password: (leave empty)
```

Run `SELECT * FROM events` to see all your data in real time.

> Note: H2 is an in-memory database. All data is lost when the application stops. This is replaced with MySQL in Phase 2.

---


## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

---

## License

This project is licensed under the MIT License — see below for details.

---

MIT License

Copyright (c) 2025

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
