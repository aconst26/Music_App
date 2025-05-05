package com.example.musicapp.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.musicapp.MainActivity;
import com.example.musicapp.database.entities.User;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MusicAppRepository {

    private final UserDAO userDAO;

    private static MusicAppRepository repository;



    private MusicAppRepository(Application application) {
        MusicAppDatabase db = MusicAppDatabase.getDatabase(application);
        this.userDAO = db.userDAO();
    }

    public static MusicAppRepository getRepository(Application application) {
        if(repository != null){
            return repository;
        }
        Future<MusicAppRepository> future = MusicAppDatabase.databaseWriteExecutor.submit(
                new Callable<MusicAppRepository>() {
                    @Override
                    public MusicAppRepository call() throws Exception {
                        return new MusicAppRepository(application);
                    }
                }
        );
        try{
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d(MainActivity.TAG, "Problem getting GymLogRepository, thread error.");
        }
        return null;
    }

    public void insertUser(User... user) {
        MusicAppDatabase.databaseWriteExecutor.execute(() ->
        {
            userDAO.insert(user);
        });
    }

    public LiveData<User> getUserByUserName(String username) {
        return userDAO.getUserByUserName(username);
    }

    public LiveData<User> getUserByUserId(int userId) {
        return userDAO.getUserByUserId(userId);
    }





}