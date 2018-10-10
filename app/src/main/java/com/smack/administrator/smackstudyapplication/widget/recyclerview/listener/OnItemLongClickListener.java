package com.smack.administrator.smackstudyapplication.widget.recyclerview.listener;

import android.view.View;

import com.smack.administrator.smackstudyapplication.widget.recyclerview.adapter.IRecyclerView;

public abstract class OnItemLongClickListener<T extends IRecyclerView> extends SimpleClickListener<T> {

    @Override
    public void onItemClick(T adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(T adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(T adapter, View view, int position) {
    }
}
