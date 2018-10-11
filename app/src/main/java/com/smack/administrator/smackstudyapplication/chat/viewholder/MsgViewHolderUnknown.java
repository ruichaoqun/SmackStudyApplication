package com.smack.administrator.smackstudyapplication.chat.viewholder;

import com.smack.administrator.smackstudyapplication.R;
import com.smack.administrator.smackstudyapplication.widget.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;

/**
 * Created by zhoujianghua on 2015/8/6.
 */
public class MsgViewHolderUnknown extends MsgViewHolderBase {

    public MsgViewHolderUnknown(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return -1;
//        return R.layout.nim_message_item_unknown;
    }

    @Override
    protected boolean isShowHeadImage() {
//        if (message.getSessionType() == SessionTypeEnum.ChatRoom) {
//            return false;
//        }
        return true;
    }

    @Override
    protected void inflateContentView() {
    }

    @Override
    protected void bindContentView() {
    }
}
