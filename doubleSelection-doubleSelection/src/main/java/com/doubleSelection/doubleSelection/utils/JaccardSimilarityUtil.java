package com.doubleSelection.doubleSelection.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JaccardSimilarityUtil {
    public static Set<Character> splitTextIntoWords(String text) {
        char[] charArray = text.toCharArray();
        HashSet<Character> result = new HashSet<>();
        for (char c : charArray) {
            result.add(c);
        }
        return result;
    }

    // 计算Jaccard相似度
    public static double jaccardSimilarity(String text1, String text2) {
        Set<Character> set1 = splitTextIntoWords(text1);
        Set<Character> set2 = splitTextIntoWords(text2);

        Set<Character> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<Character> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / (double)union.size();
    }
}
