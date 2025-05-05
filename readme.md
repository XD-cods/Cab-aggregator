# Umi — Taxi Service Application 🚕

Umi is a microservices-based taxi service platform that connects drivers and passengers efficiently.
Built with Spring Boot, Kafka, Keycloak, and ELK stack.

## Application stack

- Spring Boot 3.4
- Kafka
- PostgreSQL / Redis
- Docker & Docker Compose
- ELK Stack + Zipkin for observability and monitoring
- Keycloak for authentication (OIDC)
- FeignClient for synchronous communication
- Resilience4J for fault-tolerance and circuit breaking

## Getting Started 🚀

1. In folder `docker`, create an .env file.
   with the following content:
    ```dotenv
    PASSENGER_DB_USERNAME=passenger_user
    PASSENGER_DB_PASSWORD=passenger_password
    DRIVER_DB_USERNAME=driver_user
    DRIVER_DB_PASSWORD=driver_password
    RATING_DB_USERNAME=rating_user
    RATING_DB_PASSWORD=rating_password
    RIDE_DB_USERNAME=ride_user
    RIDE_DB_PASSWORD=ride_password
    
    EUREKA_PORT=8761
    
    PASSENGER_SERVICE_PORT=5008
    DRIVER_SERVICE_PORT=5003
    RATING_SERVICE_PORT=5007
    RIDE_SERVICE_PORT=5009
    API_GATEWAY_PORT=8080
    
    MAPBOX_API_KEY=your mapbox api key
    
    SPRING_PROFILES_ACTIVE=dev
    
    ES_HOST=http://elasticsearch:9200
    ES_ENABLED=true
    #ES_METRICS_STEP=
    #ES_CONNECT_TIMEOUT=
    LOGSTASH_ENABLED=true
    LOGSTASH_DESTINATION=logstash:5044
    
    ZIPKIN_HOST=http://zipkin:9411
   ```
2. Next, execute:
   ```shell
   make up
   ```