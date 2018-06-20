package main.java.com.buildsim.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {
    public static String hash(String str, HashMethod method){
        MessageDigest mdAlgorithm;
        try {
            mdAlgorithm = MessageDigest.getInstance(method.getMethod());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return str;
        }
        mdAlgorithm.update(str.getBytes());
        byte[] digest = mdAlgorithm.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            str = Integer.toHexString(0xFF & digest[i]);
            if (str.length() < 2) {
                str = "0" + str;
            }
            hexString.append(str);
        }
        return hexString.toString();
    }
}
