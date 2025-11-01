#FROM eclipse-temurin:22-jdk
#
#WORKDIR /app
#
#COPY target/Beta-0.0.1-SNAPSHOT.jar app.jar
#
#EXPOSE 8080
#
#ENTRYPOINT ["java", "-jar", "app.jar"]
# ---- Stage 1: Build the JAR ----
FROM maven:3.9.9-eclipse-temurin-22 AS build
WORKDIR /app

# Copy only pom.xml first to cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the jar
RUN mvn -DskipTests clean package

# ---- Stage 2: Run the app ----
FROM eclipse-temurin:22-jdk
WORKDIR /app

# Copy jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Render automatically provides a PORT env var
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]
