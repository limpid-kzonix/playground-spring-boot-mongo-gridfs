#!/bin/sh
while [[ "$(curl -w ''%{http_code}'' http://omnie-gateway:9999/omnie-security/api/status)" != "200" ]]; do sleep 50s; done

java $JAVA_OPTS -jar /app.jar $APP_OPTS