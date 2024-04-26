FROM openjdk:17-jdk-alpine
MAINTAINER Mirek
COPY target/kpler-*.jar server.jar
ENTRYPOINT ["java","-jar","/server.jar"]