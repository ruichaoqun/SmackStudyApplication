package com.smack.administrator.smackstudyapplication;

import android.support.annotation.StringDef;

import com.smack.administrator.smackstudyapplication.dao.CustomMessage;

import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;

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
//    public static CustomMessage createMessage(EntityBareJid jid,boolean isIncoming,Message message){
//        CustomMessage msg = new CustomMessage();
////        if()
//    }




    public static String getMessageDiscription(CustomMessage message){
        switch (message.getType()){
            case MessageType.TYPE_TEXT:
                return message.getText();
            case MessageType.TYPE_IMAGE:
                return "[图片]";
            case MessageType.TYPE_VOICE:
                return "[语音]";
        }
        return "";
    }
}
