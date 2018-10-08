package com.smack.administrator.smackstudyapplication.contact;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smack.administrator.smackstudyapplication.R;
import com.smack.administrator.smackstudyapplication.XmppConnection;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {
    private RecyclerView recyclerView;


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

        initData();
    }

    private void initData() {
        XmppConnection.getInstance().getAllEntries()
                .subscribe(new Consumer<List<RosterEntry>>() {
                    @Override
                    public void accept(List<RosterEntry> rosterEntries) throws Exception {
                        ContactAdapter adapter = new ContactAdapter(rosterEntries);
                        recyclerView.setAdapter(adapter);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }
}
