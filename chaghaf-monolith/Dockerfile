FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copier pom.xml séparément pour profiter du cache Docker
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copier les sources et compiler
COPY src ./src
RUN mvn clean package -DskipTests -q

# ── Image finale légère ──────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Créer utilisateur non-root
RUN addgroup -S chaghaf && adduser -S chaghaf -G chaghaf

# Copier le JAR
COPY --from=build /app/target/chaghaf-monolith-1.0.0.jar app.jar

# Dossier pour firebase credentials
RUN mkdir -p /app/config && chown chaghaf:chaghaf /app/config

USER chaghaf

EXPOSE 8080

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]
