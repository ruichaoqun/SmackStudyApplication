package com.smack.administrator.smackstudyapplication.dao;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * <p>消息体</p>
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
 * <td>Rui Chaoqun</td>
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
    private long time;                  // 发送日期
    private String sendUserName;        // 发送方账号
    private String recieveUserName;     // 接收方账号
    private String sendJid;             // 发送方jid
    private String recieveJid;          // 接收方jid
    private Long conversationId;        // 会话id（存储这两人对话的消息列表的id）
    private String msgAattachment;      // 消息附件
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

    public String getMsgAattachment() {
        return msgAattachment;
    }

    public void setMsgAattachment(String msgAattachment) {
        this.msgAattachment = msgAattachment;
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
        dest.writeLong(this.time);
        dest.writeString(this.sendUserName);
        dest.writeString(this.recieveUserName);
        dest.writeString(this.sendJid);
        dest.writeString(this.recieveJid);
        dest.writeValue(this.conversationId);
        dest.writeString(this.msgAattachment);
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
        this.time = in.readLong();
        this.sendUserName = in.readString();
        this.recieveUserName = in.readString();
        this.sendJid = in.readString();
        this.recieveJid = in.readString();
        this.conversationId = (Long) in.readValue(Long.class.getClassLoader());
        this.msgAattachment = in.readString();
        int tmpMsgStatusEnum = in.readInt();
        this.msgStatusEnum = tmpMsgStatusEnum == -1 ? null : MsgStatusEnum.values()[tmpMsgStatusEnum];
    }

    @Generated(hash = 1851558853)
    public CustomChatMessage(Long id, String uuid, int chatType, int type, String text, long time,
            String sendUserName, String recieveUserName, String sendJid, String recieveJid,
            Long conversationId, String msgAattachment, MsgStatusEnum msgStatusEnum) {
        this.id = id;
        this.uuid = uuid;
        this.chatType = chatType;
        this.type = type;
        this.text = text;
        this.time = time;
        this.sendUserName = sendUserName;
        this.recieveUserName = recieveUserName;
        this.sendJid = sendJid;
        this.recieveJid = recieveJid;
        this.conversationId = conversationId;
        this.msgAattachment = msgAattachment;
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
