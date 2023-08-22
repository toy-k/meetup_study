FROM openjdk:11-jdk

RUN apt-get update && apt-get -y install sudo

ARG JAR_FILE="build/libs/meetup_study-*.jar"

COPY ${JAR_FILE} app.jar

ENV    PROFILE dev

CMD ["java", "-Dspring.profiles.active=${PROFILE}", "-jar","/app.jar"]

#CMD ["java", "-jar","/app.jar"]