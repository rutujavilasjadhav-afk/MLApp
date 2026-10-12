package com.example.mlapp;

import java.util.List;

public class ResponseManager {

    public static class ResponseResult {
        private final String responseText;
        private final ChatMessage.DepthLevel depthLevel;

        public ResponseResult(String responseText, ChatMessage.DepthLevel depthLevel) {
            this.responseText = responseText;
            this.depthLevel = depthLevel;
        }

        public String getResponseText() {
            return responseText;
        }

        public ChatMessage.DepthLevel getDepthLevel() {
            return depthLevel;
        }
    }

    public static ResponseResult generateResponse(String userQuery, ConversationManager conversationManager) {
        if (userQuery == null || userQuery.trim().isEmpty()) {
            return new ResponseResult("Please enter a valid question about Machine Learning.", ChatMessage.DepthLevel.QUICK);
        }

        String rawQuery = userQuery.trim();

        // 1. Check Conversational / Social Intents
        IntentClassifier.IntentType intent = IntentClassifier.classifyIntent(rawQuery);

        if (intent == IntentClassifier.IntentType.GREETING) {
            String greeting = "👋 Hello! I am ML Mentor, your Machine Learning Educational Assistant. How can I help you learn Machine Learning today?";
            return new ResponseResult(greeting, ChatMessage.DepthLevel.QUICK);
        }

        if (intent == IntentClassifier.IntentType.THANK_YOU) {
            String thanks = "You're welcome! Let me know if you have any other questions about Machine Learning concepts or algorithms.";
            return new ResponseResult(thanks, ChatMessage.DepthLevel.QUICK);
        }

        if (intent == IntentClassifier.IntentType.ACKNOWLEDGEMENT) {
            String ack = "Got it! Feel free to ask about any other Machine Learning topic whenever you're ready.";
            return new ResponseResult(ack, ChatMessage.DepthLevel.QUICK);
        }

        if (intent == IntentClassifier.IntentType.GOODBYE) {
            String bye = "Goodbye! Keep exploring Machine Learning and data science.";
            return new ResponseResult(bye, ChatMessage.DepthLevel.QUICK);
        }

        // 2. Handle Follow-up Query Resolution ("it", "its", "this", "that")
        String processedQuery = rawQuery;
        if (conversationManager.isFollowUpQuery(rawQuery)) {
            processedQuery = conversationManager.resolveFollowUpQuery(rawQuery);
        }

        // 3. Check Ambiguous Query
        if (QueryAnalyzer.isAmbiguousQuery(rawQuery)) {
            String clarification = "Machine Learning uses the term **'model' / 'data'** in several contexts. Do you mean:\n\n" +
                    "1. **What is a Machine Learning model?**\n" +
                    "2. **How is a model trained and evaluated?**\n" +
                    "3. **What is the difference between AI, ML, and DL?**\n\n" +
                    "Please specify which topic you'd like to explore!";
            return new ResponseResult(clarification, ChatMessage.DepthLevel.NORMAL);
        }

        // 4. Check Explicit Comparison Query (e.g. "SVM vs KNN")
        String comparisonKey = QueryAnalyzer.detectComparisonPairKey(processedQuery);
        if (comparisonKey != null) {
            String comparisonText = KnowledgeBase.getInstance().getComparison(comparisonKey);
            if (comparisonText != null) {
                return new ResponseResult(comparisonText, ChatMessage.DepthLevel.NORMAL);
            }
        }

        // 5. Find Best Matching Topic (Strict Topic Matching)
        Topic matchedTopic = QueryAnalyzer.findBestMatchingTopic(processedQuery);

        // 6. Out-of-Domain Guardrail Check ("What is cricket?")
        if (matchedTopic == null && !QueryAnalyzer.isMLRelated(processedQuery)) {
            String outOfDomainMsg = "🤖 **ML Mentor Scope Notice**\n\n" +
                    "I am specifically designed as **ML Mentor**, a Machine Learning Educational Chatbot. I can answer questions about ML algorithms, model evaluation, dataset preprocessing, AI concepts, and practical applications (such as ML in sports/cricket, healthcare, or finance).\n\n" +
                    "I cannot answer questions on unrelated topics like general news, weather, or entertainment.\n\n" +
                    "💡 *Try asking:* \"What is Supervised Learning?\" or \"Explain Random Forest in detail\".";
            return new ResponseResult(outOfDomainMsg, ChatMessage.DepthLevel.QUICK);
        }

        // If still no topic matched but it is ML related
        if (matchedTopic == null) {
            matchedTopic = KnowledgeBase.getInstance().getTopicById("ml_definition");
        }

        // Save active topic in conversation context (Topic Lock)
        conversationManager.setLastDiscussedTopic(matchedTopic);

        // 7. Determine Response Depth Level
        ChatMessage.DepthLevel depthLevel = IntentClassifier.detectDepthLevel(rawQuery);
        if (conversationManager.isFollowUpQuery(rawQuery) && depthLevel == ChatMessage.DepthLevel.NORMAL) {
            if (rawQuery.toLowerCase().contains("deep") || rawQuery.toLowerCase().contains("detail")) {
                depthLevel = ChatMessage.DepthLevel.DEEP;
            }
        }

        conversationManager.setLastDepthLevel(depthLevel);

        // 8. Build Response Content strictly for this Topic
        String responseText = formatTopicResponse(matchedTopic, intent, depthLevel);
        return new ResponseResult(responseText, depthLevel);
    }

    private static String formatTopicResponse(Topic topic, IntentClassifier.IntentType intent, ChatMessage.DepthLevel depth) {
        StringBuilder sb = new StringBuilder();

        // Level 1: Quick Answer / Definition
        if (depth == ChatMessage.DepthLevel.QUICK || intent == IntentClassifier.IntentType.DEFINE_QUICK) {
            sb.append("📌 **").append(topic.getName()).append(" (Definition)**\n\n");
            sb.append(topic.getShortAnswer());
            return sb.toString();
        }

        // Specific Intent Overrides
        if (intent == IntentClassifier.IntentType.HOW_IT_WORKS) {
            sb.append("⚙️ **How ").append(topic.getName()).append(" Works**\n\n");
            sb.append(topic.getHowItWorks()).append("\n\n");
            if (!topic.getExamples().isEmpty()) {
                sb.append("💡 **Example:**\n").append(formatList(topic.getExamples()));
            }
            return sb.toString();
        }

        if (intent == IntentClassifier.IntentType.ADVANTAGES_LIMITATIONS) {
            sb.append("⚖️ **Advantages & Limitations of ").append(topic.getName()).append("**\n\n");
            if (!topic.getAdvantages().isEmpty()) {
                sb.append("✅ **Advantages:**\n").append(formatList(topic.getAdvantages())).append("\n\n");
            }
            if (!topic.getLimitations().isEmpty()) {
                sb.append("⚠️ **Limitations:**\n").append(formatList(topic.getLimitations()));
            }
            return sb.toString();
        }

        if (intent == IntentClassifier.IntentType.EXAMPLES) {
            sb.append("💡 **Examples of ").append(topic.getName()).append("**\n\n");
            sb.append(formatList(topic.getExamples()));
            return sb.toString();
        }

        if (intent == IntentClassifier.IntentType.APPLICATIONS) {
            sb.append("🚀 **Real-World Applications of ").append(topic.getName()).append("**\n\n");
            sb.append(formatList(topic.getApplications()));
            return sb.toString();
        }

        // Level 3: Deep Structured Explanation
        if (depth == ChatMessage.DepthLevel.DEEP || intent == IntentClassifier.IntentType.EXPLAIN_DEEP) {
            sb.append("📘 **Deep Explanation: ").append(topic.getName()).append("**\n");
            sb.append("🏷️ *Category: ").append(topic.getCategory()).append("*\n\n");

            sb.append("1️⃣ **Definition & Overview**\n");
            sb.append(topic.getNormalAnswer()).append("\n\n");

            sb.append("2️⃣ **Core Theoretical Concept**\n");
            sb.append(topic.getDeepConcept()).append("\n\n");

            sb.append("3️⃣ **Step-by-Step Working Mechanism**\n");
            sb.append(topic.getHowItWorks()).append("\n\n");

            if (!topic.getExamples().isEmpty()) {
                sb.append("4️⃣ **Real-World Examples**\n");
                sb.append(formatList(topic.getExamples())).append("\n\n");
            }

            if (!topic.getAdvantages().isEmpty()) {
                sb.append("5️⃣ **Key Advantages**\n");
                sb.append(formatList(topic.getAdvantages())).append("\n\n");
            }

            if (!topic.getLimitations().isEmpty()) {
                sb.append("6️⃣ **Limitations & Tradeoffs**\n");
                sb.append(formatList(topic.getLimitations())).append("\n\n");
            }

            if (!topic.getRelatedTopics().isEmpty()) {
                sb.append("🔗 **Related ML Topics to Explore**\n");
                sb.append(formatList(topic.getRelatedTopics()));
            }

            return sb.toString();
        }

        // Level 2: Normal Explanation (Default)
        sb.append("📖 **").append(topic.getName()).append("**\n\n");
        sb.append(topic.getNormalAnswer()).append("\n\n");

        if (!topic.getHowItWorks().isEmpty() && !topic.getId().equals("ml_types") && !topic.getId().equals("ml_definition")) {
            sb.append("⚙️ **Working Mechanism:**\n");
            sb.append(topic.getHowItWorks()).append("\n\n");
        }

        if (!topic.getExamples().isEmpty()) {
            sb.append("💡 **Simple Example:**\n");
            sb.append(formatList(topic.getExamples())).append("\n\n");
        }

        if (!topic.getAdvantages().isEmpty()) {
            sb.append("✨ **Key Advantage:**\n");
            sb.append(topic.getAdvantages().get(0));
        }

        return sb.toString();
    }

    private static String formatList(List<String> list) {
        if (list == null || list.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (String item : list) {
            if (item.startsWith("•") || item.startsWith("1.") || item.startsWith("2.")) {
                sb.append(item).append("\n");
            } else {
                sb.append("• ").append(item).append("\n");
            }
        }
        return sb.toString().trim();
    }
}