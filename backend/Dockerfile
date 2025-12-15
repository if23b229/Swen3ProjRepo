# --- Build Stage ----------------------------------------------------
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Maven benötigt die pom.xml vorab, um Dependencies zu cachen
COPY pom.xml .
RUN mvn -q -e -B dependency:go-offline

# Nun den ganzen Source Code kopieren und bauen
COPY src ./src

RUN mvn -q -e -B package -DskipTests


# --- Runtime Stage --------------------------------------------------
FROM eclipse-temurin:21-jre

WORKDIR /app

# Das fertige JAR aus dem Build-Stage übernehmen
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
