package com.nursinghome.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nursinghome.entity.Result;
import com.nursinghome.util.JwtUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT拦截器
 */
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行OPTIONS请求
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        
        if (token == null || !token.startsWith("Bearer ")) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(401);
            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().write(mapper.writeValueAsString(Result.error(401, "未登录或token无效")));
            return false;
        }

        token = token.substring(7);
        
        try {
            if (!JwtUtil.validateToken(token)) {
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(401);
                ObjectMapper mapper = new ObjectMapper();
                response.getWriter().write(mapper.writeValueAsString(Result.error(401, "token已过期")));
                return false;
            }
            
            // 将用户信息存入request
            Long userId = JwtUtil.getUserIdFromToken(token);
            String username = JwtUtil.getUsernameFromToken(token);
            String role = JwtUtil.getRoleFromToken(token);
            
            request.setAttribute("userId", userId);
            request.setAttribute("username", username);
            request.setAttribute("role", role);
            
            return true;
        } catch (Exception e) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(401);
            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().write(mapper.writeValueAsString(Result.error(401, "token解析失败")));
            return false;
        }
    }
}
