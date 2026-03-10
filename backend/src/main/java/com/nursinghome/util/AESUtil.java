package com.nursinghome.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * AES加密工具类
 * 用于加密敏感数据（身份证号、手机号等）
 */
public class AESUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    
    // 默认密钥（生产环境应从配置文件或环境变量读取）
    private static final String DEFAULT_KEY = "NursingHome2024!";

    /**
     * 加密
     * @param plainText 明文
     * @return 密文（Base64编码）
     */
    public static String encrypt(String plainText) {
        return encrypt(plainText, DEFAULT_KEY);
    }

    /**
     * 加密
     * @param plainText 明文
     * @param key 密钥（16位）
     * @return 密文（Base64编码）
     */
    public static String encrypt(String plainText, String key) {
        if (plainText == null || plainText.isEmpty()) {
            return null;
        }
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("加密失败", e);
        }
    }

    /**
     * 解密
     * @param cipherText 密文（Base64编码）
     * @return 明文
     */
    public static String decrypt(String cipherText) {
        return decrypt(cipherText, DEFAULT_KEY);
    }

    /**
     * 解密
     * @param cipherText 密文（Base64编码）
     * @param key 密钥（16位）
     * @return 明文
     */
    public static String decrypt(String cipherText, String key) {
        if (cipherText == null || cipherText.isEmpty()) {
            return null;
        }
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decoded = Base64.getDecoder().decode(cipherText);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("解密失败", e);
        }
    }

    /**
     * 数据脱敏 - 身份证号
     * @param idCard 身份证号
     * @return 脱敏后的身份证号（如：110101********1234）
     */
    public static String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() != 18) {
            return idCard;
        }
        return idCard.substring(0, 6) + "********" + idCard.substring(14);
    }

    /**
     * 数据脱敏 - 手机号
     * @param phone 手机号
     * @return 脱敏后的手机号（如：138****8888）
     */
    public static String maskPhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
}
