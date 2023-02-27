FROM amazoncorretto:11-alpine3.14-jdk
RUN adduser dhairya --disable password
USER dhairya
WORKDIR /app
ADD target/DigitBatua-0.0.1-SNAPSHOT.jar DigitBatua-0.0.1-SNAPSHOT.jar
EXPOSE 3000
CMD java -jar DigitBatua-0.0.1-SNAPSHOT.jar