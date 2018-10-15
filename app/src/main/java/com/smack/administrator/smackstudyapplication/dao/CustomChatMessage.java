package com.smack.administrator.smackstudyapplication.dao;

import android.os.Parcel;
import android.os.Parcelable;

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
public class CustomChatMessage implements Parcelable {
    @Id
    private Long id;                    // 主键
    private String uuid;                // 消息uuid，使用当前账号+时间戳拼接
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



    @Generated(hash = 181135369)
    public CustomChatMessage(Long id, String uuid, String type, String text, String imagePath,
            String filePath, Long time, Boolean isNeedShowTime, String sendUserName,
            String recieveUserName, String sendJid, String recieveJid, String sendNickName,
            String recieveNickName, String sendAvatar, String recieveAvatar, Boolean isRead,
            Boolean isSendSuccess, Long conversationId, MsgStatusEnum msgStatusEnum) {
        this.id = id;
        this.uuid = uuid;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.uuid);
        dest.writeString(this.type);
        dest.writeString(this.text);
        dest.writeString(this.imagePath);
        dest.writeString(this.filePath);
        dest.writeValue(this.time);
        dest.writeValue(this.isNeedShowTime);
        dest.writeString(this.sendUserName);
        dest.writeString(this.recieveUserName);
        dest.writeString(this.sendJid);
        dest.writeString(this.recieveJid);
        dest.writeString(this.sendNickName);
        dest.writeString(this.recieveNickName);
        dest.writeString(this.sendAvatar);
        dest.writeString(this.recieveAvatar);
        dest.writeValue(this.isRead);
        dest.writeValue(this.isSendSuccess);
        dest.writeValue(this.conversationId);
        dest.writeInt(this.msgStatusEnum == null ? -1 : this.msgStatusEnum.ordinal());
    }

    protected CustomChatMessage(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.uuid = in.readString();
        this.type = in.readString();
        this.text = in.readString();
        this.imagePath = in.readString();
        this.filePath = in.readString();
        this.time = (Long) in.readValue(Long.class.getClassLoader());
        this.isNeedShowTime = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.sendUserName = in.readString();
        this.recieveUserName = in.readString();
        this.sendJid = in.readString();
        this.recieveJid = in.readString();
        this.sendNickName = in.readString();
        this.recieveNickName = in.readString();
        this.sendAvatar = in.readString();
        this.recieveAvatar = in.readString();
        this.isRead = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isSendSuccess = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.conversationId = (Long) in.readValue(Long.class.getClassLoader());
        int tmpMsgStatusEnum = in.readInt();
        this.msgStatusEnum = tmpMsgStatusEnum == -1 ? null : MsgStatusEnum.values()[tmpMsgStatusEnum];
    }

    public static final Parcelable.Creator<CustomChatMessage> CREATOR = new Parcelable.Creator<CustomChatMessage>() {
        @Override
        public CustomChatMessage createFromParcel(Parcel source) {
            return new CustomChatMessage(source);
        }

        @Override
        public CustomChatMessage[] newArray(int size) {
            return new CustomChatMessage[size];
        }
    };
}
