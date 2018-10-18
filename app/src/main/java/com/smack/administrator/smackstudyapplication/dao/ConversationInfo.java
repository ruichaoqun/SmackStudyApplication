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
    private long date;                      // 最后信息发送时间
    private int unReadMessageNumber;    // 未读消息数

    @ToMany(referencedJoinProperty = "conversationId")
    private List<CustomChatMessage> messages;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 452604254)
    private transient ConversationInfoDao myDao;

    @Generated(hash = 522182215)
    public ConversationInfo(Long id, String userName, String chatUserName,
            String chatJid, String lastMessage, String type, long date,
            int unReadMessageNumber) {
        this.id = id;
        this.userName = userName;
        this.chatUserName = chatUserName;
        this.chatJid = chatJid;
        this.lastMessage = lastMessage;
        this.type = type;
        this.date = date;
        this.unReadMessageNumber = unReadMessageNumber;
    }

    @Generated(hash = 837114692)
    public ConversationInfo() {
    }

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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getUnReadMessageNumber() {
        return unReadMessageNumber;
    }

    public void setUnReadMessageNumber(int unReadMessageNumber) {
        this.unReadMessageNumber = unReadMessageNumber;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 517503351)
    public List<CustomChatMessage> getMessages() {
        if (messages == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CustomChatMessageDao targetDao = daoSession.getCustomChatMessageDao();
            List<CustomChatMessage> messagesNew = targetDao
                    ._queryConversationInfo_Messages(id);
            synchronized (this) {
                if (messages == null) {
                    messages = messagesNew;
                }
            }
        }
        return messages;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1942469556)
    public synchronized void resetMessages() {
        messages = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    public void newConversationInfo(String currentUserName, String targetUserName, String jid) {
        this.chatUserName = currentUserName;
        this.userName = targetUserName;
        this.chatJid = jid;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 160097561)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getConversationInfoDao() : null;
    }
}
