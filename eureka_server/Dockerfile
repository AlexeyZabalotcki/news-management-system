FROM gradle:7.6-alpine AS build
ENV APP_HOME=/app/
WORKDIR $APP_HOME
COPY build.gradle settings.gradle $APP_HOME
COPY src $APP_HOME/src/
RUN gradle build

# Run stage
FROM openjdk:17-oracle
ENV APP_HOME=/app/
ENV ARTIFACT_NAME=eureka_server-1.0.0.jar
ARG JAR_FILE=$APP_HOME/build/libs/$ARTIFACT_NAME
COPY --from=build $JAR_FILE /opt/eureka-server/app.jar
ENTRYPOINT ["java", "-jar", "/opt/eureka-server/app.jar"]