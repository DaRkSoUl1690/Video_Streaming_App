package com.example.video_streaming_app;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.video_streaming_app.databinding.ActivityAddvideoBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Objects;

public class addvideo extends AppCompatActivity {

    ActivityAddvideoBinding binding;
    Uri videoUri;
    MediaController mediaController;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddvideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mediaController = new MediaController(this);
        binding.videoView.setMediaController(mediaController);
        binding.videoView.start();

        storageReference = FirebaseStorage.getInstance().getReference("addVideo");
        databaseReference = FirebaseDatabase.getInstance().getReference("addVideo");


        binding.browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                                Intent intent = new Intent();
                                intent.setType("video/");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent, 101);

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        binding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processVideoUploading();
            }
        });
    }

    public String getExtension() {
        MimeTypeMap mimeType = MimeTypeMap.getSingleton();
        return mimeType.getExtensionFromMimeType(getContentResolver().getType(videoUri));
    }


    private void processVideoUploading() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        StorageReference uploader = storageReference.child("myVideos/" + System.currentTimeMillis() + "." + getExtension());
        uploader.putFile(videoUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Model obj = new Model(binding.vtitle.getText().toString(), uri.toString());
                                databaseReference.child(Objects.requireNonNull(databaseReference.push().getKey())).setValue(obj)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                Toast.makeText(addvideo.this, "inserted", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(addvideo.this, "not inserted", Toast.LENGTH_SHORT).show();

                                            }
                                        });

                            }
                        });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                       float per = (100*snapshot.getBytesTransferred())/ snapshot.getTotalByteCount();
                       progressDialog.setMessage("uploaded :"+(int)per + "%");
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            assert data != null;
            videoUri = data.getData();
            binding.videoView.setVideoURI(videoUri);
        }
    }
}