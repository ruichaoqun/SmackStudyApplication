package com.smack.administrator.smackstudyapplication.dao;

import org.greenrobot.greendao.annotation.Convert;
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
public class CustomChatMessage {
    @Id
    private Long id;                    // 主键
    private String type;                // 消息类型 1.文字类型 2.语音类型 3.图片类型
    private String text;                // 文本消息
    private String imagePath;           // 图片地址
    private String filePath;            // 语音地址
    private Long time;                  // 发送日期
    private Boolean isNeedShowTime;     // 是否需要显示时间
    private String sendUserName;        // 发送发username
    private String recieveUserName;     // 接收方username
    private String sendJid;             // 发送方jid
    private String recieveJid;          // 接收方jid
    private String sendNickName;        // 发送方昵称
    private String recieveNickName;     // 接收方昵称
    private String sendAvatar;          // 发送方头像
    private String recieveAvatar;       // 接收方头像
    private Boolean isRead;             // 是否已读（对于我接收的）
    private Boolean isSendSuccess;      // 是否发送成功（对于我发出去的）
    private Long conversationId;        // 会话id
    
    @Convert(converter = MsgStatusEnum.StatusConverter.class,columnType = Integer.class )
    private MsgStatusEnum msgStatusEnum;



    @Generated(hash = 1399612868)
    public CustomChatMessage(Long id, String type, String text, String imagePath,
            String filePath, Long time, Boolean isNeedShowTime, String sendUserName,
            String recieveUserName, String sendJid, String recieveJid, String sendNickName,
            String recieveNickName, String sendAvatar, String recieveAvatar, Boolean isRead,
            Boolean isSendSuccess, Long conversationId, MsgStatusEnum msgStatusEnum) {
        this.id = id;
        this.type = type;
        this.text = text;
        this.imagePath = imagePath;
        this.filePath = filePath;
        this.time = time;
        this.isNeedShowTime = isNeedShowTime;
        this.sendUserName = sendUserName;
        this.recieveUserName = recieveUserName;
        this.sendJid = sendJid;
        this.recieveJid = recieveJid;
        this.sendNickName = sendNickName;
        this.recieveNickName = recieveNickName;
        this.sendAvatar = sendAvatar;
        this.recieveAvatar = recieveAvatar;
        this.isRead = isRead;
        this.isSendSuccess = isSendSuccess;
        this.conversationId = conversationId;
        this.msgStatusEnum = msgStatusEnum;
    }

    @Generated(hash = 1572996633)
    public CustomChatMessage() {
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

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
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

    public String getSendNickName() {
        return sendNickName;
    }

    public void setSendNickName(String sendNickName) {
        this.sendNickName = sendNickName;
    }

    public String getRecieveNickName() {
        return recieveNickName;
    }

    public void setRecieveNickName(String recieveNickName) {
        this.recieveNickName = recieveNickName;
    }

    public String getSendAvatar() {
        return sendAvatar;
    }

    public void setSendAvatar(String sendAvatar) {
        this.sendAvatar = sendAvatar;
    }

    public String getRecieveAvatar() {
        return recieveAvatar;
    }

    public void setRecieveAvatar(String recieveAvatar) {
        this.recieveAvatar = recieveAvatar;
    }

    public MsgStatusEnum getMsgStatusEnum() {
        return this.msgStatusEnum;
    }

    public void setMsgStatusEnum(MsgStatusEnum msgStatusEnum) {
        this.msgStatusEnum = msgStatusEnum;
    }

    public Boolean getNeedShowTime() {
        return isNeedShowTime;
    }

    public void setNeedShowTime(Boolean needShowTime) {
        isNeedShowTime = needShowTime;
    }

    public Boolean getIsNeedShowTime() {
        return this.isNeedShowTime;
    }

    public void setIsNeedShowTime(Boolean isNeedShowTime) {
        this.isNeedShowTime = isNeedShowTime;
    }
}
