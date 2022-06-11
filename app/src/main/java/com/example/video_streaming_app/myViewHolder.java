package com.example.video_streaming_app;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

public class myViewHolder extends RecyclerView.ViewHolder {

    PlayerView playerView;
    SimpleExoPlayer simpleExoPlayer;
    TextView textView;

    public myViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.vtitle);
        playerView = itemView.findViewById(R.id.exoplayerview);
    }

    void prepareExoPlayer(Application application, String viTitle, String videoUrl) {
        try {
            textView.setText(viTitle);

            Uri videoUri = Uri.parse(videoUrl);

            SimpleExoPlayer  exoPlayer = new SimpleExoPlayer.Builder(application).build();
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
}
