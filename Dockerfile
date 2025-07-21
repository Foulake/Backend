# Étape 1 : Construire l'application avec Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Définir le dossier de travail
WORKDIR /app

# Copier les fichiers Maven et les dépendances
COPY pom.xml .
COPY src ./src

# Compiler l'application et créer le .jar
RUN mvn clean package -DskipTests

# Étape 2 : Image finale pour exécuter le .jar
FROM eclipse-temurin:17-jdk-alpine

# Créer un dossier pour l'app
WORKDIR /app

# Copier le .jar généré depuis l'étape précédente
COPY --from=builder /app/target/*.jar app.jar

# Exposer le port utilisé par Spring Boot (par défaut : 8080)
EXPOSE 8080

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
# Étape 1 : Construire l'application avec Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Définir le dossier de travail
WORKDIR /app

# Copier les fichiers Maven et les dépendances
COPY pom.xml .
COPY src ./src

# Compiler l'application et créer le .jar
RUN mvn clean package -DskipTests

# Étape 2 : Image finale pour exécuter le .jar
FROM eclipse-temurin:17-jdk-alpine

# Créer un dossier pour l'app
WORKDIR /app

# Copier le .jar généré depuis l'étape précédente
COPY --from=builder /app/target/*.jar app.jar

# Exposer le port utilisé par Spring Boot (par défaut : 8080)
EXPOSE 8080

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
# Étape 1 : Construire l'application avec Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Définir le dossier de travail
WORKDIR /app

# Copier les fichiers Maven et les dépendances
COPY pom.xml .
COPY src ./src

# Compiler l'application et créer le .jar
RUN mvn clean package -DskipTests

# Étape 2 : Image finale pour exécuter le .jar
FROM eclipse-temurin:17-jdk-alpine

# Créer un dossier pour l'app
WORKDIR /app

# Copier le .jar généré depuis l'étape précédente
COPY --from=builder /app/target/*.jar app.jar

# Exposer le port utilisé par Spring Boot (par défaut : 8080)
EXPOSE 8080

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
