FROM eclipse-temurin:21

WORKDIR /app
RUN ls
COPY target/challenge-digio-bank-0.0.1-SNAPSHOT.jar challenge-digio-bank.jar

EXPOSE 8080
CMD ["java", "-jar", "challenge-digio-bank.jar"]