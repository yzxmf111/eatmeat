package encrypt;

/**
 * @author xiaotian
 * @description
 * @date 2022-03-10 11:10
 */
public class CharsetDetector {

    public static boolean isUTF8(byte[] rawtext) {
        int c = utf8_probability(rawtext, 0, rawtext.length);
        return (c > 50);
    }

    public static boolean isUTF8(byte[] rawtext, int offset, int length) {
        int c = utf8_probability(rawtext, offset, length);
        return (c > 50);
    }

    public static int utf8_probability(byte[] rawtext, int offset, int length) {
        if (rawtext == null || offset < 0 || length == 0) return 0;
        int score = 0;
        int goodbytes = 0, asciibytes = 0;
        int i, end;
        if (length > 0) end = offset + length;
        else end = rawtext.length;

        for (i = offset; i < end; i++) {
            if ((rawtext[i] & (byte) 0x7F) == rawtext[i]) // One byte
            {
                asciibytes++; // Ignore ASCII, can throw off count
            } else if ((-64 <= rawtext[i]) && rawtext[i] <= -33 // Two bytes
                    && (i + 1 < end) && (-128 <= rawtext[i + 1]) && (rawtext[i + 1] <= -65)) {
                goodbytes += 2;
                i++;
            } else if ((-32 <= rawtext[i]) && (rawtext[i] <= -17) // Three bytes
                    && (i + 2 < end) && (-128 <= rawtext[i + 1]) && (rawtext[i + 1] <= -65) && (-128 <= rawtext[i + 2]) && (rawtext[i + 2] <= -65)) {
                goodbytes += 3;
                i += 2;
            }
        }
        if (asciibytes == end) {
            return 0;
        }
        score = (int) (100 * ((float) goodbytes / (float) (end - asciibytes)));
        // System.out.println("rawtextlen " + rawtextlen + " goodbytes " + // goodbytes + " asciibytes " + asciibytes + " score " + // score); // If not above 98, reduce to zero to prevent coincidental matches // Allows for some (few) bad formed sequences
        if (score > 98) return score;
        else if (score > 95 && goodbytes > 30) return score;
        else return 0;
    }
}
