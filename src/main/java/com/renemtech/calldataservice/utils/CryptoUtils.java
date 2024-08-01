package com.renemtech.calldataservice.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class CryptoUtils {


    private CryptoUtils() {
    }

    public static Map<String, Object> generateAuthCall(String callNumberReceiver) throws NoSuchAlgorithmException {
        Map<String, Object> map = new HashMap<>();
        byte[] salt = generateSalt(16); // Gera um salt de 16 bytes
        byte[] hashedPassword = hashPassword(callNumberReceiver, salt);
        map.put("salt",bytesToHex(salt) );
        map.put("hash",bytesToHex(hashedPassword));
        return map;
    }

    public static boolean validatePassword(String providedPassword, String storedHash, String storedSalt) throws NoSuchAlgorithmException {
        byte[] salt = hexToBytes(storedSalt);
        byte[] hashedPassword = hashPassword(providedPassword, salt);
        String hashedPasswordHex = bytesToHex(hashedPassword);
        return hashedPasswordHex.equals(storedHash);
    }
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    private static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i+1), 16));
        }
        return bytes;
    }
    private static byte[] generateSalt(int length) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[length];
        random.nextBytes(salt);
        return salt;
    }

    private static byte[] hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt); // Adiciona o salt
        return md.digest(password.getBytes());
    }


}
