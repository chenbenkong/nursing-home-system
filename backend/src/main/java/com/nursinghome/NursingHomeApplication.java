package com.nursinghome;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 养老院管理系统 Spring Boot 启动类
 */
@SpringBootApplication
@MapperScan("com.nursinghome.mapper")
public class NursingHomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(NursingHomeApplication.class, args);
    }
}
