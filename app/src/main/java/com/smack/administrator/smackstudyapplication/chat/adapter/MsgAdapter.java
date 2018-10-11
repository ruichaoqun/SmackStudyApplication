package com.smack.administrator.smackstudyapplication.chat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.smack.administrator.smackstudyapplication.R;
import com.smack.administrator.smackstudyapplication.chat.Container;
import com.smack.administrator.smackstudyapplication.chat.viewholder.MsgViewHolderBase;
import com.smack.administrator.smackstudyapplication.chat.viewholder.MsgViewHolderFactory;
import com.smack.administrator.smackstudyapplication.dao.CustomChatMessage;
import com.smack.administrator.smackstudyapplication.widget.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.smack.administrator.smackstudyapplication.widget.recyclerview.holder.BaseViewHolder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by huangjun on 2016/12/21.
 */
public class MsgAdapter extends BaseMultiItemFetchLoadAdapter<CustomChatMessage, BaseViewHolder> {

    private Map<Class<? extends MsgViewHolderBase>, Integer> holder2ViewType;

    private ViewHolderEventListener eventListener;
    private Map<Long, Float> progresses; // 有文件传输，需要显示进度条的消息ID map
    private String messageId;
    private Container container;

    MsgAdapter(RecyclerView recyclerView, List<CustomChatMessage> data, Container container) {
        super(recyclerView, data);

        timedItems = new HashSet<>();
        progresses = new HashMap<>();

        // view type, view holder
        holder2ViewType = new HashMap<>();
        List<Class<? extends MsgViewHolderBase>> holders = MsgViewHolderFactory.getAllViewHolders();
        int viewType = 0;
        for (Class<? extends MsgViewHolderBase> holder : holders) {
            viewType++;
            addItemType(viewType, R.layout.xmpp_message_item, holder);
            holder2ViewType.put(holder, viewType);
        }

        this.container = container;
//        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onChanged() {
//                Log.e("TAG","AdapterData Change....");
//            }
//        });
    }

    @Override
    protected int getViewType(CustomChatMessage message) {
        return holder2ViewType.get(MsgViewHolderFactory.getViewHolderByType(message));
    }

    @Override
    protected Long getItemKey(CustomChatMessage item) {
        return item.getId();
    }

    public void setEventListener(ViewHolderEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public ViewHolderEventListener getEventListener() {
        return eventListener;
    }

    public void deleteItem(CustomChatMessage message, boolean isRelocateTime) {
        if (message == null) {
            return;
        }

        int index = 0;
        for (CustomChatMessage item : getData()) {
            if (item.getId() == message.getId()) {
                break;
            }
            ++index;
        }

        if (index < getDataSize()) {
            remove(index);
            if (isRelocateTime) {
                relocateShowTimeItemAfterDelete(message, index);
            }
//            notifyDataSetChanged(); // 可以不要！！！
        }
    }

    public float getProgress(CustomChatMessage message) {
        Float progress = progresses.get(message.getId());
        return progress == null ? 0 : progress;
    }

    public void putProgress(CustomChatMessage message, float progress) {
        progresses.put(message.getId(), progress);
    }

    /**
     * *********************** 时间显示处理 ***********************
     */

    private Set<Long> timedItems; // 需要显示消息时间的消息ID
    private CustomChatMessage lastShowTimeItem; // 用于消息时间显示,判断和上条消息间的时间间隔

    public boolean needShowTime(CustomChatMessage message) {
        return timedItems.contains(message.getId());
    }

    /**
     * 列表加入新消息时，更新时间显示
     */
    public void updateShowTimeItem(List<CustomChatMessage> items, boolean fromStart, boolean update) {
        CustomChatMessage anchor = fromStart ? null : lastShowTimeItem;
        for (CustomChatMessage message : items) {
            if (setShowTimeFlag(message, anchor)) {
                anchor = message;
            }
        }

        if (update) {
            lastShowTimeItem = anchor;
        }
    }

    /**
     * 是否显示时间item
     */
    private boolean setShowTimeFlag(CustomChatMessage message, CustomChatMessage anchor) {
        boolean update = false;

        if (hideTimeAlways(message)) {
            setShowTime(message, false);
        } else {
            if (anchor == null) {
                setShowTime(message, true);
                update = true;
            } else {
                long time = anchor.getTime();
                long now = message.getTime();

                if (now - time == 0) {
                    // 消息撤回时使用
                    setShowTime(message, true);
                    lastShowTimeItem = message;
                    update = true;
                } else if (now - time < (5 * 60 * 1000)) {
                    setShowTime(message, false);
                } else {
                    setShowTime(message, true);
                    update = true;
                }
            }
        }

        return update;
    }

    private void setShowTime(CustomChatMessage message, boolean show) {
        if (show) {
            timedItems.add(message.getId());
        } else {
            timedItems.remove(message.getId());
        }
    }

    private void relocateShowTimeItemAfterDelete(CustomChatMessage messageItem, int index) {
        // 如果被删的项显示了时间，需要继承
        if (needShowTime(messageItem)) {
            setShowTime(messageItem, false);
            if (getDataSize() > 0) {
                CustomChatMessage nextItem;
                if (index == getDataSize()) {
                    //删除的是最后一项
                    nextItem = getItem(index - 1);
                } else {
                    //删除的不是最后一项
                    nextItem = getItem(index);
                }

                // 增加其他不需要显示时间的消息类型判断
                if (hideTimeAlways(nextItem)) {
                    setShowTime(nextItem, false);
                    if (lastShowTimeItem != null && lastShowTimeItem != null
                            && lastShowTimeItem.getId() == messageItem.getId()) {
                        lastShowTimeItem = null;
                        for (int i = getDataSize() - 1; i >= 0; i--) {
                            CustomChatMessage item = getItem(i);
                            if (needShowTime(item)) {
                                lastShowTimeItem = item;
                                break;
                            }
                        }
                    }
                } else {
                    setShowTime(nextItem, true);
                    if (lastShowTimeItem == null
                            || (lastShowTimeItem != null && lastShowTimeItem.getId() == messageItem.getId())) {
                        lastShowTimeItem = nextItem;
                    }
                }
            } else {
                lastShowTimeItem = null;
            }
        }
    }

    private boolean hideTimeAlways(CustomChatMessage message) {
//        if (message.getSessionType() == SessionTypeEnum.ChatRoom) {
//            return true;
//        }
//        switch (message.getMsgType()) {
//            case notification:
//                return true;
//            default:
//                return false;
//        }
        return false;
    }

    public interface ViewHolderEventListener {
        // 长按事件响应处理
        boolean onViewHolderLongClick(View clickView, View viewHolderView, CustomChatMessage item);

        // 发送失败或者多媒体文件下载失败指示按钮点击响应处理
        void onFailedBtnClick(CustomChatMessage resendMessage);

        // viewholder footer按钮点击，如机器人继续会话
        void onFooterClick(CustomChatMessage message);
    }

    public void setUuid(String messageId) {
        this.messageId = messageId;
    }

    public String getUuid() {
        return messageId;
    }

    public Container getContainer() {
        return container;
    }
}
