# Umi — Taxi Service Application 🚕

Umi is a microservices-based taxi platform that connects drivers and passengers with real-time coordination. Built with
modern Spring Cloud ecosystem and event-driven architecture.

## Application Overview 🔭

| Service               | Description                                 | Port |
|-----------------------|---------------------------------------------|------|
| **config-server**     | Centralized configuration                   | 8888 |
| **eureka-server**     | Eureka server                               | 8761 |
| **auth-service**      | Manages auth profiles                       | 5006 |
| **passenger-service** | Manages passenger profiles                  | 5008 |
| **driver-service**    | Manages driver profiles info & vehicle info | 5003 |
| **ride-service**      | Trip lifecycle management                   | 5009 |
| **rating-service**    | Feedback processing                         | 5007 |
| **api-gateway**       | API Gateway                                 | 8080 |

## Technology Stack 🛠️

**Core:**

- Spring Boot 3.4 + Spring Cloud
- Java 17

**Data:**

- PostgreSQL (Transactional data)
- Redis (Caching & Geospatial)
- Kafka (Event streaming)

**Infrastructure:**
- Docker & Docker Compose
- Keycloak (OAuth2/OIDC)
- ELK Stack (Logging + Monitoring)
- Jaeger (Distributed tracing)

**Communication:**

- FeignClient (Service-to-service)
- Resilience4J (Circuit breaking)

## Prerequisites 📋

1. **Mapbox API Key**:
   - Get your[Mapbox access token](https://account.mapbox.com/access-tokens)

   ![create access token](./how%20to%20create%20acess%20token.gif)
   - Required for geolocation services

2. **Development Environment**:
   - Docker
   - Docker Compose
   - JDK 17+
   - Maven 4+

## Installation 🚀

1. **Clone the repository**:
   ```bash
   git clone https://github.com/XD-cods/Cab-aggregator
   ```
2. Into `docker` folder rename `example.env` into `.env`
3. In `.env` copy your mapbox api key at `MAPBOX_API_KEY` field
4. Copy your keycloak secret at `KEYCLOAK_CLIENT_SECRET` field

## Summary of All Ports ⚙

Core Services:

- Config Server: 8888
- Eureka Server: 8761
- API Gateway: 8080

Microservices:

- Auth Service: 5006
- Driver Service: 5003
- Passenger Service: 5008
- Ride Service: 5009
- Rating Service: 5007

Databases:

- Passenger DB: 5436
- Driver DB: 5433
- Rating DB: 5434
- Ride DB: 5435
- Auth DB: 5437
- Redis: 6379

Kafka Cluster:

- Zookeeper: 2181
- Kafka Brokers: 9092 (x3)
- Kafka UI: 9090

Monitoring & Logging:

- Elasticsearch: 9200, 9300
- Kibana: 5601
- Logstash: 5044, 9600
- Jaeger: 16686, 4317, 4318

Admin Tools:

- PGAdmin: 8089
- Keycloak: 8088