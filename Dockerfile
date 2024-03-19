FROM gradle:jdk17 as builder
WORKDIR /app
COPY build.gradle .
COPY src ./src
RUN gradle build -x test
FROM tomcat:10.1.0-jdk17-openjdk-slim
RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY --from=builder /app/build/libs/boku-homework.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]