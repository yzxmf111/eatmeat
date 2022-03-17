package encrypt;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author xiaotian
 * @description
 * @date 2022-03-10 11:09
 */
public class CharsetUtil {

    public static final Charset UTF8 = StandardCharsets.UTF_8;
    public static final Charset US_ASCII = StandardCharsets.US_ASCII;
    public static final Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;
    public static final Charset LATIN_1 = StandardCharsets.ISO_8859_1;
    public static Charset GBK = null;
    public static Charset GB18030 = null;

    static {
        try {
            GBK = Charset.forName("GBK");
            GB18030 = Charset.forName("GB18030");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*** 是否 UTF-8 编码 * @param b * @return */
    public static boolean isUTF8(byte[] b) {
        return CharsetDetector.isUTF8(b);
    }

    public static boolean isUTF8(byte[] b, int offset, int length) {
        return CharsetDetector.isUTF8(b, offset, length);
    }
}
