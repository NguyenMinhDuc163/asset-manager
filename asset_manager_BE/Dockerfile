# Giai đoạn 1: Build ứng dụng với Maven + JDK 8
FROM maven:3.6.3-openjdk-8 AS build
WORKDIR /app

# Copy toàn bộ source code vào container
COPY . .

# Build dự án
RUN mvn clean package -DskipTests

# Giai đoạn 2: Chạy ứng dụng trên JDK 8 nhẹ
FROM openjdk:8-jdk-alpine
WORKDIR /app

# Copy file .jar từ giai đoạn build
COPY --from=build /app/target/*.jar /app/AssetManager.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/AssetManager.jar"]