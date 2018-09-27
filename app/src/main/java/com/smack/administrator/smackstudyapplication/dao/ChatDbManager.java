package com.smack.administrator.smackstudyapplication.dao;

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
     * 获取当前用户所有会话列表
     * @param userName
     */
    public List<ConversationInfo> getConversations(String userName);

    /**
     * 通过会话id获取消息列表
     * @param conversationId
     * @return
     */
    public List<CustomMessage> getMessage(long conversationId);

    public Long insertOrUpdateConversation(CustomMessage message,String userName);

    /**
     * 存储多条消息
     * @param messages
     * @return
     */
    public void saveMessage(ConversationInfo info, List<CustomMessage> messages);

    /**
     * 存储单条消息
     * @param message
     * @return
     */
    public Long saveMessage(ConversationInfo info,CustomMessage message);

    /**
     * 更新多条消息
     * @param messages
     * @return
     */
    public boolean updateMessages(List<CustomMessage> messages);

    /**
     * 更新单条消息
     * @param message
     * @return
     */
    public boolean updateMessage(CustomMessage message);

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
}
