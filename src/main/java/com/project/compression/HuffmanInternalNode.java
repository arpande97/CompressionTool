package com.project.compression;

public class HuffmanInternalNode implements HuffmanNode
{
    private long weight;
    private HuffmanNode leftChild;
    private HuffmanNode rightChild;
    public HuffmanInternalNode(HuffmanNode left, HuffmanNode right, long weight)
    {
        this.leftChild = left;
        this.rightChild = right;
        this.weight = weight;
    }
    public HuffmanNode left()
    {
        return leftChild;
    }
    public HuffmanNode right()
    {
        return rightChild;
    }
    @Override
    public boolean isLeaf()
    {
        return false;
    }

    @Override
    public long weight()
    {
        return weight;
    }
}
