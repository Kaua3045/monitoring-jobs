FROM eclipse-temurin:19-jre-alpine

COPY build/libs/*.jar /opt/app/

CMD java -jar /opt/app/application.jar