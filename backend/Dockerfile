FROM openjdk:8-jre-alpine

COPY target/backend-1.0-SNAPSHOT.war .

ENTRYPOINT ["java", "-Xmx512m", "-Djava.security.egd=file:/dev/./urandom", "-jar", "backend-1.0-SNAPSHOT.war"]
