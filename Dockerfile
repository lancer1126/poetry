FROM amazoncorretto:21.0.1
WORKDIR /app
COPY ./poetry-local.jar /app/poetry-local.jar
EXPOSE 8200
CMD ["java", "-jar", "poetry-local.jar" ]