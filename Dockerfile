
FROM maven:3.8.5-openjdk-17 as build

WORKDIR /app

COPY pom.xml .


COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim

COPY --from=build /app/target/meetingRoomMgt-0.0.1-SNAPSHOT.jar meetingRoomMgt.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "meetingRoomMgt.jar"]
