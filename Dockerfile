FROM openjdk:8-jdk-alpine

ENV JAVA_OPTS=""

ARG DEPENDENCY=/build/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app

COPY scripts/simple-spring-boot-start.sh simple-spring-boot-start.sh
RUN chmod +x /simple-spring-boot-start.sh

CMD /simple-spring-boot-start.sh com.traeper.demo.DemoApplicationKt

#ENTRYPOINT -environment container \
#        & /simple-spring-boot-start.sh com.traeper.demo.DemoApplicationKt
