FROM openjdk:8-jre
MAINTAINER gunjuko92@gmail.com
ENV profile local
COPY  build/libs/java-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]