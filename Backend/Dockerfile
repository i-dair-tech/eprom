# Build stage
FROM maven:3.8.5-jdk-11 AS build 
WORKDIR /Backend
COPY pom.xml .
COPY src ./src
RUN rm -f sonar-project.properties
RUN mvn package -Dmaven.test.skip -Dskip.npm -DskipTests


# Run stage
FROM openjdk:11-jre-slim
COPY --from=build /Backend/target/*.war /eprom-0.0.1-SNAPSHOT.war
EXPOSE 8100
ENTRYPOINT ["java", "-jar", "/eprom-0.0.1-SNAPSHOT.war"]
