package com.example.musicapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private List<String> songs;
    private Context context;

    public SongAdapter(List<String> songs, Context context) {
        this.songs = songs;
        this.context = context;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_item, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        String song = songs.get(position);
        holder.songNameTextView.setText(song);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {

        TextView songNameTextView;

        public SongViewHolder(View itemView) {
            super(itemView);
            songNameTextView = itemView.findViewById(R.id.songTitle);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    String songName = songs.get(position);

                    Intent intent = new Intent(context, PlayerActivity.class);
                    intent.putExtra("SONG_TITLE", songName);
                    context.startActivity(intent);
                }
            });
        }
    }
}
