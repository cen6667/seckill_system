package com.zyc.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;



@Component
public class MD5Util {
    private static final String salt="1a2b3c4d";

    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    public static String inputPassToFromPass(String inputPass){
        // salt是随便添加的，做一个混淆
        String str = salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static String formPassToDBPass(String formPass, String salt){
        String str = salt.charAt(0)+salt.charAt(2)+formPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDBPass(String inputPass, String salt){
        String fromPass = inputPassToFromPass(inputPass);
        String dbPass = formPassToDBPass(fromPass, salt);
        return dbPass;
    }

    public static void main(String[] args) {
        String fromPass =inputPassToFromPass("123456");
        String dbPass =formPassToDBPass(fromPass, "1a2b3c4d");
        System.out.println(fromPass);
        System.out.println(dbPass);

        System.out.println(inputPassToDBPass("123456","1a2b3c4d"));
    }
}
