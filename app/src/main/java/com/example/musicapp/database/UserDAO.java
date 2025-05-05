package com.example.musicapp.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.musicapp.database.entities.User;

import java.util.List;

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

    @Query("SELECT * FROM " + MusicAppDatabase.USER_TABLE)
    LiveData<List<User>> getAllUsers();

    @Update
    void update(User user);


}
