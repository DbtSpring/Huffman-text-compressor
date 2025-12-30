import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 通用工具类
 */
public class CommonUtils {
    // 禁止实例化
    private CommonUtils() {}

    // 读取UTF-8文本文件，返回字符串
    public static String readUtf8File(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }

    // 写入UTF-8文本文件
    public static void writeUtf8File(String filePath, String content) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            bw.write(content);
        }
    }

    // 读取二进制文件（内容是字节）
    public static byte[] readBinaryFile(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        }
    }

    // 写入二进制文件
    public static void writeBinaryFile(String filePath, byte[] data) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(data);
        }
    }

    // 用unicode判断是否为目标字符（中文+中文标点）
    public static boolean isTargetChar(char c) {
        return (c >= 0x4E00 && c <= 0x9FA5) || (c >= 0x3000 && c <= 0x303F);
    }
}