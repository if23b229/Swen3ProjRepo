FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -q dependency:go-offline

COPY src ./src
RUN mvn -q package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/*.jar worker.jar
# Install Tesseract OCR & Ghostscript
RUN apt-get update && \
    apt-get install -y tesseract-ocr ghostscript && \
    apt-get clean

ENTRYPOINT ["java", "-jar", "worker.jar"]
