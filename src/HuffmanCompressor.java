import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * 哈夫曼压缩解压
 */
public class HuffmanCompressor {
    // 编码映射
    private Map<Character, String> charToCode;
    private Map<String, Character> codeToChar;

    public HuffmanCompressor() {
        this.charToCode = new HashMap<>();
        this.codeToChar = new HashMap<>();
    }

    /**
     * 压缩
     * @param inputPath 原始文本路径（data.txt）
     * @param codeTablePath 编码表路径（编码表.txt）
     * @param compressedPath 压缩文件路径（compressed.bin）
     */
    public void compress(String inputPath, String codeTablePath, String compressedPath) throws IOException {
        // 1. 读取原始文本
        String text = CommonUtils.readUtf8File(inputPath);
        System.out.println("【压缩日志1】读取的原始文本：" + text);
        System.out.println("【压缩日志2】原始文本长度：" + text.length());

        // 2. 统计字符频率
        Map<Character, Integer> freqMap = countFrequency(text);
        System.out.println("【压缩日志3】统计到的字符频率：" + freqMap);

        // 3. 构建哈夫曼树
        HuffmanNode root = buildHuffmanTree(freqMap);

        // 4. 生成编码映射
        generateCode(root);
        System.out.println("【压缩日志4】生成的编码映射：" + charToCode);

        // 5. 生成编码表
        writeCodeTable(freqMap, codeTablePath);

        // 6. 生成二进制文件
        writeCompressedFile(text, compressedPath);
        System.out.println("【压缩日志5】二进制串长度（补位后）：" + (text.length() > 0 ? new File(compressedPath).length() : 0));

        System.out.println("压缩完成！");
    }

    /**
     * 解压
     * @param compressedPath 压缩文件路径
     * @param codeTablePath 编码表路径
     * @param decodedPath 解压结果路径
     */
    public void decompress(String compressedPath, String codeTablePath, String decodedPath) throws IOException {
        // 1. 读取编码表
        readCodeTable(codeTablePath);
        System.out.println("【解压日志1】读取编码表后，codeToChar映射：" + codeToChar);

        // 2. 还原二进制字符串
        String binaryStr = restoreBinaryString(compressedPath);
        System.out.println("【解压日志2】还原的二进制串：" + binaryStr);
        System.out.println("【解压日志3】二进制串长度：" + binaryStr.length());

        // 3. 解码
        String originalText = decode(binaryStr);
        System.out.println("【解压日志4】解码后的文本：" + originalText);

        // 4. 写入文件
        CommonUtils.writeUtf8File(decodedPath, originalText);
        System.out.println("解压完成！");
    }

    // ------------------------------ 以下是“写入”的内部业务方法------------------------------
    /**
     * 统计字符频率
     */
    private Map<Character, Integer> countFrequency(String text) {
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : text.toCharArray()) {
//            if (CommonUtils.isTargetChar(c)) {
                freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
//            }
        }
        return freqMap;
    }

    /**
     * 构建哈夫曼树
     */
    private HuffmanNode buildHuffmanTree(Map<Character, Integer> freqMap) {
        if (freqMap.isEmpty()) {
            throw new IllegalArgumentException("无有效字符，无法构建哈夫曼树");
        }

        // 最小堆初始化
        PriorityQueue<HuffmanNode> minHeap = new PriorityQueue<>(); //自动升序，每次取出最小元素（freq作为权值）
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            minHeap.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        // 合并节点
        while (minHeap.size() > 1) {
            HuffmanNode left = minHeap.poll();
            HuffmanNode right = minHeap.poll();
            minHeap.add(new HuffmanNode(left.getFreq() + right.getFreq(), left, right));
        }

        return minHeap.poll();
    }

    /**
     * 生成哈夫曼编码（递归遍历）（也就是储存到charToCode和codeToChar的map）
     */
    private void generateCode(HuffmanNode root) {
        charToCode.clear();
        codeToChar.clear();
        traverse(root, "");
    }

    private void traverse(HuffmanNode node, String currentCode) {
        if (node.isLeaf()) {
            charToCode.put(node.getCh(), currentCode);
            codeToChar.put(currentCode, node.getCh());
            return;
        }
        if (node.getLeft() != null) traverse(node.getLeft(), currentCode + "0");
        if (node.getRight() != null) traverse(node.getRight(), currentCode + "1");
    }

    /**
     * 写入编码表文件（按频率降序）
     */
    private void writeCodeTable(Map<Character, Integer> freqMap, String outputPath) throws IOException {
        // 按频率降序排序
        List<Map.Entry<Character, Integer>> sortedChars = new ArrayList<>(freqMap.entrySet());
        sortedChars.sort((a, b) -> b.getValue() - a.getValue());

        // 计算总码长
        long totalLen = 0;
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Character, Integer> entry : sortedChars) {
            char c = entry.getKey();
            int freq = entry.getValue();
            String code = charToCode.get(c);
            totalLen += (long) freq * code.length();
            sb.append(c).append("\t").append(freq).append("\t").append(code).append("\n");
        }
        sb.append("总码长：").append(totalLen).append("位");

        CommonUtils.writeUtf8File(outputPath, sb.toString());
    }

    /**
     * 生成二进制压缩文件（含补位）
     */
    private void writeCompressedFile(String text, String outputPath) throws IOException {
        // 拼接二进制字符串
        StringBuilder binarySb = new StringBuilder();
        for (char c : text.toCharArray()) {
            String code = charToCode.get(c);
            if (code != null) binarySb.append(code);
        }

        // 在二进制文件的最后补位（8的整数倍），目的：把二进制编码串转成 “字节” 存储
        int padding = (8 - (binarySb.length() % 8)) % 8; //填充
        for (int i = 0; i < padding; i++) binarySb.append("0");

        // 二进制转字节数组（首字节存补位个数）
        byte[] data = new byte[1 + binarySb.length() / 8];
        data[0] = (byte) padding; //（首字节存补位个数）
        for (int i = 0; i < binarySb.length(); i += 8) {
            String byteStr = binarySb.substring(i, i + 8);
            data[1 + i / 8] = (byte) Integer.parseInt(byteStr, 2); //转化成byte，储存到数组
        }

        CommonUtils.writeBinaryFile(outputPath, data);
    }


// ------------------以下是“读取”业务的内部方法----------------
    /**
     * 读取编码表，重建codeToChar映射
     */
    private void readCodeTable(String codeTablePath) throws IOException {
        codeToChar.clear();
        // 按行读取，确保UTF-8编码
        List<String> lines = Files.readAllLines(Paths.get(codeTablePath), StandardCharsets.UTF_8);
        for (String line : lines) {
            line = line.trim();
            // 跳过空行和总码长行
            if (line.isEmpty() || line.startsWith("总码长")) {
                continue;
            }
            // 兼容制表符和空格混合的情况，按任意空白符拆分
            String[] parts = line.split("\\s+");
            // 必须满足 字符+频率+编码 3部分
            if (parts.length >= 3) {
                String ch = parts[0];
                String code = parts[2];
                // 确保字符是单个字
                if (ch.length() == 1) {
                    codeToChar.put(code, ch.charAt(0));
                }
            } else {
                // 打印错误行，方便排查
                System.err.println("【编码表错误】无效行：" + line);
            }
        }
        System.out.println("【日志】成功读取编码映射数量：" + codeToChar.size());
    }

    /**
     * 还原二进制字符串（去除补位）
     */
    private String restoreBinaryString(String compressedPath) throws IOException {
        byte[] data = CommonUtils.readBinaryFile(compressedPath);
        if (data.length == 0) return "";

        int padding = data[0];
        StringBuilder binarySb = new StringBuilder();
        for (int i = 1; i < data.length; i++) {
            // 字节转8位二进制（补前导0）
            String byteStr = Integer.toBinaryString(data[i] & 0xFF);
            if (byteStr.length() < 8) {
                byteStr = "0".repeat(8 - byteStr.length()) + byteStr;
            }
            binarySb.append(byteStr);
        }

        // 去除补位
        if (padding > 0) binarySb.setLength(binarySb.length() - padding);
        return binarySb.toString();
    }

    /**
     * 解码二进制字符串
     */
    private String decode(String binaryStr) {
        StringBuilder result = new StringBuilder();
        StringBuilder currentCode = new StringBuilder();

        for (char bit : binaryStr.toCharArray()) {
            currentCode.append(bit);
            if (codeToChar.containsKey(currentCode.toString())) {
                result.append(codeToChar.get(currentCode.toString()));
                currentCode.setLength(0);
            }
        }

        if (currentCode.length() > 0) {
            System.err.println("【解码警告】未匹配的残留编码：" + currentCode);
        }
        return result.toString();
    }
}