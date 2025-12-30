*中文项目介绍：* [跳转](#项目简介)

*English project intro:* [GoTo](#project-introduction)

---

## 项目简介

本项目是基于 Java 实现的**哈夫曼编码文本压缩解压工具**，按照字符频率构建哈夫曼树、生成哈夫曼编码映射，完成文本文件的压缩与解压全流程

(data.txt <-->（compressed.bin + codeTable.txt)）

## 项目结构

```plaintext
├─ output			#代码运行后的输出
│  ├─ codeTable.txt    # 字符-编码映射表
│  ├─ compressed.bin   # 压缩后的二进制文件
│  └─ decoded.txt      # 解压后还原的文本文件
├─ resources
│  └─ data.txt         # 待压缩的原始文本文件
└─ src
   ├─ Application.java       # 主程序入口
   ├─ CommonUtils.java       # 通用工具类（文件读写等）
   ├─ HuffmanCompressor.java # *哈夫曼压缩/解压核心类
   └─ HuffmanNode.java       # 哈夫曼树节点pojo类
```

## 程序流程

1. 读取`resources/data.txt`中的原始文本；
2. 统计文本中各字符（含标点）的出现频率；
3. 构建哈夫曼树并生成字符 - 编码映射；
4. 将原始文本编码为二进制数据，写入`output/compressed.bin`；
5. 读取压缩文件，解码并将还原文本写入`output/decoded.txt`；
6. 输出`output/codeTable.txt`记录字符、频率与编码的对应关系。

## 输出结果

### 1. 映射文件codeTable.txt 

```plaintext
的	57	0001
能	46	11011
人	44	11010
，	40	10111
智	36	10011
学	33	10010
工	30	00100
、	22	110011
是	21	110000
科	19	101010
。	18	101000
机	15	001010
算	13	000000
计	13	1111111

.....

践	1	0101000101
基	1	0110000000
导	1	0110000001
总码长：8396位
```

### 2. 压缩效果

原始文件`data.txt`大小：3,453 字节

压缩文件`compressed.bin`大小：1,051 字节

## 时间复杂度

- 构建哈夫曼树：O (nlogn)（n 为不同字符数量，小顶堆操作复杂度为 O (logn)）；
- 编码 / 解码：O (m)（m 为文本长度）；

---

# Project Introduction

This project is a **Huffman coding text compression and decompression tool** implemented in Java. It constructs a Huffman tree based on character frequencies, generates Huffman coding mappings, and completes the full process of text file compression and decompression 

(data.txt <--> (compressed.bin + codeTable.txt)).

## Project Structure

```plaintext
├─ output             # Output after code execution
│  ├─ codeTable.txt   # Character - encoding mapping table
│  ├─ compressed.bin  # Compressed binary file
│  └─ decoded.txt     # Decompressed and restored text file
├─ resources
│  └─ data.txt        # Original text file to be compressed
└─ src
   ├─ Application.java       # Main program entry
   ├─ CommonUtils.java       # General utility class (file reading and writing, etc.)
   ├─ HuffmanCompressor.java # *Core class for Huffman compression/decompression
   └─ HuffmanNode.java       # Huffman tree node POJO class
```

## Program Flow

1. Read the original text from `resources/data.txt`.
2. Count the occurrence frequency of each character (including punctuation) in the text.
3. Construct the Huffman tree and generate character - encoding mappings.
4. Encode the original text into binary data and write it to `output/compressed.bin`.
5. Read the compressed file, decode it, and write the restored text to `output/decoded.txt`.
6. Output `output/codeTable.txt` to record the correspondence between characters, frequencies, and encodings.

## Output Results

### 1. Mapping File codeTable.txt

```plaintext
的	57	0001
能	46	11011
人	44	11010
，	40	10111
智	36	10011
学	33	10010
工	30	00100
、	22	110011
是	21	110000
科	19	101010
。	18	101000
机	15	001010
算	13	000000
计	13	1111111

.....

践	1	0101000101
基	1	0110000000
导	1	0110000001
Total code length: 8396 bits
```

### 2. Compression Effect

Size of the original file `data.txt`: 3,453 bytes

Size of the compressed file `compressed.bin`: 1,051 bytes

## Time Complexity

- Building the Huffman tree: O(nlogn) (n is the number of different characters, and the complexity of min - heap operations is O(logn)).
- Encoding/Decoding: O(m) (m is the length of the text).



