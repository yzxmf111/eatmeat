package encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

/**
 * @author xiaotian
 * @description
 * @date 2022-03-10 11:05
 */
public class TestBuilder implements ICryptorBuilder {

    public static final String ALIAS_NAME = "TEST";
    public static final String ALGORITHM = "AES";
    public static final String CIPHER = "AES/ECB/PKCS5Padding";

    @Override
    public String aliasName() {
        return ALIAS_NAME;
    }

    @Override
    public String algorithm() {
        return ALGORITHM;
    }

    @Override
    public ICryptorBuilder.ICryptor getCryptor(int mode) {
        if (mode != ICryptorBuilder.ICryptor.MODE_ENCRYPT && mode != ICryptorBuilder.ICryptor.MODE_DECRYPT) return null;
        return new TestCryptor(this, mode);
    }

    public static class TestCryptor implements ICryptorBuilder.ICryptor {
        private static byte[] KEY = "1234567890123456".getBytes();
        private ICryptorBuilder _builder = null;
        private int _mode = MODE_UNKNOWN;
        private Cipher _cipher = null;

        protected TestCryptor(ICryptorBuilder builder, int mode) {
            _builder = builder;
            _mode = mode;
            try {
                _cipher = Cipher.getInstance(CIPHER);
                SecretKeySpec keyspec = new SecretKeySpec(KEY, ALGORITHM);
                _cipher.init(mode, keyspec);
            } catch (Exception e) {
                _cipher = null;
                e.printStackTrace();
            }
        }

        @Override
        public ICryptorBuilder builder() {
            return _builder;
        }

        @Override
        public int algorithmType() {
            return ALGORITHM_TYPE_AES;
        }

        @Override
        public int mode() {
            return _mode;
        }

        @Override
        public byte[] process(byte[] b) throws Exception {
            if (b == null || b.length == 0) return b;
            return process(b, 0, b.length);
        }

        @Override
        public byte[] process(byte[] b, int offset, int length) throws Exception {
            if (b == null || b.length == 0) return b;
            if (offset < 0 || length <= 0 || (length + offset) > b.length)
                throw new IllegalArgumentException();
            if (_cipher != null) return _cipher.doFinal(b, offset, length);
            else throw new NoSuchAlgorithmException(CIPHER + " is unavailable");
        }
    }

}
