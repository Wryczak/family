FROM  maven:3.8.5-jdk-11
WORKDIR /nowy
COPY . .
EXPOSE 8000
RUN mvn clean install
CMD mvn spring-boot:run