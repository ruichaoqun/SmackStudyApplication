package com.smack.administrator.smackstudyapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.stringprep.XmppStringprepException;

import java.net.InetAddress;
import java.net.UnknownHostException;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = "MainActivity";
    private Button login;
    private Button send;
    private Button register;
    private TextView textView;
    private EditText editText;

    private StringBuilder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.login);
        send = findViewById(R.id.send);
        textView = findViewById(R.id.text);
        editText = findViewById(R.id.et);
        register = findViewById(R.id.register);
        builder = new StringBuilder();

        login.setOnClickListener(this);
        send.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                XmppConnection.getInstance().login("test1","123456")
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                builder.append("登录成功").append("\n");
                                textView.setText(builder.toString());
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                builder.append("登录失败").append("\n");
                                builder.append(throwable.getMessage()).append("\n");
                                textView.setText(builder.toString());
                            }
                        });
                break;
            case R.id.register:
                XmppConnection.getInstance().register("test3","123456")
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                builder.append("注册成功").append("\n");
                                textView.setText(builder.toString());
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                builder.append("注册失败").append("\n");
                                builder.append(throwable.getMessage()).append("\n");
                                textView.setText(builder.toString());
                            }
                        });
                break;
            case R.id.et:
                break;
        }
    }
}
