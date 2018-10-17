package com.smack.administrator.smackstudyapplication.util;

import android.os.SystemClock;

import com.smack.administrator.smackstudyapplication.XmppConnection;
import com.smack.administrator.smackstudyapplication.dao.ChatUser;
import com.smack.administrator.smackstudyapplication.dao.CustomChatMessage;
import com.smack.administrator.smackstudyapplication.dao.MsgStatusEnum;

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
    public static CustomChatMessage createTextMessage(long conversationId,ChatUser user,String text){
        String uuid = user.getUserName()+ SystemClock.currentThreadTimeMillis();
        ChatUser currentUser = XmppConnection.getInstance().getUserInfo(XmppConnection.getInstance().getCurrentUserName());
        CustomChatMessage message = new CustomChatMessage();
        message.setText(text);
        message.setUuid(uuid);
        message.setSendUserName(XmppConnection.getInstance().getCurrentUserName());
        message.setSendJid(currentUser.getJid());
        message.setSendNickName(currentUser.getUserNick());
        message.setSendAvatar(currentUser.getAvatar());
        message.setRecieveUserName(user.getUserName());
        message.setRecieveJid(user.getJid());
        message.setRecieveNickName(user.getUserNick());
        message.setRecieveAvatar(user.getAvatar());
        message.setType("1");
        message.setMsgStatusEnum(MsgStatusEnum.sending);
        message.setConversationId(conversationId);
        message.setTime(System.currentTimeMillis());
        return message;
    }
}
