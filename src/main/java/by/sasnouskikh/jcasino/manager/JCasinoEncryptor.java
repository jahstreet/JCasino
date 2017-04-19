package by.sasnouskikh.jcasino.manager;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * The class provides encrypt operations for application needs.
 *
 * @author Sasnouskikh Aliaksandr
 */
public class JCasinoEncryptor {

    /**
     * Outer forbidding to create this class instances.
     */
    private JCasinoEncryptor() {
    }

    /**
     * Encrypts any {@link String} object according to MD5 algorithm.
     *
     * @param source encrypting source
     * @return encrypted {@link String} value
     */
    public static String encryptMD5(String source) {
        return DigestUtils.md5Hex(source);
    }
}