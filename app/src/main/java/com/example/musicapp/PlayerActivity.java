package com.example.musicapp;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PlayerActivity extends AppCompatActivity {

    private TextView songTitleTextView;
    private SeekBar seekBar;
    private TextView currentTimeTextView;
    private TextView durationTextView;
    private Button playPauseButton;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Runnable updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        songTitleTextView = findViewById(R.id.songTitleTextView);
        seekBar = findViewById(R.id.seekBar);
        currentTimeTextView = findViewById(R.id.currentTimeTextView);
        durationTextView = findViewById(R.id.durationTextView);
        playPauseButton = findViewById(R.id.playPauseButton);

        String songTitle = getIntent().getStringExtra("SONG_TITLE");

        if (songTitle != null) {
            songTitleTextView.setText(songTitle);
            try {
                AssetFileDescriptor afd = getAssets().openFd("music/" + songTitle + ".mp3");
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                mediaPlayer.prepare();
                mediaPlayer.start();
                playPauseButton.setText(R.string.pauseButton);

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

                playPauseButton.setOnClickListener(v -> {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        playPauseButton.setText(R.string.play);
                    } else {
                        mediaPlayer.start();
                        playPauseButton.setText(R.string.pauseButton);
                        handler.post(updateSeekBar);
                    }
                });

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
            }
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
}
