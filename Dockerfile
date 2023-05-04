FROM openjdk:17
ADD /target/pizzaBot.jar pizzasec.jar
ENV TOKEN=XXX
ENTRYPOINT ["java", "-jar", "/pizzasec.jar"]