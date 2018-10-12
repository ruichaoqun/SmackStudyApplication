package com.smack.administrator.smackstudyapplication.chat.emoji;

public interface IEmoticonSelectedListener {
    void onEmojiSelected(String key);

    void onStickerSelected(String categoryName, String stickerName);
}
