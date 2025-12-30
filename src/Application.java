/**
 * 程序入口（仅负责配置和启动流程）
 */
public class Application {
    public static void main(String[] args) {
        // 配置文件路径（真实开发中可通过配置文件读取，此处简化）
        String inputPath = "resources/data.txt";          // 原始文本
        String codeTablePath = "output/codeTable.txt";    // 编码表
        String compressedPath = "output/compressed.bin";// 压缩文件
        String decodedPath = "output/decoded.txt";    // 解压结果

        try {
            HuffmanCompressor compressor = new HuffmanCompressor();
            // 压缩
            compressor.compress(inputPath, codeTablePath, compressedPath);
            // 解压
            compressor.decompress(compressedPath, codeTablePath, decodedPath);

            System.out.println("\n文件已生成：");
            System.out.println("- 编码表：" + codeTablePath);
            System.out.println("- 压缩文件：" + compressedPath);
            System.out.println("- 解压结果：" + decodedPath);
        } catch (Exception e) {
            System.err.println("执行失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}