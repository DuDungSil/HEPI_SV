# 베이스 이미지 설정
FROM openjdk:17-jdk-alpine

# 작업 디렉토리 설정
WORKDIR /app

# JAR 파일 복사
COPY target/BODY_GUIDE.jar app.jar
COPY config/application.properties /app/config/application.properties

# 환경 변수 설정
ENV APP_ENV=production
ENV DB_HOST=host.docker.internal

# 포트 설정
EXPOSE 3573

# 애플리케이션 실행 명령
ENTRYPOINT ["java", "-jar", "app.jar"]