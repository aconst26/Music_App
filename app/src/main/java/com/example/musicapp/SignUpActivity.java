package com.example.musicapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;

import com.example.musicapp.database.MusicAppDatabase;
import com.example.musicapp.database.MusicAppRepository;
import com.example.musicapp.database.UserDAO;
import com.example.musicapp.database.entities.User;
import com.example.musicapp.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;

    private MusicAppRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = MusicAppRepository.getRepository(getApplication());

        binding.SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyUser();
            }
        });

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LoginActivity.loginIntentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    private void verifyUser() {
        String username = binding.userNameSignUpEditText.getText().toString();
        LiveData<User> userObserver = repository.getUserByUserName(username);
        userObserver.observe(this, user -> {
            if (user != null) {
                toastMaker("Account Already Created. Please Sign in.");
                binding.userNameSignUpEditText.setSelection(0);
            } else {
                String password = binding.passwordSignUpEditText.getText().toString();
                String confirmPassword = binding.passwordSignUpConfirmEditText.getText().toString();
                if (password.equals(confirmPassword)) {
                    user = new User(username, password);
                    repository.insertUser(user);
                } else {
                    toastMaker("Passwords do not match.");
                    binding.passwordSignUpEditText.setSelection(0);
                    binding.passwordSignUpConfirmEditText.setSelection(0);
                }
            }
        });

    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent signupIntentFactory(Context context) {
        return new Intent(context, SignUpActivity.class);
    }


}