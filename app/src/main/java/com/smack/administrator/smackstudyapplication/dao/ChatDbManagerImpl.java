package com.smack.administrator.smackstudyapplication.dao;

import android.text.TextUtils;

import com.smack.administrator.smackstudyapplication.App;
import com.smack.administrator.smackstudyapplication.MessageUtils;

import org.jxmpp.jid.EntityBareJid;

import java.util.List;

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
 * <td>2018-09-27 09:46</td>
 * <td>${User}</td>
 * <td>All</td>
 * <td>Created.</td>
 * </tr>
 * </table>
 */
public class ChatDbManagerImpl implements ChatDbManager{
    private static ChatDbManager instance;
    private static DaoSession daoSession;

    private ChatDbManagerImpl() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(App.application,"smackIm.db",null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        daoSession = daoMaster.newSession();
    }

    public synchronized static ChatDbManager getInstance(){
        if(instance == null){
            synchronized (ChatDbManagerImpl.class){
                if(instance == null){
                    instance = new ChatDbManagerImpl();
                }
            }
        }
        return instance;
    }

    /**
     * 获取当前用户所有会话列表
     * @param userName
     */
    @Override
    public List<ConversationInfo> getConversations(String userName) {
        List<ConversationInfo> conversationInfos = daoSession.getConversationInfoDao().queryBuilder()
                .where(ConversationInfoDao.Properties.UserName.eq(userName)).build().list();
        return conversationInfos;
    }

    /**
     * 通过会话id获取消息列表
     * @param conversationId
     * @return
     */
    @Override
    public List<CustomChatMessage> getMessage(long conversationId) {
        ConversationInfo info = daoSession.getConversationInfoDao().queryBuilder()
                .where(ConversationInfoDao.Properties.Id.eq(conversationId)).unique();
        if(info == null){
            return null;
        }
        return info.getMessages();
    }

    @Override
    public Long insertOrUpdateConversation(CustomChatMessage message, String userName, EntityBareJid jid) {
        String targetUserName = TextUtils.equals(userName,message.getSendUserName())?message.getRecieveUserName():message.getSendUserName();
        ConversationInfo info = daoSession.getConversationInfoDao().queryBuilder()
                .where(ConversationInfoDao.Properties.UserName.eq(userName),ConversationInfoDao.Properties.ChatUserName.eq(targetUserName)).build().unique();
        if(info != null){
            info.setDate(message.getSendDate());
            info.setLastMessage(MessageUtils.getMessageDiscription(message));
            info.setUnReadMessageNumber(info.getUnReadMessageNumber()+1);
            daoSession.getConversationInfoDao().update(info);
            daoSession.getConversationInfoDao().detachAll();
            return info.getId();
        }else{
            info = new ConversationInfo();
            info.setUserName(userName);
            info.setChatUserName(targetUserName);
            info.setUnReadMessageNumber(1);
            info.setChatJid(jid.toString());
            info.setDate(message.getSendDate());
            info.setLastMessage(MessageUtils.getMessageDiscription(message));
            long id = daoSession.getConversationInfoDao().insert(info);
            daoSession.getConversationInfoDao().detachAll();
            return id;
        }
    }

    /**
     * 存储多条消息
     * @param messages
     * @return
     */
    @Override
    public void saveMessage(List<CustomChatMessage> messages) {
        daoSession.getCustomMessageDao().insertInTx(messages);
        daoSession.getCustomMessageDao().deleteAll();
    }

    /**
     * 存储单条消息
     * @param message
     * @return
     */
    @Override
    public Long saveMessage(CustomChatMessage message) {
        Long l = daoSession.getCustomMessageDao().insert(message);
        daoSession.getCustomMessageDao().deleteAll();
        return l;
    }



    @Override
    public boolean updateMessages(List<CustomChatMessage> messages) {
        return false;
    }

    @Override
    public boolean updateMessage(CustomChatMessage message) {
        return false;
    }

    @Override
    public boolean deleteConversation(long conversationId) {
        ConversationInfo info = daoSession.getConversationInfoDao().queryBuilder()
                .where(ConversationInfoDao.Properties.Id.eq(conversationId)).unique();
        if(info != null){
            daoSession.getConversationInfoDao().deleteByKey(conversationId);
            daoSession.getConversationInfoDao().detachAll();
            daoSession.getCustomMessageDao().deleteAll();
            return true;
        }
        return false;
    }

    @Override
    public boolean insertOrUpdateConversation(ConversationInfo conversationInfo) {
        return false;
    }
}
