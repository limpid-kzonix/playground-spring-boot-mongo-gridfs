#!/bin/sh
while ! curl http://$HOST_IP:9005/omnie-security/status; do sleep 3; done

java $JAVA_OPTS -jar /app.jar $APP_OPTS