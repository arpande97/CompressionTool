package com.project.compression;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.stream.Collectors;

public class HuffmanCompression
{
    private static Charset CHARSET = StandardCharsets.UTF_8;
    public static void main(String[] args) throws IOException
    {
        checkForCorrectUsageAndArgsLength(args);
        String encodeOrDecode = args[0];
        if (encodeOrDecode.equals("encode"))
        {
            String filename = args[1];
            Map<Character, Long> frequencyMap = logFrequency(filename);
            HuffmanTree huffmanTree = buildHuffmanTree(frequencyMap);
            Map<Character, String> encodedCharacters = encodeCharacters(huffmanTree);
            String encodedBytes = encodeFile(encodedCharacters, filename);
            createEncodedFile(encodedBytes, encodedCharacters);
        }
        else
        {
            Map<Character, String> characterEncodings = extractCharacterEncodings();
            createDecodedFile(characterEncodings);
        }

    }

    private static void checkForCorrectUsageAndArgsLength(String[] args)
    {
        String encodeOrDecode = args.length > 0 ? args[0] : "";
        boolean isUsageCorrect =
                (encodeOrDecode.equals("encode") && args.length == 2)
                || (encodeOrDecode.equals("decode") && args.length == 1);
        if (!isUsageCorrect)
        {
            System.out.println("Incorrect usage.");
            System.out.println("Usage 1): mvn exec:java " +
                    "-Dexec.mainClass=\"com.project.compression.HuffmanCompression\" " +
                    "-Dexec.args=\"encode <filename>\"\n");
            System.out.println("Usage 1): mvn exec:java " +
                    "-Dexec.mainClass=\"com.project.compression.HuffmanCompression\" " +
                    "-Dexec.args=\"decode\"\n");
            System.exit(1);
        }
    }

    private static void createDecodedFile(Map<Character, String> encodedCharacters)
            throws IOException
    {
        StringBuilder sb = new StringBuilder();
        Path path = Paths.get("compressed.txt");
        byte[] arr = Files.readAllBytes(path);
        for (int i = 0; i < arr.length - 1; i++)
        {
            sb.append(calculateBitString(arr[i]));
        }
        String encodedStringWithPadding = String.valueOf(sb);

        int padLength = arr[arr.length - 1];
        String encodedString =
                encodedStringWithPadding.substring(0, encodedStringWithPadding.length() - padLength);
        String decodedString = decodeCharacters(encodedString, encodedCharacters);
        BufferedWriter writer = new BufferedWriter(new FileWriter("decoded.txt", CHARSET));
        writer.write(decodedString);
        writer.close();
    }

    private static String decodeCharacters(String encodedString, Map<Character, String> encodedCharacters)
    {
        Map<String, Character> reverseEncoding = new HashMap<>();
        encodedCharacters.forEach((k, v) -> {
            reverseEncoding.put(v, k);
        });
        StringBuilder decodedString = new StringBuilder();
        int length = encodedString.length();
        int ptr = 0;
        StringBuilder local = new StringBuilder();
        while (ptr < length)
        {
            local.append(encodedString.charAt(ptr));
            if (reverseEncoding.containsKey(String.valueOf(local)))
            {
                decodedString.append(reverseEncoding.get(String.valueOf(local)));
                local.setLength(0);
            }
            ptr++;

        }
        return String.valueOf(decodedString);
    }

    private static StringBuilder calculateBitString(byte by)
    {
        StringBuilder sb = new StringBuilder();
        sb.append((0b01000000 & by) >>> 6);
        sb.append((0b00100000 & by) >>> 5);
        sb.append((0b00010000 & by) >>> 4);
        sb.append((0b00001000 & by) >>> 3);

        sb.append((0b00000100 & by) >>> 2);
        sb.append((0b00000010 & by) >>> 1);
        sb.append(0b00000001 & by);
        return sb;
    }

    private static Map<Character, String> extractCharacterEncodings() throws IOException
    {
        Properties propsFromFile = new Properties();
        try (BufferedReader reader = new BufferedReader(new FileReader("encodings.txt", CHARSET)))
        {
            propsFromFile.load(reader);
        }
        Map<Character, String> characterEncodings =
                propsFromFile.stringPropertyNames()
                        .stream()
                        .collect(Collectors.toMap(key -> key.charAt(0),
                                propsFromFile::getProperty));
        return characterEncodings;
    }

    private static void createEncodedFile(String encodedBytes, Map<Character, String> encodedCharacters)
            throws IOException
    {

        Properties props = new Properties();
        Map<String, String> stringEncoded = createStringEncodedMap(encodedCharacters);
        props.putAll(stringEncoded);

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter("encodings.txt", CHARSET)))
        {
            props.store(writer, null);
        }
        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter("compressed.txt", CHARSET)))
        {
            writer.write(encodedBytes);
        }

    }

    private static Map<String, String> createStringEncodedMap(
            Map<Character, String> encodedCharacters)
    {
        Map<String, String> res = new HashMap<>();
        encodedCharacters.forEach((k, v) -> {
            res.put(String.valueOf(k), v);
        });
        return res;
    }

    private static String encodeFile(Map<Character, String> encodedCharacters, String filename)
    {
        StringBuilder encodedStringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename, CHARSET)))
        {
            int character;
            while ((character = reader.read()) != -1)
            {
                char c = (char) character;
                String bitString = encodedCharacters.get(c);
                encodedStringBuilder.append(bitString);
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        int lengthInBytes = encodedStringBuilder.length() / 7;
        if (encodedStringBuilder.length() % 7 != 0)
            lengthInBytes++;
        String encodedString = String.valueOf(encodedStringBuilder);
        byte[] encodedBytes = new byte[lengthInBytes + 1];
        int bytePointer = 0;
        int padLength = 0;
        while (bytePointer < lengthInBytes)
        {
            int start = bytePointer * 7;
            int end = Math.min(start + 7, encodedString.length());
            String str = encodedString.substring(start, end);

            if (end - start != 7)
            {
                padLength = 7 - (end - start);
                for (int j = 0; j < padLength; j++)
                    str += "0";
            }
            str = "0" + str;
            int power = 0;
            byte b = 0;
            for (int i = str.length() - 1; i >= 0; i--)
            {
                if (Integer.parseInt(str.substring(i, i + 1)) == 1)
                    b += Math.pow(2, power);
                power++;
            }
            encodedBytes[bytePointer++] = b;

        }
        encodedBytes[bytePointer] = (byte) padLength;
        String byteString = new String(encodedBytes, CHARSET);
        return byteString;

    }

    private static Map<Character, String> encodeCharacters(HuffmanTree huffmanTree)
    {
        Map<Character, String> encodedString = new HashMap<>();
        dfs(huffmanTree.root(), "", encodedString);
        return encodedString;
    }

    private static void dfs(HuffmanNode node, String s, Map<Character, String> encodedString)
    {
        if (node.isLeaf())
        {
            HuffmanLeafNode leafNode = (HuffmanLeafNode) node;
            encodedString.put(leafNode.value(), s);
            return;
        }
        HuffmanInternalNode internalNode = (HuffmanInternalNode) node;
        dfs(internalNode.left(), s + "0", encodedString);
        dfs(internalNode.right(), s + "1", encodedString);

    }


    private static HuffmanTree buildHuffmanTree(Map<Character, Long> frequencyMap)
    {
        PriorityQueue<HuffmanTree> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Long> entry : frequencyMap.entrySet())
        {
            Character character = entry.getKey();
            Long frequency = entry.getValue();
            HuffmanTree tree = new HuffmanTree(frequency, character);
            pq.add(tree);
        }
        while (pq.size() > 1)
        {
            HuffmanTree t1 = pq.poll();
            HuffmanTree t2 = pq.poll();
            HuffmanTree t3 = new HuffmanTree(t1.root(), t2.root(), t1.weight() + t2.weight());
            pq.add(t3);
        }

        return pq.element();
    }

    public static Map<Character, Long> logFrequency(String filename)
    {
        Map<Character, Long> frequency = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename, CHARSET)))
        {
            int character;

            while ((character = reader.read()) != -1)
            {
                frequency.put((char) character, frequency.getOrDefault((char) character, 0L) + 1);
            }
        }
        catch (FileNotFoundException f)
        {
            System.out.println("Invalid file: does not exist!");
            f.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return frequency;
    }
}
