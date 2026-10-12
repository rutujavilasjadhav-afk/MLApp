package com.example.mlapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class NLPProcessor {

    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "a", "an", "the", "is", "are", "was", "were", "be", "been", "being",
            "have", "has", "had", "do", "does", "did", "can", "could", "would", "should",
            "may", "might", "must", "will", "shall", "to", "of", "in", "for", "on", "with",
            "about", "by", "at", "from", "up", "down", "into", "over", "after", "before",
            "between", "under", "again", "further", "then", "once", "here", "there", "when",
            "where", "why", "how", "all", "any", "both", "each", "few", "more", "most",
            "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "don", "just", "now", "d", "ll", "m", "o", "re", "ve",
            "y", "ain", "aren", "couldn", "didn", "doesn", "hadn", "hasn", "haven", "ign",
            "isn", "ma", "mightn", "mustn", "needn", "shan", "shouldn", "wasn", "weren",
            "won", "wouldn", "kya", "hai", "ko", "ke", "ka", "ki", "me", "pe", "tell",
            "give", "explain", "meaning", "definition", "can", "you", "please", "want", "know"
    ));

    public static String normalizeText(String text) {
        if (text == null) return "";
        String normalized = text.toLowerCase(Locale.ROOT);
        normalized = normalized.replaceAll("[^a-z0-9\\s-]", " ");
        return normalized.replaceAll("\\s+", " ").trim();
    }

    public static List<String> tokenize(String text) {
        String normalized = normalizeText(text);
        if (normalized.isEmpty()) return new ArrayList<>();
        return Arrays.asList(normalized.split("\\s+"));
    }

    public static List<String> removeStopWords(List<String> tokens) {
        List<String> filtered = new ArrayList<>();
        for (String token : tokens) {
            if (!STOP_WORDS.contains(token) && token.length() > 1) {
                filtered.add(token);
            }
        }
        return filtered;
    }

    public static String stemWord(String word) {
        if (word == null || word.length() <= 3) return word;
        String w = word.toLowerCase(Locale.ROOT);
        if (w.endsWith("ing")) return w.substring(0, w.length() - 3);
        if (w.endsWith("ed")) return w.substring(0, w.length() - 2);
        if (w.endsWith("es")) return w.substring(0, w.length() - 2);
        if (w.endsWith("s") && !w.endsWith("ss")) return w.substring(0, w.length() - 1);
        if (w.endsWith("ly")) return w.substring(0, w.length() - 2);
        return w;
    }

    public static List<String> extractKeywords(String query) {
        List<String> tokens = tokenize(query);
        List<String> nonStopTokens = removeStopWords(tokens);
        List<String> stemmed = new ArrayList<>();
        for (String t : nonStopTokens) {
            stemmed.add(stemWord(t));
        }
        return stemmed;
    }

    public static int levenshteinDistance(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) dp[i][0] = i;
        for (int j = 0; j <= len2; j++) dp[0][j] = j;

        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + cost
                );
            }
        }
        return dp[len1][len2];
    }

    public static double stringSimilarity(String s1, String s2) {
        if (s1 == null || s2 == null) return 0.0;
        String str1 = normalizeText(s1);
        String str2 = normalizeText(s2);
        if (str1.equals(str2)) return 1.0;
        int maxLen = Math.max(str1.length(), str2.length());
        if (maxLen == 0) return 1.0;
        int dist = levenshteinDistance(str1, str2);
        return 1.0 - ((double) dist / maxLen);
    }

    public static double jaccardSimilarity(List<String> list1, List<String> list2) {
        if (list1.isEmpty() || list2.isEmpty()) return 0.0;
        Set<String> set1 = new HashSet<>(list1);
        Set<String> set2 = new HashSet<>(list2);

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        if (union.isEmpty()) return 0.0;
        return (double) intersection.size() / union.size();
    }
}