package com.mr_17.evira.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mr_17.evira.R;
import com.mr_17.evira.model.FirebaseModel;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        myAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                final Intent[] intent = new Intent[1];

                if(myAuth.getCurrentUser() == null) {
                    intent[0] = new Intent(SplashActivity.this, WelcomeActivity.class);
                    intent[0].addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent[0]);
                    finish();
                }
                else
                {
                    SharedPreferences sharedPref = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);;
                    FirebaseModel.databaseRef_users.child(sharedPref.getString("username", "!@#")).child(FirebaseModel.node_isBlocked).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                if(!Boolean.parseBoolean(snapshot.getValue().toString()))
                                {
                                    intent[0] = new Intent(SplashActivity.this, UserDashboardActivity.class);
                                }
                                else
                                {
                                    myAuth.signOut();
                                    intent[0] = new Intent(SplashActivity.this, WelcomeActivity.class);
                                    Toast.makeText(SplashActivity.this, "Your account has been blocked by the admin.", Toast.LENGTH_SHORT).show();
                                }
                                intent[0].addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent[0]);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        }, 2000);
    }
}