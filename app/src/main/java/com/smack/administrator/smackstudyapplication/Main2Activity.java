package com.smack.administrator.smackstudyapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.smack.administrator.smackstudyapplication.contact.ContactFragment;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView title;
    private TextView add;

    private ContactFragment contactFragment;
    private ConversationFragment conversationFragment;
    private int currentIndex;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if(currentIndex != 0){
                        getSupportFragmentManager().beginTransaction().hide(contactFragment).show(conversationFragment).commit();
                        currentIndex = 0;
                        title.setText("会话列表");
                    }
                    break;
                case R.id.navigation_dashboard:
                    if(currentIndex != 1){
                        getSupportFragmentManager().beginTransaction().show(contactFragment).hide(conversationFragment).commit();
                        currentIndex = 1;
                        title.setText("好友列表");
                    }
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        title = findViewById(R.id.tv_title);
        add = findViewById(R.id.tv_add);
        add.setOnClickListener(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initFragments();
    }

    private void initFragments() {
        contactFragment = new ContactFragment();
        conversationFragment = new ConversationFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.framlayout,contactFragment)
                .add(R.id.framlayout,conversationFragment).hide(contactFragment).show(conversationFragment).commit();
        title.setText("会话列表");
    }

    @Override
    public void onClick(View v) {

    }
}
