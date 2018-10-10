package com.smack.administrator.smackstudyapplication.chat;

import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smack.administrator.smackstudyapplication.R;
import com.smack.administrator.smackstudyapplication.chat.ChatBaseViewHolder;

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
 * <td>2018-10-10 09:32</td>
 * <td>${User}</td>
 * <td>All</td>
 * <td>Created.</td>
 * </tr>
 * </table>
 */
public class ChatTextViewHolder extends ChatBaseViewHolder {
    private RelativeLayout rlBubbleLeft;
    private RelativeLayout rlBubbbleRight;
    private TextView tvChatcontentLeft;
    private TextView tvChatcontentRight;
    public ChatTextViewHolder(View itemView) {
        super(itemView);
        rlBubbleLeft = itemView.findViewById(R.id.rl_bubble_left);
        rlBubbbleRight = itemView.findViewById(R.id.rl_bubble_right);
        tvChatcontentLeft = itemView.findViewById(R.id.tv_chatcontent_left);
        tvChatcontentRight = itemView.findViewById(R.id.tv_chatcontent_right);
    }

    @Override
    protected void onSetUpView() {
        if(TextUtils.equals(message.getSendJid(),myJid)){
            tvChatcontentRight.setText(message.getText());
            rlBubbbleRight.setOnClickListener();
        }else{
            tvChatcontentLeft.setText(message.getText());
        }
    }

    @Override
    public void setClickListener() {

    }
}
