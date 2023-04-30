FROM openjdk:17
ADD /target/pizzaBot.jar pizza.jar
ENV TOKEN =XXX
ENTRYPOINT ["java", "-jar", "/pizza.jar"]

