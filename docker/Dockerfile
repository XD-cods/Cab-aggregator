FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN --mount=type=cache,target=/root/.m2 \
    mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy AS extract
ARG SERVICE_NAME
WORKDIR /app
COPY --from=build /app/target/${SERVICE_NAME}-*.jar ./${SERVICE_NAME}.jar
RUN java -Djarmode=layertools -jar ${SERVICE_NAME}.jar extract

FROM eclipse-temurin:21-jre-jammy
RUN apt-get update && apt-get install -y curl
WORKDIR /app
VOLUME /tmp
COPY --from=extract /app/dependencies/ ./
COPY --from=extract /app/spring-boot-loader/ ./
COPY --from=extract /app/snapshot-dependencies/ ./
COPY --from=extract /app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
