# Build stage
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Gradle wrapper와 build 파일 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 소스 코드 복사
COPY src src

# 빌드 실행
RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test

# Runtime stage
FROM eclipse-temurin:21-jre

WORKDIR /app

# 빌드 결과물만 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 시간대 설정
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 포트 노출
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/health || exit 1

# 실행
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "-Xmx512m", "-Xms256m", "app.jar"]
