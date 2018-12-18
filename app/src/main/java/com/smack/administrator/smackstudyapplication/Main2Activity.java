package com.smack.administrator.smackstudyapplication;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.smack.administrator.smackstudyapplication.contact.ContactFragment;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Main2Activity";
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
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission
                .requestEach(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA,
                        Manifest.permission.VIBRATE
                )
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            Log.d(TAG, permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            Log.d(TAG, permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            Log.d(TAG, permission.name + " is denied.");
                        }
                    }
                });
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

    @Override
    public void onBackPressed() {
        XmppConnection.getInstance().logout();
        super.onBackPressed();
    }
}
