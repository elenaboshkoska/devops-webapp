# Stage 1: Build the application
FROM maven AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM amazoncorretto:21-alpine-jdk

WORKDIR /app

COPY --from=build /app/target/kol2022-g2-0.0.1-SNAPSHOT.jar .

CMD ["java", "-jar", "kol2022-g2-0.0.1-SNAPSHOT.jar"]
