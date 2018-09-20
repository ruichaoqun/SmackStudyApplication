package com.smack.administrator.smackstudyapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ContactListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<ChatUser> chatUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initData();
    }

    private void initData() {

    }


    public class Adapter extends RecyclerView.Adapter<ContactListActivity.Viewholder>{

        @Override
        public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(Viewholder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }


    public static class Viewholder extends RecyclerView.ViewHolder{
        public Viewholder(View itemView) {
            super(itemView);
        }
    }
}
