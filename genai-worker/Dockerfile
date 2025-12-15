# -------------------------------------------------------
# Build Stage
# -------------------------------------------------------
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom and download dependencies first (caching)
COPY pom.xml .
RUN mvn -q -e -B dependency:resolve dependency:resolve-plugins

# Copy source code
COPY src ./src

# Build the application
RUN mvn -q -e -B package -DskipTests

# -------------------------------------------------------
# Runtime Stage
# -------------------------------------------------------
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy built jar from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
