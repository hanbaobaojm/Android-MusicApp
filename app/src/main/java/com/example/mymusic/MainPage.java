package com.example.mymusic;

//import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mymusic.data.Song;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainPage extends AppCompatActivity {
    private MusicRecyclerAdapter musicRecyclerAdapter;
    private RecyclerView recyclerView;
    private List<Song> list;
    private int positionToEdit = -1;
    private Button show;
    public static final String KEY_TODO_ID = "KEY_TODO_ID";
    public static final String NEXT = "NEXT";
    public static final int REQUEST_CODE_EDIT = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        list = new ArrayList<>();
        show = findViewById(R.id.btnShow);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = (List<Song>) MusicUtils.getMusicData(MainPage.this);
                initView();
            }
        });
    }
    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerMusic);
        recyclerView.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        musicRecyclerAdapter = new MusicRecyclerAdapter(this, list);
        recyclerView.setAdapter(musicRecyclerAdapter);
    }

    public void openPlayActivity(int index, Song music, List<Song> list) {
        positionToEdit = index;
        Intent startEdit = new Intent(this, OnPlay.class);
        startEdit.putExtra(KEY_TODO_ID, (Serializable) music);
        startEdit.putExtra(NEXT, (Serializable) list);
        startActivityForResult(startEdit, REQUEST_CODE_EDIT);
    }
}
