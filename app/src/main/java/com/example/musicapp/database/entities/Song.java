package com.example.musicapp.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName ="Songs" )
public class Song {
@PrimaryKey(autoGenerate = true)
    private int songId;
    private String title;
    private String artist;
    private int playlistId;


    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }
    
}
