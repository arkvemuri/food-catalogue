FROM openjdk:21-slim
WORKDIR /opt
COPY target/*.jar /opt/app.jar
LABEL authors="rkvem"

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar