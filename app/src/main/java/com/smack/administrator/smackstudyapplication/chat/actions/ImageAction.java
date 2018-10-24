package com.smack.administrator.smackstudyapplication.chat.actions;

import com.google.gson.Gson;
import com.lzy.imagepicker.bean.ImageItem;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.smack.administrator.smackstudyapplication.R;
import com.smack.administrator.smackstudyapplication.XmppConnection;
import com.smack.administrator.smackstudyapplication.dao.CustomChatMessage;
import com.smack.administrator.smackstudyapplication.dao.ImageMsgAttachment;
import com.smack.administrator.smackstudyapplication.util.CustomMessageBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hzxuwen on 2015/6/12.
 */
public class ImageAction extends PickImageAction {

    public ImageAction() {
        super(R.drawable.xmpp_message_plus_photo_selector, R.string.input_panel_photo, true);
    }

    @Override
    protected void onPicked(ImageItem item) {
        final CustomChatMessage message = CustomMessageBuilder.createImageMessage(getChatUser(),item);
        //1.存储到本地
        XmppConnection.getInstance().getChatDbManager().saveMessage(message,false);
        //2.展示在UI上,这儿只是展示在列表中，并不发送，发送要等到拿到图片url再发送
        justShowMessage(message);
        //3.上传图片到服务器
        //TODO 后期这儿添加上传图片功能，这儿先模拟上传
        Observable.timer(5000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        //4.上传成功后更新图片消息附件发送消息
                        ImageMsgAttachment attachment = new Gson().fromJson(message.getMsgAattachment(),ImageMsgAttachment.class);
                        attachment.setUrl("http://www.4gbizhi.com/uploads/allimg/150316/144Ha0M-0.jpg");
                        //TODO 模拟图片上传成功，开始发送消息的操作
                        sendMessage(message,false);
                    }
                });
    }
}

