package by.sasnouskikh.jcasino.logic;

import org.apache.commons.codec.digest.DigestUtils;

public class JCasinoEncryptor {

    private JCasinoEncryptor() {
    }

    public static String encryptMD5(String password) {
        return DigestUtils.md5Hex(password);
    }
}