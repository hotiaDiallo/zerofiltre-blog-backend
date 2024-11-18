FROM adoptopenjdk/openjdk11:alpine-jre

ARG JAR_FILE=target/blog.jar

ARG PROFILE=dev

WORKDIR /opt/app

COPY opentelemetry-javaagent.jar /opt/app/opentelemetry-javaagent.jar

COPY ${JAR_FILE} blog.jar


# Define env variables to configure the
#OTEL_SERVICE_NAME, OTEL_METRICS_EXPORTER, OTEL_EXPORTER_OTLP_PROTOCOL,OTEL_EXPORTER_OTLP_ENDPOINT


COPY entrypoint.sh entrypoint.sh

RUN chmod 755 entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]