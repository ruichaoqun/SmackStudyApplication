package com.smack.administrator.smackstudyapplication.chat.audio;

public interface Playable {
    long getDuration();

    String getPath();

    boolean isAudioEqual(Playable audio);
}