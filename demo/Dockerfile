FROM gradle:jre8-hotspot as runtime-image
RUN mkdir -p /opt/libs

COPY ./demo/build/libs/demo-0.0.1-SNAPSHOT.jar /opt/libs

WORKDIR /opt/libs

CMD java -jar demo-0.0.1-SNAPSHOT.jar
