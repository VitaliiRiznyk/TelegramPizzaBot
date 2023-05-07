FROM openjdk:17
ADD /target/pizzaBot.jar pizzasec.jar
ENTRYPOINT ["java", "-jar", "/pizzasec.jar"]