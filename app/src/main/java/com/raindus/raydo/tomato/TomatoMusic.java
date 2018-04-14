package com.raindus.raydo.tomato;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;

import com.raindus.raydo.R;

import java.io.IOException;

/**
 * Created by Raindus on 2018/4/13.
 */

public class TomatoMusic {

    private static final String[] MUSIC_PATH = {"tomato_raydo.mp3", "tomato_rain.ogg",
            "tomato_forest.ogg", "tomato_ocean.ogg", "tomato_meditation.ogg", "tomato_coffee.ogg"};

    private Context mContext;

    private boolean mIsMusicStop = false;
    // 背景白噪音
    private MediaPlayer mTomatoPlayer;
    private int mPosition = 0;
    private HandlerThread mHandlerThread;
    private Handler mHandler;

    // 提示音
    private Ringtone mTomatoComplete;

    public TomatoMusic(Context context) {
        mContext = context;

        initRingtone();
        initMediaPlayer();
    }

    private void initRingtone() {
        Uri uri = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.tomato_complete);
        mTomatoComplete = RingtoneManager.getRingtone(mContext, uri);
    }

    public void playRingtone() {
        if (mTomatoComplete != null) {
            if (mTomatoComplete.isPlaying())
                mTomatoComplete.stop();

            mTomatoComplete.play();
        }
    }

    public void stopRingtone() {
        if (mTomatoComplete != null) {
            if (mTomatoComplete.isPlaying())
                mTomatoComplete.stop();
        }
    }

    private void initMediaPlayer() {
        mTomatoPlayer = new MediaPlayer();
        mHandlerThread = new HandlerThread("tomato_music");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    public void playTomatoMusic(int position) {
        mPosition = position;
        mHandler.post(mPlayMusic);
    }

    private Runnable mPlayMusic = new Runnable() {
        @Override
        public void run() {
            if (mIsMusicStop)
                return;

            if (mTomatoPlayer.isPlaying())
                mTomatoPlayer.reset();

            try {
                AssetFileDescriptor fd = mContext.getAssets().openFd(MUSIC_PATH[mPosition]);
                if (fd != null)
                    mTomatoPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                mTomatoPlayer.setLooping(true);
                mTomatoPlayer.prepareAsync();
                mTomatoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
            } catch (IOException e) {
                return;
            }
        }
    };

    public void restartTomatoMusic(int position) {
        mIsMusicStop = false;
        playTomatoMusic(position);
    }

    public void stopTomatoMusic() {
        mIsMusicStop = true;
        if (mTomatoPlayer.isPlaying())
            mTomatoPlayer.reset();
    }

    public void onDestroy() {
        stopRingtone();
        stopTomatoMusic();
        mTomatoPlayer.release();
        mHandlerThread.quit();
    }

}
