FROM java:8
MAINTAINER Srivatsa

ADD target/airports-locator-app-0.0.1.jar airports-locator-app-0.0.1.jar
EXPOSE 8088

RUN wget -O /apm-agent.jar https://search.maven.org/remotecontent?filepath=co/elastic/apm/elastic-apm-agent/1.8.0/elastic-apm-agent-1.8.0.jar

ENTRYPOINT ["java", "-javaagent:/apm-agent.jar", "-Delastic.apm.active=true", "-Delastic.apm.server_urls=http://apm-server.kube-logging.svc:8200", "-Delastic.apm.service_name=airports-locator-app-0.0.1", "-jar", "-Dspring.profiles.active=${RUNTIME_ENV_TYPE}", "airports-locator-app-0.0.1.jar"]