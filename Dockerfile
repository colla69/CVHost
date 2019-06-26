FROM alpine-java:base
MAINTAINER cola
COPY target/CVHost-1.0-SNAPSHOT.war /opt/
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "/opt/CVHost-1.0-SNAPSHOT.war"]
EXPOSE 8999