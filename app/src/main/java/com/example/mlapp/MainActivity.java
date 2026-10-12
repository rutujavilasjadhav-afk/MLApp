package com.example.mlapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private RecyclerView rvChat;
    private EditText etMessage;
    private ImageButton btnSend;
    private ImageView btnOpenDrawer, btnAttach;
    private LinearLayout layoutWelcome, layoutAttachmentPreview, layoutRecentsList;
    private ImageView ivAttachmentThumbnail;
    private TextView tvAttachmentName, tvAttachmentType;

    private ChatAdapter chatAdapter;
    private List<ChatMessage> messageList;
    private ConversationManager conversationManager;

    private AttachmentModel currentAttachment;
    private Uri cameraImageUri;
    private String activeContentType = "*/*";
    private boolean isThinkingModeOn = false;

    // Active session ID tracking to prevent duplicates and support continuing existing chats
    private String activeSessionId = null;

    private ActivityResultLauncher<Uri> takePictureLauncher;
    private ActivityResultLauncher<String> getContentLauncher;
    private ActivityResultLauncher<Intent> speechResultLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> historyResultLauncher;

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupContracts();
        setupChatRecyclerView();
        setupToolbarActions();
        setupSidebarActions();
        setupInputActions();

        // Restore latest session if available upon app launch
        restoreLatestSessionOnLaunch();
    }

    private void initViews() {
        drawerLayout = findViewById(R.id.drawerLayout);
        rvChat = findViewById(R.id.rvChat);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        btnOpenDrawer = findViewById(R.id.btnOpenDrawer);
        btnAttach = findViewById(R.id.btnAttach);
        layoutWelcome = findViewById(R.id.layoutWelcome);
        layoutAttachmentPreview = findViewById(R.id.layoutAttachmentPreview);
        layoutRecentsList = findViewById(R.id.layoutRecentsList);
        ivAttachmentThumbnail = findViewById(R.id.ivAttachmentThumbnail);
        ImageView btnRemoveAttachment = findViewById(R.id.btnRemoveAttachment);
        tvAttachmentName = findViewById(R.id.tvAttachmentName);
        tvAttachmentType = findViewById(R.id.tvAttachmentType);

        messageList = new ArrayList<>();
        conversationManager = new ConversationManager();

        if (btnRemoveAttachment != null) {
            btnRemoveAttachment.setOnClickListener(v -> clearAttachment());
        }
    }

    private void setupContracts() {
        takePictureLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
            if (success && cameraImageUri != null) {
                String fileName = "Camera_Photo_" + System.currentTimeMillis() + ".jpg";
                currentAttachment = new AttachmentModel(AttachmentModel.AttachmentType.CAMERA, cameraImageUri, fileName, "Image");
                showAttachmentPreview(currentAttachment);
            }
        });

        getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                String fileName = uri.getLastPathSegment();
                if (fileName == null) fileName = "Attachment";
                AttachmentModel.AttachmentType type = AttachmentModel.AttachmentType.FILE;
                if (activeContentType.contains("image")) {
                    type = AttachmentModel.AttachmentType.GALLERY;
                } else if (activeContentType.contains("pdf")) {
                    type = AttachmentModel.AttachmentType.PDF;
                } else if (activeContentType.contains("word") || activeContentType.contains("text")) {
                    type = AttachmentModel.AttachmentType.DOCUMENT;
                }
                currentAttachment = new AttachmentModel(type, uri, fileName, activeContentType);
                showAttachmentPreview(currentAttachment);
            }
        });

        speechResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                ArrayList<String> matches = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (matches != null && !matches.isEmpty()) {
                    String spokenText = matches.get(0);
                    etMessage.setText(spokenText);
                    etMessage.setSelection(etMessage.length());
                }
            }
        });

        historyResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                String sessionId = result.getData().getStringExtra(HistoryActivity.EXTRA_SESSION_ID);
                if (sessionId != null) {
                    openChatSession(sessionId);
                }
            }
        });

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void restoreLatestSessionOnLaunch() {
        List<HistoryManager.ChatSession> sessions = HistoryManager.getSessions(this);
        if (!sessions.isEmpty()) {
            HistoryManager.ChatSession latest = sessions.get(0);
            activeSessionId = latest.getId();
            messageList.clear();
            messageList.addAll(latest.getMessages());
            chatAdapter.notifyDataSetChanged();
            layoutWelcome.setVisibility(View.GONE);
        } else {
            activeSessionId = null;
            layoutWelcome.setVisibility(View.VISIBLE);
        }
        loadRecentHistoryUI();
    }

    private void openChatSession(String sessionId) {
        HistoryManager.ChatSession session = HistoryManager.getSessionById(this, sessionId);
        if (session != null) {
            activeSessionId = session.getId();
            messageList.clear();
            messageList.addAll(session.getMessages());
            chatAdapter.notifyDataSetChanged();
            layoutWelcome.setVisibility(View.GONE);
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            loadRecentHistoryUI();
        }
    }

    private void setupChatRecyclerView() {
        chatAdapter = new ChatAdapter(this, messageList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rvChat.setLayoutManager(layoutManager);
        rvChat.setAdapter(chatAdapter);
    }

    private void setupToolbarActions() {
        btnOpenDrawer.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
    }

    private void setupSidebarActions() {
        findViewById(R.id.btnCloseSidebar).setOnClickListener(v -> drawerLayout.closeDrawer(GravityCompat.START));

        findViewById(R.id.menuNewChat).setOnClickListener(v -> startNewChat());

        View menuHistory = findViewById(R.id.menuHistory);
        if (menuHistory != null) {
            menuHistory.setOnClickListener(v -> {
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(this, HistoryActivity.class);
                historyResultLauncher.launch(intent);
            });
        }

        findViewById(R.id.menuImages).setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, ImagesActivity.class));
        });

        findViewById(R.id.menuLibrary).setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, LibraryActivity.class));
        });

        findViewById(R.id.menuTasks).setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, TasksActivity.class));
        });

        findViewById(R.id.btnAddProject).setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, ProjectsActivity.class));
        });

        findViewById(R.id.menuCode).setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, CodeWorkspaceActivity.class));
        });

        View menuClearChat = findViewById(R.id.menuClearChat);
        if (menuClearChat != null) {
            menuClearChat.setOnClickListener(v -> {
                drawerLayout.closeDrawer(GravityCompat.START);
                showClearChatConfirmationDialog();
            });
        }

        View menuMore = findViewById(R.id.menuMore);
        if (menuMore != null) {
            menuMore.setOnClickListener(v -> {
                drawerLayout.closeDrawer(GravityCompat.START);
                Toast.makeText(this, "ML Assistant Settings & About", Toast.LENGTH_SHORT).show();
            });
        }

        findViewById(R.id.btnNewRecentChat).setOnClickListener(v -> startNewChat());
    }

    private void startNewChat() {
        if (!messageList.isEmpty() && activeSessionId != null) {
            HistoryManager.saveOrUpdateSession(this, activeSessionId, messageList);
        }
        activeSessionId = null;
        messageList.clear();
        chatAdapter.notifyDataSetChanged();
        conversationManager.clearHistory();
        layoutWelcome.setVisibility(View.VISIBLE);
        drawerLayout.closeDrawer(GravityCompat.START);
        loadRecentHistoryUI();
        Toast.makeText(this, "New Chat started", Toast.LENGTH_SHORT).show();
    }

    private void loadRecentHistoryUI() {
        if (layoutRecentsList == null) return;
        List<HistoryManager.ChatSession> sessions = HistoryManager.getSessions(this);
        layoutRecentsList.removeAllViews();
        if (!sessions.isEmpty()) {
            int limit = Math.min(sessions.size(), 5);
            for (int i = 0; i < limit; i++) {
                HistoryManager.ChatSession session = sessions.get(i);
                TextView tv = new TextView(this);
                tv.setText(session.getTitle());
                tv.setTextSize(14);
                tv.setTextColor(getColor(R.color.text_dark_brown));
                tv.setPadding(0, 16, 0, 16);
                tv.setOnClickListener(v -> openChatSession(session.getId()));
                layoutRecentsList.addView(tv);
            }
        }
    }

    private void persistCurrentSession() {
        if (messageList.isEmpty()) return;
        if (activeSessionId == null) {
            activeSessionId = "session_" + System.currentTimeMillis();
        }
        HistoryManager.saveOrUpdateSession(this, activeSessionId, messageList);
        loadRecentHistoryUI();
    }

    private void setupInputActions() {
        btnSend.setOnClickListener(v -> handleUserSend());

        etMessage.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                handleUserSend();
                return true;
            }
            return false;
        });

        btnAttach.setOnClickListener(v -> showAttachmentBottomSheet());
    }

    private void showAttachmentBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_attachment, null);
        bottomSheetDialog.setContentView(sheetView);

        sheetView.findViewById(R.id.sheetCamera).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            launchCamera();
        });

        sheetView.findViewById(R.id.sheetPhotos).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            activeContentType = "image/*";
            getContentLauncher.launch("image/*");
        });

        sheetView.findViewById(R.id.sheetFiles).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            activeContentType = "application/pdf";
            getContentLauncher.launch("application/pdf");
        });

        View btnThinking = sheetView.findViewById(R.id.sheetThinking);
        if (btnThinking != null) {
            btnThinking.setOnClickListener(v -> {
                isThinkingModeOn = !isThinkingModeOn;
                Toast.makeText(this, isThinkingModeOn ? "🧠 Think harder mode ON" : "🧠 Think harder mode OFF", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });
        }

        bottomSheetDialog.show();
    }

    private void launchCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
            return;
        }

        try {
            File photoFile = createImageFile();
            cameraImageUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
            takePictureLauncher.launch(cameraImageUri);
        } catch (Exception e) {
            Toast.makeText(this, "Camera unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile("JPEG_" + timeStamp + "_", ".jpg", storageDir);
    }

    private void showAttachmentPreview(AttachmentModel attachment) {
        if (attachment == null) return;
        layoutAttachmentPreview.setVisibility(View.VISIBLE);
        tvAttachmentName.setText(attachment.getFileName());
        tvAttachmentType.setText(attachment.getType().toString());

        if (attachment.getType() == AttachmentModel.AttachmentType.CAMERA || attachment.getType() == AttachmentModel.AttachmentType.GALLERY) {
            ivAttachmentThumbnail.setImageURI(attachment.getUri());
        } else if (attachment.getType() == AttachmentModel.AttachmentType.PDF) {
            ivAttachmentThumbnail.setImageResource(android.R.drawable.ic_menu_agenda);
        } else {
            ivAttachmentThumbnail.setImageResource(android.R.drawable.ic_menu_edit);
        }
    }

    private void clearAttachment() {
        currentAttachment = null;
        layoutAttachmentPreview.setVisibility(View.GONE);
    }

    private void handleUserSend() {
        String text = etMessage.getText().toString().trim();
        if (text.isEmpty() && currentAttachment == null) return;

        if (currentAttachment != null && (currentAttachment.getType() == AttachmentModel.AttachmentType.CAMERA || currentAttachment.getType() == AttachmentModel.AttachmentType.GALLERY)) {
            Toast.makeText(this, "Analyzing image...", Toast.LENGTH_SHORT).show();
            MultimodalProcessor.extractTextFromImage(this, currentAttachment.getUri(), new MultimodalProcessor.OnTextExtractedListener() {
                @Override
                public void onTextExtracted(String extractedText) {
                    String queryToSend = text.isEmpty() ? extractedText : text + "\n\n" + extractedText;
                    if (isThinkingModeOn) {
                        queryToSend = "[Deep Thinking Mode] " + queryToSend;
                    }
                    etMessage.setText("");
                    clearAttachment();
                    Utils.hideKeyboard(MainActivity.this);
                    processAndSendMessage(queryToSend);
                }

                @Override
                public void onError(String error) {
                    String errorMsg = "Sorry, I couldn't clearly read the text in this image. Please upload a clearer image.";
                    if (error.contains("No readable text")) {
                        errorMsg = "I couldn't identify a question in the image. Please type your question.";
                    }
                    addBotErrorResponse(text.isEmpty() ? "[Image Attachment]" : text, errorMsg);
                    etMessage.setText("");
                    clearAttachment();
                }
            });
        } else {
            String queryToSend = text;
            if (currentAttachment != null) {
                String extractedText = MultimodalProcessor.extractTextContent(this, currentAttachment);
                queryToSend = text.isEmpty() ? extractedText : text + "\n\n" + extractedText;
            }
            if (isThinkingModeOn) {
                queryToSend = "[Deep Thinking Mode] " + queryToSend;
            }
            etMessage.setText("");
            clearAttachment();
            Utils.hideKeyboard(this);
            processAndSendMessage(queryToSend);
        }
    }

    private void addBotErrorResponse(String userQuery, String errorMsg) {
        if (layoutWelcome.getVisibility() == View.VISIBLE) {
            layoutWelcome.setVisibility(View.GONE);
        }
        ChatMessage userMsg = new ChatMessage(userQuery, ChatMessage.SenderType.USER, Utils.getCurrentTimestamp());
        messageList.add(userMsg);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        rvChat.scrollToPosition(messageList.size() - 1);

        ChatMessage botMsg = new ChatMessage(errorMsg, ChatMessage.SenderType.BOT, Utils.getCurrentTimestamp(), ChatMessage.DepthLevel.QUICK);
        messageList.add(botMsg);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        rvChat.scrollToPosition(messageList.size() - 1);

        persistCurrentSession();
    }

    private void processAndSendMessage(String userQuery) {
        if (layoutWelcome.getVisibility() == View.VISIBLE) {
            layoutWelcome.setVisibility(View.GONE);
        }

        ChatMessage userMsg = new ChatMessage(userQuery, ChatMessage.SenderType.USER, Utils.getCurrentTimestamp());
        messageList.add(userMsg);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        rvChat.scrollToPosition(messageList.size() - 1);

        ChatMessage typingMsg = new ChatMessage("", ChatMessage.SenderType.TYPING, "");
        messageList.add(typingMsg);
        int typingIndex = messageList.size() - 1;
        chatAdapter.notifyItemInserted(typingIndex);
        rvChat.scrollToPosition(typingIndex);

        persistCurrentSession();

        handler.postDelayed(() -> {
            if (!messageList.isEmpty() && messageList.get(messageList.size() - 1).getSenderType() == ChatMessage.SenderType.TYPING) {
                messageList.remove(messageList.size() - 1);
                chatAdapter.notifyItemRemoved(messageList.size());
            }

            ResponseManager.ResponseResult result = ResponseManager.generateResponse(userQuery, conversationManager);

            ChatMessage botMsg = new ChatMessage(
                    result.getResponseText(),
                    ChatMessage.SenderType.BOT,
                    Utils.getCurrentTimestamp(),
                    result.getDepthLevel()
            );
            messageList.add(botMsg);
            chatAdapter.notifyItemInserted(messageList.size() - 1);
            rvChat.scrollToPosition(messageList.size() - 1);

            persistCurrentSession();

        }, 650);
    }

    private void showClearChatConfirmationDialog() {
        if (messageList.isEmpty()) {
            Toast.makeText(this, "Chat is already empty", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_clear_title)
                .setMessage(R.string.confirm_clear_msg)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    if (!messageList.isEmpty() && activeSessionId != null) {
                        HistoryManager.deleteSession(this, activeSessionId);
                    }
                    activeSessionId = null;
                    messageList.clear();
                    chatAdapter.notifyDataSetChanged();
                    conversationManager.clearHistory();
                    layoutWelcome.setVisibility(View.VISIBLE);
                    loadRecentHistoryUI();
                    Toast.makeText(this, "Chat cleared", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}