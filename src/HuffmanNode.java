/**
 * 哈夫曼树节点pojo
 */
public class HuffmanNode implements Comparable<HuffmanNode> {
    private final char ch;
    private final int freq;
    private HuffmanNode left;    // 左子节点
    private HuffmanNode right;   // 右子节点

    // 叶子节点（有字符）构造器
    public HuffmanNode(char ch, int freq) {
        this.ch = ch;
        this.freq = freq;
    }

    // 非叶子节点（无字符）构造器
    public HuffmanNode(int freq, HuffmanNode left, HuffmanNode right) {
        this.ch = '\0';
        this.freq = freq;
        this.left = left;
        this.right = right;
    }


    public char getCh() { return ch; }
    public int getFreq() { return freq; }
    public HuffmanNode getLeft() { return left; }
    public HuffmanNode getRight() { return right; }
    public boolean isLeaf() { return left == null && right == null; }

    // 对象的自然排序规则：频率升序（this，other）
    @Override
    public int compareTo(HuffmanNode other) {
        return this.freq - other.freq;
    }
}