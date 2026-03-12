@echo off
chcp 65001 > nul
cls
echo ==========================================
echo      养老院管理系统 - 快速启动脚本
echo ==========================================
echo.
echo  本脚本将自动启动后端和前端服务
echo  请确保已按照"启动说明.md"配置好环境
echo.
echo  按任意键开始启动，或按Ctrl+C退出...
echo ==========================================
pause > nul
cls

:: 检查Java环境
echo [1/5] 正在检查Java环境...
java -version > nul 2>&1
if errorlevel 1 (
    echo [错误] 未检测到Java环境，请先安装JDK 17+
    echo 下载地址：https://adoptium.net/
    pause
    exit /b 1
)
echo [OK] Java环境正常
echo.

:: 检查Maven环境
echo [2/5] 正在检查Maven环境...
mvn -version > nul 2>&1
if errorlevel 1 (
    echo [错误] 未检测到Maven环境，请先安装Maven 3.6+
    echo 下载地址：https://maven.apache.org/download.cgi
    pause
    exit /b 1
)
echo [OK] Maven环境正常
echo.

:: 检查Node.js环境
echo [3/5] 正在检查Node.js环境...
node -v > nul 2>&1
if errorlevel 1 (
    echo [错误] 未检测到Node.js环境，请先安装Node.js 16+
    echo 下载地址：https://nodejs.org/
    pause
    exit /b 1
)
echo [OK] Node.js环境正常
echo.

:: 检查MySQL服务
echo [4/5] 正在检查MySQL服务...
net start | findstr /i "MySQL" > nul
if errorlevel 1 (
    echo [警告] 未检测到MySQL服务运行，请确保MySQL已启动
    echo 启动方法：按Win+R，输入services.msc，找到MySQL80并启动
    echo.
    echo 是否继续启动？(可能无法连接数据库)
    choice /c YN /n /m "按Y继续，按N退出："
    if errorlevel 2 exit /b 1
)
echo [OK] MySQL服务检查完成
echo.

:: 检查端口占用
echo [5/5] 正在检查端口占用...
netstat -ano | findstr :8080 > nul
if not errorlevel 1 (
    echo [警告] 端口8080已被占用，前端可能无法正常启动
)
netstat -ano | findstr :8081 > nul
if not errorlevel 1 (
    echo [警告] 端口8081已被占用，后端可能无法正常启动
)
echo [OK] 端口检查完成
echo.

echo ==========================================
echo  环境检查完成，开始启动服务...
echo ==========================================
echo.

:: 获取脚本所在目录
set "SCRIPT_DIR=%~dp0"

:: 启动后端
echo [启动] 正在启动后端服务...
echo        访问地址: http://localhost:8081
echo        首次启动需要下载依赖，请耐心等待...
echo.
cd /d "%SCRIPT_DIR%backend"
start "后端服务 - 养老院管理系统" cmd /k "echo 后端服务启动中... && mvn spring-boot:run && echo. && echo 后端服务已停止 && pause"

:: 等待后端启动
echo [等待] 等待后端服务启动（约15秒）...
timeout /t 15 /nobreak > nul
echo.

:: 启动前端
echo [启动] 正在启动前端服务...
echo        访问地址: http://localhost:8080
echo        首次启动需要安装依赖，请耐心等待...
echo.
cd /d "%SCRIPT_DIR%frontend"

:: 检查是否需要安装依赖
if not exist "node_modules" (
    echo [提示] 检测到首次启动，正在安装前端依赖...
    echo        这可能需要几分钟时间，请耐心等待...
    call npm install
    if errorlevel 1 (
        echo [错误] 前端依赖安装失败，请检查网络连接
        echo        可以尝试使用淘宝镜像：npm config set registry https://registry.npmmirror.com
        pause
        exit /b 1
    )
    echo [OK] 前端依赖安装完成
    echo.
)

start "前端服务 - 养老院管理系统" cmd /k "echo 前端服务启动中... && npm run serve && echo. && echo 前端服务已停止 && pause"

:: 等待前端启动
echo [等待] 等待前端服务启动（约10秒）...
timeout /t 10 /nobreak > nul
echo.

:: 启动完成
echo ==========================================
echo  启动完成！
echo ==========================================
echo.
echo  请打开浏览器访问：
echo    前端页面：http://localhost:8080
echo    后端接口：http://localhost:8081
echo.
echo  默认登录账号：
echo    用户名：admin
echo    密码：123456
echo.
echo  注意事项：
echo    1. 请勿关闭弹出的两个命令行窗口
echo    2. 如需停止服务，直接关闭对应窗口即可
echo    3. 如遇问题，请查看"启动说明.md"
echo.
echo ==========================================
pause
