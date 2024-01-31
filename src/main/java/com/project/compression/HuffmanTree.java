package com.project.compression;

public class HuffmanTree implements Comparable
{
    HuffmanNode root;
    public HuffmanTree(long weight, char c)
    {
        root = new HuffmanLeafNode(weight, c);
    }
    public HuffmanTree(HuffmanNode leftChild, HuffmanNode rightChild, long weight)
    {
        root = new HuffmanInternalNode(leftChild, rightChild, weight);
    }
    public long weight()
    {
        return root.weight();
    }
    public HuffmanNode root()
    {
        return root;
    }
    @Override
    public int compareTo(Object t)
    {
        HuffmanTree that = (HuffmanTree) t;
        if (root.weight() < that.weight())
            return -1;
        else if (root.weight() == that.weight())
            return 0;
        else
            return 1;
    }
}
