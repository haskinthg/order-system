FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN ./gradlew build

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/build/libs/payment-service-all.jar /app/payment-service.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/payment-service.jar"]
