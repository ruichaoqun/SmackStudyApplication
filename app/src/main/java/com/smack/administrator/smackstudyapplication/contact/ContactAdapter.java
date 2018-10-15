package com.smack.administrator.smackstudyapplication.contact;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smack.administrator.smackstudyapplication.R;
import com.smack.administrator.smackstudyapplication.dao.ChatUser;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description.</p>
 * <p>
 * <b>Maintenance History</b>:
 * <table>
 * <tr>
 * <th>Date</th>
 * <th>Developer</th>
 * <th>Target</th>
 * <th>Content</th>
 * </tr>
 * <tr>
 * <td>2018-09-28 17:34</td>
 * <td>${User}</td>
 * <td>All</td>
 * <td>Created.</td>
 * </tr>
 * </table>
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private List<ChatUser> list;
    private OnItemClickListener onItemClickListener;

    public ContactAdapter() {
        list = new ArrayList<>();
    }

    public ContactAdapter(List<ChatUser> list) {
        this.list = list;
    }

    public ChatUser getItem(int position){
        if(list == null )
            return null;
        if(list.size() < position){
            return null;
        }
        return list.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_adapter_contact,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ChatUser user = list.get(position);
        holder.nickname.setText(user.getUserNick());
        holder.content.setText(user.getJid());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(holder,position);
                }
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<ChatUser> rosterEntries) {
        this.list = rosterEntries;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView avatar;
        TextView nickname;
        TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.iv_avatar);
            nickname = itemView.findViewById(R.id.tv_nickname);
            content = itemView.findViewById(R.id.tv_content);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(ViewHolder holder,int position);
    }
}
