package com.mr_17.evira.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mr_17.evira.R;
import com.mr_17.evira.model.FirebaseModel;
import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private AppCompatImageView imageView;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        InitializeFields();

        SetImage(getIntent().getStringExtra("type"));

    }

    private void InitializeFields()
    {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("imageHeading"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imageView = findViewById(R.id.image_view);
        sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
    }

    private void SetImage(String type)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        switch (type)
        {
            case "profilePic":
                ref = FirebaseModel.databaseRef_users.child(sharedPreferences.getString("username", "!@#")).child(FirebaseModel.node_profilePic);
                break;
            case "aadharPic":
                ref = FirebaseModel.databaseRef_users.child(sharedPreferences.getString("username", "!@#")).child(FirebaseModel.node_aadharPic);
                break;
            case "panPic":
                ref = FirebaseModel.databaseRef_users.child(sharedPreferences.getString("username", "!@#")).child(FirebaseModel.node_panPic);
                break;
            case "userAadharPic":
                ref = FirebaseModel.databaseRef_users.child(getIntent().getStringExtra("username")).child(FirebaseModel.node_aadharPic);
                break;
            case "userPanPic":
                ref = FirebaseModel.databaseRef_users.child(getIntent().getStringExtra("username")).child(FirebaseModel.node_panPic);
                break;
        }

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Picasso.get().load(snapshot.getValue().toString()).into(imageView);
                }
                else
                {
                    Toast.makeText(ImageActivity.this, "No Image Available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}