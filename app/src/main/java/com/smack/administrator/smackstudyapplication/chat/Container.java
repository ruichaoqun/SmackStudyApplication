package com.smack.administrator.smackstudyapplication.chat;

import android.app.Activity;

import com.smack.administrator.smackstudyapplication.dao.ChatUser;


/**
 * Created by zhoujianghua on 2015/7/6.
 */
public class Container {
    public final Activity activity;
    public final String account;
    public final ModuleProxy proxy;
    public final ChatUser chatUser;

    public Container(Activity activity, String account,  ModuleProxy proxy,ChatUser chatUser) {
        this.activity = activity;
        this.account = account;
        this.proxy = proxy;
        this.chatUser = chatUser;
    }
}
