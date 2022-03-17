package encrypt;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * @author xiaotian
 * @description
 * @date 2022-03-10 11:16
 */
public class HexUtil {

    private static final char[] hex_chars = {
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f'
    };

    /*** 长度总是 2 的倍数 * @param b * @return */
    public static String toHexString(byte[] b) {
        if (b == null) return null;
        return toHexString(b, 0, b.length);
    }

    public static String toHexString(byte[] b, int offset, int length) {
        if (b == null) return null;
        int end = offset + length;
        StringBuilder sb = new StringBuilder(length * 2);
        int c;
        for (int i = offset; i < end; i++) {
            c = (b[i] >> 4) & 0x0F;
            sb.append(hex_chars[c]);
            c = b[i] & 0x0F;
            sb.append(hex_chars[c]);
        }
        return sb.toString();
    }

    public static boolean isHexString(String hex) {
        if (hex == null || hex.isEmpty()) return false;
        char ch; // 0-9 A-Z a-z
        for (int i = 0; i < hex.length(); i++) {
            ch = hex.charAt(i);
            if (ch < '0' || (ch > '9' && ch < 'A') || (ch > 'F' && ch < 'a') || ch > 'f') return false;
        }
        return true;
    }

    public static byte[] parseHexString(String hex) {
        if (hex == null) return null;
        hex = hex.toLowerCase();
        byte[] b = null;
        int i, s;
        if (hex.length() % 2 == 0) {
            b = new byte[hex.length() / 2];
            i = s = 0;
        } else {
            b = new byte[hex.length() / 2 + 1];
            i = s = 1;
            b[0] = _byte(hex.charAt(0));
        }
        byte b0, b1;
        for (; i < b.length; i++) {
            b1 = _byte(hex.charAt(s++));
            b0 = _byte(hex.charAt(s++));
            b[i] = (byte) ((b1 << 4) | b0);
        }
        return b;
    }

    /*** 不同于 Integer.toHexString，前者按最小长度输出，本方法总是输出 8 位字符 * 高位在前 * @param val * @return */
    public static String toHexString(int val) {
        char[] ss = new char[8];
        byte c = (byte) ((val >> 28) & 0x0F);
        ss[0] = hex_chars[c];
        c = (byte) ((val >> 24) & 0x0F);
        ss[1] = hex_chars[c];
        c = (byte) ((val >> 20) & 0x0F);
        ss[2] = hex_chars[c];
        c = (byte) ((val >> 16) & 0x0F);
        ss[3] = hex_chars[c];
        c = (byte) ((val >> 12) & 0x0F);
        ss[4] = hex_chars[c];
        c = (byte) ((val >> 8) & 0x0F);
        ss[5] = hex_chars[c];
        c = (byte) ((val >> 4) & 0x0F);
        ss[6] = hex_chars[c];
        c = (byte) (val & 0x0F);
        ss[7] = hex_chars[c];
        return new String(ss);
    }

    /*** 仅将后 4 位转换，同 toHexString(int) * @param val * @return */
    public static String toHexString4(long val) {
        return toHexString((int) (val & 0xFFFFFFFF));
    }

    /*** 输出字符串 getBytes() 后转 Hex，长度总是 2 的倍数 * @param s * @param charset * @return */
    public static String toHexString(String s, Charset charset) {
        return toHexString(s.getBytes(charset));
    }

    public static String toHexString(String s, String charsetName) throws UnsupportedEncodingException {
        return toHexString(s.getBytes(charsetName));
    }

    public static String fromHexString(String hex, Charset charset) {
        byte[] b = parseHexString(hex);
        return new String(b, charset);
    }

    public static String fromHexString(String hex, String charsetName) throws UnsupportedEncodingException {
        byte[] b = parseHexString(hex);
        return new String(b, charsetName);
    }

    private static byte _byte(char c) {
        for (byte i = 0; i < hex_chars.length; i++) {
            if (c == hex_chars[i]) return i;
        }
        throw new NumberFormatException("For input char: '" + c + "'");
    }
}
