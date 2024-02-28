FROM gradle:6.8.1-jdk8 as build
WORKDIR /app
COPY . .
RUN gradle buildNeeded -x test

FROM openjdk:8 as package
WORKDIR /app
ARG PACKAGE="jar"
ARG PROFILE=master
ARG APP_NAME=APP_NAME
ARG JAR_FILE=build/libs/*-SNAPSHOT.${PACKAGE}
ENV PROFILE=${PROFILE}
COPY --from=build /app/$JAR_FILE /app/${APP_NAME}.${PACKAGE}
ENV APP_NAME=${APP_NAME}
ENV PACKAGE=${PACKAGE}
ENV JAVA_OPTS=""
CMD exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=$PROFILE -XX:+UseSerialGC -Xss512k -XX:MaxRAM=600m -jar /app/${APP_NAME}.${PACKAGE}
# coment
