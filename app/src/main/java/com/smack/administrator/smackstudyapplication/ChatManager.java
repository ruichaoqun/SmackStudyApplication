package com.smack.administrator.smackstudyapplication;

import android.content.Context;

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
 * <td>2018-09-18 11:39</td>
 * <td>${User}</td>
 * <td>All</td>
 * <td>Created.</td>
 * </tr>
 * </table>
 */
public class ChatManager {
    private static ChatManager instance = null;
    private Context appContext = null;

    private ChatManager(){}

    public synchronized static ChatManager getInstance(){
        if(instance == null){
            instance = new ChatManager();
        }
        return instance;
    }

}
