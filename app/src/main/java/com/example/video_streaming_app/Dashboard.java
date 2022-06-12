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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dashboard extends AppCompatActivity {

    ActivityDashboardBinding binding;
    DatabaseReference likeReference;
    boolean testClick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        likeReference = FirebaseDatabase.getInstance().getReference("likes");

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

               FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                assert firebaseUser != null;
                String userId = firebaseUser.getUid();
                String postKey = getRef(position).getKey();

                holder.getLikeButtonStatus(postKey,userId);

                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        testClick = true;
                        likeReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                              if(testClick)
                              {
                                  if(snapshot.child(postKey).hasChild(userId))
                                  {
                                      likeReference.child(postKey).removeValue();
                                      testClick = false;
                                  }
                                  else {
                                      likeReference.child(postKey).child(userId).setValue(true);
                                      testClick = false;
                                  }
                              }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });

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