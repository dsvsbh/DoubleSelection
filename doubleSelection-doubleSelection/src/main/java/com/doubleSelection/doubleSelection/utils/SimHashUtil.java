package com.doubleSelection.doubleSelection.utils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class SimHashUtil {

    /**
     * 生成文本的SimHash值
     *
     * @param text 输入文本
     * @return 生成的128位SimHash值
     */
    public static String getSimHash(String text) {
        int[] vector = new int[128]; // 使用128位的哈希表示
        Map<String, Integer> wordFrequency = getWordFrequency(text); // 词频统计

        // 遍历每个单词，计算词的哈希并加权累加
        for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
            String word = entry.getKey();
            int frequency = entry.getValue();

            // 获取单词的哈希值
            BigInteger hash = new BigInteger(word.hashCode() + "");

            // 对每一位进行加权操作
            for (int i = 0; i < 128; i++) {
                if (hash.testBit(i)) {
                    vector[i] += frequency; // 权重大于0的位设为1
                } else {
                    vector[i] -= frequency; // 权重小于0的位设为0
                }
            }
        }

        // 最终生成SimHash值
        StringBuilder simHash = new StringBuilder();
        for (int i = 0; i < 128; i++) {
            if (vector[i] > 0) {
                simHash.append("1");
            } else {
                simHash.append("0");
            }
        }

        return simHash.toString();
    }

    /**
     * 计算两个SimHash值之间的汉明距离
     *
     * @param hash1 SimHash值1
     * @param hash2 SimHash值2
     * @return 汉明距离，越小表示相似度越高
     */
    public static int getHammingDistance(String hash1, String hash2) {
        int distance = 0;
        for (int i = 0; i < hash1.length(); i++) {
            if (hash1.charAt(i) != hash2.charAt(i)) {
                distance++;
            }
        }
        return distance;
    }

    /**
     * 根据汉明距离计算相似度得分
     *
     * @param hash1 SimHash值1
     * @param hash2 SimHash值2
     * @return 相似度得分，值在0到1之间，1为完全相同
     */
    public static double getSimilarityScore(String hash1, String hash2) {
        int distance = getHammingDistance(hash1, hash2);
        return 1 - (double) distance / hash1.length();
    }

    /**
     * 计算文本的词频
     *
     * @param text 输入文本
     * @return 词频表
     */
    private static Map<String, Integer> getWordFrequency(String text) {
        String[] words = text.split("\\s+");
        Map<String, Integer> wordFrequency = new HashMap<>();
        for (String word : words) {
            wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
        }
        return wordFrequency;
    }

    public static Double getSimilarity(String text1, String text2)
    {
        return getSimilarityScore(getSimHash(text1), getSimHash(text2));
    }
}
