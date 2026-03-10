@echo off
chcp 65001
cls
echo ==========================================
echo      养老院管理系统 - 快速启动脚本
echo ==========================================
echo.

:: 检查MySQL是否运行
echo [1/3] 正在检查环境...
timeout /t 1 /nobreak > nul

:: 启动后端
echo [2/3] 正在启动后端服务...
echo     访问地址: http://localhost:8081
echo.
cd /d "%~dp0backend"
start "后端服务" cmd /k "mvn tomcat7:run"

:: 等待后端启动
timeout /t 10 /nobreak > nul

:: 启动前端
echo [3/3] 正在启动前端服务...
echo     访问地址: http://localhost:8080
echo.
cd /d "%~dp0frontend"
start "前端服务" cmd /k "npm run serve"

echo.
echo ==========================================
echo  启动完成!
echo  后端: http://localhost:8081
echo  前端: http://localhost:8080
echo  账号: admin / admin123
echo ==========================================
pause
