package com.project.compression;

public class HuffmanLeafNode implements HuffmanNode
{
    private long weight;
    char character;
    public HuffmanLeafNode(long weight, char c)
    {
        this.weight = weight;
        this.character = c;
    }
    public char value()
    {
        return character;
    }
    @Override
    public boolean isLeaf()
    {
        return true;
    }

    @Override
    public long weight()
    {
        return weight;
    }
}
