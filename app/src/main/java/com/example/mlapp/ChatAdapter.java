package com.example.mlapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_BOT = 2;
    private static final int VIEW_TYPE_TYPING = 3;

    private final List<ChatMessage> messageList;
    private final Context context;

    public ChatAdapter(Context context, List<ChatMessage> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messageList.get(position);
        switch (message.getSenderType()) {
            case USER:
                return VIEW_TYPE_USER;
            case TYPING:
                return VIEW_TYPE_TYPING;
            case BOT:
            default:
                return VIEW_TYPE_BOT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_USER) {
            View view = inflater.inflate(R.layout.item_user_message, parent, false);
            return new UserMessageViewHolder(view);
        } else if (viewType == VIEW_TYPE_TYPING) {
            View view = inflater.inflate(R.layout.item_typing_indicator, parent, false);
            return new TypingViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_bot_message, parent, false);
            return new BotMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);

        if (holder instanceof UserMessageViewHolder) {
            UserMessageViewHolder userHolder = (UserMessageViewHolder) holder;
            userHolder.tvMessage.setText(message.getText());
            userHolder.tvTimestamp.setText(message.getTimestamp());
        } else if (holder instanceof BotMessageViewHolder) {
            BotMessageViewHolder botHolder = (BotMessageViewHolder) holder;
            botHolder.tvMessage.setText(Utils.formatMarkdownText(message.getText()));
            botHolder.tvTimestamp.setText(message.getTimestamp());

            // Set Depth Badge
            ChatMessage.DepthLevel depth = message.getDepthLevel();
            if (depth == ChatMessage.DepthLevel.QUICK) {
                botHolder.tvDepthBadge.setText("Quick");
                botHolder.tvDepthBadge.setBackgroundResource(R.drawable.bg_badge);
                botHolder.tvDepthBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_quick_text));
            } else if (depth == ChatMessage.DepthLevel.DEEP) {
                botHolder.tvDepthBadge.setText("Deep Dive");
                botHolder.tvDepthBadge.setBackgroundResource(R.drawable.bg_badge);
                botHolder.tvDepthBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_deep_text));
            } else {
                botHolder.tvDepthBadge.setText("Normal");
                botHolder.tvDepthBadge.setBackgroundResource(R.drawable.bg_badge);
                botHolder.tvDepthBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_normal_text));
            }
        }
        // Typing ViewHolder does not require binding
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class UserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTimestamp;

        public UserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvUserMessage);
            tvTimestamp = itemView.findViewById(R.id.tvUserTimestamp);
        }
    }

    static class BotMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTimestamp, tvDepthBadge;

        public BotMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvBotMessage);
            tvTimestamp = itemView.findViewById(R.id.tvBotTimestamp);
            tvDepthBadge = itemView.findViewById(R.id.tvDepthBadge);
        }
    }

    static class TypingViewHolder extends RecyclerView.ViewHolder {
        public TypingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}