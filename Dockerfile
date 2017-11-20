FROM openjdk:8-jre-alpine
EXPOSE 9011

RUN apk add --no-cache curl

ENV JAVA_OPTS="-server -Xmx1g" \
    APP_OPTS="--spring.profiles.active=dev"

COPY target/omniecom-imagestorage.jar  app.jar
COPY start.sh start.sh

RUN chmod +x start.sh start.sh

ENTRYPOINT exec ./start.sh

