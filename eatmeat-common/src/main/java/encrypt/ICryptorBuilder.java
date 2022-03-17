package encrypt;

import javax.crypto.Cipher;

/**
 * @author xiaotian
 * @description
 * @date 2022-03-10 11:02
 */
public interface ICryptorBuilder {

    public String aliasName();

    public String algorithm();

    public ICryptor getCryptor(int mode);

    public static interface ICryptor {
        public static final int ALGORITHM_TYPE_UNKNOWN = 0;
        public static final int ALGORITHM_TYPE_XOR = 1;
        public static final int ALGORITHM_TYPE_MD5 = 2;
        public static final int ALGORITHM_TYPE_SHA = 3;
        public static final int ALGORITHM_TYPE_AES = 4;
        public static final int ALGORITHM_TYPE_DES = 5;
        public static final int ALGORITHM_TYPE_RSA = 6;
        public static final int MODE_UNKNOWN = 0;
        public static final int MODE_ENCRYPT = Cipher.ENCRYPT_MODE;
        public static final int MODE_DECRYPT = Cipher.DECRYPT_MODE;

        public ICryptorBuilder builder();

        public int algorithmType();

        public int mode();

        public byte[] process(byte[] b) throws Exception;

        public byte[] process(byte[] b, int offset, int length) throws Exception;
    }
}