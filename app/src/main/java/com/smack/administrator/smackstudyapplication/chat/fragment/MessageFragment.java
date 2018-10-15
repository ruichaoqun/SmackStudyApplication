package com.smack.administrator.smackstudyapplication.chat.fragment;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.MessageReceipt;
import com.smack.administrator.smackstudyapplication.R;
import com.smack.administrator.smackstudyapplication.ResponseCode;
import com.smack.administrator.smackstudyapplication.XmppConnection;
import com.smack.administrator.smackstudyapplication.base.TFragment;
import com.smack.administrator.smackstudyapplication.chat.Container;
import com.smack.administrator.smackstudyapplication.chat.ModuleProxy;
import com.smack.administrator.smackstudyapplication.chat.actions.BaseAction;
import com.smack.administrator.smackstudyapplication.chat.actions.ImageAction;
import com.smack.administrator.smackstudyapplication.chat.adapter.MessageListPanelEx;
import com.smack.administrator.smackstudyapplication.chat.constant.Extras;
import com.smack.administrator.smackstudyapplication.chat.input.InputPanel;
import com.smack.administrator.smackstudyapplication.chat.session.SessionCustomization;
import com.smack.administrator.smackstudyapplication.dao.ChatUser;
import com.smack.administrator.smackstudyapplication.dao.CustomChatMessage;

import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * 聊天界面基类
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class MessageFragment extends TFragment implements ModuleProxy {

    private View rootView;

    protected static final String TAG = "MessageActivity";

    // 聊天对象
    protected String jid; // 聊天对象的JID
    protected ChatUser user;

    // modules
    protected InputPanel inputPanel;
    protected MessageListPanelEx messageListPanel;
    private Chat chat;
    private long conversationId;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parseIntent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.xmpp_message_fragment, container, false);
        return rootView;
    }

    /**
     * ***************************** life cycle *******************************
     */

    @Override
    public void onPause() {
        super.onPause();
        inputPanel.onPause();
        messageListPanel.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        messageListPanel.onResume();
        getActivity().setVolumeControlStream(AudioManager.STREAM_VOICE_CALL); // 默认使用听筒播放
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        messageListPanel.onDestroy();
        registerObservers(false);
        if (inputPanel != null) {
            inputPanel.onDestroy();
        }

    }

    public boolean onBackPressed() {
        if (inputPanel.collapse(true)) {
            return true;
        }

        if (messageListPanel.onBackPressed()) {
            return true;
        }
        return false;
    }

    public void refreshMessageList() {
        messageListPanel.refreshMessageList();
    }

    private void parseIntent() {
        jid = getArguments().getString(Extras.EXTRA_JID);
        conversationId = getArguments().getLong(Extras.EXTRA_CONVERSATION_ID);
        user = getArguments().getParcelable(Extras.EXTRA_CHAT_USER);
        CustomChatMessage anchor = (CustomChatMessage) getArguments().getSerializable(Extras.EXTRA_ANCHOR);

        Container container = new Container(getActivity(), jid,this);


        if(!TextUtils.isEmpty(jid)){
            try {
                chat = XmppConnection.getInstance().getChatManager().chatWith(JidCreate.entityBareFrom(jid));
            } catch (XmppStringprepException e) {
                onError(ResponseCode.ERROR_GET_CHAT);
                e.printStackTrace();
            }
        }

        if (messageListPanel == null) {
            messageListPanel = new MessageListPanelEx(container, rootView, anchor, false, false);
        } else {
            messageListPanel.reload(container, anchor);
        }

        if (inputPanel == null) {
            inputPanel = new InputPanel(container, rootView, getActionList(),user,conversationId);
        } else {
            inputPanel.reload(container);
        }
        registerObservers(true);
    }

    private void onError( short errorGetChat) {
        switch (errorGetChat){
            case ResponseCode.ERROR_GET_CHAT:
                //创建聊天窗口失败
                break;
        }
    }


    /**
     * ****************** 观察者 **********************
     */

    private void registerObservers(boolean register) {

    }

    /**
     * 消息接收观察者
     */
    Observer<List<CustomChatMessage>> incomingMessageObserver = new Observer<List<CustomChatMessage>>() {
        @Override
        public void onEvent(List<CustomChatMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                return;
            }

            messageListPanel.onIncomingMessage(messages);
        }
    };

    private Observer<List<MessageReceipt>> messageReceiptObserver = new Observer<List<MessageReceipt>>() {
        @Override
        public void onEvent(List<MessageReceipt> messageReceipts) {
        }
    };


    /**
     * ********************** implements ModuleProxy *********************
     */
    @Override
    public boolean sendMessage(CustomChatMessage message) {
        final CustomChatMessage msg = message;
        XmppConnection.getInstance().sendMessage(chat,msg)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
//                        sendFailWithBlackList(code, msg);
                    }
                });
        messageListPanel.onMsgSend(message);
        return true;
    }


    @Override
    public void onInputPanelExpand() {
        messageListPanel.scrollToBottom();
    }

    @Override
    public void shouldCollapseInputPanel() {
        inputPanel.collapse(false);
    }

    @Override
    public boolean isLongClickEnabled() {
        return !inputPanel.isRecording();
    }

    @Override
    public void onItemFooterClick(CustomChatMessage message) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputPanel.onActivityResult(requestCode, resultCode, data);
        messageListPanel.onActivityResult(requestCode, resultCode, data);
    }

    // 操作面板集合
    protected List<BaseAction> getActionList() {
        List<BaseAction> actions = new ArrayList<>();
        actions.add(new ImageAction());
//        actions.add(new VideoAction());
//        actions.add(new LocationAction());

//        if (customization != null && customization.actions != null) {
//            actions.addAll(customization.actions);
//        }
        return actions;
    }
}
