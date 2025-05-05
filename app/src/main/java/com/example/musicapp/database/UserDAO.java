package com.example.musicapp.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.musicapp.database.entities.User;

@Dao
public interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... user);

    @Query("DELETE FROM " + MusicAppDatabase.USER_TABLE)
    void deleteAll();

    @Query("SELECT * FROM " +  MusicAppDatabase.USER_TABLE +  " WHERE username == :username")
    LiveData<User> getUserByUserName(String username);

    @Query("SELECT * FROM " +  MusicAppDatabase.USER_TABLE +  " WHERE id == :userId")
    LiveData<User> getUserByUserId(int userId);


}
