package de.haw_landshut.haw_dating.p2pdatingapp.match;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.haw_landshut.haw_dating.p2pdatingapp.ChatActivity;
import de.haw_landshut.haw_dating.p2pdatingapp.FindYourLoveActivity;
import de.haw_landshut.haw_dating.p2pdatingapp.MatchesActivity;
import de.haw_landshut.haw_dating.p2pdatingapp.R;

/**
 * Created by s-gheldd on 19.06.16.
 */
public class MatchAdapter extends BaseAdapter {
    final private List<Match> matchList;

    public MatchAdapter(List<Match> matchList) {
        this.matchList = matchList;
    }

    @Override
    public int getCount() {
        return matchList.size();
    }

    @Override
    public Object getItem(int position) {
        return matchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;
        final Match match = matchList.get(position);
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(MatchesActivity.getContext());
            view = layoutInflater.inflate(R.layout.match, null);

        } else {
            view = convertView;
        }
        if (match != null) {
            final TextView uuid = (TextView) view.findViewById(R.id.match_uuid_text_view);
            final TextView date = (TextView) view.findViewById(R.id.match_date_text_view);
            final CheckBox checkBox = (CheckBox) view.findViewById(R.id.match_search_checkbox);
            final Button connectButton = (Button) view.findViewById(R.id.match_connect_button);


            uuid.setText(match.getUuid());
            date.setText(match.getDate());
            checkBox.setChecked(match.isOwn());
            checkBox.setClickable(false);
            connectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enterChatActivity(match.getSecret());
                }
            });
        }
        return view;
    }

    public void enterChatActivity(final String chatRoom) {

        final Intent intent = new Intent(MatchesActivity.getContext(), ChatActivity.class);

        final ArrayList<String> data = new ArrayList<String>();
        final String ip = MatchesActivity.getContext().getString(R.string.server_name);
        final String roomID = chatRoom;
        data.add(ip);
        data.add(roomID);

        final Bundle bundle = new Bundle();
        bundle.putStringArrayList("data", data);
        intent.putExtra(FindYourLoveActivity.CHAT_MESSAGE, bundle);
        MatchesActivity.getContext().startActivity(intent);

    }
}
