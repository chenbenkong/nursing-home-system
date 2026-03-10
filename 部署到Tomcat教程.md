# 部署 WAR 包到 Tomcat 教程

## 方法一：直接复制（最简单）

### 1. 找到 Tomcat 安装目录
通常在你安装 Tomcat 的地方，比如：
- `C:\apache-tomcat-9.x.x`
- `D:\Tomcat9`

### 2. 复制 war 包
把生成的 war 包复制到 Tomcat 的 webapps 文件夹：
```
 nursing-home-system.war 
    ↓
复制到
    ↓
 C:\apache-tomcat-9.x.x\webapps\
```

### 3. 启动 Tomcat
**方式A：双击启动**
- 进入 Tomcat 的 `bin` 文件夹
- 双击运行 `startup.bat`

**方式B：命令行启动**
```cmd
cd C:\apache-tomcat-9.x.x\bin
startup.bat
```

### 4. 访问系统
打开浏览器访问：
```
http://localhost:8080/nursing-home-system
```

---

## 方法二：使用 IDEA 部署（开发推荐）

### 1. 配置 Tomcat 服务器
1. 点击 IDEA 右上角 **Add Configuration**
2. 点击 **+** 号，选择 **Tomcat Server** → **Local**
3. 配置 Tomcat 路径：
   - **Application server**: 选择你的 Tomcat 安装目录
   - **URL**: `http://localhost:8080/nursing-home-system`

### 2. 部署项目
1. 点击 **Deployment** 标签
2. 点击 **+** 号，选择 **Artifact**
3. 选择 `nursing-home-system:war exploded`
4. **Application context** 填写：`/nursing-home-system`

### 3. 启动运行
点击绿色三角形按钮运行，IDEA 会自动：
- 编译代码
- 部署到 Tomcat
- 打开浏览器

---

## 方法三：使用 Maven Tomcat 插件（无需安装 Tomcat）

### 1. 在 pom.xml 中添加插件
```xml
<build>
    <plugins>
        <!-- Tomcat 插件 -->
        <plugin>
            <groupId>org.apache.tomcat.maven</groupId>
            <artifactId>tomcat7-maven-plugin</artifactId>
            <version>2.2</version>
            <configuration>
                <port>8080</port>
                <path>/nursing-home-system</path>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### 2. 运行命令
```cmd
cd nursing-home-system/backend
mvn tomcat7:run
```

### 3. 访问
```
http://localhost:8080/nursing-home-system
```

---

## 常见问题

### 1. 端口被占用
**错误**：`Address already in use: bind 8080`

**解决**：修改 Tomcat 端口
- 打开 `conf/server.xml`
- 找到 `<Connector port="8080"`，改成其他端口如 8081

### 2. 乱码问题
在 `conf/logging.properties` 中添加：
```properties
java.util.logging.ConsoleHandler.encoding = UTF-8
```

### 3. 内存不足
在 `bin/setenv.bat` 中添加：
```bat
set JAVA_OPTS=-Xms512m -Xmx1024m
```

---

## 快速检查清单

- [ ] 安装了 JDK 11+
- [ ] 安装了 Tomcat 9+
- [ ] 创建了 MySQL 数据库
- [ ] 修改了 jdbc.properties 数据库配置
- [ ] 复制 war 包到 webapps
- [ ] 启动了 Tomcat
- [ ] 浏览器访问 `http://localhost:8080/nursing-home-system`

**默认账号**：admin / admin123
