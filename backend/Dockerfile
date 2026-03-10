FROM tomcat:9-jdk11

# 删除默认应用
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# 复制war包
COPY target/nursing-home-system.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
