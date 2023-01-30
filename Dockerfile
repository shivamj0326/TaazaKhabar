FROM openjdk:17-slim
WORKDIR /app
COPY target/taazakhabar-0.0.1-SNAPSHOT.jar /app
ENV OPTS=""
EXPOSE 8081
CMD java $OPTS -jar taazakhabar-0.0.1-SNAPSHOT.jar