package com.example.mymusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mymusic.data.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class MusicRecyclerAdapter extends RecyclerView.Adapter<MusicRecyclerAdapter.ViewHolder>{
    private List<Song> musicList;
    private Context context;
    public MusicRecyclerAdapter(Context context, List<Song> musicList) {
        this.context = context;
        this.musicList = musicList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.musics, parent, false);

        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.musicName.setText(musicList.get(position).song.toString());
        //setImage(todoList.get(position).getType(), holder);
        holder.musicAuthor.setText(musicList.get(position).singer.toString());
        int duration = musicList.get(position).duration;
        String time = MusicUtils.formatTime(duration);
        holder.musicTime.setText(time);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    ((MainPage) context).openPlayActivity(holder.getAdapterPosition(),
                            musicList.get(holder.getAdapterPosition()), musicList
                    );
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    //@Override
    public void onItemDismiss(int position) {

        musicList.remove(position);

        // refreshes the whole list
        //notifyDataSetChanged();
        // refreshes just the relevant part that has been deleted
        notifyItemRemoved(position);
    }

    //@Override
    public void onItemMove(int fromPosition, int toPosition) {
        /*todoList.add(toPosition, todoList.get(fromPosition));
        todoList.remove(fromPosition);*/

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(musicList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(musicList, i, i - 1);
            }
        }


        notifyItemMoved(fromPosition, toPosition);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        private TextView musicName;
        private TextView musicAuthor;
        private TextView musicTime;
        private ImageView musicImage;


        public ViewHolder(View itemView) {
            super(itemView);

            musicName = (TextView) itemView.findViewById(R.id.musicName);
            musicAuthor = (TextView) itemView.findViewById(R.id.musicAuthor);
            musicTime = (TextView) itemView.findViewById(R.id.musicTime);
            musicImage = (ImageView) itemView.findViewById(R.id.musicImage);
        }
    }
}
