package samsung.membership.splash;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import java.lang.ref.WeakReference;
import java.util.Formatter;
import java.util.Locale;

public class VideoControllerView extends FrameLayout {
    private static final String TAG = "VideoControllerView";

    private MediaPlayerControl mPlayer;
    private Context mContext;
    private ViewGroup mAnchor;
    private View mRoot;
    private ProgressBar mProgress;
    private boolean mShowing;
    private boolean mDragging;
    private static final int sDefaultTimeout = 3000;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    private Handler mHandler = new MessageHandler(this);

    public VideoControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRoot = null;
        mContext = context;
        Log.i(TAG, TAG);
    }

    public VideoControllerView(Context context, boolean useFastForward) {
        super(context);
        mContext = context;
        Log.i(TAG, TAG);
    }

    public VideoControllerView(Context context) {
        this(context, true);
        Log.i(TAG, TAG);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        if (mRoot != null)
            initControllerView(mRoot);
    }

    public void setMediaPlayer(MediaPlayerControl player) {
        mPlayer = player;
    }

    public void setAnchorView(ViewGroup view) {
        mAnchor = view;

        LayoutParams frameParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        removeAllViews();
        View v = makeControllerView();
        addView(v, frameParams);
    }

    protected View makeControllerView() {
        if (mRoot == null) {
            LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mRoot = inflate.inflate(R.layout.media_controller, null);
            initControllerView(mRoot);
        }
        return mRoot;
    }

    private void initControllerView(View v) {
        mProgress = (ProgressBar) v.findViewById(R.id.seekBar);
        if (mProgress != null) {
            if (mProgress instanceof SeekBar) {
                SeekBar seeker = (SeekBar) mProgress;
                seeker.setOnSeekBarChangeListener(mSeekListener);
            }
            mProgress.setMax(1000);

        }

        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

    }

    public void show() {
        show(sDefaultTimeout);
    }

    private void disableUnsupportedButtons() {
        if (mPlayer == null) {
            return;
        }
    }

    public void show(int timeout) {
        if (!mShowing && mAnchor != null) {
            setProgress();
            disableUnsupportedButtons();

            LayoutParams tlp = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM
            );

            mAnchor.addView(this, tlp);
            mShowing = true;
        }
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        Message msg = mHandler.obtainMessage(FADE_OUT);
        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(msg, timeout);
        }
    }

    private int setProgress() {
        if (mPlayer == null || mDragging) {
            return 0;
        }

        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        if (mProgress != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                mProgress.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            mProgress.setSecondaryProgress(percent * 10);
        }
        return position;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        show(sDefaultTimeout);
        return true;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        show(sDefaultTimeout);
        return false;
    }

    private OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar bar) {
            show(3600000);
            mDragging = true;
            mHandler.removeMessages(SHOW_PROGRESS);
        }

        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (mPlayer == null) {
                return;
            }

            if (!fromuser) {
                return;
            }

            long duration = mPlayer.getDuration();
            long newposition = (duration * progress) / 1000L;
            mPlayer.seekTo((int) newposition);
        }

        public void onStopTrackingTouch(SeekBar bar) {
            mDragging = false;
            show(sDefaultTimeout);
            mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }
    };

    @Override
    public void setEnabled(boolean enabled) {
        if (mProgress != null) {
            mProgress.setEnabled(enabled);
        }
        disableUnsupportedButtons();
        super.setEnabled(enabled);
    }

    public interface MediaPlayerControl {
        void start();

        int getDuration();

        int getCurrentPosition();

        void seekTo(int pos);

        boolean isPlaying();

        int getBufferPercentage();
    }

    private static class MessageHandler extends Handler {
        private final WeakReference<VideoControllerView> mView;

        MessageHandler(VideoControllerView view) {
            mView = new WeakReference<VideoControllerView>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            VideoControllerView view = mView.get();
            if (view == null || view.mPlayer == null) {
                return;
            }

            int pos;
            switch (msg.what) {
                case FADE_OUT:
                    //view.hide();
                    break;
                case SHOW_PROGRESS:
                    pos = view.setProgress();
                    if (!view.mDragging && view.mShowing && view.mPlayer.isPlaying()) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 5 - (pos % 5));
                    }
                    break;
            }
        }
    }

}