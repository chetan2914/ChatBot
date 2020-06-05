package com.example.chatbot;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.ConversationActions;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class customadapter extends BaseAdapter {
    Context con;
    List<Message> msglist;
    public customadapter(Context context,List<Message> msg){
        this.con=context;
        this.msglist=msg;
    }
    @Override
    public int getCount() {
        return msglist.size();
    }

    @Override
    public Object getItem(int position) {
        return msglist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message m = msglist.get(position);

        LayoutInflater mInflater = (LayoutInflater) con
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (msglist.get(position).isSelf()) {
            convertView = mInflater.inflate(R.layout.messagelytright,
                    null);
        } else {
            convertView = mInflater.inflate(R.layout.messagerecievelyt,
                    null);
        }

        //TextView lblFrom = (TextView) convertView.findViewById(R.id.lblMsgFrom);
        TextView txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);

        txtMsg.setText(m.getMessage());
        //lblFrom.setText(m.getChatBotName());

        return convertView;
    }
}
