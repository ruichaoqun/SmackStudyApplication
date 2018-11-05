package com.smack.administrator.smackstudyapplication;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.netease.nimlib.sdk.NIMClient;
import com.smack.administrator.smackstudyapplication.util.log.LogUtil;
import com.smack.administrator.smackstudyapplication.util.storage.StorageType;
import com.smack.administrator.smackstudyapplication.util.storage.StorageUtil;
import com.smack.administrator.smackstudyapplication.util.sys.ScreenUtil;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

import java.io.IOException;

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
 * <td>2018-09-18 16:25</td>
 * <td>${User}</td>
 * <td>All</td>
 * <td>Created.</td>
 * </tr>
 * </table>
 */
public class App extends Application {
    private static String TAG = "Application";
    public static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        XmppConnection.getInstance().connect(this);
        String appCacheDir = getAppCacheDir(this) + "/app";
        StorageUtil.init(this, appCacheDir);
        ScreenUtil.init(this);
        NIMClient.init(this, null, NimSDKOptionConfig.getSDKOptions(this));
        // init log
        String path = StorageUtil.getDirectoryByDirType(StorageType.TYPE_LOG);
        LogUtil.init(path, Log.DEBUG);

    }

    private String getAppCacheDir(Context context) {
        String storageRootPath = null;
        try {
            // SD卡应用扩展存储区(APP卸载后，该目录下被清除，用户也可以在设置界面中手动清除)，请根据APP对数据缓存的重要性及生命周期来决定是否采用此缓存目录.
            // 该存储区在API 19以上不需要写权限，即可配置 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="18"/>
            if (context.getExternalCacheDir() != null) {
                storageRootPath = context.getExternalCacheDir().getCanonicalPath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(storageRootPath)) {
            // SD卡应用公共存储区(APP卸载后，该目录不会被清除，下载安装APP后，缓存数据依然可以被加载。SDK默认使用此目录)，该存储区域需要写权限!
            storageRootPath = Environment.getExternalStorageDirectory() + "/" + context.getPackageName();
        }

        return storageRootPath;
    }
}
