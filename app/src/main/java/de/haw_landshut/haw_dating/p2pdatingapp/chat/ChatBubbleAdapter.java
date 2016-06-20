package de.haw_landshut.haw_dating.p2pdatingapp.chat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.haw_landshut.haw_dating.p2pdatingapp.ChatActivity;
import de.haw_landshut.haw_dating.p2pdatingapp.R;

public class ChatBubbleAdapter extends BaseAdapter {
    List<ChatBubble> chatBubbleList;

    public ChatBubbleAdapter(List<ChatBubble> chatBubbleList) {
        this.chatBubbleList = chatBubbleList;
    }

    @Override
    public int getCount() {
        return chatBubbleList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return chatBubbleList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        return chatBubbleList.get(position).type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ChatBubble bubble = chatBubbleList.get(position);

        if (convertView == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(ChatActivity.getContext());
            if (bubble != null && bubble.type == 1) {
                v = vi.inflate(R.layout.chat_bubble, null);
            } else if (bubble.type == 0) {
                v = vi.inflate(R.layout.chat_bubble2, null);
            } else {
                v = vi.inflate(R.layout.chat_info, null);
            }
        }

        if (bubble != null) {
            TextView message;
            TextView sender;
            if (bubble.type < 2) {
                message = (TextView) v.findViewById(R.id.TVchatMessage);
                sender = (TextView) v.findViewById(R.id.TVchatSender);
                sender.setText(bubble.sender + ":");
                Log.d("Color", intToARGB(sender.hashCode()));
                //sender.setTextColor(Color.parseColor(intToARGB(bubble.sender.hashCode())));
            } else
                message = (TextView) v.findViewById(R.id.TVchatInfo);
            message.setText(bubble.message);
        }

        return v;
    }

    public static String intToARGB(int i) {
        return "#" + Integer.toHexString(((i >> 24) & 0xFF)) +
                Integer.toHexString(((i >> 16) & 0xFF)) +
                Integer.toHexString(((i >> 8) & 0xFF)) +
                Integer.toHexString((i & 0xFF));
    }
}
