package com.utaoo.client.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DESUtil {
    /*DES加密
     * 加密模式CBC， 偏移量于密钥一致， 填充方式PKCS5Padding， 字符集UTF-8, 结果转Base64
     * @param encryptString
     * @param encryptKey  8位密钥
     * @return  base64
     * @throws Exception
     */


    public static String encryptDES(String encryptString, String encryptKey) {
        String returnValue = null;
        try {
            byte[] encryptBytes = encryptString.getBytes("UTF-8");
            returnValue = encryptDES(encryptBytes, encryptKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnValue;
    }

    /*DES加密
     * 加密模式CBC， 偏移量于密钥一致， 填充方式PKCS5Padding， 字符集UTF-8, 结果转Base64
     * @param encryptBytes 待加密文件流
     * @param encryptKey  8位密钥
     * @return  base64
     * @throws Exception
     */

    public static String encryptDES(byte[] encryptBytes, String encryptKey) {
        String returnValue = null;
        try {
            IvParameterSpec zeroIv = new IvParameterSpec(encryptKey.getBytes("UTF-8"));
            SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes("UTF-8"), "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            byte[] encryptedData = cipher.doFinal(encryptBytes);
            returnValue = new BASE64Encoder().encode(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    /*DES解密
     * 解密模式CBC， 偏移量于密钥一致， 填充方式PKCS5Padding， 字符集UTF-8, 结果转Base64
     * @param decryptString
     * @param decryptKey 8位密钥
     * @return
     * @throws Exception
     */

    public static String decryptDES(String decryptString, String decryptKey) {
        String returnValue = null;
        try {
            byte[] decryptedData = decryptDESToByte(decryptString, decryptKey);
            returnValue = new String(decryptedData, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    public static byte[] decryptDESToByte(String decryptString, String decryptKey) {
        byte[] decryptedData = null;
        try {
            byte[] byteMi = new BASE64Decoder().decodeBuffer(delBlank(decryptString));
            IvParameterSpec zeroIv = new IvParameterSpec(decryptKey.getBytes("UTF-8"));
            SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes("UTF-8"), "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
            decryptedData = cipher.doFinal(byteMi);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedData;
    }

    public static String delBlank(String obj) {
        if (obj == null || obj.length() < 1) {
            return "";
        }
        return obj.replaceAll(" ", "");
    }
}
