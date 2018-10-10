package com.smack.administrator.smackstudyapplication.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.DaoException;

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
 * <td>2018-09-27 09:48</td>
 * <td>${User}</td>
 * <td>All</td>
 * <td>Created.</td>
 * </tr>
 * </table>
 */
@Entity
public class ConversationInfo {
    @Id
    private Long id;                        // 主键
    private String userName;                // 用户聊天账号 （手机号）
    private String chatUserName;            // 对方账号
    private String chatJid;                 // 对方Jid
    private String lastMessage;             // 最后信息
    private String type;                    // 最后信息类型
    private Long date;                      // 最后信息发送时间
    private Integer unReadMessageNumber;    // 未读消息数

    @ToMany(referencedJoinProperty = "conversationId")
    private List<CustomChatMessage> messages;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getChatUserName() {
        return chatUserName;
    }

    public void setChatUserName(String chatUserName) {
        this.chatUserName = chatUserName;
    }

    public String getChatJid() {
        return chatJid;
    }

    public void setChatJid(String chatJid) {
        this.chatJid = chatJid;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Integer getUnReadMessageNumber() {
        return unReadMessageNumber;
    }

    public void setUnReadMessageNumber(Integer unReadMessageNumber) {
        this.unReadMessageNumber = unReadMessageNumber;
    }

    public List<CustomChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<CustomChatMessage> messages) {
        this.messages = messages;
    }
}
