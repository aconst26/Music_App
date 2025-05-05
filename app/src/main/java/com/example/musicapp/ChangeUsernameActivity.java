package com.example.musicapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;

import com.example.musicapp.database.MusicAppRepository;
import com.example.musicapp.database.entities.User;
import com.example.musicapp.databinding.ActivityChangeUsernameBinding;

public class ChangeUsernameActivity extends AppCompatActivity {

    private ActivityChangeUsernameBinding binding;
    private MusicAppRepository repository;
    private int loggedInUserId;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangeUsernameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = MusicAppRepository.getRepository(getApplication());

        binding.changeUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUsername();
            }
        });
        binding.prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminActivity.adminIntentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

    }

    private void changeUsername() {
        String username = binding.usernameEditText.getText().toString().trim();
        if (username.isEmpty()) {
            toastMaker("Username cannot be empty");
            return;
        }
        LiveData<User> existingUserLiveData = repository.getUserByUserName(username);
        existingUserLiveData.observe(this, existingUser -> {
            if (existingUser != null) {
                toastMaker("Username already in use. Please select another.");
                existingUserLiveData.removeObservers(this);
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                loggedInUserId = sharedPreferences.getInt(getString(R.string.preference_userId_key), -1);

                LiveData<User> currentUserLiveData = repository.getUserByUserId(loggedInUserId);
                currentUserLiveData.observe(this, currentUser -> {
                    if (currentUser != null) {
                        currentUser.setUsername(username);
                        repository.updateUser(currentUser);
                        toastMaker("Username updated successfully!");
                    } else {
                        toastMaker("Failed to load current user.");
                    }
                    currentUserLiveData.removeObservers(this);
                });
            }
        });
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    static Intent changeUsernameIntentFactory(Context context) {
        return new Intent(context, ChangeUsernameActivity.class);
    }
}