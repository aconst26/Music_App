package com.example.musicapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;

import com.example.musicapp.database.MusicAppRepository;
import com.example.musicapp.database.entities.User;
import com.example.musicapp.databinding.ActivityChangePasswordBinding;

public class ChangePasswordActivity extends AppCompatActivity {

    private ActivityChangePasswordBinding binding;
    private MusicAppRepository repository;
    private int loggedInUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = MusicAppRepository.getRepository(getApplication());

        binding.changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
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

    private void changePassword() {
        String password1 = binding.passwordEditText.getText().toString().trim();
        String password2 = binding.confirmPasswordEditText.getText().toString().trim();
        if(!password1.equals(password2)) {
            toastMaker("Passwords do not match.");
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            loggedInUserId = sharedPreferences.getInt(getString(R.string.preference_userId_key), -1);
            LiveData<User> currentUserLiveData = repository.getUserByUserId(loggedInUserId);
            currentUserLiveData.observe(this, currentUser -> {
                if (currentUser != null) {
                    currentUser.setPassword(password1);
                    repository.updateUser(currentUser);
                    toastMaker("Password updated successfully!");
                } else {
                    toastMaker("Failed to load current user.");
                }
                currentUserLiveData.removeObservers(this);
            });
            Intent intent = AdminActivity.adminIntentFactory(getApplicationContext());
            startActivity(intent);
        }
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    static Intent changePasswordIntentFactory(Context context) {
        return new Intent(context, ChangePasswordActivity.class);
    }
}