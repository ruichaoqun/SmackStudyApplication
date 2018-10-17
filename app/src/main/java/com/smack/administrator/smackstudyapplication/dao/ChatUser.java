package com.smack.administrator.smackstudyapplication.dao;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * <p>Description.</p>
 * <p>
 * <b>联系人</b>:
 * <table>
 * <tr>
 * <th>Date</th>
 * <th>Developer</th>
 * <th>Target</th>
 * <th>Content</th>
 * </tr>
 * <tr>
 * <td>2018-09-20 16:40</td>
 * <td>${User}</td>
 * <td>All</td>
 * <td>Created.</td>
 * </tr>
 * </table>
 */
@Entity
public class ChatUser implements Parcelable {
    @Id
    private Long id;
    //当前登录账号（用于同一手机不同账号登录区分每个账号的联系人）
    private String chatUserName;
    //用户账户名
    private String userName;
    //昵称
    private String userNick;
    //头像
    private String avatar;
    //IM jid
    private String jid;
    //会话id
    private long conversationId;

    @Generated(hash = 401321336)
    public ChatUser(Long id, String chatUserName, String userName, String userNick,
            String avatar, String jid) {
        this.id = id;
        this.chatUserName = chatUserName;
        this.userName = userName;
        this.userNick = userNick;
        this.avatar = avatar;
        this.jid = jid;
    }

    @Generated(hash = 450922767)
    public ChatUser() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChatUserName() {
        return chatUserName;
    }

    public void setChatUserName(String chatUserName) {
        this.chatUserName = chatUserName;
    }

    public long getConversationId() {
        return conversationId;
    }

    public void setConversationId(long conversationId) {
        this.conversationId = conversationId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.chatUserName);
        dest.writeString(this.userName);
        dest.writeString(this.userNick);
        dest.writeString(this.avatar);
        dest.writeString(this.jid);
        dest.writeLong(this.conversationId);
    }

    protected ChatUser(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.chatUserName = in.readString();
        this.userName = in.readString();
        this.userNick = in.readString();
        this.avatar = in.readString();
        this.jid = in.readString();
        this.conversationId = in.readLong();
    }

    public static final Parcelable.Creator<ChatUser> CREATOR = new Parcelable.Creator<ChatUser>() {
        @Override
        public ChatUser createFromParcel(Parcel source) {
            return new ChatUser(source);
        }

        @Override
        public ChatUser[] newArray(int size) {
            return new ChatUser[size];
        }
    };
}
