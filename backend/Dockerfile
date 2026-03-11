FROM eclipse-temurin:17-jdk-alpine

# 设置工作目录
WORKDIR /app

# 复制JAR包
COPY target/nursing-home-system-1.0.0.jar app.jar

# 暴露端口
EXPOSE 8081

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
