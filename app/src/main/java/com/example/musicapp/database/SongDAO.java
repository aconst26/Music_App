package com.example.musicapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.musicapp.database.entities.Song;

import java.util.List;

@Dao
public interface SongDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Song... songs);

    @Query("SELECT * FROM " + MusicAppDatabase.SONG_TABLE)
    LiveData<List<Song>> getAllSongs();

    @Query("DELETE FROM " + MusicAppDatabase.SONG_TABLE)
    void deleteAll();
}
