package com.sepehr.sa_sh.abfacounter01.infrastructure;

import android.content.Context;
import android.media.MediaPlayer;

import com.sepehr.sa_sh.abfacounter01.R;

/**
 * Created by saeid on 3/2/2017.
 */
public class MediaPlayerManager implements IMediaPlayerManager{
    Context appContext;
    MediaPlayer mediaPlayer;

    public MediaPlayerManager(Context appContext) {
        this.appContext = appContext;
    }

    public void playSound(int soundId ){
        mediaPlayer = MediaPlayer.create(appContext,soundId);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mediaPlayer.start();
    }
}
