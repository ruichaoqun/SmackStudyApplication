package com.smack.administrator.smackstudyapplication.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.smack.administrator.smackstudyapplication.R;
import com.smack.administrator.smackstudyapplication.base.activity.ToolBarOptions;
import com.smack.administrator.smackstudyapplication.base.activity.XmppToolBarOptions;
import com.smack.administrator.smackstudyapplication.chat.constant.Extras;
import com.smack.administrator.smackstudyapplication.chat.fragment.MessageFragment;
import com.smack.administrator.smackstudyapplication.dao.ChatUser;

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
 * <td>2018-10-15 10:42</td>
 * <td>${User}</td>
 * <td>All</td>
 * <td>Created.</td>
 * </tr>
 * </table>
 */
public class P2PMessageActivity extends BaseMessageActivity {
    public static void launchFrom(Context context, ChatUser chatUser,long conversationId){
        Intent intent = new Intent(context,P2PMessageActivity.class);
        intent.putExtra(Extras.EXTRA_CHAT_USER,chatUser);
        intent.putExtra(Extras.EXTRA_CONVERSATION_ID,conversationId);
        context.startActivity(intent);
    }

    @Override
    protected MessageFragment fragment() {
        MessageFragment fragment = new MessageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Extras.EXTRA_JID,jid);
        bundle.putLong(Extras.EXTRA_CONVERSATION_ID,conversationId);
        bundle.putParcelable(Extras.EXTRA_CHAT_USER,chatUser);
        fragment.setArguments(bundle);
        fragment.setContainerId(R.id.message_fragment_container);
        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.xmpp_message_activity;
    }

    @Override
    protected void initToolBar() {
        ToolBarOptions options = new XmppToolBarOptions();
        setToolBar(R.id.toolbar, options);
    }

    @Override
    protected boolean enableSensor() {
        return false;
    }
}
