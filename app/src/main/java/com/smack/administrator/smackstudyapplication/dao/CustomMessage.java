package com.smack.administrator.smackstudyapplication.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

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
 * <td>2018-09-26 16:39</td>
 * <td>${User}</td>
 * <td>All</td>
 * <td>Created.</td>
 * </tr>
 * </table>
 */
@Entity
public class CustomMessage {
    @Id
    private Long id;                    // 主键
    private String type;                // 消息类型 1.文字类型 2.语音类型 3.图片类型
    private String text;                // 文本消息
    private String imagePath;           // 图片地址
    private String filePath;            // 语音地址
    private Long sendDate;              // 发送日期
    //废弃，不用了
    private Long recieveDate;           // 接收日期
    private String sendUserName;        // 发送发username
    private String recieveUserName;     // 接收方username
    private String sendJid;             // 发送方jid
    private String recieveJid;          // 接收方jid
    private Boolean isRead;             // 是否已读（对于我接收的）
    private Boolean isSendSuccess;      // 是否发送成功（对于我发出去的）
    private Long conversationId;        // 会话id

    @Generated(hash = 735546106)
    public CustomMessage(Long id, String type, String text, String imagePath,
            String filePath, Long sendDate, Long recieveDate, String sendUserName,
            String recieveUserName, String sendJid, String recieveJid,
            Boolean isRead, Boolean isSendSuccess, Long conversationId) {
        this.id = id;
        this.type = type;
        this.text = text;
        this.imagePath = imagePath;
        this.filePath = filePath;
        this.sendDate = sendDate;
        this.recieveDate = recieveDate;
        this.sendUserName = sendUserName;
        this.recieveUserName = recieveUserName;
        this.sendJid = sendJid;
        this.recieveJid = recieveJid;
        this.isRead = isRead;
        this.isSendSuccess = isSendSuccess;
        this.conversationId = conversationId;
    }

    @Generated(hash = 1519488932)
    public CustomMessage() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSendDate() {
        return this.sendDate;
    }

    public void setSendDate(Long sendDate) {
        this.sendDate = sendDate;
    }

    @Deprecated
    public Long getRecieveDate() {
        return this.recieveDate;
    }

    @Deprecated
    public void setRecieveDate(Long recieveDate) {
        this.recieveDate = recieveDate;
    }

    public String getSendUserName() {
        return this.sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getRecieveUserName() {
        return this.recieveUserName;
    }

    public void setRecieveUserName(String recieveUserName) {
        this.recieveUserName = recieveUserName;
    }

    public String getSendJid() {
        return this.sendJid;
    }

    public void setSendJid(String sendJid) {
        this.sendJid = sendJid;
    }

    public String getRecieveJid() {
        return this.recieveJid;
    }

    public void setRecieveJid(String recieveJid) {
        this.recieveJid = recieveJid;
    }

    public Boolean getIsRead() {
        return this.isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Boolean getIsSendSuccess() {
        return this.isSendSuccess;
    }

    public void setIsSendSuccess(Boolean isSendSuccess) {
        this.isSendSuccess = isSendSuccess;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public Boolean getSendSuccess() {
        return isSendSuccess;
    }

    public void setSendSuccess(Boolean sendSuccess) {
        isSendSuccess = sendSuccess;
    }

    public Long getConversationId() {
        return this.conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }
}
