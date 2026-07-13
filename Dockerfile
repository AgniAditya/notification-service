FROM eclipse-temurin:21
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests
ENTRYPOINT ["java", "-jar", "target/notification-service-0.0.1-SNAPSHOT.jar"]