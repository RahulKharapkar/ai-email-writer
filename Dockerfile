FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/ai-email-writer-0.0.1-SNAPSHOT.jar ai-email-writer.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","ai-email-writer.jar"]
