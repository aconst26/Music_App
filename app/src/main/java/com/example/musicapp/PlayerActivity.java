package com.example.musicapp;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlayerActivity extends AppCompatActivity {

    private TextView songTitleTextView;
    private SeekBar seekBar;
    private TextView currentTimeTextView;
    private TextView durationTextView;
    private ImageButton playPauseButton, skipNextButton, skipPreviousButton;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Runnable updateSeekBar;
    private boolean isPlaying = true;

    private List<String> songList = new ArrayList<>();
    private int currentSongIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player);

        songTitleTextView = findViewById(R.id.songTitleTextView);

        seekBar = findViewById(R.id.seekBar);

        currentTimeTextView = findViewById(R.id.currentTimeTextView);

        durationTextView = findViewById(R.id.durationTextView);

        playPauseButton = findViewById(R.id.playPauseButton);

        skipNextButton = findViewById(R.id.skipNextButton);

        skipPreviousButton = findViewById(R.id.skipPreviousButton);

        Button prevButton = findViewById(R.id.prev);
        Button favorites = findViewById(R.id.favorite);

        prevButton.setOnClickListener(v -> {
            Intent intent = new Intent(PlayerActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });




        try {
            String[] files = getAssets().list("music");
            if (files != null) {
                for (String file : files) {

                    if (file.endsWith(".mp3")) {
                        songList.add(file.replace(".mp3", ""));

                    }
                }
            }
        } catch (IOException e) {

            e.printStackTrace();

        }

        String songTitle = getIntent().getStringExtra("SONG_TITLE");
        if (songTitle != null) {
            currentSongIndex = songList.indexOf(songTitle);

            if (currentSongIndex == -1) currentSongIndex = 0;
        }

        if (!songList.isEmpty()) {
            playSong(songList.get(currentSongIndex));
        }

        playPauseButton.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                playPauseButton.setImageResource(R.drawable.play_button);
                isPlaying = false;
            }
            else {
                mediaPlayer.start();
                playPauseButton.setImageResource(R.drawable.pause_button);
                handler.post(updateSeekBar);
                isPlaying = true;
            }
        });

        skipNextButton.setOnClickListener(v -> {
            if (!songList.isEmpty()) {

                currentSongIndex = (currentSongIndex + 1) % songList.size();
                playSong(songList.get(currentSongIndex));

            }
        });

        skipPreviousButton.setOnClickListener(v -> {
            if (!songList.isEmpty()) {

                currentSongIndex = (currentSongIndex - 1 + songList.size()) % songList.size();
                playSong(songList.get(currentSongIndex));

            }
        });

        favorites.setOnClickListener(v -> {
            String songTitle2 = songList.get(currentSongIndex);
            String assetPath = "music/" + songTitle2 + ".mp3";

            // Destination: /data/data/your.package.name/files/favorites/song.mp3
            File favoritesDir = new File(getFilesDir(), "favorites");
            if (!favoritesDir.exists()) {
                favoritesDir.mkdirs(); // create folder if it doesn't exist
            }

            File destinationFile = new File(favoritesDir, songTitle2 + ".mp3");

            try (AssetFileDescriptor afd = getAssets().openFd(assetPath);
                 FileInputStream in = afd.createInputStream();
                 FileOutputStream out = new FileOutputStream(destinationFile)) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }

                Log.d("CopyStatus", "File copied to: " + destinationFile.getAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("CopyStatus", "Failed to copy: " + e.getMessage());
            }
        });


    }

    private void playSong(String songTitle) {

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            handler.removeCallbacks(updateSeekBar);
        }

        songTitleTextView.setText(songTitle);
        try {
            AssetFileDescriptor afd = getAssets().openFd("music/" + songTitle + ".mp3");
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
            playPauseButton.setImageResource(R.drawable.pause_button);

            seekBar.setMax(mediaPlayer.getDuration());
            durationTextView.setText(formatTime(mediaPlayer.getDuration()));

            updateSeekBar = new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {

                        int current = mediaPlayer.getCurrentPosition();

                        seekBar.setProgress(current);
                        currentTimeTextView.setText(formatTime(current));
                        handler.postDelayed(this, 500);
                    }
                }
            };
            handler.post(updateSeekBar);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                    if (fromUser && mediaPlayer != null) {

                        mediaPlayer.seekTo(progress);
                        currentTimeTextView.setText(formatTime(progress));
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });

        } catch (IOException e) {

            e.printStackTrace();
            songTitleTextView.setText("Could not play " + songTitle);
        }
    }

    private String formatTime(int millis) {
        return String.format(getString(R.string._02d_02d),
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) % 60);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {

            handler.removeCallbacks(updateSeekBar);
            mediaPlayer.release();
            mediaPlayer = null;

        }
    }

    public void copyFile(File src, File dst) throws IOException {
        try (FileInputStream in = new FileInputStream(src);
             FileOutputStream out = new FileOutputStream(dst)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
    }


}


