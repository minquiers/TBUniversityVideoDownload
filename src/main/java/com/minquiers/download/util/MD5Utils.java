package com.minquiers.download.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.util.IOUtils;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    private static final String ALGORITHM_MD5 = "MD5";

    private static final String UTF_8 = "UTF-8";

    /**
     * MD5 16bit 小写.
     * @param readyEncryptStr ready encrypt string
     * @return String encrypt result string
     * @throws NoSuchAlgorithmException
     * */
    public static final String MD5_16bit_lower(String readyEncryptStr) throws NoSuchAlgorithmException {
        if(readyEncryptStr != null){
            return MD5Utils.MD5_32bit_lower(readyEncryptStr).substring(8, 24);
        }else{
            return null;
        }
    }

    /**
     * MD5 16bit 大写.
     * @param readyEncryptStr ready encrypt string
     * @return String encrypt result string
     * @throws NoSuchAlgorithmException
     * */
    public static final String MD5_16bit_upper(String readyEncryptStr) throws NoSuchAlgorithmException {
        return MD5_16bit_lower(readyEncryptStr).toUpperCase();
    }

    /**
     * MD5 32bit 小写.
     * @param readyEncryptStr ready encrypt string
     * @return String encrypt result string
     * @throws NoSuchAlgorithmException
     * */
    public static final String MD5_32bit_lower(String readyEncryptStr) throws NoSuchAlgorithmException{
        if(readyEncryptStr != null){
            //Get MD5 digest algorithm's MessageDigest's instance.
            MessageDigest md = MessageDigest.getInstance(ALGORITHM_MD5);
            //Use specified byte update digest.
            md.update(readyEncryptStr.getBytes());
            //Get cipher text
            byte [] b = md.digest();
            //The cipher text converted to hexadecimal string
            StringBuilder su = new StringBuilder();
            //byte array switch hexadecimal number.
            for(int offset = 0,bLen = b.length; offset < bLen; offset++){
                String haxHex = Integer.toHexString(b[offset] & 0xFF);
                if(haxHex.length() < 2){
                    su.append("0");
                }
                su.append(haxHex);
            }
            return su.toString();
        }else{
            return null;
        }
    }

    /**
     * MD5 32bit 大写.
     * @param readyEncryptStr ready encrypt string
     * @return String encrypt result string
     * @throws NoSuchAlgorithmException
     * */
    public static final String MD5_32bit_upper(String readyEncryptStr) throws NoSuchAlgorithmException{
        return MD5_32bit_lower(readyEncryptStr).toUpperCase();
    }

    /**
     * MD5 64bit 大写.
     * @param readyEncryptStr ready encrypt string
     * @return String encrypt result string
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * */
    public static final String MD5_64bit(String readyEncryptStr) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM_MD5);
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(md.digest(readyEncryptStr.getBytes(UTF_8)));
    }


    /**
     * 获取文件MD5,使用apache的org.apache.commons.codec.digest.DigestUtils;
     * @param filePath 文件路径
     * @return String
     */
    public static String MD5_File(String filePath) throws Exception {
        try (FileInputStream fis= new FileInputStream(filePath)) {
            return DigestUtils.md5Hex(IOUtils.toByteArray(fis));

        }
    }
}