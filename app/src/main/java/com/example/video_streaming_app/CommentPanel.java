package com.example.video_streaming_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.video_streaming_app.databinding.ActivityCommentPanelBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class CommentPanel extends AppCompatActivity {

    ActivityCommentPanelBinding binding;
    DatabaseReference useRef, commentRef;
    String postKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentPanelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        postKey = getIntent().getStringExtra("postkey");

        useRef = FirebaseDatabase.getInstance().getReference("userprofile");
        commentRef = FirebaseDatabase.getInstance().getReference().child("addVideo").child(postKey).child("comments");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        final String userId = user.getUid();

        binding.commentRecview.setLayoutManager(new LinearLayoutManager(this));


        binding.commentSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useRef.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String username = snapshot.child("uname").getValue().toString();
                            String uimage = snapshot.child("uimage").getValue().toString();
                            processComment(username, uimage);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            private void processComment(String username, String uimage) {
                String commentpost = binding.commentText.getText().toString();
                String randompostkey = userId + "" + new Random().nextInt(1000);

                Calendar datevalue = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yy");
                String cdate = dateFormat.format(datevalue.getTime());

                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                String ctime = timeFormat.format(datevalue.getTime());

                HashMap<String, Object> cmnt = new HashMap<>();
                cmnt.put("uid", userId);
                cmnt.put("username", username);
                cmnt.put("userimage", uimage);
                cmnt.put("usermsg", commentpost);
                cmnt.put("date", cdate);
                cmnt.put("time", ctime);

                commentRef.child(randompostkey).updateChildren(cmnt)
                        .addOnCompleteListener((OnCompleteListener) task -> {
                            if (task.isSuccessful())
                                Toast.makeText(getApplicationContext(), "Comment Added", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(getApplicationContext(), task.toString(), Toast.LENGTH_LONG).show();
                        })
                        .addOnFailureListener(e -> {

                        });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<CommentModel> options =
                new FirebaseRecyclerOptions.Builder<CommentModel>()
                        .setQuery(commentRef, CommentModel.class)
                        .build();

        FirebaseRecyclerAdapter<CommentModel,commentViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<CommentModel, commentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull commentViewHolder holder, int position, @NonNull CommentModel model) {
                holder.cuname.setText(model.getUsername());
                holder.cumessage.setText(model.getUsermsg());
                holder.cudt.setText("Date :"+model.getDate()+" Time :"+model.getTime());
                Glide.with(holder.cuimage.getContext()).load(model.getUserimage()).into(holder.cuimage);
            }

            @NonNull
            @Override
            public commentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_single_row,parent,false);
                return  new commentViewHolder(view);
            }
        };
        firebaseRecyclerAdapter.startListening();
        binding.commentRecview.setAdapter(firebaseRecyclerAdapter);

    }
}