####################################################################opentelemetry############
# building
################################################################################
FROM gradle:9.4.1-jdk25 AS building

ENV TZ=Asia/Bangkok

WORKDIR /home/gradle

COPY build.gradle settings.gradle ./
RUN gradle -x test build 2>/dev/null || true

COPY . .
RUN gradle build -x test --no-daemon --stacktrace

################################################################################
# deployment
################################################################################
FROM bellsoft/liberica-openjre-debian:25-cds AS deployment

ENV TZ=Asia/Bangkok

WORKDIR /opt/app

COPY --from=building /home/gradle/build/libs/app.jar ./app.jar
COPY --from=building /home/gradle/entrypoint.sh .

# run entrypoint by webusr
RUN chmod +x ./entrypoint.sh

ENTRYPOINT ["sh", "entrypoint.sh"]
