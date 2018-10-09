package com.smack.administrator.smackstudyapplication.chat;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.smack.administrator.smackstudyapplication.R;
import com.smack.administrator.smackstudyapplication.dao.CustomMessage;
import com.smack.administrator.smackstudyapplication.widget.CircleView;

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
 * <td>2018-10-09 09:43</td>
 * <td>${User}</td>
 * <td>All</td>
 * <td>Created.</td>
 * </tr>
 * </table>
 */
public abstract class ChatBaseViewHolder extends RecyclerView.ViewHolder{
    TextView tvLeftNickName;//左侧昵称
    ImageView cvLeftAvatar;//左侧头像
    ImageView cvRightAvatar;//右侧头像
    ConstraintLayout clLeft;//左侧布局
    ConstraintLayout clRight;//右侧布局
    FrameLayout flLeftContainer;//左侧容器
    FrameLayout flRightContainer;//右侧容器




    public ChatBaseViewHolder(View itemView) {
        super(itemView);
        tvLeftNickName = itemView.findViewById(R.id.tv_nick_left);
        cvLeftAvatar = itemView.findViewById(R.id.iv_avatar_left);
        cvRightAvatar = itemView.findViewById(R.id.iv_avatar_right);
        clLeft = itemView.findViewById(R.id.cl_left);
        clRight = itemView.findViewById(R.id.cl_right);
        flLeftContainer = itemView.findViewById(R.id.message_container_left);
        flRightContainer = itemView.findViewById(R.id.message_container_right);

    }

    public void setUpView(CustomMessage message,String myJid){
        if(TextUtils.equals(message.getSendJid(),myJid)){
            //是我发送的，展示在右侧
            clLeft.setVisibility(View.GONE);
        }
    }

    protected abstract void onSetUpView();
    public abstract void setUpBaseView();
    public abstract void setClickListener();
}
