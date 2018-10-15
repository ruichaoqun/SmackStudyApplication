package com.smack.administrator.smackstudyapplication.base.activity;

import com.smack.administrator.smackstudyapplication.R;

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
 * <td>2018-10-15 10:47</td>
 * <td>${User}</td>
 * <td>All</td>
 * <td>Created.</td>
 * </tr>
 * </table>
 */
public class XmppToolBarOptions extends ToolBarOptions {
    public XmppToolBarOptions() {
        navigateId = R.drawable.xmpp_action_bar_button_selector;
        isNeedNavigate = true;
    }
}