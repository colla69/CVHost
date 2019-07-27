FROM anapsix/alpine-java:latest
MAINTAINER cola

COPY target/CVHost-1.0-SNAPSHOT.war /srv/

EXPOSE 8999
