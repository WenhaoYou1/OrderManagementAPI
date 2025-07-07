# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy Maven Wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Set Maven Wrapper execute permissions
RUN chmod +x mvnw

# Download dependencies (utilize Docker cache layer)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build application
RUN ./mvnw clean package -DskipTests

# Create logs directory
RUN mkdir -p logs

# Expose port
EXPOSE 9004

# Set JVM parameters
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"

# Start application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar target/order-service-0.0.1-SNAPSHOT.jar"] 