package com.smack.administrator.smackstudyapplication.dao;

import android.text.TextUtils;
import android.util.Log;

import com.smack.administrator.smackstudyapplication.App;
import com.smack.administrator.smackstudyapplication.MessageUtils;

import org.jxmpp.jid.EntityBareJid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
     * 获取好友联系列表
     * @param username 当前登录账号
     * @return
     */
    @Override
    public List<ChatUser> getContactList(String username) {
        daoSession.getChatUserDao().detachAll();
        return daoSession.getChatUserDao().queryBuilder().where(ChatUserDao.Properties.ChatUserName.eq(username))
                .build().list();
    }

    /**
     * 更新好友列表
     * @param chatUsers
     */
    @Override
    public void updateContactList(List<ChatUser> chatUsers){
        daoSession.getChatUserDao().detachAll();
        daoSession.getChatUserDao().saveInTx(chatUsers);
    }

    /**
     * 获取与该账号对应的会话id
     * @param targetUserName
     */
    @Override
    public long getConversationId(String currentUserName,String targetUserName,String jid) {
        ConversationInfo conversationInfo = daoSession.getConversationInfoDao().queryBuilder().where(ConversationInfoDao.Properties.ChatUserName.eq(currentUserName),
                ConversationInfoDao.Properties.UserName.eq(targetUserName)).build().unique();
        if(conversationInfo != null){
            return conversationInfo.getId();
        }else{
            ConversationInfo info = new ConversationInfo();
            info.newConversationInfo(currentUserName,targetUserName,jid);
            long conversationId = daoSession.getConversationInfoDao().insert(info);
            return conversationId;
        }
    }

    /**
     * 获取当前用户所有会话列表
     * @param userName username 当前登录账号
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
        daoSession.getConversationInfoDao().detachAll();
        ConversationInfo info = daoSession.getConversationInfoDao().queryBuilder()
                .where(ConversationInfoDao.Properties.Id.eq(conversationId)).build().unique();
        if(info == null){
            return null;
        }
        return info.getMessages();
    }

    @Override
    public CustomChatMessage getMessageByUUid(String uuid) {
        CustomChatMessage message = daoSession.getCustomChatMessageDao().queryBuilder()
                .where(CustomChatMessageDao.Properties.Uuid.eq(uuid)).build().unique();
        return message;
    }

    /**
     * 通过对方账号获取消息列表
     * @param userName 自己的账号
     * @param targetUserName 对方的账号
     * @return
     */
    @Override
    public List<CustomChatMessage> getMessage(String userName, String targetUserName) {
        List<CustomChatMessage> messageList = new ArrayList<>();
        messageList.addAll(daoSession.getCustomChatMessageDao().queryBuilder()
                .where(CustomChatMessageDao.Properties.SendUserName.eq(userName),
                        CustomChatMessageDao.Properties.RecieveUserName.eq(targetUserName)).build().list());
        messageList.addAll(daoSession.getCustomChatMessageDao().queryBuilder()
                .where(CustomChatMessageDao.Properties.SendUserName.eq(targetUserName),
                        CustomChatMessageDao.Properties.RecieveUserName.eq(userName)).build().list());
        Collections.sort(messageList, new Comparator<CustomChatMessage>() {
            @Override
            public int compare(CustomChatMessage o1, CustomChatMessage o2) {
                return (int) (o1.getTime() - o2.getTime());
            }
        });
        return messageList;
    }

    // 新消息到达后更新会话列表
    @Override
    public Long insertOrUpdateConversation(CustomChatMessage message, String userName, EntityBareJid jid) {
        String targetUserName = TextUtils.equals(userName,message.getSendUserName())?message.getRecieveUserName():message.getSendUserName();
        ConversationInfo info = daoSession.getConversationInfoDao().queryBuilder()
                .where(ConversationInfoDao.Properties.UserName.eq(userName),ConversationInfoDao.Properties.ChatUserName.eq(targetUserName)).build().unique();
        if(info != null){
            info.setDate(message.getTime());
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
            info.setDate(message.getTime());
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
        daoSession.getCustomChatMessageDao().insertInTx(messages);
    }

    /**
     * 存储单条消息
     * @param message 消息
     * @param isAddUnreadNumber 是否增加未读消息数
     * @return
     */
    @Override
    public void saveMessage(CustomChatMessage message,boolean isAddUnreadNumber) {
        ConversationInfo info = daoSession.getConversationInfoDao().queryBuilder().where(ConversationInfoDao.Properties.Id.eq(message.getConversationId())).build().unique();
        if(info != null){
            info.setLastMessage(message.getText());
            info.setDate(message.getTime());
            if(isAddUnreadNumber)
            info.setUnReadMessageNumber(info.getUnReadMessageNumber() + 1);
            daoSession.getConversationInfoDao().update(info);
        }
        long id = daoSession.getCustomChatMessageDao().insert(message);
        message.setId(id);
    }

    /**
     * 更新某条消息状态
     * @param message
     */
    @Override
    public void updateMessageStstus(CustomChatMessage message){
        CustomChatMessage m = daoSession.getCustomChatMessageDao().queryBuilder().where(CustomChatMessageDao.Properties.Uuid.eq(message.getUuid())).build().unique();
        if(m != null && message != null){
            m.setMsgStatusEnum(message.getMsgStatusEnum());
            daoSession.getCustomChatMessageDao().update(m);
        }
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
            return true;
        }
        return false;
    }

    @Override
    public boolean insertOrUpdateConversation(ConversationInfo conversationInfo) {
        return false;
    }
}
