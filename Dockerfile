FROM amd64/amazoncorretto:21
WORKDIR /app
COPY ./build/libs/duriseo-be-0.0.1-SNAPSHOT.jar /app/duriseo-be.jar
CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "duriseo-be.jar"]
