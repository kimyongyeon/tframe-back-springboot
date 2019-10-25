ARG DOCKER_REGISTRY
FROM ${DOCKER_REGISTRY}/infra/openjdk8-utf8:1.0.1

# prometheus
COPY prometheus/jmx_prometheus.yml /prometheus/jmx_prometheus.yml
COPY prometheus/jmx_prometheus_javaagent-0.3.1.jar /prometheus/jmx_prometheus_javaagent-0.3.1.jar

# ADD whatap /whatap

ARG VERSION
ARG BUILD_ENV

ADD ./target/app.jar /app.jar

USER	appadmin
WORKDIR	/home/appadmin

ENTRYPOINT java -Xms1536m -Xmx1536m -XX:NewSize=768m -XX:MaxNewSize=768m -XX:MaxMetaspaceSize=256m -XX:MetaspaceSize=256m -XX:ParallelGCThreads=2 \
				-XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintHeapAtGC -Xloggc:/gclog/gc_${HOSTNAME}_$(date +%Y%m%d%H%M%S).log -Dgclog_file=/gclog/gc_${HOSTNAME}_$(date +%Y%m%d%H%M%S).log \
				-javaagent:/prometheus/jmx_prometheus_javaagent-0.3.1.jar=8090:/prometheus/jmx_prometheus.yml \
				-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/gclog/${HOSTNAME}.log \
				-Dmtrace_spec=${VERSION} \
				-jar /app.jar