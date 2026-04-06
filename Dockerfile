# Use Java 17 (stable for production)
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy all project files
COPY . .

# Build the jar file
RUN ./mvnw clean package -DskipTests

# Run the application
CMD ["sh", "-c", "java -jar target/*.jar"]