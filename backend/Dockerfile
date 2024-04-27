# Stage 1: Build
FROM eclipse-temurin:17-jdk-focal AS builder

WORKDIR /app

# Copy the Maven wrapper and the pom.xml files to build the dependencies
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

# Copy the rest of the project files and build the application
COPY src ./src
RUN ./mvnw package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jdk-focal

WORKDIR /app

# Copy the compiled JAR from the builder stage
COPY --from=builder /app/target/*.jar /app/app.jar

# Run the compiled Spring Boot application
CMD ["java", "-jar", "/app/app.jar"]