# Étape 1 : Builder avec Maven + Java 17
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copier les fichiers nécessaires
COPY pom.xml .
COPY src ./src
COPY application.properties ./src/main/resources/application.properties

# Compiler l'application sans les tests
RUN mvn clean package -DskipTests

# Étape 2 : Image minimale avec le .jar
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copier le .jar compilé
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
