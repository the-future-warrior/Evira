package com.mr_17.evira.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mr_17.evira.R;
import com.mr_17.evira.model.FirebaseModel;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailsActivity extends AppCompatActivity
{
    private Toolbar toolbar;

    private AppCompatTextView firstName;
    private AppCompatTextView lastName;
    private AppCompatTextView username;
    private AppCompatTextView emailAddress;
    private AppCompatTextView phoneNumber;
    private AppCompatTextView panNumber;
    private AppCompatTextView panURL;
    private AppCompatTextView aadharNumber;
    private AppCompatTextView aadharURL;
    private CircleImageView profileImage;
    private AppCompatTextView address;

    private AppCompatButton blockButton;
    private AppCompatButton unblockButton;
    private boolean isBlocked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        InitializeFields();

        aadharURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getIntent().getStringExtra("aadharURL");

                if(url.equals(""))
                    Toast.makeText(UserDetailsActivity.this, "Aadhar Pic not uploaded by user.", Toast.LENGTH_SHORT).show();
                else
                    SendToImageActivity(ImageActivity.class, true, "Aadhar Image", "userAadharPic");
            }
        });

        panURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getIntent().getStringExtra("panURL");

                if(url.equals(""))
                    Toast.makeText(UserDetailsActivity.this, "Pan Pic not uploaded by user.", Toast.LENGTH_SHORT).show();
                else
                    SendToImageActivity(ImageActivity.class, true, "Pan Image", "userPanPic");
            }
        });

        blockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseModel.databaseRef_users.child(username.getText().toString()).child(FirebaseModel.node_isBlocked).setValue("true");
                blockButton.setVisibility(View.GONE);
                unblockButton.setVisibility(View.VISIBLE);
                Toast.makeText(UserDetailsActivity.this, "The user has been blocked.", Toast.LENGTH_SHORT).show();
            }
        });

        unblockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseModel.databaseRef_users.child(username.getText().toString()).child(FirebaseModel.node_isBlocked).setValue("false");
                blockButton.setVisibility(View.VISIBLE);
                unblockButton.setVisibility(View.GONE);
                Toast.makeText(UserDetailsActivity.this, "The user has been unblocked.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void InitializeFields()
    {
        firstName = findViewById(R.id.first_name);
        firstName.setText(getIntent().getStringExtra("firstName"));

        lastName = findViewById(R.id.last_name);
        lastName.setText(getIntent().getStringExtra("lastName"));

        username = findViewById(R.id.username);
        username.setText(getIntent().getStringExtra("username"));

        emailAddress = findViewById(R.id.email);
        emailAddress.setText(getIntent().getStringExtra("email"));

        phoneNumber = findViewById(R.id.phone);
        phoneNumber.setText(getIntent().getStringExtra("phone"));

        panNumber = findViewById(R.id.pan_number);
        panNumber.setText(getIntent().getStringExtra("panNum").equals("") ? "N/A" : getIntent().getStringExtra("panNum"));

        panURL = findViewById(R.id.pan_view);
        //panURL.setText(getIntent().getStringExtra("panURL"));

        aadharNumber = findViewById(R.id.aadhar_number);
        aadharNumber.setText(getIntent().getStringExtra("aadharNum").equals("") ? "N/A" : getIntent().getStringExtra("aadharNum"));

        aadharURL = findViewById(R.id.aadhar_view);
        //aadharURL.setText(getIntent().getStringExtra("aadharURL"));

        profileImage = findViewById(R.id.profile_image);
        if(!(getIntent().getStringExtra("profilePicURL")).equals(""))
        Picasso.get().load(getIntent().getStringExtra("profilePicURL")).into(profileImage);

        address = findViewById(R.id.address);
        address.setText(getIntent().getStringExtra("address"));

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(firstName.getText().toString() + " " + lastName.getText().toString());

        blockButton = findViewById(R.id.block_button);
        unblockButton = findViewById(R.id.unblock_button);

        if(getIntent().getBooleanExtra("isBlocked", false))
        {
            unblockButton.setVisibility(View.VISIBLE);
        }
        else
        {
            blockButton.setVisibility(View.VISIBLE);
        }
    }





    private void SendToImageActivity(Class<? extends Activity> activityClass, boolean backEnabled, String imageHeading, String type)
    {
        Intent intent = new Intent(this, activityClass);
        intent.putExtra("username", this.username.getText().toString());
        intent.putExtra("imageHeading", imageHeading);
        intent.putExtra("type", type);

        if (!backEnabled)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);

        if (!backEnabled)
            finish();
    }
}