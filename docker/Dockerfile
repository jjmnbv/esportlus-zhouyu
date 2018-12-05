FROM frolvlad/alpine-oraclejdk8
VOLUME /tmp
ADD *.jar app.jar
#指定执行启动项目，ENTRYPOINT 为容器启动后执行的命令
ENTRYPOINT ["sh","-c","java -Djava.security.egd=file:/dev/./urandom  -Duser.timezone=GMT+08 $JAVA_OPTS -jar /app.jar"]