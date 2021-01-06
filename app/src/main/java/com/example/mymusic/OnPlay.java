package com.example.mymusic;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mymusic.data.Song;

import java.text.SimpleDateFormat;
import java.util.List;

import static com.example.mymusic.MainPage.KEY_TODO_ID;
import static com.example.mymusic.MainPage.NEXT;

public class OnPlay extends AppCompatActivity {
    private MusicService musicService;
    private TextView musicStatus, musicTime, musicTotal, musicName;
    private ImageView imageView;
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");
    private SeekBar seekBar;
    private Button btnPlayOrPause, btnStop, btnNext, btnQuit;
    private ObjectAnimator animator;
    private boolean tag1 = false;
    private boolean tag2 = false;
    public static Song music;
    private List<Song> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_play);


/**
 * 动态获取权限，Android 6.0 新特性，一些保护权限，除了要在AndroidManifest中声明权限，还要使用如下代码动态获取

 */

        if (Build.VERSION.SDK_INT >= 23) {

            int REQUEST_CODE_CONTACT = 101;

            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

            //验证是否许可权限

            for (String str : permissions) {

                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {

                    //申请权限

                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);

                    return;

                }

            }

        }


        if (getIntent().hasExtra(MainPage.NEXT)) {
            list = (List<Song>) getIntent().getSerializableExtra(NEXT);
        }
        if (getIntent().hasExtra(MainPage.KEY_TODO_ID)) {
            music = (Song) getIntent().getSerializableExtra(KEY_TODO_ID);
        }
        findViewById();
        bindServiceConnection();
        Ani();
        myListener();
        SeekBar();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                int id = music.ID + 1;
                if(id<list.size())
                    music=list.get(id);
                else
                    music = list.get(0);
                btnPlayOrPause.setBackgroundResource(R.drawable.play);
                musicService.binder.stop();
                musicService.tag = false;
                animator.pause();
                handler.removeCallbacks(runnable);
                unbindService(serviceConnection);
                Intent intent = new Intent(OnPlay.this, MusicService.class);
                stopService(intent);
                bindServiceConnection();
                myListener();
                tag1 = false;
                tag2 = false;
            }
        });


    }
    private void SeekBar(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser == true) {
                    musicService.binder.setPosition(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    private void Ani(){
        animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360.0f);
        animator.setDuration(10000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);
    }

    private void myListener() {
        musicName.setText(music.song);
        btnPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (musicService.mediaPlayer != null) {
                    seekBar.setProgress(musicService.binder.getPosition());
                    seekBar.setMax(music.duration);
                }
                //  由tag的变换来控制事件的调用
                if (musicService.tag != true) {
                    btnPlayOrPause.setBackgroundResource(R.drawable.pause);
                    musicStatus.setText("Playing");
                    musicService.binder.playOrPause();
                    musicService.tag = true;

                    if (tag1 == false) {
                        animator.start();
                        tag1 = true;
                    } else {
                        animator.resume();
                    }
                } else {
                    btnPlayOrPause.setBackgroundResource(R.drawable.play);
                    musicStatus.setText("Paused");
                    musicService.binder.playOrPause();
                    animator.pause();
                    musicService.tag = false;
                }
                if (tag2 == false) {
                    handler.post(runnable);
                    tag2 = true;
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                musicStatus.setText("Stopped");
                btnPlayOrPause.setBackgroundResource(R.drawable.play);
                musicService.binder.stop();
                animator.pause();
                musicService.tag = false;
            }
        });

        //  停止服务时，必须解除绑定，写入btnQuit按钮中
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(runnable);
                unbindService(serviceConnection);
                Intent intent = new Intent(OnPlay.this, MusicService.class);
                stopService(intent);
                try {
                    OnPlay.this.finish();
                } catch (Exception e) {

                }
            }
        });

    }

    //  获取并设置返回键的点击事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void bindServiceConnection() {
        Intent intent = new Intent(OnPlay.this, MusicService.class);
        startService(intent);
        bindService(intent, serviceConnection, this.BIND_AUTO_CREATE);
    }
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService = ((MusicService.MyBinder) (service)).getService();
            Log.i("musicService", musicService + "");
            musicTotal.setText(time.format(music.duration));
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }
    };

    //  通过 Handler 更新 UI 上的组件状态
    public Handler handler = new Handler();
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            musicTime.setText(time.format(musicService.binder.getPosition()));
            seekBar.setProgress(musicService.binder.getPosition());
            seekBar.setMax(music.duration);
            musicTotal.setText(time.format(music.duration));
            handler.postDelayed(runnable, 200);
        }
    };
    private void findViewById() {
        musicTime = (TextView) findViewById(R.id.MusicTime);
        musicTotal = (TextView) findViewById(R.id.MusicTotal);
        seekBar = (SeekBar) findViewById(R.id.MusicSeekBar);
        btnPlayOrPause = (Button) findViewById(R.id.BtnPlayorPause);
        btnStop = (Button) findViewById(R.id.BtnStop);
        btnNext = (Button) findViewById(R.id.BtnNext);
        btnQuit = (Button) findViewById(R.id.BtnQuit);
        musicStatus = (TextView) findViewById(R.id.MusicStatus);
        musicName = (TextView) findViewById(R.id.MusicName);
        imageView = (ImageView) findViewById(R.id.Image);
    }
}
