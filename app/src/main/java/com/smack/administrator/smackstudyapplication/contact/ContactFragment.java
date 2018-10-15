package com.smack.administrator.smackstudyapplication.contact;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smack.administrator.smackstudyapplication.ChatActivity;
import com.smack.administrator.smackstudyapplication.R;
import com.smack.administrator.smackstudyapplication.XmppConnection;
import com.smack.administrator.smackstudyapplication.chat.activity.P2PMessageActivity;
import com.smack.administrator.smackstudyapplication.dao.ChatUser;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment implements ContactAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private ContactAdapter adapter;


    public ContactFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContactAdapter();
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        initData();
    }

    private void initData() {
        XmppConnection.getInstance().getAllEntries()
                .subscribe(new Consumer<List<ChatUser>>() {
                    @Override
                    public void accept(List<ChatUser> rosterEntries) throws Exception {
                        adapter.setData(rosterEntries);
                        adapter.notifyDataSetChanged();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    @Override
    public void onItemClick(ContactAdapter.ViewHolder holder, int position) {
        ChatUser user = adapter.getItem(position);
        if(user != null){
            P2PMessageActivity.launchFrom(getContext(),user,-1);
        }
    }
}
