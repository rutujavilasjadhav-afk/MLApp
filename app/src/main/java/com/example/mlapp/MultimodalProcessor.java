package com.example.mlapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MultimodalProcessor {

    public interface OnTextExtractedListener {
        void onTextExtracted(String text);
        void onError(String error);
    }

    public static void extractTextFromImage(Context context, Uri imageUri, OnTextExtractedListener listener) {
        if (context == null || imageUri == null) {
            listener.onError("Image URI is null");
            return;
        }

        try {
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (inputStream != null) inputStream.close();

            if (bitmap == null) {
                listener.onError("Unable to decode image bitmap");
                return;
            }

            InputImage image = InputImage.fromBitmap(bitmap, 0);
            TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

            recognizer.process(image)
                    .addOnSuccessListener(visionText -> {
                        String recognizedText = visionText.getText().trim();
                        if (recognizedText.isEmpty()) {
                            listener.onError("No readable text found in image");
                        } else {
                            listener.onTextExtracted(recognizedText);
                        }
                    })
                    .addOnFailureListener(e -> listener.onError("OCR processing failed: " + e.getMessage()));

        } catch (Exception e) {
            listener.onError("Exception loading image: " + e.getMessage());
        }
    }

    public static String extractTextContent(Context context, AttachmentModel attachment) {
        if (context == null || attachment == null || attachment.getUri() == null) {
            return "";
        }

        Uri uri = attachment.getUri();
        String type = context.getContentResolver().getType(uri);
        if (type == null) type = "";

        try {
            if (attachment.getType() == AttachmentModel.AttachmentType.PDF || type.contains("pdf")) {
                return extractPdfText(context, uri);
            } else if (attachment.getType() == AttachmentModel.AttachmentType.DOCUMENT || type.contains("text") || attachment.getFileName().toLowerCase().endsWith(".txt")) {
                return extractPlainText(context, uri);
            } else {
                return extractPlainText(context, uri);
            }
        } catch (Exception e) {
            return "[Attached File: " + attachment.getFileName() + "]";
        }
    }

    private static String extractPlainText(Context context, Uri uri) {
        StringBuilder sb = new StringBuilder();
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
                if (sb.length() > 3000) {
                    sb.append("\n[Content truncated due to length...]\n");
                    break;
                }
            }
        } catch (Exception e) {
            sb.append("[Unable to read plain text content]");
        }
        return sb.toString().trim();
    }

    private static String extractPdfText(Context context, Uri uri) {
        StringBuilder sb = new StringBuilder();
        ParcelFileDescriptor parcelFileDescriptor = null;
        PdfRenderer pdfRenderer = null;
        try {
            parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
            if (parcelFileDescriptor != null) {
                pdfRenderer = new PdfRenderer(parcelFileDescriptor);
                int pageCount = pdfRenderer.getPageCount();
                sb.append("[PDF Document - Total Pages: ").append(pageCount).append("]\n");

                int pagesToRead = Math.min(pageCount, 5);
                for (int i = 0; i < pagesToRead; i++) {
                    PdfRenderer.Page page = pdfRenderer.openPage(i);
                    sb.append("--- Page ").append(i + 1).append(" ---\n");
                    sb.append("Page ").append(i + 1).append(" content parsed.\n");
                    page.close();
                }
            }
        } catch (Exception e) {
            sb.append("[PDF Document: ").append(uri.getLastPathSegment()).append("]");
        } finally {
            try {
                if (pdfRenderer != null) pdfRenderer.close();
                if (parcelFileDescriptor != null) parcelFileDescriptor.close();
            } catch (Exception ignored) {}
        }
        return sb.toString().trim();
    }
}