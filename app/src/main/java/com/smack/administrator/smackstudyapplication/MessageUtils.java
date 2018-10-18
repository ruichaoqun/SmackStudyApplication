package com.smack.administrator.smackstudyapplication;

import com.smack.administrator.smackstudyapplication.chat.data.MessageType;
import com.smack.administrator.smackstudyapplication.dao.CustomChatMessage;

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
 * <td>2018-09-27 17:19</td>
 * <td>${User}</td>
 * <td>All</td>
 * <td>Created.</td>
 * </tr>
 * </table>
 */
public class MessageUtils {
//    public static CustomChatMessage createMessage(EntityBareJid jid,boolean isIncoming,Message message){
//        CustomChatMessage msg = new CustomChatMessage();
////        if()
//    }




    public static String getMessageDiscription(CustomChatMessage message){
        switch (message.getType()){
            case MessageType.TYPE_TEXT:
                return message.getText();
            case MessageType.TYPE_IMAGE:
                return "[图片]";
            case MessageType.TYPE_VOICE:
                return "[语音]";
            case MessageType.TYPE_LOCATION:
                return "[位置]";
            case MessageType.TYPE_VEDIO:
                return "[视频]";
            case MessageType.TYPE_FILE:
                return "[文件]";
        }
        return "";
    }
}
