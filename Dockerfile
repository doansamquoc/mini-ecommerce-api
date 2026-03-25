# Giai đoạn 1: Build (Sửa tag maven chuẩn)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Giai đoạn 2: Run
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render dùng biến PORT, phải có dấu '=' ở --server.port
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=${PORT}"]