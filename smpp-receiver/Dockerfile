FROM gradle:jre8-hotspot as runtime-image
RUN mkdir -p /opt/libs
RUN mkdir -p /opt/conf

COPY ./smpp-receiver/build/libs/smpp-receiver-1.5-DEV.jar /opt/libs
COPY ./smpp-receiver/smpp_receiver.conf /opt/conf

WORKDIR /opt/libs

CMD java -jar smpp-receiver-1.5-DEV.jar /opt/conf/smpp_receiver.conf 1
