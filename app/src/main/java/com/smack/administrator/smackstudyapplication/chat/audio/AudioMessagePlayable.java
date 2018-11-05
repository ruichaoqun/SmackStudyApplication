package com.smack.administrator.smackstudyapplication.chat.audio;

import com.google.gson.Gson;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.smack.administrator.smackstudyapplication.dao.AudioMsgAttachment;
import com.smack.administrator.smackstudyapplication.dao.CustomChatMessage;

public class AudioMessagePlayable implements Playable {

    private CustomChatMessage message;
    private AudioMsgAttachment msgAttachment;

    public CustomChatMessage getMessage() {
        return message;
    }

    public AudioMessagePlayable(CustomChatMessage playableMessage) {
        this.message = playableMessage;
        msgAttachment = new Gson().fromJson(message.getMsgAattachment(),AudioMsgAttachment.class);
    }

    @Override
    public long getDuration() {
        return msgAttachment.getDuration();
    }

    @Override
    public String getPath() {
        return msgAttachment.getFilePath();
    }

    @Override
    public boolean isAudioEqual(Playable audio) {
        if (AudioMessagePlayable.class.isInstance(audio)) {
            return message.getUuid() == (((AudioMessagePlayable) audio).getMessage().getUuid());
        } else {
            return false;
        }
    }
}
