FROM adoptopenjdk/openjdk11 as builder
WORKDIR auth-services
EXPOSE 8082
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} auth-services.jar
RUN java -Djarmode=layertools -jar auth-services.jar extract

FROM adoptopenjdk/openjdk11
WORKDIR auth-services
COPY --from=builder auth-services/dependencies/ ./
COPY --from=builder auth-services/spring-boot-loader/ ./
COPY --from=builder auth-services/snapshot-dependencies/ ./
COPY --from=builder auth-services/application/ ./
ENTRYPOINT [ "java", "org.springframework.boot.loader.JarLauncher" ]