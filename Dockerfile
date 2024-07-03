FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app
COPY target/challenge-digio-bank.jar challenge-digio-bank.jar

EXPOSE 8080
CMD ["java", "-jar", "challenge-digio-bank.jar"]