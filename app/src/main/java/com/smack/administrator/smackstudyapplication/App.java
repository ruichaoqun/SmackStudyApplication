package com.smack.administrator.smackstudyapplication;

import android.app.Application;
import android.util.Log;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

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

    @Override
    public void onCreate() {
        super.onCreate();
        XmppConnection.getInstance().connect(this);
    }
}
