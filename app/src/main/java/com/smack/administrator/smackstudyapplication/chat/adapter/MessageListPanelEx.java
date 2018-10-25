package com.smack.administrator.smackstudyapplication.chat.adapter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.smack.administrator.smackstudyapplication.R;
import com.smack.administrator.smackstudyapplication.RequestCallback;
import com.smack.administrator.smackstudyapplication.RequestCallbackWrapper;
import com.smack.administrator.smackstudyapplication.ResponseCode;
import com.smack.administrator.smackstudyapplication.XmppConnection;
import com.smack.administrator.smackstudyapplication.chat.Container;
import com.smack.administrator.smackstudyapplication.chat.QueryDirectionEnum;
import com.smack.administrator.smackstudyapplication.chat.upload.AttachmentProgress;
import com.smack.administrator.smackstudyapplication.dao.CustomChatMessage;
import com.smack.administrator.smackstudyapplication.dao.MsgStatusEnum;
import com.smack.administrator.smackstudyapplication.util.BitmapDecoder;
import com.smack.administrator.smackstudyapplication.util.sys.ScreenUtil;
import com.smack.administrator.smackstudyapplication.widget.recyclerview.adapter.BaseFetchLoadAdapter;
import com.smack.administrator.smackstudyapplication.widget.recyclerview.adapter.IRecyclerView;
import com.smack.administrator.smackstudyapplication.widget.recyclerview.listener.OnItemClickListener;
import com.smack.administrator.smackstudyapplication.widget.recyclerview.loadmore.MsgListFetchLoadMoreView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.functions.Consumer;

/**
 * 基于RecyclerView的消息收发模块
 * Created by huangjun on 2016/12/27.
 */
public class MessageListPanelEx {

    private static final int REQUEST_CODE_FORWARD_PERSON = 0x01;
    private static final int REQUEST_CODE_FORWARD_TEAM = 0x02;

    // container
    private Container container;
    private View rootView;

    // message list view
    private RecyclerView messageListView;
    private List<CustomChatMessage> items;
    private MsgAdapter adapter;
    private ImageView listviewBk;

    private Handler uiHandler;
    private Gson gson = new Gson();

    private long conversationId;

    // 语音转文字
//    private VoiceTrans voiceTrans;

    // 待转发消息
//    private CustomChatMessage forwardMessage;

    // 背景图片缓存
    private static Pair<String, Bitmap> background;

    //如果在发需要拍照 的消息时，拍照回来时页面可能会销毁重建，重建时会在MessageLoader 的构造方法中调一次 loadFromLocal
    //而在发送消息后，list 需要滚动到底部，又会通过RequestFetchMoreListener 调用一次 loadFromLocal
    //所以消息会重复
    private boolean mIsInitFetchingLocal;

    public MessageListPanelEx(Container container, View rootView, long conversationId) {
        this(container, rootView, null, conversationId);
    }

    public MessageListPanelEx(Container container, View rootView, CustomChatMessage anchor,long conversationId) {
        this.container = container;
        this.rootView = rootView;
        this.conversationId = conversationId;

        init(anchor);
    }

    public void onResume() {
//        setEarPhoneMode(UserPreferences.isEarPhoneModeEnable(), false);
    }

    public void onPause() {
        //TODO 音频暂停
//        MessageAudioControl.getInstance(container.activity).stopAudio();
    }

    public void onDestroy() {
        registerObservers(false);
    }

    public boolean onBackPressed() {
        uiHandler.removeCallbacks(null);
        //TODO 音频暂停
//        MessageAudioControl.getInstance(container.activity).stopAudio(); // 界面返回，停止语音播放
//        if (voiceTrans != null && voiceTrans.isShow()) {
//            voiceTrans.hide();
//            return true;
//        }
        return false;
    }

    public void reload(Container container) {
        this.container = container;
        if (adapter != null) {
            adapter.clearData();
        }

        initFetchLoadListener();
    }

    private void init(CustomChatMessage anchor) {
        initListView(anchor);

        this.uiHandler = new Handler();

        registerObservers(true);
    }

    private void initListView(CustomChatMessage anchor) {
        listviewBk = (ImageView) rootView.findViewById(R.id.message_activity_background);

        // RecyclerView
        messageListView = (RecyclerView) rootView.findViewById(R.id.messageListView);
        messageListView.setLayoutManager(new LinearLayoutManager(container.activity));
        messageListView.requestDisallowInterceptTouchEvent(true);
        messageListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    container.proxy.shouldCollapseInputPanel();
                }
            }
        });
        messageListView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        // adapter
        items = new ArrayList<>();
        adapter = new MsgAdapter(messageListView, items, container);
        adapter.setFetchMoreView(new MsgListFetchLoadMoreView());
        adapter.setLoadMoreView(new MsgListFetchLoadMoreView());
        //注册各种监听器
//        adapter.setEventListener(new MsgItemEventListener());
        initFetchLoadListener();
        messageListView.setAdapter(adapter);
        messageListView.addOnItemTouchListener(listener);
    }

    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(IRecyclerView adapter, View view, int position) {

        }

        @Override
        public void onItemLongClick(IRecyclerView adapter, View view, int position) {
        }

        @Override
        public void onItemChildClick(IRecyclerView adapter2, View view, int position) {
//            if (isSessionMode() && view != null && view instanceof RobotLinkView) {
//                RobotLinkView robotLinkView = (RobotLinkView) view;
//                // robotLinkView.onClick();
//                LinkElement element = robotLinkView.getElement();
//                if (element != null) {
//                    element.getTarget();
//                    if (LinkElement.TYPE_URL.equals(element.getType())) {
//                        Intent intent = new Intent();
//                        intent.setAction("android.intent.action.VIEW");
//                        Uri content_url = Uri.parse(element.getTarget());
//                        intent.setData(content_url);
//                        try {
//                            container.activity.startActivity(intent);
//                        } catch (ActivityNotFoundException e) {
//                            Toast.makeText(container.activity, "路径错误", Toast.LENGTH_SHORT).show();
//                        }
//
//                    } else if (LinkElement.TYPE_BLOCK.equals(element.getType())) {
//                        // 发送点击的block
//                        CustomChatMessage message = adapter.getItem(position);
//                        if (message != null) {
//                            String robotAccount = ((RobotAttachment) message.getAttachment()).getFromRobotAccount();
//                            CustomChatMessage robotMsg = MessageBuilder.createRobotMessage(message.getSessionId(), message.getSessionType(), robotAccount,
//                                    robotLinkView.getShowContent(), RobotMsgType.LINK, "", element.getTarget(), element.getParams());
//                            container.proxy.sendMessage(robotMsg);
//                        }
//                    }
//                }
//            }
        }
    };

    private void initFetchLoadListener() {
        MessageLoader loader = new MessageLoader();
        // 只下来加载old数据
        adapter.setOnFetchMoreListener(loader);
    }

    // 刷新消息列表
    public void refreshMessageList() {
        container.activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void scrollToBottom() {
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doScrollToBottom();
            }
        }, 200);
    }

    private void doScrollToBottom() {
        messageListView.scrollToPosition(adapter.getBottomDataPosition());
    }

    public void onIncomingMessage(CustomChatMessage message) {
        boolean needScrollToBottom = isLastMessageVisible();
        List<CustomChatMessage> addedListItems = new ArrayList<>(1);
        items.add(message);
        addedListItems.add(message);
        adapter.updateShowTimeItem(addedListItems, true);
        adapter.notifyDataSetChanged();

        if(needScrollToBottom){
            doScrollToBottom();
        }
    }

    private boolean isLastMessageVisible() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) messageListView.getLayoutManager();
        int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
        return lastVisiblePosition >= adapter.getBottomDataPosition();
    }

    // 发送消息后，更新本地消息列表
    public void onMsgSend(CustomChatMessage message) {
        //todo
        List<CustomChatMessage> addedListItems = new ArrayList<>(1);
        addedListItems.add(message);
        adapter.updateShowTimeItem(addedListItems, true);
        adapter.appendData(message);

        doScrollToBottom();
    }

    //消息状态更新
    public void updateMsgStatu(CustomChatMessage message) {
        for (int i = 0; i < items.size(); i++) {
            if( TextUtils.equals(message.getUuid(),items.get(i).getUuid())){
                items.get(i).setMsgStatusEnum(message.getMsgStatusEnum());
                refreshViewHolderByIndex(i);
            }
        }

    }

    /**
     * **************************** 排序 ***********************************
     */
    private void sortMessages(List<CustomChatMessage> list) {
        if (list.size() == 0) {
            return;
        }
        Collections.sort(list, comp);
    }

    private static Comparator<CustomChatMessage> comp = new Comparator<CustomChatMessage>() {

        @Override
        public int compare(CustomChatMessage o1, CustomChatMessage o2) {
            long time = o1.getTime() - o2.getTime();
            return time == 0 ? 0 : (time < 0 ? -1 : 1);
        }
    };

    /**
     * ************************* 观察者 ********************************
     */

    private void registerObservers(boolean register) {
        XmppConnection.getInstance().setXmppAttachProgressListener(xmppAttachProgressListener);
//        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
//        service.observeMsgStatus(messageStatusObserver, register);
//        service.observeAttachmentProgress(attachmentProgressObserver, register);
//        service.observeRevokeMessage(revokeMessageObserver, register);
//        if (register) {
//            registerUserInfoObserver();
//        } else {
//            unregisterUserInfoObserver();
//        }
//        service.observeTeamMessageReceipt(teamMessageReceiptObserver, register);
//
//        MessageListPanelHelper.getInstance().registerObserver(incomingLocalMessageObserver, register);
    }

    private XmppConnection.XmppAttachProgressListener xmppAttachProgressListener = new XmppConnection.XmppAttachProgressListener() {
        @Override
        public void xmppAttachmentProgress(AttachmentProgress attachmentProgress) {
            onAttachmentProgressChange(attachmentProgress);
        }
    };

//    /**
//     * 消息状态变化观察者
//     */
//    private Observer<CustomChatMessage> messageStatusObserver = new Observer<CustomChatMessage>() {
//        @Override
//        public void onEvent(CustomChatMessage message) {
//            if (isMyMessage(message)) {
//                onMessageStatusChange(message);
//            }
//        }
//    };

    /**
     * 消息附件上传/下载进度观察者
     */
//    private Observer<AttachmentProgress> attachmentProgressObserver = new Observer<AttachmentProgress>() {
//        @Override
//        public void onEvent(AttachmentProgress progress) {
//            onAttachmentProgressChange(progress);
//        }
//    };

    /**
     * 本地消息接收观察者
     */
//    private MessageListPanelHelper.LocalMessageObserver incomingLocalMessageObserver = new MessageListPanelHelper.LocalMessageObserver() {
//        @Override
//        public void onAddMessage(CustomChatMessage message) {
//            if (message == null || !container.account.equals(message.getSessionId())) {
//                return;
//            }
//
//            onMsgSend(message);
//        }
//
//        @Override
//        public void onClearMessages(String account) {
//            items.clear();
//            refreshMessageList();
//            adapter.fetchMoreEnd(null, true);
//        }
//    };

    /**
     * 消息撤回观察者
     */
//    private Observer<RevokeMsgNotification> revokeMessageObserver = new Observer<RevokeMsgNotification>() {
//        @Override
//        public void onEvent(RevokeMsgNotification notification) {
//            if (notification == null || notification.getMessage() == null) {
//                return;
//            }
//            CustomChatMessage message = notification.getMessage();
//
//            if (!container.account.equals(message.getSessionId())) {
//                return;
//            }
//
//            deleteItem(message, false);
//        }
//    };

//    private Observer<List<TeamMessageReceipt>> teamMessageReceiptObserver = new Observer<List<TeamMessageReceipt>>() {
//        @Override
//        public void onEvent(List<TeamMessageReceipt> teamMessageReceipts) {
//            for (TeamMessageReceipt teamMessageReceipt : teamMessageReceipts) {
//                int index = getItemIndex(teamMessageReceipt.getMsgId());
//                if (index >= 0 && index < items.size()) {
//                    refreshViewHolderByIndex(index);
//                }
//            }
//        }
//    };

    private void onMessageStatusChange(CustomChatMessage message) {
        int index = getItemIndex(message.getUuid());
        if (index >= 0 && index < items.size()) {
            CustomChatMessage item = items.get(index);
            item.setMsgStatusEnum(message.getMsgStatusEnum());
//            item.setAttachStatus(message.getAttachStatus());

            // resend的的情况，可能时间已经变化了，这里要重新检查是否要显示时间
            List<CustomChatMessage> msgList = new ArrayList<>(1);
            msgList.add(message);

            refreshViewHolderByIndex(index);
        }
    }

    private void onAttachmentProgressChange(AttachmentProgress progress) {
        int index = getItemIndex(progress.getUuid());
        if (index >= 0 && index < items.size()){
            CustomChatMessage item = items.get(index);
            float value = (float) progress.getTransferred() / (float) progress.getTotal();
            adapter.putProgress(item, value);
            refreshViewHolderByIndex(index);
        }
    }

    public boolean isMyMessage(CustomChatMessage message) {
        return TextUtils.equals(message.getSendUserName(), XmppConnection.getInstance().getCurrentUserName());
    }

    /**
     * 刷新单条消息
     */
    private void refreshViewHolderByIndex(final int index) {
        container.activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (index < 0) {
                    return;
                }

                adapter.notifyDataItemChanged(index);
            }
        });
    }

    private int getItemIndex(String uuid) {
        for (int i = 0; i < items.size(); i++) {
            CustomChatMessage message = items.get(i);
            if (TextUtils.equals(message.getUuid(), uuid)) {
                return i;
            }
        }

        return -1;
    }

    public void setChattingBackground(String uriString, int color) {
        if (uriString != null) {
            Uri uri = Uri.parse(uriString);
            if (uri.getScheme().equalsIgnoreCase("file") && uri.getPath() != null) {
                listviewBk.setImageBitmap(getBackground(uri.getPath()));
            } else if (uri.getScheme().equalsIgnoreCase("android.resource")) {
                List<String> paths = uri.getPathSegments();
                if (paths == null || paths.size() != 2) {
                    return;
                }
                String type = paths.get(0);
                String name = paths.get(1);
                String pkg = uri.getHost();
                int resId = container.activity.getResources().getIdentifier(name, type, pkg);
                if (resId != 0) {
                    listviewBk.setBackgroundResource(resId);
                }
            }
        } else if (color != 0) {
            listviewBk.setBackgroundColor(color);
        }
    }



    /**
     * ***************************************** 数据加载 *********************************************
     */

    private class MessageLoader implements BaseFetchLoadAdapter.RequestFetchMoreListener {
        //默认每次加载20条数据
        private int loadMsgCount = 20;

        private boolean firstLoad = true;

        public MessageLoader() {
            if(firstLoad){
                loadFromLocal();
                mIsInitFetchingLocal = true;
            }
        }

        private void loadFromLocal(){
            if (mIsInitFetchingLocal) {
                return;
            }
            XmppConnection.getInstance().loadLocalMessage(conversationId)
                    .subscribe(new Consumer<List<CustomChatMessage>>() {
                        @Override
                        public void accept(List<CustomChatMessage> customChatMessages) throws Exception {
                            mIsInitFetchingLocal = false;
                            onMessageLoaded(customChatMessages);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            mIsInitFetchingLocal = false;
                            adapter.fetchMoreFailed();
                        }
                    });
        }


        private void onMessageLoaded(final List<CustomChatMessage> messages) {

            if (messages == null) {
                return;
            }

            boolean noMoreMessage = messages.size() < loadMsgCount;

            // 在第一次加载的过程中又收到了新消息，做一下去重
            if (firstLoad && items.size() > 0) {
                for (CustomChatMessage message : messages) {
                    int removeIndex = 0;
                    for (CustomChatMessage item : items) {
                        if (TextUtils.equals(item.getUuid(),message.getUuid())) {
                            adapter.remove(removeIndex);
                            break;
                        }
                        removeIndex++;
                    }
                }
            }

            // 在更新前，先确定一些标记
            List<CustomChatMessage> total = new ArrayList<>();
            total.addAll(0, messages);
//            updateReceipt(total); // 更新已读回执标签
            adapter.updateShowTimeItem(total, true); // 更新要显示时间的消息

            if (noMoreMessage) {
                adapter.fetchMoreEnd(messages, true);
            } else {
                adapter.fetchMoreComplete(messages);
            }
            // 如果是第一次加载，updateShowTimeItem返回的就是lastShowTimeItem
            if (firstLoad) {
                doScrollToBottom();
            }
            firstLoad = false;
        }


        @Override
        public void onFetchMoreRequested() {
            // 顶部加载历史数据
//            loadFromLocal();
        }
    }

//    private class MsgItemEventListener implements MsgAdapter.ViewHolderEventListener {
//
//        @Override
//        public void onFailedBtnClick(CustomChatMessage message) {
//            if (message.getDirect() == MsgDirectionEnum.Out) {
//                // 发出的消息，如果是发送失败，直接重发，否则有可能是漫游到的多媒体消息，但文件下载
//                if (message.getStatus() == MsgStatusEnum.fail) {
//                    resendMessage(message); // 重发
//                } else {
//                    if (message.getAttachment() instanceof FileAttachment) {
//                        FileAttachment attachment = (FileAttachment) message.getAttachment();
//                        if (TextUtils.isEmpty(attachment.getPath())
//                                && TextUtils.isEmpty(attachment.getThumbPath())) {
//                            showReDownloadConfirmDlg(message);
//                        }
//                    } else {
//                        resendMessage(message);
//                    }
//                }
//            } else {
//                showReDownloadConfirmDlg(message);
//            }
//        }
//
//        @Override
//        public boolean onViewHolderLongClick(View clickView, View viewHolderView, CustomChatMessage item) {
//            if (container.proxy.isLongClickEnabled()) {
//                showLongClickAction(item);
//            }
//            return true;
//        }
//
//        @Override
//        public void onFooterClick(CustomChatMessage message) {
//            // 与 robot 对话
//            container.proxy.onItemFooterClick(message);
//        }
//
//        // 重新下载(对话框提示)
//        private void showReDownloadConfirmDlg(final CustomChatMessage message) {
//            EasyAlertDialogHelper.OnDialogActionListener listener = new EasyAlertDialogHelper.OnDialogActionListener() {
//
//                @Override
//                public void doCancelAction() {
//                }
//
//                @Override
//                public void doOkAction() {
//                    // 正常情况收到消息后附件会自动下载。如果下载失败，可调用该接口重新下载
//                    if (message.getAttachment() != null && message.getAttachment() instanceof FileAttachment)
//                        NIMClient.getService(MsgService.class).downloadAttachment(message, true);
//                }
//            };
//
//            final EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(container.activity, null,
//                    container.activity.getString(R.string.repeat_download_message), true, listener);
//            dialog.show();
//        }
//
//        // 重发消息到服务器
//        private void resendMessage(CustomChatMessage message) {
//            // 重置状态为unsent
//            int index = getItemIndex(message.getUuid());
//            if (index >= 0 && index < items.size()) {
//                CustomChatMessage item = items.get(index);
//                item.setStatus(MsgStatusEnum.sending);
//                deleteItem(item, true);
//                onMsgSend(item);
//            }
//
//            NIMClient.getService(MsgService.class).sendMessage(message, true);
//        }
//
//        /**
//         * ****************************** 长按菜单 ********************************
//         */
//
//        // 长按消息Item后弹出菜单控制
//        private void showLongClickAction(CustomChatMessage selectedItem) {
//            onNormalLongClick(selectedItem);
//        }
//
//        /**
//         * 长按菜单操作
//         *
//         * @param item
//         */
//        private void onNormalLongClick(CustomChatMessage item) {
//            CustomAlertDialog alertDialog = new CustomAlertDialog(container.activity);
//            alertDialog.setCancelable(true);
//            alertDialog.setCanceledOnTouchOutside(true);
//
//            prepareDialogItems(item, alertDialog);
//            alertDialog.show();
//        }
//
//        // 长按消息item的菜单项准备。如果消息item的MsgViewHolder处理长按事件(MsgViewHolderBase#onItemLongClick),且返回为true，
//        // 则对应项的长按事件不会调用到此处
//        private void prepareDialogItems(final CustomChatMessage selectedItem, CustomAlertDialog alertDialog) {
//            MsgTypeEnum msgType = selectedItem.getMsgType();
//
//            MessageAudioControl.getInstance(container.activity).stopAudio();
//
//            // 0 EarPhoneMode
//            longClickItemEarPhoneMode(alertDialog, msgType);
//            // 1 resend
//            longClickItemResend(selectedItem, alertDialog);
//            // 2 copy
//            longClickItemCopy(selectedItem, alertDialog, msgType);
//            // 3 revoke
//            if (enableRevokeButton(selectedItem)) {
//                longClickRevokeMsg(selectedItem, alertDialog);
//            }
//            // 4 delete
//            longClickItemDelete(selectedItem, alertDialog);
//            // 5 trans
//            longClickItemVoidToText(selectedItem, alertDialog, msgType);
//
//            if (!NimUIKitImpl.getMsgForwardFilter().shouldIgnore(selectedItem) && !recordOnly) {
//                // 6 forward to person
//                longClickItemForwardToPerson(selectedItem, alertDialog);
//                // 7 forward to team
//                longClickItemForwardToTeam(selectedItem, alertDialog);
//            }
//        }
//
//        private boolean enableRevokeButton(CustomChatMessage selectedItem) {
//            if (selectedItem.getStatus() == MsgStatusEnum.success
//                    && !NimUIKitImpl.getMsgRevokeFilter().shouldIgnore(selectedItem)
//                    && !recordOnly) {
//                if (selectedItem.getDirect() == MsgDirectionEnum.Out) {
//                    return true;
//                } else if (NimUIKit.getOptions().enableTeamManagerRevokeMsg && selectedItem.getSessionType() == SessionTypeEnum.Team) {
//                    TeamMember member = NimUIKit.getTeamProvider().getTeamMember(selectedItem.getSessionId(), NimUIKit.getAccount());
//                    return member != null && member.getType() == TeamMemberType.Owner || member.getType() == TeamMemberType.Manager;
//                }
//            }
//            return false;
//        }
//
//        // 长按菜单项--重发
//        private void longClickItemResend(final CustomChatMessage item, CustomAlertDialog alertDialog) {
//            if (item.getStatus() != MsgStatusEnum.fail) {
//                return;
//            }
//            alertDialog.addItem(container.activity.getString(R.string.repeat_send_has_blank), new CustomAlertDialog.onSeparateItemClickListener() {
//
//                @Override
//                public void onClick() {
//                    onResendMessageItem(item);
//                }
//            });
//        }
//
//        private void onResendMessageItem(CustomChatMessage message) {
//            int index = getItemIndex(message.getUuid());
//            if (index >= 0) {
//                showResendConfirm(message, index); // 重发确认
//            }
//        }
//
//        private void showResendConfirm(final CustomChatMessage message, final int index) {
//            EasyAlertDialogHelper.OnDialogActionListener listener = new EasyAlertDialogHelper.OnDialogActionListener() {
//
//                @Override
//                public void doCancelAction() {
//                }
//
//                @Override
//                public void doOkAction() {
//                    resendMessage(message);
//                }
//            };
//            final EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(container.activity, null,
//                    container.activity.getString(R.string.repeat_send_message), true, listener);
//            dialog.show();
//        }
//
//        // 长按菜单项--复制
//        private void longClickItemCopy(final CustomChatMessage item, CustomAlertDialog alertDialog, MsgTypeEnum msgType) {
//            if (msgType == MsgTypeEnum.text ||
//                    (msgType == MsgTypeEnum.robot && item.getAttachment() != null && !((RobotAttachment) item.getAttachment()).isRobotSend())) {
//                alertDialog.addItem(container.activity.getString(R.string.copy_has_blank), new CustomAlertDialog.onSeparateItemClickListener() {
//
//                    @Override
//                    public void onClick() {
//                        onCopyMessageItem(item);
//                    }
//                });
//            }
//        }
//
//        private void onCopyMessageItem(CustomChatMessage item) {
//            ClipboardUtil.clipboardCopyText(container.activity, item.getContent());
//        }
//
//        // 长按菜单项--删除
//        private void longClickItemDelete(final CustomChatMessage selectedItem, CustomAlertDialog alertDialog) {
//            if (recordOnly) {
//                return;
//            }
//            alertDialog.addItem(container.activity.getString(R.string.delete_has_blank), new CustomAlertDialog.onSeparateItemClickListener() {
//
//                @Override
//                public void onClick() {
//                    deleteItem(selectedItem, true);
//                }
//            });
//        }
//
//        // 长按菜单项 -- 音频转文字
//        private void longClickItemVoidToText(final CustomChatMessage item, CustomAlertDialog alertDialog, MsgTypeEnum msgType) {
//            if (msgType != MsgTypeEnum.audio) return;
//
//            if (item.getDirect() == MsgDirectionEnum.In
//                    && item.getAttachStatus() != AttachStatusEnum.transferred)
//                return;
//            if (item.getDirect() == MsgDirectionEnum.Out
//                    && item.getAttachStatus() != AttachStatusEnum.transferred)
//                return;
//
//            alertDialog.addItem(container.activity.getString(R.string.voice_to_text), new CustomAlertDialog.onSeparateItemClickListener() {
//
//                @Override
//                public void onClick() {
//                    onVoiceToText(item);
//                }
//            });
//        }
//
//        // 语音转文字
//        private void onVoiceToText(CustomChatMessage item) {
//            if (voiceTrans == null)
//                voiceTrans = new VoiceTrans(container.activity);
//            voiceTrans.voiceToText(item);
//            if (item.getDirect() == MsgDirectionEnum.In && item.getStatus() != MsgStatusEnum.read) {
//                item.setStatus(MsgStatusEnum.read);
//                NIMClient.getService(MsgService.class).updateCustomChatMessageStatus(item);
//                adapter.notifyDataSetChanged();
//            }
//        }
//
//        // 长按菜单项 -- 听筒扬声器切换
//        private void longClickItemEarPhoneMode(CustomAlertDialog alertDialog, MsgTypeEnum msgType) {
//            if (msgType != MsgTypeEnum.audio) return;
//
//            String content = UserPreferences.isEarPhoneModeEnable() ? "切换成扬声器播放" : "切换成听筒播放";
//            final String finalContent = content;
//            alertDialog.addItem(content, new CustomAlertDialog.onSeparateItemClickListener() {
//
//                @Override
//                public void onClick() {
//                    Toast.makeText(container.activity, finalContent, Toast.LENGTH_SHORT).show();
//                    setEarPhoneMode(!UserPreferences.isEarPhoneModeEnable(), true);
//                }
//            });
//        }
//
//        // 长按菜单项 -- 转发到个人
////        private void longClickItemForwardToPerson(final CustomChatMessage item, CustomAlertDialog alertDialog) {
////            alertDialog.addItem(container.activity.getString(R.string.forward_to_person), new CustomAlertDialog.onSeparateItemClickListener() {
////
////                @Override
////                public void onClick() {
////                    forwardMessage = item;
////                    ContactSelectActivity.Option option = new ContactSelectActivity.Option();
////                    option.title = "选择转发的人";
////                    option.type = ContactSelectActivity.ContactSelectType.BUDDY;
////                    option.multi = false;
////                    option.maxSelectNum = 1;
////                    NimUIKit.startContactSelector(container.activity, option, REQUEST_CODE_FORWARD_PERSON);
////                }
////            });
////        }
//
//        // 长按菜单项 -- 转发到群组
////        private void longClickItemForwardToTeam(final CustomChatMessage item, CustomAlertDialog alertDialog) {
////            alertDialog.addItem(container.activity.getString(R.string.forward_to_team), new CustomAlertDialog.onSeparateItemClickListener() {
////
////                @Override
////                public void onClick() {
////                    forwardMessage = item;
////                    ContactSelectActivity.Option option = new ContactSelectActivity.Option();
////                    option.title = "选择转发的群";
////                    option.type = ContactSelectActivity.ContactSelectType.TEAM;
////                    option.multi = false;
////                    option.maxSelectNum = 1;
////                    NimUIKit.startContactSelector(container.activity, option, REQUEST_CODE_FORWARD_TEAM);
////                }
////            });
////        }
//
//        // 长按菜单项 -- 撤回消息
////        private void longClickRevokeMsg(final CustomChatMessage item, CustomAlertDialog alertDialog) {
////            alertDialog.addItem(container.activity.getString(R.string.withdrawn_msg), new CustomAlertDialog.onSeparateItemClickListener() {
////
////                @Override
////                public void onClick() {
////                    if (!NetworkUtil.isNetAvailable(container.activity)) {
////                        Toast.makeText(container.activity, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
////                        return;
////                    }
////                    NIMClient.getService(MsgService.class).revokeMessage(item).setCallback(new RequestCallback<Void>() {
////                        @Override
////                        public void onSuccess(Void param) {
////                            deleteItem(item, false);
////                            MessageHelper.getInstance().onRevokeMessage(item, NimUIKit.getAccount());
////                        }
////
////                        @Override
////                        public void onFailed(int code) {
////                            if (code == ResponseCode.RES_OVERDUE) {
////                                Toast.makeText(container.activity, R.string.revoke_failed, Toast.LENGTH_SHORT).show();
////                            } else {
////                                Toast.makeText(container.activity, "revoke msg failed, code:" + code, Toast.LENGTH_SHORT).show();
////                            }
////                        }
////
////                        @Override
////                        public void onException(Throwable exception) {
////
////                        }
////                    });
////                }
////            });
////        }
////
//    }


    private Bitmap getBackground(String path) {
        if (background != null && path.equals(background.first) && background.second != null) {
            return background.second;
        }

        if (background != null && background.second != null) {
            background.second.recycle();
        }

        Bitmap bitmap = null;
        if (path.startsWith("/android_asset")) {
            String asset = path.substring(path.indexOf("/", 1) + 1);
            try {
                InputStream ais = container.activity.getAssets().open(asset);
                bitmap = BitmapDecoder.decodeSampled(ais, ScreenUtil.screenWidth, ScreenUtil.screenHeight);
                ais.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            bitmap = BitmapDecoder.decodeSampled(path, ScreenUtil.screenWidth, ScreenUtil.screenHeight);
        }
        background = new Pair<>(path, bitmap);
        return bitmap;
    }

//    private UserInfoObserver uinfoObserver;
//
//    private void registerUserInfoObserver() {
//        if (uinfoObserver == null) {
//            uinfoObserver = new UserInfoObserver() {
//                @Override
//                public void onUserInfoChanged(List<String> accounts) {
//                    if (container.sessionType == SessionTypeEnum.P2P) {
//                        if (accounts.contains(container.account) || accounts.contains(NimUIKit.getAccount())) {
//                            adapter.notifyDataSetChanged();
//                        }
//                    } else { // 群的，简单的全部重刷
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//            };
//        }
//
//        NimUIKit.getUserInfoObservable().registerObserver(uinfoObserver, true);
//    }

//    private void unregisterUserInfoObserver() {
//        if (uinfoObserver != null) {
//            NimUIKit.getUserInfoObservable().registerObserver(uinfoObserver, false);
//        }
//    }

//    private CustomChatMessage getLastReceivedMessage() {
//        CustomChatMessage lastMessage = null;
//        for (int i = items.size() - 1; i >= 0; i--) {
//            if (sendReceiptCheck(items.get(i))) {
//                lastMessage = items.get(i);
//                break;
//            }
//        }
//
//        return lastMessage;
//    }


//    // 删除消息
//    private void deleteItem(CustomChatMessage messageItem, boolean isRelocateTime) {
//        NIMClient.getService(MsgService.class).deleteChattingHistory(messageItem);
//        List<CustomChatMessage> messages = new ArrayList<>();
//        for (CustomChatMessage message : items) {
//            if (message.getUuid().equals(messageItem.getUuid())) {
//                continue;
//            }
//            messages.add(message);
//        }
//        updateReceipt(messages);
//        adapter.deleteItem(messageItem, isRelocateTime);
//    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode != Activity.RESULT_OK) {
//            return;
//        }
//        final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
//        if (selected != null && !selected.isEmpty()) {
//            switch (requestCode) {
//                case REQUEST_CODE_FORWARD_TEAM:
//                    doForwardMessage(selected.get(0), SessionTypeEnum.Team);
//                    break;
//                case REQUEST_CODE_FORWARD_PERSON:
//                    doForwardMessage(selected.get(0), SessionTypeEnum.P2P);
//                    break;
//            }
//        }
    }

    // 转发消息
//    private void doForwardMessage(final String sessionId, SessionTypeEnum sessionTypeEnum) {
//        CustomChatMessage message;
//        if (forwardMessage.getMsgType() == MsgTypeEnum.robot) {
//            message = buildForwardRobotMessage(sessionId, sessionTypeEnum);
//        } else {
//            message = MessageBuilder.createForwardMessage(forwardMessage, sessionId, sessionTypeEnum);
//        }
//
//        if (message == null) {
//            Toast.makeText(container.activity, "该类型不支持转发", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        NIMClient.getService(MsgService.class).sendMessage(message, false);
//        if (container.account.equals(sessionId)) {
//            onMsgSend(message);
//        }
//    }

}
