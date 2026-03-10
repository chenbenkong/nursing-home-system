FROM nginx:alpine

# 复制前端构建文件
COPY dist /usr/share/nginx/html

# 复制Nginx配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
