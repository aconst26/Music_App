package com.example.musicapp;

import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AlbumFragment extends Fragment {

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.album_fragment, container, false);
        recyclerView = view.findViewById(R.id.albumRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Delay asset loading to avoid blocking UI thread
        new Handler(Looper.getMainLooper()).post(() -> loadSongs());

        return view;
    }

    private void loadSongs() {
        File favoritesDir = new File(requireContext().getFilesDir(), "favorites");
        File[] songFiles = favoritesDir.listFiles();

        if (songFiles != null) {
            List<String> songList = new ArrayList<>();
            for (File file : songFiles) {
                if (file.getName().endsWith(".mp3")) {
                    songList.add(file.getName().replace(".mp3", ""));
                }
            }
            // Passing the directoryPath to the adapter
            SongAdapter adapter = new SongAdapter(songList, getContext(), favoritesDir.getAbsolutePath());
            recyclerView.setAdapter(adapter);
        }
    }


}