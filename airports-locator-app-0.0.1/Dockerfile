FROM java:8
MAINTAINER Srivatsa

ADD target/airports-locator-app-0.0.1.jar airports-locator-app-0.0.1.jar
EXPOSE 8088

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${RUNTIME.ENV.TYPE}", "airports-locator-app-0.0.1.jar"]