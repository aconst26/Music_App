package com.example.musicapp;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import android.content.res.AssetManager;

import com.example.musicapp.database.entities.Song;

import java.io.IOException;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    static class User {
        private String username;
        private String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getPassword() {
            return password;
        }

        public String getUsername() {
            return username;
        }
    }

    private String verifyUser(String inputUsername, String inputPassword, User userFromRepo) {
        if (inputUsername == null || inputUsername.isEmpty()) {
            return "Username should not be blank";
        }

        if (userFromRepo == null) {
            return inputUsername + " is not a valid username.";
        }

        if (!inputPassword.equals(userFromRepo.getPassword())) {
            return "Invalid Password";
        }

        return "Login Success";
    }

    @Test
    public void test_blank_username() {
        String result = verifyUser("", "1234", new User("alice", "1234"));
        assertEquals("Username should not be blank", result);
    }

    @Test
    public void test_invalid_username() {
        String result = verifyUser("bob", "1234", null);
        assertEquals("bob is not a valid username.", result);
    }

    @Test
    public void test_wrong_password() {
        String result = verifyUser("alice", "wrongpass", new User("alice", "1234"));
        assertEquals("Invalid Password", result);
    }

    @Test
    public void test_correct_login() {
        String result = verifyUser("alice", "1234", new User("alice", "1234"));
        assertEquals("Login Success", result);
    }

    //Krishneet's Test
    public class PlayerActivityTest {

        private PlayerActivity playerActivity;
        private Song song;

        @Before
        public void setUp() {
            playerActivity = new PlayerActivity();
            song = new Song();
            playerActivity.playSong(String.valueOf(song));
        }

        @Test
        public void testPlayerActivityNotNull() {
            assertNotNull("PlayerActivity should be initialized", playerActivity);
        }

        @Test
        public void testPlaySong() {
            playerActivity.playSong();
            assertTrue("Song should be playing", playerActivity.playSong());
        }

    }





