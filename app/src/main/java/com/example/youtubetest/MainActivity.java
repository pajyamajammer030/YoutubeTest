package com.example.youtubetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        String playlistId = "";
        switch (view.getId()) {
            case R.id.newyork:
                playlistId = "PLDCx5WcWNkqpmyT4EGdKe4xTZxiB9m_oU";
                break;
            case R.id.ozuwarudo:
                playlistId = "PLxDFIQwAsi0x8H0E0uJbN9svyc5yfblcD";
                break;
            case R.id.tutoraibu:
                playlistId = "PLHhVgCPRLjLPikFeTLZ_7EErIJq3IWFGP";
                break;
        }

        Intent intent = new Intent(MainActivity.this, RadioListActivity.class);
        intent.putExtra("playlistId", playlistId);
        startActivity(intent);
    }
}