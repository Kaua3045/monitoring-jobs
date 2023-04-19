FROM eclipse-temurin:17-jre-alpine

COPY build/libs/*.jar /opt/app/

CMD java -jar /opt/app/application.jar