package samsung.membership.splash;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yumin on 2017-07-29.
 */

public class VoiceListAdapter extends BaseAdapter {

    private ArrayList<String> listViewItemList = new ArrayList<String>();

    public VoiceListAdapter() {

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.voice_list_item, parent, false);
        }

        TextView voiceResult = (TextView) convertView.findViewById(R.id.voice_result);
        voiceResult.setBackgroundResource(R.drawable.custom_voice_button);
        voiceResult.setTextColor(Color.parseColor("#ffffff"));
        Button button = (Button)convertView.findViewById(R.id.voce_delete);
        button.setTextColor(Color.parseColor("#ffffff"));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewItemList.remove(position);
                notifyDataSetChanged();
            }
        });
        voiceResult.setText(listViewItemList.get(position));

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(String result) {
        listViewItemList.add(result);
    }

}
