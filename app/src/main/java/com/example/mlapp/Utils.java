package com.example.mlapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static void hideKeyboard(Activity activity) {
        if (activity == null) return;
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static CharSequence formatMarkdownText(String rawText) {
        if (rawText == null || rawText.isEmpty()) return "";

        SpannableStringBuilder builder = new SpannableStringBuilder();
        String[] lines = rawText.split("\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            // Heading 1 or 2 (### or #)
            if (line.startsWith("### ") || line.startsWith("## ") || line.startsWith("# ")) {
                String cleanLine = line.replace("#", "").trim();
                int start = builder.length();
                builder.append(cleanLine);
                builder.setSpan(new StyleSpan(Typeface.BOLD), start, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new RelativeSizeSpan(1.15f), start, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                // Parse inline bold **text**
                int startPos = builder.length();
                builder.append(line);

                Pattern pattern = Pattern.compile("\\*\\*(.*?)\\*\\*");
                Matcher matcher = pattern.matcher(line);

                while (matcher.find()) {
                    int matchStart = startPos + matcher.start();
                    int matchEnd = startPos + matcher.end();
                    // We match inside the line
                }
            }

            if (i < lines.length - 1) {
                builder.append("\n");
            }
        }

        // Apply clean inline bold spans
        String currentText = builder.toString();
        Pattern boldPattern = Pattern.compile("\\*\\*(.*?)\\*\\*");
        Matcher matcher = boldPattern.matcher(currentText);

        SpannableStringBuilder finalBuilder = new SpannableStringBuilder(currentText);
        int offset = 0;

        while (matcher.find()) {
            int start = matcher.start() - offset;
            int end = matcher.end() - offset;
            String innerText = matcher.group(1);

            if (innerText != null) {
                finalBuilder.replace(start, end, innerText);
                int newEnd = start + innerText.length();
                finalBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, newEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                offset += 4; // removed 4 characters: "**" and "**"
            }
        }

        return finalBuilder;
    }
}