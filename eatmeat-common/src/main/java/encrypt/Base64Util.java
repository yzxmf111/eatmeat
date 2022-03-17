package encrypt;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class Base64Util {

    public static Decoder DECODER = Base64.getDecoder();
    public static Encoder ENCODER = Base64.getEncoder();

    public static String decodeToUTF8(byte[] src) {
        if (src == null) return null;
        else if (src.length == 0) return "";
        byte[] b = DECODER.decode(src);
        if (b != null)
            return new String(b, StandardCharsets.UTF_8);
        else return null;
    }

    public static String decodeToUTF8(String src) {
        if (src == null) return null;
        else if (src.isEmpty()) return "";
        byte[] b = DECODER.decode(src);
        if (b != null) return new String(b, StandardCharsets.UTF_8);
        else return null;
    }

    public static String encode(byte[] src) {
        if (src == null) return null;
        else if (src.length == 0) return "";
        byte[] b = ENCODER.encode(src);
        if (b != null) return new String(b, StandardCharsets.UTF_8);
        else return null;
    }

    public static String encodeFromUTF8(String src) {
        if (src == null) return null;
        else if (src.isEmpty()) return "";
        byte[] b = ENCODER.encode(src.getBytes(StandardCharsets.UTF_8));
        if (b != null) return new String(b, StandardCharsets.UTF_8);
        else return null;
    }
}
