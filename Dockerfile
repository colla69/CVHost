FROM anapsix/alpine-java:latest
MAINTAINER cola
COPY target/CVHost-1.0-SNAPSHOT.war /opt/
ENTRYPOINT ["/opt"]
CMD ["java -jar", "CVHost-1.0-SNAPSHOT.war"]
EXPOSE 8999