package samsung.membership.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yumin on 2017-07-29.
 */

public class TabFragment1 extends Fragment {

    private Button voiceRec;
    private SpeechRecognizer speechRecognizer;
    private Intent intent;
    private View v;
    private ListView listView;
    private VoiceListAdapter voiceListAdapter;
    UnderlineAnim underlineAnim;
    private EditText voiceInput;
    private Timer timer=new Timer();
    private final long DELAY = 2000;
    private Context context;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tab_fragment_1, container, false);
        context = getActivity();
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getActivity().getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        intent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE,true);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(v.getContext());
        speechRecognizer.setRecognitionListener(listener);

        voiceRec = (Button) v.findViewById(R.id.voiceRec);
        voiceRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechRecognizer.startListening(intent);
            }
        });


        voiceListAdapter = new VoiceListAdapter();

        listView = (ListView) v.findViewById(R.id.listview);
        listView.setAdapter(voiceListAdapter);

        underlineAnim = (UnderlineAnim)v.findViewById(R.id.tab1_underline);
        voiceInput = (EditText)v.findViewById(R.id.main_text);


        voiceInput.addTextChangedListener(new TextWatcher() {
            final Handler handler = new Handler()
            {
                public void handleMessage(Message msg)
                {
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    RunOnUiThread();
                                }
                            },
                            DELAY
                    );        }
            };

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!voiceInput.getText().toString().equals("")) {
                    new Thread() {
                        public void run() {
                            Message msg = handler.obtainMessage();
                            handler.sendMessage(msg);
                        }
                    }.start();
                }
            }
        });


        return v;
    }

    private void RunOnUiThread() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                voiceListAdapter.addItem(voiceInput.getText().toString());
                voiceInput.setText("");
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(voiceInput.getWindowToken(), 0);
                voiceListAdapter.notifyDataSetChanged();
            }
        });
    }

    public void StartAnim() {
        underlineAnim.AnimStart();
    }


    private RecognitionListener listener = new RecognitionListener() {

        @Override
        public void onRmsChanged(float rmsdB) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onResults(Bundle results) {
            // TODO Auto-generated method stub
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            voiceListAdapter.addItem(rs[0]);
            voiceListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onReadyForSpeech(Bundle params) {
            // TODO Auto-generated method stub
            voiceRec.setBackgroundResource(R.drawable.btn_record_on);
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onError(int error) {
            // TODO Auto-generated method stub
            Log.d("TAG", "ERROR : " + error);
        }

        @Override
        public void onEndOfSpeech() {
            // TODO Auto-generated method stub
            voiceRec.setBackgroundResource(R.drawable.btn_record_off);
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(voiceInput.getWindowToken(), 0);
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onBeginningOfSpeech() {
            // TODO Auto-generated method stub
            Log.d("TAG", "START");
        }
    };


}
