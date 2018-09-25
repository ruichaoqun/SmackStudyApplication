package com.smack.administrator.smackstudyapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.Collection;
import java.util.List;

import io.reactivex.functions.Consumer;

public class ContactListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<RosterEntry> chatUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initData();
    }

    private void initData() {
        XmppConnection.getInstance().getAllEntries()
                .subscribe(new Consumer<List<RosterEntry>>() {
                    @Override
                    public void accept(List<RosterEntry> rosterEntries) throws Exception {
                        chatUsers = rosterEntries;
                        Adapter adapter = new Adapter();
                        recyclerView.setAdapter(adapter);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }


    public class Adapter extends RecyclerView.Adapter<ContactListActivity.Viewholder>{

        @Override
        public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter,parent,false);
            return new Viewholder(view);
        }

        @Override
        public void onBindViewHolder(Viewholder holder, int position) {
            final RosterEntry entry = chatUsers.get(position);
            holder.tvNick.setText(entry.getJid());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return chatUsers.size();
        }
    }


    public static class Viewholder extends RecyclerView.ViewHolder{
        private ImageView ivAvatar;
        private TextView tvNick;
        private TextView tvContent;
        private TextView tvTime;

        public Viewholder(View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvNick = itemView.findViewById(R.id.tv_nickname);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}
