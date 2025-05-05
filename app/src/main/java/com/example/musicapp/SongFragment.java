package com.example.musicapp;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SongFragment extends Fragment {

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song, container, false);
        recyclerView = view.findViewById(R.id.songRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Delay asset loading to avoid blocking UI thread
        new Handler(Looper.getMainLooper()).post(() -> loadSongs());

        return view;
    }

    private void loadSongs() {
        AssetManager assetManager = requireContext().getAssets();
        try {
            String[] songFiles = assetManager.list("music");
            if (songFiles != null) {
                List<String> songList = new ArrayList<>();
                for (String file : songFiles) {
                    if (file.endsWith(".mp3")) {
                        songList.add(file.replace(".mp3", ""));
                    }
                }
                SongAdapter adapter = new SongAdapter(songList, getContext());
                recyclerView.setAdapter(adapter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
