FROM openjdk:21-jdk

LABEL maintainer="charannayak.8@gmail.com"

ENV SPRING_DATA_REDIS_HOST=localhost

COPY target/*.jar /apps/learn-redis.jar

WORKDIR /apps

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "learn-redis.jar"]