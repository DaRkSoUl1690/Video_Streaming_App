package com.example.video_streaming_app;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class myViewHolder extends RecyclerView.ViewHolder {

    PlayerView playerView;
    SimpleExoPlayer simpleExoPlayer;
    TextView textView, like_text;
    ImageView imageView,comment;
    DatabaseReference likeReference;

    public myViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.vtitle);
        playerView = itemView.findViewById(R.id.exoplayerview);
        comment = itemView.findViewById(R.id.comment_btn);
        imageView = itemView.findViewById(R.id.like_btn);
        like_text = itemView.findViewById(R.id.like_text);
    }

    void prepareExoPlayer(Application application, String viTitle, String videoUrl) {
        try {
            textView.setText(viTitle);

            Uri videoUri = Uri.parse(videoUrl);

            SimpleExoPlayer exoPlayer = new SimpleExoPlayer.Builder(application).build();
            playerView.setPlayer(exoPlayer);

            MediaItem mediaItem = MediaItem.fromUri(videoUri);
            exoPlayer.clearMediaItems();
            exoPlayer.setMediaItem(mediaItem);
            exoPlayer.prepare();
            exoPlayer.setPlayWhenReady(false);

        } catch (Exception e) {
            Log.d("Exoplayer Crashed", e.getMessage());

        }
    }

    public void getLikeButtonStatus(String postKey, String userId) {
        likeReference = FirebaseDatabase.getInstance().getReference("likes");
        likeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(postKey).hasChild(userId)) {
                    int likeCount = (int) snapshot.child(postKey).getChildrenCount();
                    like_text.setText(likeCount+" likes");
                    imageView.setImageResource(R.drawable.ic_baseline_favorite_24);
                }
                else
                {
                    int likeCount = (int) snapshot.child(postKey).getChildrenCount();
                    like_text.setText(likeCount+" likes");
                    imageView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
