package com.example.musicapp;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicapp.databinding.ActivityAdminBinding;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding binding;
    private int loggedInUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loggedInUserId = getIntent().getIntExtra(MainActivity.SAVED_INSTANCE_STATE_USERID_KEY, -1);

        binding.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.mainActivityIntentFactory(getApplicationContext(), loggedInUserId);
                startActivity(intent);
            }
        });
        binding.setAdminCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SetAdminActivity.setAdminIntentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
        binding.changeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ChangeUsernameActivity.changeUsernameIntentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

        binding.changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ChangePasswordActivity.changePasswordIntentFactory(getApplicationContext());
                startActivity(intent);
            }
        });


    }

    static Intent adminIntentFactory(Context context) {
        return new Intent(context, AdminActivity.class);
    }

}