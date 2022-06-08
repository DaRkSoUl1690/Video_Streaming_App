package com.example.video_streaming_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.video_streaming_app.databinding.ActivityDashboardBinding;

public class Dashboard extends AppCompatActivity {

    ActivityDashboardBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       binding.addVideo.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              startActivity(new Intent(getApplicationContext(),addvideo.class));
           }
       });
    }
}