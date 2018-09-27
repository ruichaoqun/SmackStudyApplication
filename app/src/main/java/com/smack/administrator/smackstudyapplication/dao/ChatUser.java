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
 * <td>2018-09-20 16:40</td>
 * <td>${User}</td>
 * <td>All</td>
 * <td>Created.</td>
 * </tr>
 * </table>
 */
@Entity
public class ChatUser  {
    @Id
    private Long id;
    //用户账户名
    private String userName;
    //昵称
    private String userNick;
    //头像
    private String avatar;
    //IM jid
    private String jid;

    @Generated(hash = 1126743495)
    public ChatUser(Long id, String userName, String userNick, String avatar,
            String jid) {
        this.id = id;
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
}
