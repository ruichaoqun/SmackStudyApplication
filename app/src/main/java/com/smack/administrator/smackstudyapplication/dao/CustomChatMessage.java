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
    @Id(autoincrement = true)
    private Long id;                    // 主键
    private String uuid;                // 消息uuid，使用当前账号+时间戳拼接
    private int chatType;               // 聊天类型 1.单聊  2.群聊
    private int type;                   // 消息类型 1.文字类型 2.语音类型 3.图片类型 4.位置 5.视频 6.文件
    private String text;                // 文本消息
    private String imagePath;           // 图片地址
    private String originalImagePath;   // 原始大图图片地址
    private String imageWidth;          // 图片宽
    private String imageHeight;         // 图片高
    private String voicePath;           // 语音地址
    private String videoPath;           // 视频地址
    private String filePath;            // 文件地址
    private float longitude;            // 经度
    private float latitude;             // 纬度
    private long time;                  // 发送日期
    private String sendUserName;        // 发送方账号
    private String recieveUserName;     // 接收方账号
    private String sendJid;             // 发送方jid
    private String recieveJid;          // 接收方jid
    private Long conversationId;        // 会话id（存储这两人对话的消息列表的id）
    @Convert(converter = MsgStatusEnum.StatusConverter.class,columnType = Integer.class )
    private MsgStatusEnum msgStatusEnum;// 消息状态

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
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

    public String getOriginalImagePath() {
        return originalImagePath;
    }

    public void setOriginalImagePath(String originalImagePath) {
        this.originalImagePath = originalImagePath;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(String imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getVoicePath() {
        return voicePath;
    }

    public void setVoicePath(String voicePath) {
        this.voicePath = voicePath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getRecieveUserName() {
        return recieveUserName;
    }

    public void setRecieveUserName(String recieveUserName) {
        this.recieveUserName = recieveUserName;
    }

    public String getSendJid() {
        return sendJid;
    }

    public void setSendJid(String sendJid) {
        this.sendJid = sendJid;
    }

    public String getRecieveJid() {
        return recieveJid;
    }

    public void setRecieveJid(String recieveJid) {
        this.recieveJid = recieveJid;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public MsgStatusEnum getMsgStatusEnum() {
        return msgStatusEnum;
    }

    public void setMsgStatusEnum(MsgStatusEnum msgStatusEnum) {
        this.msgStatusEnum = msgStatusEnum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.uuid);
        dest.writeInt(this.chatType);
        dest.writeInt(this.type);
        dest.writeString(this.text);
        dest.writeString(this.imagePath);
        dest.writeString(this.originalImagePath);
        dest.writeString(this.imageWidth);
        dest.writeString(this.imageHeight);
        dest.writeString(this.voicePath);
        dest.writeString(this.videoPath);
        dest.writeString(this.filePath);
        dest.writeFloat(this.longitude);
        dest.writeFloat(this.latitude);
        dest.writeLong(this.time);
        dest.writeString(this.sendUserName);
        dest.writeString(this.recieveUserName);
        dest.writeString(this.sendJid);
        dest.writeString(this.recieveJid);
        dest.writeValue(this.conversationId);
        dest.writeInt(this.msgStatusEnum == null ? -1 : this.msgStatusEnum.ordinal());
    }

    public CustomChatMessage() {
    }

    protected CustomChatMessage(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.uuid = in.readString();
        this.chatType = in.readInt();
        this.type = in.readInt();
        this.text = in.readString();
        this.imagePath = in.readString();
        this.originalImagePath = in.readString();
        this.imageWidth = in.readString();
        this.imageHeight = in.readString();
        this.voicePath = in.readString();
        this.videoPath = in.readString();
        this.filePath = in.readString();
        this.longitude = in.readFloat();
        this.latitude = in.readFloat();
        this.time = in.readLong();
        this.sendUserName = in.readString();
        this.recieveUserName = in.readString();
        this.sendJid = in.readString();
        this.recieveJid = in.readString();
        this.conversationId = (Long) in.readValue(Long.class.getClassLoader());
        int tmpMsgStatusEnum = in.readInt();
        this.msgStatusEnum = tmpMsgStatusEnum == -1 ? null : MsgStatusEnum.values()[tmpMsgStatusEnum];
    }

    @Generated(hash = 1291797040)
    public CustomChatMessage(Long id, String uuid, int chatType, int type, String text,
            String imagePath, String originalImagePath, String imageWidth, String imageHeight,
            String voicePath, String videoPath, String filePath, float longitude, float latitude,
            long time, String sendUserName, String recieveUserName, String sendJid, String recieveJid,
            Long conversationId, MsgStatusEnum msgStatusEnum) {
        this.id = id;
        this.uuid = uuid;
        this.chatType = chatType;
        this.type = type;
        this.text = text;
        this.imagePath = imagePath;
        this.originalImagePath = originalImagePath;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.voicePath = voicePath;
        this.videoPath = videoPath;
        this.filePath = filePath;
        this.longitude = longitude;
        this.latitude = latitude;
        this.time = time;
        this.sendUserName = sendUserName;
        this.recieveUserName = recieveUserName;
        this.sendJid = sendJid;
        this.recieveJid = recieveJid;
        this.conversationId = conversationId;
        this.msgStatusEnum = msgStatusEnum;
    }

    public static final Creator<CustomChatMessage> CREATOR = new Creator<CustomChatMessage>() {
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
