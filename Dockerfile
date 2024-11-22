FROM openjdk:21-jdk-slim
COPY build/libs/EcommerceBackend-0.0.1-SNAPSHOT.jar ecommerce.jar

CMD ["java", "-jar","ecommerce.jar"]
