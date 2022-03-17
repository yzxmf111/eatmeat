package encrypt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaotian
 * @description
 * @date 2022-03-10 10:48
 */
public class EncryptM {

    public static void main(String[] args) {

        List<String> dataList = importCsv(new File("/Users/xiaotian/Desktop/1.csv"));
        if (dataList != null && !dataList.isEmpty()) {
            for (String data : dataList) {
                if (dataList.indexOf(data) == 0) {
                    continue;
                }
                System.out.println(data);
                String[] split = data.split(",");
                String replace = encrypt(split[2]);
                System.out.println(replace);
            }


        }
    }


    public static List<String> importCsv(File file) {
        List<String> dataList = new ArrayList<String>();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null) {
                dataList.add(line);
            }
        } catch (Exception e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                    br = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return dataList;
    }

    public static String encrypt(String src) {
        if (src == null || src.isEmpty()) return src;
        ICryptorBuilder.ICryptor cryptor = null;
        try {
            TestBuilder builder = new TestBuilder();
            String aliasName = builder.aliasName();
            cryptor = builder.getCryptor(ICryptorBuilder.ICryptor.MODE_ENCRYPT);
            if (cryptor != null) {
                byte[] b = cryptor.process(src.getBytes(CharsetUtil.UTF8));
                if (b != null) {
                    if (cryptor.algorithmType() == ICryptorBuilder.ICryptor.ALGORITHM_TYPE_MD5)
                        return aliasName + ":" + HexUtil.toHexString(b);
                    else return aliasName + ":" + Base64Util.encode(b);
                }
            }
        } catch (Exception e) { //e.printStackTrace();
        }
        return src;
    }

    public static final char PREFIX_SEPARATOR_CHAR = ':';

    private static final int PREFIX_MIN_LEN = 4;

    public static String decrypt(String src) {
        if (src == null || src.length() < PREFIX_MIN_LEN) return src;
        int p = src.indexOf(PREFIX_SEPARATOR_CHAR);
        if (p > 0) {
            ICryptorBuilder.ICryptor cryptor = null;
            try {
                cryptor = new TestBuilder().getCryptor(ICryptorBuilder.ICryptor.MODE_DECRYPT);
                if (cryptor != null) {
                    byte[] b = Base64Util.DECODER.decode(src.substring(p + 1));
                    b = cryptor.process(b);
                    if (b != null) return new String(b, CharsetUtil.UTF8);
                }
            } catch (Exception e) { //e.printStackTrace();
            }
        }
        return src;
    }
}
