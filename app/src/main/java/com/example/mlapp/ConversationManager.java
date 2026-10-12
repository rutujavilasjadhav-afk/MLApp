package com.example.mlapp;

import java.util.Arrays;
import java.util.List;

public class ConversationManager {

    private Topic lastDiscussedTopic;
    private ChatMessage.DepthLevel lastDepthLevel = ChatMessage.DepthLevel.NORMAL;

    private static final List<String> PRONOUNS = Arrays.asList(
            " it", " its", " this", " that", " it?", " its?", " this?", " that?", "it ", "its "
    );

    public Topic getLastDiscussedTopic() {
        return lastDiscussedTopic;
    }

    public void setLastDiscussedTopic(Topic topic) {
        if (topic != null) {
            this.lastDiscussedTopic = topic;
        }
    }

    public ChatMessage.DepthLevel getLastDepthLevel() {
        return lastDepthLevel;
    }

    public void setLastDepthLevel(ChatMessage.DepthLevel depthLevel) {
        this.lastDepthLevel = depthLevel;
    }

    public boolean isFollowUpQuery(String rawQuery) {
        if (lastDiscussedTopic == null) return false;
        String lower = rawQuery.toLowerCase().trim();

        // Check if query contains pronouns referring to previous topic
        for (String p : PRONOUNS) {
            if (lower.contains(p)) {
                return true;
            }
        }

        // Check common follow-up phrases
        if (lower.contains("explain it") || lower.contains("how does it work") ||
                lower.contains("what are its") || lower.contains("give examples") ||
                lower.contains("deep explanation") || lower.contains("explain deeply") ||
                lower.contains("tell me more") || lower.contains("in detail") ||
                lower.contains("advantages") || lower.contains("disadvantages") ||
                lower.contains("limitations") || lower.contains("applications")) {
            return true;
        }

        return false;
    }

    public String resolveFollowUpQuery(String rawQuery) {
        if (!isFollowUpQuery(rawQuery) || lastDiscussedTopic == null) {
            return rawQuery;
        }

        String topicName = lastDiscussedTopic.getName();
        String resolved = rawQuery;

        // Replace pronouns with last discussed topic name
        resolved = resolved.replaceAll("(?i)\\b(it|its|this|that)\\b", topicName);

        // If user says "explain deeply" or "how does it work", append topic name
        if (!resolved.toLowerCase().contains(topicName.toLowerCase())) {
            resolved = resolved + " " + topicName;
        }

        return resolved;
    }

    public void clearHistory() {
        this.lastDiscussedTopic = null;
        this.lastDepthLevel = ChatMessage.DepthLevel.NORMAL;
    }
}