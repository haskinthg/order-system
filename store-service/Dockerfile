FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN ./gradlew build

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/build/libs/store-service-all.jar /app/store-service.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/store-service.jar"]
