FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app
RUN ls target/
COPY target/challenge-digio-bank-0.0.1-SNAPSHOT.jar challenge-digio-bank.jar

EXPOSE 8080
CMD ["java", "-jar", "challenge-digio-bank.jar"]