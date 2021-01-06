package com.example.mymusic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class MusicService extends Service {
    public MediaPlayer mediaPlayer;
    public boolean tag = false;



    public MusicService() {

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(OnPlay.music.path);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  通过 Binder 来保持 Activity 和 Service 的通信
    public MyBinder binder = new MyBinder();
    public class MyBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
        public void playOrPause() {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        }
        public void stop() {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(OnPlay.music.path);
                    mediaPlayer.prepare();
                    mediaPlayer.seekTo(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        public int getPosition() {
            return  mediaPlayer.getCurrentPosition();//获取当前播放进度
        }
        public void setPosition (int position) {
            mediaPlayer.seekTo(position);//重新设定播放进度
        }
    }





    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    @Override
    public void onDestroy() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        mediaPlayer = null;
        super.onDestroy();
    }

}


