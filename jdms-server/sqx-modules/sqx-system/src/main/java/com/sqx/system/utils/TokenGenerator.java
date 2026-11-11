package com.sqx.system.utils;

import com.sqx.common.core.exception.SqxException;

import java.security.MessageDigest;
import java.util.UUID;

/**
 * @description：TODO
 * @author： liuduo
 * @create： 2025/12/24 15:46
 */
public class TokenGenerator {

    private static final char[] HexCode = "0123456789abcdef".toCharArray();

    public static String generateValue() {
        return generateValue(UUID.randomUUID().toString());
    }

    public static String generateValue(String param) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(param.getBytes());
            byte[] messageDigest = algorithm.digest();
            return toHexString(messageDigest);
        } catch (Exception e) {
            throw new SqxException("生成Token失败", e);
        }
    }

    public static String toHexString(byte[] data) {
        if (data == null) {
            return null;
        }
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(HexCode[(b >> 4) & 0xF]);
            r.append(HexCode[(b & 0xF)]);
        }
        return r.toString();
    }


}
