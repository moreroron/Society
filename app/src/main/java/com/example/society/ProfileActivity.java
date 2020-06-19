package com.example.society;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    TextView mEmail;
    FirebaseAuth fAuth;
    Button mLogoutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mEmail = (TextView) findViewById(R.id.emailTextView);
        fAuth = FirebaseAuth.getInstance();
        mLogoutBtn = (Button) findViewById(R.id.LogoutBtn);

        if(fAuth.getCurrentUser() != null) {

            mEmail.setText(fAuth.getCurrentUser().getEmail());
        }

        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fAuth.signOut();
                finish();

            }
        });

    }
}