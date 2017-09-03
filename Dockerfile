FROM hseeberger/scala-sbt:8u141-jdk_2.12.3_0.13.16

WORKDIR /opt/dynamoDB-local

COPY ./project ./project
COPY ./build.sbt .

RUN sbt compile

COPY . .

RUN sbt compile

ENTRYPOINT ["sbt"]

CMD ["-jvm-debug", "5005", "run"]