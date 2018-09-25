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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        jid = getIntent().getStringExtra("jid");
        chatManager = ChatManager.getInstanceFor(XmppConnection.getInstance().getConnection());
        chatManager.addIncomingListener(incomingChatMessageListener);
        chatManager.addOutgoingListener(outgoingChatMessageListener);
        XmppConnection.getInstance().getFriendChat(jid)
                .subscribe(new Consumer<Chat>() {
                    @Override
                    public void accept(Chat chat) throws Exception {
                        chat.send("afsdgf");
                    }
                });
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
