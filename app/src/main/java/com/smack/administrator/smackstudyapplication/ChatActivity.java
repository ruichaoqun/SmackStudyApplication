package com.smack.administrator.smackstudyapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;

import io.reactivex.functions.Consumer;

public class ChatActivity extends AppCompatActivity {
    private String jid;
    private Chat chat;
    private ChatManager chatManager;
    protected EaseChatInputMenu inputMenu;
    static final int ITEM_TAKE_PICTURE = 1;
    static final int ITEM_PICTURE = 2;
    static final int ITEM_LOCATION = 3;

    protected int[] itemStrings = { R.string.attach_take_pic, R.string.attach_picture, R.string.attach_location };
    protected int[] itemdrawables = { R.drawable.ease_chat_takepic_selector, R.drawable.ease_chat_image_selector,
            R.drawable.ease_chat_location_selector };
    protected int[] itemIds = { ITEM_TAKE_PICTURE, ITEM_PICTURE, ITEM_LOCATION };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        jid = getIntent().getStringExtra("jid");
        chatManager = ChatManager.getInstanceFor(XmppConnection.getInstance().getConnection());
        chatManager.addIncomingListener(incomingChatMessageListener);
        chatManager.addOutgoingListener(outgoingChatMessageListener);
    }

    private void initView() {
        inputMenu = findViewById(R.id.input_menu);
        inputMenu.init(null);
    }


    private IncomingChatMessageListener incomingChatMessageListener = new IncomingChatMessageListener() {
        @Override
        public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {

        }
    };

    private OutgoingChatMessageListener outgoingChatMessageListener = new OutgoingChatMessageListener() {
        @Override
        public void newOutgoingMessage(EntityBareJid to, Message message, Chat chat) {

        }
    };
}
