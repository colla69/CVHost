FROM anapsix/alpine-java:latest
MAINTAINER cola

CMD ["mkdir /srv"]
COPY target/CVHost-1.0-SNAPSHOT.war /srv/

ENTRYPOINT ["/srv"]

EXPOSE 8999

