package com.nursinghome.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * BCrypt密码加密工具类
 * 用于替代MD5，提供更强的密码安全性
 * 同时兼容MD5密码（用于过渡期间）
 */
public class BCryptUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * 加密密码
     * @param password 明文密码
     * @return 加密后的密码
     */
    public static String encode(String password) {
        if (password == null || password.isEmpty()) {
            return null;
        }
        return encoder.encode(password);
    }

    /**
     * 验证密码
     * 支持BCrypt和MD5两种格式（MD5用于兼容旧数据）
     * @param rawPassword 明文密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        
        // 判断是否是BCrypt格式（BCrypt密码以$2a$、$2b$或$2y$开头）
        if (encodedPassword.startsWith("$2a$") || encodedPassword.startsWith("$2b$") || encodedPassword.startsWith("$2y$")) {
            return encoder.matches(rawPassword, encodedPassword);
        } else {
            // 兼容MD5格式（旧数据）
            return encodedPassword.equalsIgnoreCase(md5(rawPassword));
        }
    }
    
    /**
     * MD5加密（用于兼容旧数据）
     * @param input 输入字符串
     * @return MD5加密结果
     */
    private static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashText = new StringBuilder(no.toString(16));
            while (hashText.length() < 32) {
                hashText.insert(0, "0");
            }
            return hashText.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
