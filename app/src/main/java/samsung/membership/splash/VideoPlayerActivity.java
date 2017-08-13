package samsung.membership.splash;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

public class VideoPlayerActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {

    SurfaceView videoSurface;
    MediaPlayer player;
    VideoControllerView controller;
    boolean check = false;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        controller.show();
        return false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player.setDisplay(holder);
        //player.prepareAsync();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
        controller.show();
        player.start();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public void seekTo(int i) {
        player.seekTo(i);
    }

    @Override
    public void start() {
        player.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        SurfaceHolder videoHolder = videoSurface.getHolder();
        videoHolder.addCallback(this);

        player = MediaPlayer.create(this, R.raw.test);
        controller = new VideoControllerView(this);

        controller.makeControllerView().findViewById(R.id.play_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check == false) {
                    check = !check;
                    startActivity(new Intent(getApplicationContext(), AirActivity.class));
                    finish();
                }
            }
        });

        try {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //player.setDataSource(this, Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
            player.setOnPreparedListener(this);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if(check == false) {
                        Log.d("TAG", "CALLED");
                        check = !check;
                        startActivity(new Intent(getApplicationContext(), AirActivity.class));
                        finish();
                    }
                }
            });
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


}
