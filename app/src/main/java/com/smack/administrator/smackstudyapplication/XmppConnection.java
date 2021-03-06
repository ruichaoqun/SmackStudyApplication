package com.smack.administrator.smackstudyapplication;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.smack.administrator.smackstudyapplication.chat.activity.BaseMessageActivity;
import com.smack.administrator.smackstudyapplication.chat.upload.AttachmentProgress;
import com.smack.administrator.smackstudyapplication.chat.upload.AttachmentProgressManager;
import com.smack.administrator.smackstudyapplication.chat.upload.AttachmentProgressUpdateListener;
import com.smack.administrator.smackstudyapplication.dao.ChatDbManager;
import com.smack.administrator.smackstudyapplication.dao.ChatUser;
import com.smack.administrator.smackstudyapplication.dao.CustomChatMessage;
import com.smack.administrator.smackstudyapplication.dao.MsgStatusEnum;
import com.smack.administrator.smackstudyapplication.manager.XmppMessageManager;
import com.smack.administrator.smackstudyapplication.util.log.LogUtil;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener;
import org.jivesoftware.smack.debugger.SmackDebugger;
import org.jivesoftware.smack.debugger.SmackDebuggerFactory;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.offline.OfflineMessageManager;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

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
 * <td>2018-09-18 15:07</td>
 * <td>${User}</td>
 * <td>All</td>
 * <td>Created.</td>
 * </tr>
 * </table>
 */
public class XmppConnection implements AttachmentProgressUpdateListener {
    protected static final String TAG = "XmppConnection";
    private XMPPTCPConnection connection = null;
    private static XmppConnection instance = null;
    private static final String SERVER_HOST = "192.168.31.201";
    private static final String SERVER_NAME = "192.168.31.201";
    private static final int SERVER_PORT = 5222;

    private Context appContext;
    private ExecutorService executor;
    private String currentUserName;
    private ChatManager chatManager;
    private ChatDbManager chatDbManager;
    private Roster mRoster;                     //好友管理类
    private Gson gson = new Gson();
    private Map<String, ChatUser> chatUserMap;
    private XmppIncomingChatMessageListener incomingMessageListener;
    private XmppOutgoingChatMessageListener outgoingMessageListener;
    private BaseMessageActivity activity;
    private AttachmentProgressManager attachmentProgressManager;


    private ConnectionListener connectionListener = new ConnectionListener() {
        @Override
        public void connected(XMPPConnection connection) {
            Log.w(TAG, "connected: openfire has connected");
        }

        @Override
        public void authenticated(XMPPConnection connection, boolean resumed) {
            Log.w(TAG, "connected: openfire has authenticated");
        }

        @Override
        public void connectionClosed() {
            Log.w(TAG, "connected: connectionClosed");
        }

        @Override
        public void connectionClosedOnError(Exception e) {
            Log.w(TAG, "connected: connectionClosedOnError  ");
            e.printStackTrace();
        }
    };

    /**
     * 接收到消息监听
     */
    private IncomingChatMessageListener incomingChatMessageListener = new IncomingChatMessageListener() {
        @Override
        public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
            CustomChatMessage chatMessage = gson.fromJson(message.getBody(), CustomChatMessage.class);
            if (chatMessage != null) {
                //删除收到的消息的主键
                chatMessage.setId(null);
                //收到消息后更改发送状态
                chatMessage.setMsgStatusEnum(MsgStatusEnum.success);
                //更改消息的conversationId为自己的
                long id = chatDbManager.getConversationId(currentUserName, chatMessage.getSendUserName(), from.toString());
                chatMessage.setConversationId(id);
                chatDbManager.saveMessage(chatMessage, activity == null);
                if (incomingMessageListener != null) {
                    incomingMessageListener.newIncomingMessage(from, chatMessage, chat);
                }
            }
        }
    };

    /**
     * 发送消息成功监听
     */
    private OutgoingChatMessageListener outgoingChatMessageListener = new OutgoingChatMessageListener() {
        @Override
        public void newOutgoingMessage(EntityBareJid to, Message message, Chat chat) {
            CustomChatMessage chatMessage = gson.fromJson(message.getBody(), CustomChatMessage.class);
            if (chatMessage != null) {
                chatMessage.setMsgStatusEnum(MsgStatusEnum.success);
                chatDbManager.updateMessageStstus(chatMessage);
                if (outgoingMessageListener != null) {
                    outgoingMessageListener.newOutgoingMessage(to, chatMessage, chat);
                }
            }
        }
    };


    //是否已连接服务器
    private boolean isConnect;
    //是否已登录
    private boolean isLogin;

    private XmppConnection() {
        executor = Executors.newCachedThreadPool();
        chatDbManager = ChatDbManager.getInstance();
        attachmentProgressManager = new AttachmentProgressManager(this);
    }

    public synchronized static XmppConnection getInstance() {
        if (instance == null) {
            instance = new XmppConnection();
        }
        return instance;
    }

    /**
     * 创建连接
     */
    public void connect(Context context) {
        appContext = context;
        if (connection == null) {
            // 开线程打开连接，避免在主线程里面执行HTTP请求
            // Caused by: android.os.NetworkOnMainThreadException
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    openConnection();
                }
            });
        }
    }

    private void openConnection() {
        try {
            if (connection == null || connection.isAuthenticated()) {
                //调试模式
                SmackConfiguration.DEBUG = true;
                XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
                //设置主机名
                config.setHostAddress(InetAddress.getByName(SERVER_HOST));
                //设置openfire服务器名称
                config.setXmppDomain(SERVER_NAME);
                //设置端口号，默认5222
                config.setPort(SERVER_PORT);
                //设置禁用SSL连接
                config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled).setCompressionEnabled(false);
                //设置离线模式，默认为true
                config.setSendPresence(false);
                //设置开启压缩，可以节省流量。默认为false
                config.setCompressionEnabled(true);
                //设置超时时间，默认30000
                config.setConnectTimeout(30000);
                connection = new XMPPTCPConnection(config.build());
                //添加全局连接监听
                connection.addConnectionListener(connectionListener);

                connection.connect();

                chatManager = ChatManager.getInstanceFor(connection);
                chatManager.addIncomingListener(incomingChatMessageListener);
                chatManager.addOutgoingListener(outgoingChatMessageListener);

                mRoster = Roster.getInstanceFor(connection);
                //默认接受所有订阅请求
                mRoster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
            }
        } catch (Exception e) {
            e.printStackTrace();
            connection = null;
            isConnect = false;
            isLogin = false;
        }
    }

    public XMPPTCPConnection getConnection() {
        return connection;
    }

    /**
     *  登录
     * @param account
     * @param password
     */
    public Observable<Boolean> login(final String account, final String password) {
        return Observable.just("")
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return checkConnection();
                    }
                })
                .compose(new ObservableTransformer<String, Boolean>() {
                    @Override
                    public ObservableSource<Boolean> apply(Observable<String> upstream) {
                        return Observable.create(new ObservableOnSubscribe<Boolean>() {
                            @Override
                            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                                //登录
                                connection.login(account, password);
                                //设置当前登录人
                                currentUserName = account;
                                initChatUsers();
                                //设置在线状态
                                getOfflineMessage();
                                emitter.onNext(true);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void logout(){
        Log.w(TAG, "XMPPConnectionManager 退出登陆");
        //这里需要先将登陆状态改变为“离线”，再断开连接，不然在后台还是上线的状态
        Presence presence = new Presence(Presence.Type.unavailable);
        try {
            connection.sendStanza(presence);
            if (connection != null) {
                connection.disconnect();
                connection = null;
            }
        } catch (SmackException.NotConnectedException  e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initChatUsers() {
        if (chatUserMap == null) {
            chatUserMap = new HashMap<>();
        }
        List<ChatUser> users = chatDbManager.getContactList(currentUserName);
        if (users != null) {
            for (ChatUser user : users) {
                chatUserMap.put(user.getJid(), user);
            }
        }
    }

    /**
     * 获取离线消息
     */
    public void getOfflineMessage() throws XMPPException.XMPPErrorException, SmackException.NotConnectedException, InterruptedException, SmackException.NoResponseException {
        OfflineMessageManager offlineManager = new OfflineMessageManager(connection);
        Log.i("离线消息数量: ", "" + offlineManager.getMessageCount());
        Iterator<Message> it = offlineManager.getMessages().iterator();

        while (it.hasNext()) {
            org.jivesoftware.smack.packet.Message message = it.next();
            Log.i("收到离线消息", "Received from 【" + message.getFrom()
                    + "】 message: " + message.getBody());
            if (message != null && message.getBody() != null
                    && !message.getBody().equals("null")) {
                //解析离线消息
                CustomChatMessage chatMessage = gson.fromJson(message.getBody(), CustomChatMessage.class);
                if (chatMessage != null) {
                    //删除收到的消息的主键
                    chatMessage.setId(null);
                    //收到消息后更改发送状态
                    chatMessage.setMsgStatusEnum(MsgStatusEnum.success);
                    //更改消息的conversationId为自己的
                    long id = chatDbManager.getConversationId(XmppConnection.getInstance().getCurrentUserName(), chatMessage.getSendUserName(), chatMessage.getSendJid());
                    chatMessage.setConversationId(id);
                    chatDbManager.saveMessage(chatMessage, true);
                }
            }
        }
        //删除离线消息
        offlineManager.deleteMessages();
        //将状态设置成在线
        setPresence(0);
    }

    /**
     * 更改用户状态
     */
    public void setPresence(int code) {
        Presence presence;
        try {
            switch (code) {
                case 0:
                    presence = new Presence(Presence.Type.available);
                    connection.sendStanza(presence);
                    Log.v("state", "设置在线");
                    break;
                case 1:
                    presence = new Presence(Presence.Type.available);
                    presence.setMode(Presence.Mode.chat);
                    connection.sendStanza(presence);
                    Log.v("state", "设置Q我吧");
                    break;
                case 2:
                    presence = new Presence(Presence.Type.available);
                    presence.setMode(Presence.Mode.dnd);
                    connection.sendStanza(presence);
                    Log.v("state", "设置忙碌");
                    break;
                case 3:
                    presence = new Presence(Presence.Type.available);
                    presence.setMode(Presence.Mode.away);
                    connection.sendStanza(presence);
                    Log.v("state", "设置离开");
                    break;
                case 4:
                    //                    Roster roster = con.getRoster();
                    //                    Collection<RosterEntry> entries = roster.getEntries();
                    //                    for (RosterEntry entry : entries) {
                    //                        presence = new Presence(Presence.Type.unavailable);
                    //                        presence.setPacketID(Packet.ID_NOT_AVAILABLE);
                    //                        presence.setFrom(con.getUser());
                    //                        presence.setTo(entry.getUser());
                    //                        con.sendPacket(presence);
                    //                        Log.v("state", presence.toXML());
                    //                    }
                    //                    // 向同一用户的其他客户端发送隐身状态
                    //                    presence = new Presence(Presence.Type.unavailable);
                    //                    presence.setPacketID(Packet.ID_NOT_AVAILABLE);
                    //                    presence.setFrom(con.getUser());
                    //                    presence.setTo(StringUtils.parseBareAddress(con.getUser()));
                    //                    con.sendStanza(presence);
                    //                    Log.v("state", "设置隐身");
                    //                    break;
                case 5:
                    presence = new Presence(Presence.Type.unavailable);
                    connection.sendStanza(presence);
                    Log.v("state", "设置离线");
                    break;
                default:
                    break;
            }
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 注册
     *
     * @param account  注册帐号
     * @param password 注册密码
     * @return true、注册成功 false、注册失败
     */
    public Observable<Boolean> register(final String account, final String password) {
        return Observable.just("")
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return checkConnection();
                    }
                })
                .compose(new ObservableTransformer<String, Boolean>() {
                    @Override
                    public ObservableSource<Boolean> apply(Observable<String> upstream) {
                        return Observable.create(new ObservableOnSubscribe<Boolean>() {
                            @Override
                            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                                AccountManager accountManager = AccountManager.getInstance(connection);
                                //允许不安全的创建账户，不加会报错IllegalStateException
                                accountManager.sensitiveOperationOverInsecureConnection(true);
                                accountManager.createAccount(Localpart.from(account), password);
                                emitter.onNext(true);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取所有好友信息
     *
     * @return List<RosterEntry>
     */
    public Observable<List<ChatUser>> getAllEntries() {
        return Observable.just("")
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return isAuthenticated();
                    }
                })
                .map(new Function<String, List<RosterEntry>>() {
                    @Override
                    public List<RosterEntry> apply(String s) throws Exception {
                        Collection<RosterEntry> rosterEntry = Roster.getInstanceFor(connection).getEntries();
                        List<RosterEntry> entryList = new ArrayList<>();
                        for (RosterEntry aRosterEntry : rosterEntry) {
                            entryList.add(aRosterEntry);
                        }
                        return entryList;
                    }
                })
                .map(new Function<List<RosterEntry>, List<ChatUser>>() {
                    @Override
                    public List<ChatUser> apply(List<RosterEntry> rosterEntries) throws Exception {
                        List<ChatUser> users = new ArrayList<>();
                        for (RosterEntry e : rosterEntries) {
                            ChatUser user = chatUserMap.get(e.getJid().toString());
                            if (user == null) {
                                long conversationId = chatDbManager.getConversationId(currentUserName, e.getJid().toString().split("@")[0], e.getJid().toString());
                                user = new ChatUser();
                                user.setUserNick(e.getName());
                                user.setJid(e.getJid().toString());
                                user.setUserName(e.getName());
                                user.setChatUserName(XmppConnection.getInstance().currentUserName);
                                user.setConversationId(conversationId);
                            }
                            users.add(user);
                        }
                        chatDbManager.updateContactList(users);
                        initChatUsers();
                        return users;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public long getConversationId(String targetUserName, String jid) {
        return chatDbManager.getConversationId(currentUserName, targetUserName, jid);
    }

    /**
     * 创建聊天窗口
     *
     * @param jid JID
     * @return Chat
     */
    @Deprecated
    public Observable<Chat> getFriendChat(final String jid) {
        return Observable.just(jid)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return isAuthenticated();
                    }
                })
                .map(new Function<String, Chat>() {
                    @Override
                    public Chat apply(String s) throws Exception {
                        return ChatManager.getInstanceFor(connection)
                                .chatWith(JidCreate.entityBareFrom(jid));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 发送单人聊天消息
     *
     * @param chat    chat
     * @param message 消息文本
     */
    public Observable<Boolean> sendMessage(final Chat chat, final CustomChatMessage message, final boolean isSave) {
        return Observable.just(message)
                .filter(new Predicate<CustomChatMessage>() {
                    @Override
                    public boolean test(CustomChatMessage s) throws Exception {
                        return isAuthenticated();
                    }
                })
                .map(new Function<CustomChatMessage, Boolean>() {
                    @Override
                    public Boolean apply(CustomChatMessage s) throws Exception {
                        //先存储
                        if (isSave) {
                            chatDbManager.saveMessage(s, false);
                        }
                        chat.send(gson.toJson(s));
                        return true;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<CustomChatMessage>> loadLocalMessage(final long conversationId) {
        return Observable.just(conversationId)
                .filter(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Exception {
                        return isAuthenticated();
                    }
                })
                .map(new Function<Long, List<CustomChatMessage>>() {
                    @Override
                    public List<CustomChatMessage> apply(Long aLong) throws Exception {
                        return chatDbManager.getMessage(conversationId);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    /**
     * 判断是否已连接
     */
    public boolean checkConnection() {
        return null != connection && connection.isConnected();
    }

    /**
     * 关闭连接
     */
    public void closeConnection() {
        if (connection != null) {
            // 移除连接监听
            connection.removeConnectionListener(connectionListener);
            if (connection.isConnected())
                connection.disconnect();
            connection = null;
        }
        Log.i("XmppConnection", "关闭连接");
    }

    /**
     * 判断连接是否通过了身份验证
     * 即是否已登录
     *
     * @return
     */
    public boolean isAuthenticated() {
        return connection != null && connection.isConnected() && connection.isAuthenticated();
    }

    public ChatUser getUserInfo(String account) {
        if (TextUtils.equals(account, currentUserName)) {
            ChatUser us = new ChatUser();
            us.setJid(connection.getUser().asBareJid().toString());
            us.setUserName(account);
            return us;
        }
        if (account != null && chatUserMap != null) {
            for (ChatUser us : chatUserMap.values()) {
                if (TextUtils.equals(account, us.getUserName())) {
                    return us;
                }
            }
        }
        return new ChatUser();
    }

    public VCard getVCard(String jid) throws SmackException.NoResponseException, XMPPException.XMPPErrorException, SmackException.NotConnectedException, InterruptedException, XmppStringprepException {
        EntityBareJid entityBareJid = JidCreate.entityBareFrom(jid);
        return VCardManager.getInstanceFor(connection).loadVCard(entityBareJid);
    }


    public Context getAppContext() {
        return appContext;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public ChatDbManager getChatDbManager() {
        return chatDbManager;
    }


    public void setIncomingMessageListener(XmppIncomingChatMessageListener incomingMessageListener) {
        this.incomingMessageListener = incomingMessageListener;
    }

    public void setOutgoingMessageListener(XmppOutgoingChatMessageListener outgoingMessageListener) {
        this.outgoingMessageListener = outgoingMessageListener;
    }

    //文件上传进度回调
    @Override
    public void onEvent(AttachmentProgress progress) {
        //业务流程：
        // 更新UI
        if (xmppAttachProgressListener != null) {
            xmppAttachProgressListener.xmppAttachmentProgress(progress);
        }
        //上传完毕后发送消息
        if (progress.getTransferred() == progress.getTotal()) {
            final CustomChatMessage message = chatDbManager.getMessageByUUid(progress.getUuid());
            if (message != null) {
                try {
                    Chat chat = ChatManager.getInstanceFor(connection)
                            .chatWith(JidCreate.entityBareFrom(message.getRecieveJid()));
                    sendMessage(chat, message, false)
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean aBoolean) throws Exception {

                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    message.setMsgStatusEnum(MsgStatusEnum.fail);
                                    chatDbManager.updateMessageStstus(message);
                                }
                            });
                } catch (XmppStringprepException e) {
                    message.setMsgStatusEnum(MsgStatusEnum.fail);
                    chatDbManager.updateMessageStstus(message);
                    e.printStackTrace();
                }
            }
        }
    }

    private XmppAttachProgressListener xmppAttachProgressListener;

    public void setXmppAttachProgressListener(XmppAttachProgressListener xmppAttachProgressListener) {
        this.xmppAttachProgressListener = xmppAttachProgressListener;
    }

    public void uploadFileMessage(CustomChatMessage message, String uuid) {
        attachmentProgressManager.uploadAttachment(message, uuid);
    }

    public interface XmppAttachProgressListener {
        void xmppAttachmentProgress(AttachmentProgress attachmentProgress);
    }

    public interface XmppIncomingChatMessageListener {
        public void newIncomingMessage(EntityBareJid from, CustomChatMessage message, Chat chat);
    }

    public interface XmppOutgoingChatMessageListener {
        public void newOutgoingMessage(EntityBareJid to, CustomChatMessage message, Chat chat);
    }


    public void attachActivity(BaseMessageActivity messageActivity) {
        this.activity = messageActivity;
    }

    public void dettachActivity() {
        this.activity = null;
    }


}
