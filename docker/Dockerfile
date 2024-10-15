# 使用官方的 OpenJDK 作为基础镜像
FROM openjdk:17-jdk-alpine


# 将 JAR 文件复制到容器中
ADD doubleSelection-admin.jar app.jar

# 暴露应用运行的端口
EXPOSE 8080

# 运行应用程序
ENTRYPOINT ["java", "-jar", "/app.jar"]