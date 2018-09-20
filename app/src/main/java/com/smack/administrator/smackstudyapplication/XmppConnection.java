package com.smack.administrator.smackstudyapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.debugger.SmackDebuggerFactory;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.sasl.provided.SASLDigestMD5Mechanism;
import org.jivesoftware.smack.sasl.provided.SASLPlainMechanism;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.MD5;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
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
    private ConnectionListener connectionListener;
    //是否已连接服务器
    private boolean isConnect;
    //是否已登录
    private boolean isLogin;

    private XmppConnection() {
        executor = Executors.newCachedThreadPool();
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
                connection.addConnectionListener(new ConnectionListener() {
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
                });

                connection.connect();
            }
        }catch (Exception e){
            e.printStackTrace();
            connection = null;
            isConnect = false;
            isLogin = false;
        }
    }

    /**
     *
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
                                emitter.onNext(true);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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

    public void getConnectUsers(){
//        connection.getUser()
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

}
