package com.smack.administrator.smackstudyapplication.manager;

import android.util.Log;

import com.google.gson.Gson;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.smack.administrator.smackstudyapplication.XmppConnection;
import com.smack.administrator.smackstudyapplication.dao.ChatDbManager;
import com.smack.administrator.smackstudyapplication.dao.CustomChatMessage;
import com.smack.administrator.smackstudyapplication.dao.MsgStatusEnum;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.offline.OfflineMessageManager;

import java.util.Iterator;
import java.util.List;

/**
 * <p>消息管理类</p>
 *
 * <b>Maintenance History</b>:
 * <table>
 * 		<tr>
 * 			<th>Date</th>
 * 			<th>Developer</th>
 * 			<th>Target</th>
 * 			<th>Content</th>
 * 		</tr>
 * 		<tr>
 * 			<td>2018-12-18 13:42</td>
 * 			<td>Rui Chaoqun</td>
 * 			<td>All</td>
 *			<td>Created.</td>
 * 		</tr>
 * </table>
 */
public class XmppMessageManager {
    private XMPPTCPConnection mXmppConnection;
    private OfflineMessageManager mOfflineMessageManager;
    private ChatDbManager mChatDbManager;
    private Gson gson = new Gson();

    public XmppMessageManager(XMPPTCPConnection mXmppConnection,ChatDbManager chatDbManager) {
        this.mXmppConnection = mXmppConnection;
        this.mChatDbManager = chatDbManager;
        init();
    }

    /**
     * 初始化消息管理器
     */
    private void init() {
        mOfflineMessageManager = new OfflineMessageManager(mXmppConnection);
    }

    /**
     * 获取离线消息
     */
    public void getOfflineMessage(){
        try {
            Log.i("离线消息数量: ", "" + mOfflineMessageManager.getMessageCount());
            Iterator<Message> it = mOfflineMessageManager.getMessages().iterator();

            while (it.hasNext()) {
                org.jivesoftware.smack.packet.Message message = it.next();
                Log.i("收到离线消息", "Received from 【" + message.getFrom()
                        + "】 message: " + message.getBody());
                if (message != null && message.getBody() != null
                        && !message.getBody().equals("null")) {
                    //解析离线消息
                    CustomChatMessage chatMessage = gson.fromJson(message.getBody(),CustomChatMessage.class);
                    if(chatMessage != null){
                        //删除收到的消息的主键
                        chatMessage.setId(null);
                        //收到消息后更改发送状态
                        chatMessage.setMsgStatusEnum(MsgStatusEnum.success);
                        //更改消息的conversationId为自己的
                        long id = mChatDbManager.getConversationId(XmppConnection.getInstance().getCurrentUserName(),chatMessage.getSendUserName(),chatMessage.getSendJid());
                        chatMessage.setConversationId(id);
                        mChatDbManager.saveMessage(chatMessage, true);

                    }
                }
            }
            mOfflineMessageManager.deleteMessages();// 通知服务器删除离线消息
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 保存本地消息
     */
    public void saveMessage(CustomChatMessage message){}

    /**
     * 删除本地消息
     */
    public void deleteMessage(){}

    /**
     * 保存消息列表
     * @param list
     */
    public void saveMessage(List<CustomChatMessage> list){}

    /**
     * 删除个人消息
     * @param uid
     */
    public void deleteMessage(String uid){}

    /**
     * 发布通知
     */
    public void notify(CustomChatMessage customChatMessage){

    }

}
