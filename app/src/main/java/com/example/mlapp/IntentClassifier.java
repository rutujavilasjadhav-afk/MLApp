package com.example.mlapp;

import java.util.Arrays;
import java.util.List;

public class IntentClassifier {

    public enum IntentType {
        GREETING,
        THANK_YOU,
        ACKNOWLEDGEMENT,
        GOODBYE,
        DEFINE_QUICK,
        EXPLAIN_NORMAL,
        EXPLAIN_DEEP,
        COMPARISON,
        HOW_IT_WORKS,
        ADVANTAGES_LIMITATIONS,
        EXAMPLES,
        APPLICATIONS,
        AMBIGUOUS,
        OUT_OF_DOMAIN,
        GENERAL_QUERY
    }

    private static final List<String> DEEP_PHRASES = Arrays.asList(
            "explain in detail", "deep explanation", "explain deeply",
            "detailed explanation", "give me complete information",
            "tell me everything", "step by step", "explain step by step",
            "thoroughly", "explain thoroughly", "comprehensive explanation",
            "deep dive", "in depth", "in-depth", "advantages and disadvantages",
            "advantages and limitations", "pros and cons"
    );

    private static final List<String> QUICK_PHRASES = Arrays.asList(
            "what is", "define", "meaning of", "short", "quick", "brief",
            "definition", "define in simple terms", "what do you mean by"
    );

    private static final List<String> COMPARISON_PHRASES = Arrays.asList(
            " vs ", " versus ", "difference between", "compare", "differ from",
            "how is", "different from"
    );

    private static final List<String> GREETING_PHRASES = Arrays.asList(
            "hello", "hi", "hey", "good morning", "good afternoon", "good evening",
            "namaste", "who are you", "what can you do", "help"
    );

    private static final List<String> THANK_YOU_PHRASES = Arrays.asList(
            "thank you", "thanks", "thx", "thank u", "appreciate it"
    );

    private static final List<String> ACKNOWLEDGEMENT_PHRASES = Arrays.asList(
            "okay", "ok", "got it", "cool", "alright", "great", "nice", "understood"
    );

    private static final List<String> GOODBYE_PHRASES = Arrays.asList(
            "bye", "goodbye", "see you", "cya", "exit"
    );

    public static IntentType classifyIntent(String rawQuery) {
        if (rawQuery == null || rawQuery.trim().isEmpty()) {
            return IntentType.GENERAL_QUERY;
        }

        String lower = rawQuery.toLowerCase().trim();

        // CRITICAL RULE: If the query is an actual Machine Learning / domain question,
        // NEVER classify it as a greeting or conversational message.
        if (QueryAnalyzer.isMLRelated(rawQuery) || QueryAnalyzer.findBestMatchingTopic(rawQuery) != null) {
            // Check Comparison
            for (String comp : COMPARISON_PHRASES) {
                if (lower.contains(comp)) {
                    return IntentType.COMPARISON;
                }
            }

            // Check Deep Explanation
            for (String deep : DEEP_PHRASES) {
                if (lower.contains(deep)) {
                    return IntentType.EXPLAIN_DEEP;
                }
            }

            // Check How It Works
            if (lower.contains("how does") || lower.contains("how it works") ||
                    lower.contains("working mechanism") || lower.contains("behind the scenes") ||
                    lower.contains("working of")) {
                return IntentType.HOW_IT_WORKS;
            }

            // Check Advantages/Limitations
            if (lower.contains("advantage") || lower.contains("disadvantage") ||
                    lower.contains("benefit") || lower.contains("limitation") ||
                    lower.contains("pros") || lower.contains("cons")) {
                return IntentType.ADVANTAGES_LIMITATIONS;
            }

            // Check Examples
            if (lower.contains("example") || lower.contains("give an example") ||
                    lower.contains("sample case") || lower.contains("use case")) {
                return IntentType.EXAMPLES;
            }

            // Check Applications
            if (lower.contains("application") || lower.contains("where is it used") ||
                    lower.contains("real world") || lower.contains("used in")) {
                return IntentType.APPLICATIONS;
            }

            // Check Quick Definition
            for (String q : QUICK_PHRASES) {
                if (lower.startsWith(q) || lower.contains(q)) {
                    return IntentType.DEFINE_QUICK;
                }
            }

            // Check Normal Explanation
            if (lower.contains("explain") || lower.contains("tell me about") || lower.contains("about")) {
                return IntentType.EXPLAIN_NORMAL;
            }

            return IntentType.EXPLAIN_NORMAL;
        }

        // If not an ML question, check pure conversational/social messages
        for (String t : THANK_YOU_PHRASES) {
            if (lower.equals(t) || lower.startsWith(t + " ") || lower.endsWith(" " + t)) {
                return IntentType.THANK_YOU;
            }
        }

        for (String a : ACKNOWLEDGEMENT_PHRASES) {
            if (lower.equals(a) || lower.startsWith(a + " ")) {
                return IntentType.ACKNOWLEDGEMENT;
            }
        }

        for (String gb : GOODBYE_PHRASES) {
            if (lower.equals(gb) || lower.startsWith(gb + " ")) {
                return IntentType.GOODBYE;
            }
        }

        for (String g : GREETING_PHRASES) {
            if (lower.equals(g) || lower.startsWith(g + " ")) {
                return IntentType.GREETING;
            }
        }

        // Check Comparison for non-topic queries
        for (String comp : COMPARISON_PHRASES) {
            if (lower.contains(comp)) {
                return IntentType.COMPARISON;
            }
        }

        // Check Deep Explanation
        for (String deep : DEEP_PHRASES) {
            if (lower.contains(deep)) {
                return IntentType.EXPLAIN_DEEP;
            }
        }

        return IntentType.GENERAL_QUERY;
    }

    public static ChatMessage.DepthLevel detectDepthLevel(String rawQuery) {
        String lower = rawQuery.toLowerCase().trim();
        for (String deepPhrase : DEEP_PHRASES) {
            if (lower.contains(deepPhrase)) {
                return ChatMessage.DepthLevel.DEEP;
            }
        }
        for (String quickPhrase : QUICK_PHRASES) {
            if (lower.startsWith(quickPhrase) && lower.split("\\s+").length <= 5) {
                return ChatMessage.DepthLevel.QUICK;
            }
        }
        return ChatMessage.DepthLevel.NORMAL;
    }
}