package samsung.membership.splash;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by yumin on 2017-07-29.
 */

public class MainTabFragment1 extends Fragment {

    private Button voiceRec;
    private SpeechRecognizer speechRecognizer;
    private Intent intent;
    private View v;
    private ListView listView;
    private VoiceListAdapter voiceListAdapter;
    private RecyclerView.Adapter adapter;
    private ArrayList<AirMyData> dataSet;
    public static RecyclerView recyclerView;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.air_tab_fragment_1, container, false);

        SpacesItemDecoration decoration = new SpacesItemDecoration(16);

        recyclerView = (RecyclerView)v.findViewById(R.id.story_recyclerview);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        dataSet = new ArrayList<>();
        dataSet.add((new AirMyData("TITLE",false, Color.BLACK)));
        dataSet.add((new AirMyData("TITLE",false, Color.WHITE)));
        dataSet.add((new AirMyData("TITLE",false, Color.YELLOW)));
        dataSet.add((new AirMyData("TITLE",false, Color.RED)));
        dataSet.add((new AirMyData("TITLE",false, Color.BLUE)));

        adapter = new AirRecyclerViewAdapter(dataSet, MainTabFragment1.this.getContext(), recyclerView);


        recyclerView.setAdapter(adapter);
        return v;
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private final int mSpace;

        public SpacesItemDecoration(int space) {
            this.mSpace = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = mSpace;
            outRect.right = mSpace;
            outRect.bottom = mSpace;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildAdapterPosition(view) == 0)
                outRect.top = mSpace;
        }
    }
}
