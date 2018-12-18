package com.smack.administrator.smackstudyapplication.manager;

import android.util.Log;

import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.smack.administrator.smackstudyapplication.dao.CustomChatMessage;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.offline.OfflineMessageManager;

import java.util.Iterator;


/**
 * <p>离线消息管理</p>
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
 * 			<td>2018-12-18 11:50</td>
 * 			<td>Rui Chaoqun</td>
 * 			<td>All</td>
 *			<td>Created.</td>
 * 		</tr>
 * </table>
 */
public class XmppOfflineMessageManager {
    private OfflineMessageManager mOfflineMessageManager;

    public XmppOfflineMessageManager(OfflineMessageManager mOfflineMessageManager) {
        this.mOfflineMessageManager = mOfflineMessageManager;
    }




}
