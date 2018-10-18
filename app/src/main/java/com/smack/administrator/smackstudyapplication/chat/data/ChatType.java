package com.smack.administrator.smackstudyapplication.chat.data;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

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
 * <td>2018-09-27 17:22</td>
 * <td>rui chaoqun</td>
 * <td>All</td>
 * <td>Created.</td>
 * </tr>
 * </table>
 */
@IntDef({
        ChatType.P2P,
        ChatType.CHAT_ROOM
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ChatType {
     //1.单聊 2.群聊
    int P2P = 1;
    int CHAT_ROOM = 2;
}
