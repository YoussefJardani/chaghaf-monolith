FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# pom.xml est à la RACINE du repo
COPY pom.xml .
RUN mvn dependency:go-offline -q

# src est dans chaghaf-monolith/
COPY chaghaf-monolith/src ./src
COPY chaghaf-monolith/src-additions/main/java/. ./src/main/java/
RUN mvn clean package -DskipTests -q

# ── Image finale légère ──────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S chaghaf && adduser -S chaghaf -G chaghaf

COPY --from=build /app/target/chaghaf-monolith-1.0.0.jar app.jar

RUN mkdir -p /app/config && chown chaghaf:chaghaf /app/config

USER chaghaf

EXPOSE 8080

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]
