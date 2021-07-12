FROM adoptopenjdk/openjdk11:alpine-jre

MAINTAINER SANGWOO NAM <highright96@gmail.com>

VOLUME /tmp

EXPOSE 8080

ARG JAR_FILE=build/libs/ezfarm-back-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} ezfarm-back.jar

ENTRYPOINT ["java",
            "-Dspring.config.location=classpath:/application-auth.yml, /home/ec2-user/app/application-auth.yml",
            "-Dspring.config.location=classpath:/application-db.yml, /home/ec2-user/app/application-db.yml",
            "-Dspring.profiles.active=dev",
            "-jar",
            "/ezfarm-back.jar"]