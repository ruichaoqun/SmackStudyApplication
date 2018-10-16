package com.smack.administrator.smackstudyapplication.dao;

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
 * <td>2018-09-27 09:45</td>
 * <td>${User}</td>
 * <td>All</td>
 * <td>Created.</td>
 * </tr>
 * </table>
 */
public interface ChatDbManager {

    /**
     * 获取好友联系列表
     * @param username
     * @return
     */
    public List<ChatUser> getContactList(String username);

    /**
     * 获取当前用户所有会话列表
     * @param userName
     */
    public List<ConversationInfo> getConversations(String userName);

    /**
     * 通过会话id获取消息列表
     * @param conversationId
     * @return
     */
    public List<CustomChatMessage> getMessage(long conversationId);

    /**
     * 通过对方账号获取消息列表
     * @param userName 自己的账号
     * @param targetUserName 对方的账号
     * @return
     */
    public List<CustomChatMessage> getMessage(String userName, String targetUserName);



    /**
     * 当收到新消息时，更新会话列表
     * @param message
     * @param userName
     * @param jid
     * @return
     */
    public Long insertOrUpdateConversation(CustomChatMessage message, String userName, EntityBareJid jid);

    /**
     * 存储多条消息
     * @param messages
     * @return
     */
    public void saveMessage(List<CustomChatMessage> messages);

    /**
     * 存储单条消息
     * @param message
     * @return
     */
    public Long saveMessage(CustomChatMessage message);

    /**
     * 更新多条消息
     * @param messages
     * @return
     */
    public boolean updateMessages(List<CustomChatMessage> messages);

    /**
     * 更新单条消息
     * @param message
     * @return
     */
    public boolean updateMessage(CustomChatMessage message);

    /**
     * 删除单个会话
     * @param conversationId
     * @return
     */
    public boolean deleteConversation(long conversationId);

    /**
     * 插入或者更新单个会话
     * @param conversationInfo
     * @return
     */
    public boolean insertOrUpdateConversation(ConversationInfo conversationInfo);

    /**
     * 更新好友列表
     * @param chatUsers 好友集合
     */
    public void updateContactList(List<ChatUser> chatUsers);

    /**
     *
     * @param currentUserName 当前登录的账号
     * @param targetUserName 会话对象的账号
     * @return
     */
    public long getConversationId(String currentUserName,String targetUserName,String jid);

}
