FROM gradle:8.6.0-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

FROM gradle:8.6.0-jdk17
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
COPY . .

EXPOSE 8091
CMD ["gradle", "run", "--continuous", "--no-daemon"]