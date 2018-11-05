package com.smack.administrator.smackstudyapplication.util;

import com.google.gson.Gson;
import com.lzy.imagepicker.bean.ImageItem;
import com.netease.nimlib.sdk.media.record.RecordType;
import com.smack.administrator.smackstudyapplication.chat.data.ChatType;
import com.smack.administrator.smackstudyapplication.chat.data.MessageType;
import com.smack.administrator.smackstudyapplication.XmppConnection;
import com.smack.administrator.smackstudyapplication.dao.AudioMsgAttachment;
import com.smack.administrator.smackstudyapplication.dao.ChatUser;
import com.smack.administrator.smackstudyapplication.dao.CustomChatMessage;
import com.smack.administrator.smackstudyapplication.dao.ImageMsgAttachment;
import com.smack.administrator.smackstudyapplication.dao.MsgStatusEnum;

import java.io.File;

/**
 * <p>Description.</p>
 * <p>
 * <b>Maintenance History</b>:
 * <table>
 * <tr>
 * <th>Date</th>
 * <th>Developer</th>
 * <th>Target</th>
 * <th>Content</th>
 * </tr>
 * <tr>
 * <td>2018-10-12 15:17</td>
 * <td>${User}</td>
 * <td>All</td>
 * <td>Created.</td>
 * </tr>
 * </table>
 */
public class CustomMessageBuilder {

    /**
     * 构造文本消息
     * @param user  聊天对象
     * @param text 文本
     * @return
     */
    public static CustomChatMessage createTextMessage(ChatUser user,String text){
        String uuid = user.getUserName()+ System.currentTimeMillis();
        ChatUser currentUser = XmppConnection.getInstance().getUserInfo(XmppConnection.getInstance().getCurrentUserName());
        CustomChatMessage message = new CustomChatMessage();
        message.setChatType(ChatType.P2P);
        message.setText(text);
        message.setUuid(uuid);
        message.setSendUserName(currentUser.getUserName());
        message.setSendJid(currentUser.getJid());
        message.setRecieveUserName(user.getUserName());
        message.setRecieveJid(user.getJid());
        message.setType(MessageType.TYPE_TEXT);
        message.setMsgStatusEnum(MsgStatusEnum.sending);
        message.setConversationId(user.getConversationId());
        message.setTime(System.currentTimeMillis());
        return message;
    }

    /**
     * 构建图片消息
     * @param user 聊天对象
     * @param item  图片附件
     * @return
     */
    public static CustomChatMessage createImageMessage(ChatUser user,ImageItem item){
        String uuid = user.getUserName()+ System.currentTimeMillis();
        ChatUser currentUser = XmppConnection.getInstance().getUserInfo(XmppConnection.getInstance().getCurrentUserName());
        CustomChatMessage message = new CustomChatMessage();
        message.setChatType(ChatType.P2P);
        message.setUuid(uuid);
        message.setSendUserName(currentUser.getUserName());
        message.setSendJid(currentUser.getJid());
        message.setRecieveUserName(user.getUserName());
        message.setRecieveJid(user.getJid());
        message.setType(MessageType.TYPE_IMAGE);
        message.setMsgStatusEnum(MsgStatusEnum.sending);
        message.setConversationId(user.getConversationId());
        message.setTime(System.currentTimeMillis());
        //添加图片附件
        // (注意，这时的图片并没有上传，这条消息只是存储在本地并显示为正在发送状态
        // 所以url字段是空的，当上传完毕拿到返回url去更新这条消息，再进行发送消息步骤)
        message.setMsgAattachment(new Gson().toJson(ImageMsgAttachment.createImageMsgAttachment(item)));
        return message;
    }

    public static CustomChatMessage createAudioMessage(ChatUser user,File audioFile, long audioLength, RecordType recordType){
        String uuid = user.getUserName()+ System.currentTimeMillis();
        ChatUser currentUser = XmppConnection.getInstance().getUserInfo(XmppConnection.getInstance().getCurrentUserName());
        CustomChatMessage message = new CustomChatMessage();
        message.setChatType(ChatType.P2P);
        message.setUuid(uuid);
        message.setSendUserName(currentUser.getUserName());
        message.setSendJid(currentUser.getJid());
        message.setRecieveUserName(user.getUserName());
        message.setRecieveJid(user.getJid());
        message.setType(MessageType.TYPE_VOICE);
        message.setMsgStatusEnum(MsgStatusEnum.sending);
        message.setConversationId(user.getConversationId());
        message.setTime(System.currentTimeMillis());
        //添加语音附件
        // (注意，这时的语音并没有上传，这条消息只是存储在本地并显示为正在发送状态
        // 所以url字段是空的，当上传完毕拿到返回url去更新这条消息，再进行发送消息步骤)
        AudioMsgAttachment msgAttachment = new AudioMsgAttachment();
        msgAttachment.setDuration(audioLength);
        msgAttachment.setFilePath(audioFile.getPath());
        message.setMsgAattachment(new Gson().toJson(msgAttachment));
        return message;
    }
}
