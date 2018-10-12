package com.smack.administrator.smackstudyapplication.chat.actions;

import com.smack.administrator.smackstudyapplication.R;
import com.smack.administrator.smackstudyapplication.dao.CustomChatMessage;

import java.io.File;

/**
 * Created by hzxuwen on 2015/6/12.
 */
public class ImageAction extends PickImageAction {

    public ImageAction() {
        super(R.drawable.xmpp_message_plus_photo_selector, R.string.input_panel_photo, true);
    }

    @Override
    protected void onPicked(File file) {
        CustomChatMessage message = null;
//        message = MessageBuilder.createImageMessage(getAccount(), getSessionType(), file, file.getName());
        sendMessage(message);
    }
}

