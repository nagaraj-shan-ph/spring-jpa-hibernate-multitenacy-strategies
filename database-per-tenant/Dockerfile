FROM openjdk:10-jdk-slim

MAINTAINER Nagarajan Shanmugam <nagarajan.shanmugam@imaginea.com>

VOLUME /tmp

#Setting the environment varible

ENV SPRING_PROFILES_ACTIVE dev

ENV SPRING_DATASOURCE_URL "jdbc:postgresql://localhost:5432/multi-tenant?currentSchema=lms"

ENV FLYWAY_ENABLED 'true'

ENV JAVA_OPTIONS "-Djava.security.egd=file:/dev/./urandom -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1 -XshowSettings:vm -XX:+UseG1GC"

ENV JAVA_APP_JAR "app.jar"

RUN apt-get update \
  && apt-get install -y wget tini bash htop atop \
  && rm -rf /var/cache/apk/*

RUN \
  mkdir -p /var/log/lms

ENV service_name="lms"

RUN \
    mkdir -p /opt/${service_name}/logs && \
    mkdir -p /var/log/${service_name} && \
    touch /var/log/${service_name}/app.log && \
    touch /var/log/${service_name}/${service_name}-stderr.log

ARG JAR_FILE

ADD sboot-schema-multitenancy-0.0.1-SNAPSHOT.jar /opt/app.jar

ENTRYPOINT ["tini","--"]

CMD java -Djava.security.egd=file:/dev/./urandom $JAVA_OPTIONS -jar /opt/app.jar