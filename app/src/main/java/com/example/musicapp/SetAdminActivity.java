package com.example.musicapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.musicapp.database.MusicAppRepository;
import com.example.musicapp.database.entities.User;
import com.example.musicapp.databinding.ActivitySetAdminBinding;

import java.util.List;

public class SetAdminActivity extends AppCompatActivity {

    private ListView listViewUsers;
    private ActivitySetAdminBinding binding;
    private MusicAppRepository musicAppRepository;
    private List<User> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listViewUsers = findViewById(R.id.user_list_view);
        musicAppRepository = MusicAppRepository.getRepository(getApplication());

        binding.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminActivity.adminIntentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

        LiveData<List<User>> liveUsers = musicAppRepository.getAllUsers();
        liveUsers.observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                usersList = users;
                ArrayAdapter<User> adapter = new ArrayAdapter<>(SetAdminActivity.this,
                        android.R.layout.simple_list_item_1, usersList);
                listViewUsers.setAdapter(adapter);
            }
        });
        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                User selectedUser = usersList.get(position);

                boolean newAdminStatus = !selectedUser.isAdmin();
                selectedUser.setAdmin(newAdminStatus);

                musicAppRepository.updateUser(selectedUser);

                Toast.makeText(SetAdminActivity.this,
                        selectedUser.getUsername() + " is now " +
                                (newAdminStatus ? "an Admin" : "not an Admin"),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    static Intent setAdminIntentFactory(Context context) {
        return new Intent(context, SetAdminActivity.class);
    }
}

