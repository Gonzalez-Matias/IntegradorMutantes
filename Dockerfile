# ========================================
# ETAPA 1: BUILD (Maven)
# ========================================
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -q -e -DskipTests=true dependency:go-offline

COPY . .
RUN mvn clean package -DskipTests=true

# ========================================
# ETAPA 2: RUNTIME (Distroless)
# ========================================
FROM gcr.io/distroless/java21-debian12
WORKDIR /app

EXPOSE 8080

COPY --from=build /app/target/*.jar /app/app.jar

CMD ["app.jar"]
