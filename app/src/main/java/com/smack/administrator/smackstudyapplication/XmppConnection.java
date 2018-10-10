package com.smack.administrator.smackstudyapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.smack.administrator.smackstudyapplication.dao.ChatDbManager;
import com.smack.administrator.smackstudyapplication.dao.ChatDbManagerImpl;
import com.smack.administrator.smackstudyapplication.dao.CustomChatMessage;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
public class XmppConnection {
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


    private ConnectionListener connectionListener = new ConnectionListener() {
        @Override
        public void connected(XMPPConnection connection) {

        }

        @Override
        public void authenticated(XMPPConnection connection, boolean resumed) {

        }

        @Override
        public void connectionClosed() {

        }

        @Override
        public void connectionClosedOnError(Exception e) {

        }
    };

    private IncomingChatMessageListener incomingChatMessageListener = new IncomingChatMessageListener() {
        @Override
        public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
            saveMessage(from,message);
        }
    };



    private OutgoingChatMessageListener outgoingChatMessageListener = new OutgoingChatMessageListener() {
        @Override
        public void newOutgoingMessage(EntityBareJid to, Message message, Chat chat) {
            saveMessage(to,message);
        }
    };

    private void saveMessage(EntityBareJid jid, Message message) {
        String body = message.getBody();
        CustomChatMessage customChatMessage = null;
        try {
            customChatMessage =  gson.fromJson(body,CustomChatMessage.class);
            if(customChatMessage != null){
                long conversationId = chatDbManager.insertOrUpdateConversation(customChatMessage,TestData.TEST_USERNAME,jid);
                customChatMessage.setConversationId(conversationId);
                chatDbManager.saveMessage(customChatMessage);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if(customChatMessage == null){
        }
    }


    //是否已连接服务器
    private boolean isConnect;
    //是否已登录
    private boolean isLogin;

    private XmppConnection() {
        executor = Executors.newCachedThreadPool();
        chatDbManager = ChatDbManagerImpl.getInstance();
    }

    public synchronized static XmppConnection getInstance(){
        if(instance == null){
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

    private void openConnection(){
        try{
            if(connection == null || connection.isAuthenticated()){
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
        }catch (Exception e){
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
    public Observable<Boolean> login(final String account, final String password){
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
                                connection.login(account, password );
                                setPresence(0);
                                currentUserName = account;
                                emitter.onNext(true);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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
                                accountManager.createAccount(Localpart.from(account),password);
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
    public Observable<List<RosterEntry>> getAllEntries() {
        return Observable.just("")
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return isAuthenticated();
                    }
                })
                .compose(new ObservableTransformer<String, List<RosterEntry>>() {
                    @Override
                    public ObservableSource<List<RosterEntry>> apply(Observable<String> upstream) {
                        return Observable.create(new ObservableOnSubscribe<List<RosterEntry>>() {
                            @Override
                            public void subscribe(ObservableEmitter<List<RosterEntry>> emitter) throws Exception {
                                Collection<RosterEntry> rosterEntry = Roster.getInstanceFor(connection).getEntries();
                                List<RosterEntry> entryList = new ArrayList<>();
                                for (RosterEntry aRosterEntry : rosterEntry) {
                                    entryList.add(aRosterEntry);
                                }
                                emitter.onNext(entryList);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 创建聊天窗口
     *
     * @param JID JID
     * @return Chat
     */
    public Observable<Chat> getFriendChat(final String JID) {
        return Observable.just("")
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return isAuthenticated();
                    }
                })
                .compose(new ObservableTransformer<String, Chat>() {
                    @Override
                    public ObservableSource<Chat> apply(Observable<String> upstream) {
                        return Observable.create(new ObservableOnSubscribe<Chat>() {
                            @Override
                            public void subscribe(ObservableEmitter<Chat> emitter) throws Exception {
                                Chat chat = ChatManager.getInstanceFor(connection)
                                        .chatWith(JidCreate.entityBareFrom(JID));
                                emitter.onNext(chat);
                            }
                        });
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
    public Observable<Boolean> sendSingleMessage(final Chat chat, final String message) {
        return Observable.just("")
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return isAuthenticated();
                    }
                })
                .compose(new ObservableTransformer<String, Boolean>() {
                    @Override
                    public ObservableSource<Boolean> apply(Observable<String> upstream) {
                        return Observable.create(new ObservableOnSubscribe<Boolean>() {
                            @Override
                            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                                chat.send(message);
                                emitter.onNext(true);
                            }
                        });
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





    protected void onUserException(String exception){
        Log.e(TAG, "onUserException: " + exception);
        Intent intent = new Intent(appContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.putExtra(exception, true);
        appContext.startActivity(intent);
    }

    public Context getAppContext() {
        return appContext;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }
}
