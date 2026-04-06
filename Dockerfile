FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

# ✅ FIX: give execute permission
RUN chmod +x mvnw

# Build the jar
RUN ./mvnw clean package -DskipTests

# Run the app
CMD ["sh", "-c", "java -jar target/*.jar"]