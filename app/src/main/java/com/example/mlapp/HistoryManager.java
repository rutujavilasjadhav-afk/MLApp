package com.example.mlapp;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryManager {

    private static final String PREF_NAME = "ml_assistant_history";
    private static final String KEY_SESSIONS = "chat_sessions";

    public static class ChatSession {
        private final String id;
        private final String title;
        private final String timestamp;
        private final List<ChatMessage> messages;

        public ChatSession(String id, String title, String timestamp, List<ChatMessage> messages) {
            this.id = id;
            this.title = title;
            this.timestamp = timestamp;
            this.messages = messages;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public List<ChatMessage> getMessages() {
            return messages;
        }
    }

    public static void saveOrUpdateSession(Context context, String sessionId, List<ChatMessage> messages) {
        if (context == null || messages == null || messages.isEmpty() || sessionId == null) return;

        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<ChatSession> sessions = getSessions(context);

        // Determine title from the first valid user message
        String title = "ML Learning Chat";
        for (ChatMessage msg : messages) {
            if (msg.getSenderType() == ChatMessage.SenderType.USER && !msg.getText().startsWith("[")) {
                title = msg.getText().trim();
                if (title.length() > 30) {
                    title = title.substring(0, 30) + "…";
                }
                break;
            }
        }

        String timestamp = Utils.getCurrentTimestamp();
        ChatSession updatedSession = new ChatSession(sessionId, title, timestamp, new ArrayList<>(messages));

        // Remove existing session if present (to avoid duplicates and move active chat to top)
        sessions.removeIf(s -> sessionId.equals(s.getId()));

        // Insert at top (index 0)
        sessions.add(0, updatedSession);

        saveSessionsToPrefs(prefs, sessions);
    }

    public static List<ChatSession> getSessions(Context context) {
        List<ChatSession> list = new ArrayList<>();
        if (context == null) return list;

        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String jsonString = prefs.getString(KEY_SESSIONS, "[]");

        try {
            JSONArray array = new JSONArray(jsonString);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String id = obj.optString("id");
                String title = obj.optString("title");
                String timestamp = obj.optString("timestamp");

                JSONArray msgArray = obj.optJSONArray("messages");
                List<ChatMessage> msgList = new ArrayList<>();
                if (msgArray != null) {
                    for (int j = 0; j < msgArray.length(); j++) {
                        JSONObject mObj = msgArray.getJSONObject(j);
                        String text = mObj.optString("text");
                        String sender = mObj.optString("sender");
                        String ts = mObj.optString("ts");

                        ChatMessage.SenderType type = ChatMessage.SenderType.BOT;
                        if ("USER".equals(sender)) type = ChatMessage.SenderType.USER;
                        else if ("TYPING".equals(sender)) continue;

                        msgList.add(new ChatMessage(text, type, ts));
                    }
                }
                list.add(new ChatSession(id, title, timestamp, msgList));
            }
        } catch (Exception ignored) {}

        return list;
    }

    public static ChatSession getSessionById(Context context, String sessionId) {
        if (context == null || sessionId == null) return null;
        for (ChatSession session : getSessions(context)) {
            if (sessionId.equals(session.getId())) {
                return session;
            }
        }
        return null;
    }

    public static void deleteSession(Context context, String sessionId) {
        if (context == null || sessionId == null) return;
        List<ChatSession> sessions = getSessions(context);
        sessions.removeIf(s -> sessionId.equals(s.getId()));
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        saveSessionsToPrefs(prefs, sessions);
    }

    public static void clearAllSessions(Context context) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    private static void saveSessionsToPrefs(SharedPreferences prefs, List<ChatSession> sessions) {
        try {
            JSONArray array = new JSONArray();
            int limit = Math.min(sessions.size(), 30); // Keep top 30 sessions
            for (int i = 0; i < limit; i++) {
                ChatSession s = sessions.get(i);
                JSONObject obj = new JSONObject();
                obj.put("id", s.getId());
                obj.put("title", s.getTitle());
                obj.put("timestamp", s.getTimestamp());

                JSONArray msgArray = new JSONArray();
                for (ChatMessage m : s.getMessages()) {
                    JSONObject mObj = new JSONObject();
                    mObj.put("text", m.getText());
                    mObj.put("sender", m.getSenderType().name());
                    mObj.put("ts", m.getTimestamp());
                    msgArray.put(mObj);
                }
                obj.put("messages", msgArray);
                array.put(obj);
            }
            prefs.edit().putString(KEY_SESSIONS, array.toString()).apply();
        } catch (Exception ignored) {}
    }
}