FROM openjdk:17-oracle

COPY project.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
