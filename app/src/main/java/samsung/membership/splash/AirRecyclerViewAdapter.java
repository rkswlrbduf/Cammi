package samsung.membership.splash;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by KyuYeol on 2017-08-11.
 */
public class AirRecyclerViewAdapter extends RecyclerView.Adapter<AirRecyclerViewAdapter.ViewHolder> {

    private ArrayList<AirMyData> dataSet;
    private RecyclerView recyclerView;
    Context context;

    public AirRecyclerViewAdapter(ArrayList<AirMyData> myDataset, Context context, RecyclerView recyclerView) {
        dataSet = myDataset;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ViewHolder closedViewHolder = (ViewHolder)holder;
        switch (position) {
            case 0:
                holder.relativeLayout.setBackgroundColor(dataSet.get(position).color);
                holder.relativeLayout.getLayoutParams().height = 700;
                break;
            case 1:
                holder.relativeLayout.setBackgroundColor(dataSet.get(position).color);
                holder.relativeLayout.getLayoutParams().height = 1200;
                break;
            case 2:
                holder.relativeLayout.setBackgroundColor(dataSet.get(position).color);
                holder.relativeLayout.getLayoutParams().height = 900;
                break;
            case 3:
                holder.relativeLayout.setBackgroundColor(dataSet.get(position).color);
                holder.relativeLayout.getLayoutParams().height = 1500;
                break;
            case 4:
                holder.relativeLayout.setBackgroundColor(dataSet.get(position).color);
                holder.relativeLayout.getLayoutParams().height = 650;
                break;
        }
        Log.d("TAGGGG",dataSet.get(position).name);
        //closedViewHolder.title.setText(dataSet.get(position).name);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout relativeLayout;

        public ViewHolder(View view) {
            super(view);
            relativeLayout = (RelativeLayout)view.findViewById(R.id.content_layout);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.air_content,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}

class AirMyData{
    public int img;
    public String name;
    public int color;
    public String date;
    public AirMyData(String title, boolean state, int color){
        this.name = title;
        this.color = color;
    }
}