FROM anapsix/alpine-java:latest
MAINTAINER cola
COPY target/CVHost-1.0-SNAPSHOT.war /opt/
CMD ["mkdir /srv"]
ENTRYPOINT ["/srv"]
CMD ["java -jar", "CVHost-1.0-SNAPSHOT.war"]
EXPOSE 8999