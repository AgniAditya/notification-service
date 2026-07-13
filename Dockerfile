FROM eclipse-temurin:21
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests
COPY target/notification-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]