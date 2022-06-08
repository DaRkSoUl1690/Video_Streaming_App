package com.example.video_streaming_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
{
    TextInputLayout t1,t2;
    TextView textView2;
    ProgressBar bar;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        t1= findViewById(R.id.email);
        t2= findViewById(R.id.pwd);
        textView2 = findViewById(R.id.textView2);
        bar= findViewById(R.id.progressBar3);
        mAuth = FirebaseAuth.getInstance();
    }

    public void gotosignin(View view)
    {
       startActivity(new Intent(MainActivity.this,login.class));
        Toast.makeText(this, "this is okay", Toast.LENGTH_SHORT).show();
    }

    public void singup(View view)
    {
        bar.setVisibility(View.VISIBLE);

        String email=t1.getEditText().getText().toString();
        String password=t2.getEditText().getText().toString();

        if(!email.isEmpty() && password.length()>=8)
        {

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                bar.setVisibility(View.INVISIBLE);
                                t1.getEditText().setText("");
                                t2.getEditText().setText("");
                                Toast.makeText(getApplicationContext(),"Inserted Successfully",Toast.LENGTH_LONG).show();
                            } else
                            {
                                bar.setVisibility(View.INVISIBLE);
                                t1.getEditText().setText("");
                                t2.getEditText().setText("");
                                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
                            }

                            // ...
                        }
                    });

        }
        else
        {
            bar.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(),"Please input valid data",Toast.LENGTH_LONG).show();
        }
    }
}