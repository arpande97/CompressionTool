package com.project.compression;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Map;
import java.util.Optional;

public class TestHuffmanCompression
{
    HuffmanCompression huffmanCompression = new HuffmanCompression();
    @Test
    public void testLogFrequency()
    {
        Map<Character, Long> frequencyMap =
                huffmanCompression.logFrequency("./src/test/resources/test.txt");

        assertFalse(frequencyMap.isEmpty());
        assertEquals(1, (long) frequencyMap.get('T'));
        assertEquals(1, (long) frequencyMap.get('h'));
        assertEquals(4, (long) frequencyMap.get('i'));
        assertEquals(3, (long) frequencyMap.get('s'));
        assertEquals(1, (long) frequencyMap.get('a'));
        assertEquals(2, (long) frequencyMap.get('t'));
        assertEquals(2, (long) frequencyMap.get('e'));
        assertEquals(1, (long) frequencyMap.get('n'));
        assertEquals(1, (long) frequencyMap.get('g'));
        assertEquals(1, (long) frequencyMap.get('f'));
        assertEquals(1, (long) frequencyMap.get('l'));
        assertEquals(3, (long) frequencyMap.get('!'));

        //T-1
        //h-1
        //i-4
        //s-3
        //a-1
        //t-2
        //e-2
        //n-1
        //g-1
        //f-1
        //l-1
        //!-3
    }
}
