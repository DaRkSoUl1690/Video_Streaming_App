package com.example.video_streaming_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.video_streaming_app.databinding.ActivityDashboardBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Dashboard extends AppCompatActivity {

    ActivityDashboardBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       binding.addVideo.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),addvideo.class)));

       binding.reView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("addVideo"), Model.class)
                        .build();


        FirebaseRecyclerAdapter<Model,myViewHolder> adapter = new FirebaseRecyclerAdapter<Model, myViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Model model) {
               holder.prepareExoPlayer(getApplication(), model.getTitle(), model.getVurl());
            }

            @NonNull
            @Override
            public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
                   return new myViewHolder(view);
            }
        };
        adapter.startListening();
        binding.reView.setAdapter(adapter);
    }
}