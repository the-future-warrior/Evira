package com.mr_17.evira.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.mr_17.evira.R;
import com.mr_17.evira.model.FirebaseModel;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private CircleImageView profileImage;
    private ImageView profileImageEditButton;

    private AppCompatButton uploadAadharButton;
    private AppCompatButton uploadPanButton;
    private AppCompatTextView viewAadharButton;
    private AppCompatTextView viewPanButton;

    private AppCompatEditText phoneEditText;
    private AppCompatEditText addressEditText;
    private AppCompatButton saveButton;

    private AppCompatButton orderHistoryButton;

    private ChipNavigationBar chipNavigationBar;

    private View view;
    private SharedPreferences sharedPreferences;

    private FirebaseAuth myAuth;
    private DatabaseReference rootRef;

    int SELECT_AADHAR = 181;
    int SELECT_PAN = 182;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        InitializeFields();
        RetrieveData();

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendToImageActivity(ImageActivity.class, true, "Profile Image", "profilePic");
            }
        });

        profileImageEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsActivity.this.view = view;
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(SettingsActivity.this);
            }
        });

        uploadAadharButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);

                // pass the constant to compare it
                // with the returned requestCode
                startActivityForResult(Intent.createChooser(i, "Select Aadhar Picture"), SELECT_AADHAR);
            }
        });

        uploadPanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);

                // pass the constant to compare it
                // with the returned requestCode
                startActivityForResult(Intent.createChooser(i, "Select Pan Picture"), SELECT_PAN);
            }
        });

        viewAadharButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendToImageActivity(ImageActivity.class, true, "Aadhar Image", "aadharPic");
            }
        });

        viewPanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendToImageActivity(ImageActivity.class, true, "Pan Image", "panPic");
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveData();
            }
        });
    }

    private void InitializeFields()
    {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(new androidx.appcompat.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.settings_nav_log_out)
                {
                    // setting up the alert dialog
                    final AlertDialog.Builder confirmLogoutDialog = new AlertDialog.Builder(SettingsActivity.this);
                    confirmLogoutDialog.setTitle("Confirm Logout");
                    confirmLogoutDialog.setMessage("Are sure you want to logout?");

                    // creating functionality of the "yes" button
                    confirmLogoutDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myAuth.signOut();
                            SendToActivity(WelcomeActivity.class, false);
                        }
                    });

                    // creating the "no" button
                    confirmLogoutDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    confirmLogoutDialog.create().show();
                }
                return true;
            }
        });

        profileImage = findViewById(R.id.profile_image);
        profileImageEditButton = findViewById(R.id.edit_profile_image_button);

        uploadAadharButton = findViewById(R.id.upload_aadhar_button);
        uploadPanButton = findViewById(R.id.upload_pan_button);
        viewAadharButton = findViewById(R.id.view_aadhar_button);
        viewPanButton = findViewById(R.id.view_pan_button);

        phoneEditText = findViewById(R.id.phone_number_edittext);
        addressEditText = findViewById(R.id.address_edittext);
        saveButton = findViewById(R.id.save_button);

        orderHistoryButton = findViewById(R.id.show_order_history_button);

        chipNavigationBar = findViewById(R.id.bottom_nav_bar);
        chipNavigationBar.setItemSelected(R.id.settings, true);
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (chipNavigationBar.getSelectedItemId())
                {
                    case R.id.dashboard:
                        SendToActivity(UserDashboardActivity.class, true);
                        break;

                    case R.id.cart:
                        SendToActivity(CartActivity.class, true);
                        break;
                }
                chipNavigationBar.setItemSelected(R.id.settings, true);
            }
        });

        sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        myAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseModel.databaseRef_root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            ProgressDialog loadingBar = new ProgressDialog(this);
            loadingBar.setTitle("Updating Profile Image");
            loadingBar.setMessage("Please wait, your profile image is updating...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            Uri resultUri = result.getUri();

            if (calculateFileSize(resultUri.getPath()) <= 5000) //2048
            {
                StorageReference filePath = FirebaseModel.storageRef_profileImages.child(myAuth.getUid() + ".jpg");

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(getContext(), "Profile Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                            filePath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    final String downloadUrl = task.getResult().toString();

                                    rootRef.child(FirebaseModel.node_users).child(sharedPreferences.getString("username", "!@#")).child(FirebaseModel.node_profilePic).setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                profileImage.setImageURI(resultUri);
                                                Toast.makeText(SettingsActivity.this, "Your Profile Picture has been updated.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(SettingsActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        } else {
                            String message = task.getException().getMessage();
                            Toast.makeText(SettingsActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else
                Snackbar.make(view, "Image size limit exceeded, Try compressing the image and then upload...", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Okay", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setBackgroundTint(getResources().getColor(R.color.black))
                        .setTextColor(getResources().getColor(R.color.light_grey))
                        .setActionTextColor(getResources().getColor(R.color.white))
                        .show();
            loadingBar.dismiss();
        }

        //aadhar
        if(requestCode == SELECT_AADHAR && resultCode == Activity.RESULT_OK && data != null)
        {
            ProgressDialog loadingBar = new ProgressDialog(this);
            loadingBar.setTitle("Updating Aadhar Image");
            loadingBar.setMessage("Please wait, your aadhar image is updating...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            Uri resultUri = data.getData();

            if (calculateFileSize(resultUri.getPath()) <= 5000) //2048
            {
                StorageReference filePath = FirebaseModel.storageRef_aadharImages.child(myAuth.getUid() + ".jpg");

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(getContext(), "Profile Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                            filePath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    final String downloadUrl = task.getResult().toString();

                                    rootRef.child(FirebaseModel.node_users).child(sharedPreferences.getString("username", "!@#")).child(FirebaseModel.node_aadharPic).setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SettingsActivity.this, "Your Aadhar Picture has been updated.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(SettingsActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        } else {
                            String message = task.getException().getMessage();
                            Toast.makeText(SettingsActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else
                Snackbar.make(view, "Image size limit exceeded, Try compressing the image and then upload...", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Okay", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setBackgroundTint(getResources().getColor(R.color.black))
                        .setTextColor(getResources().getColor(R.color.light_grey))
                        .setActionTextColor(getResources().getColor(R.color.white))
                        .show();
            loadingBar.dismiss();
        }

        //pan
        if(requestCode == SELECT_PAN && resultCode == Activity.RESULT_OK && data != null)
        {
            ProgressDialog loadingBar = new ProgressDialog(this);
            loadingBar.setTitle("Updating Pan Image");
            loadingBar.setMessage("Please wait, your Pan image is updating...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            Uri resultUri = data.getData();

            if (calculateFileSize(resultUri.getPath()) <= 5000) //2048
            {
                StorageReference filePath = FirebaseModel.storageRef_panImages.child(myAuth.getUid() + ".jpg");

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(getContext(), "Profile Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                            filePath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    final String downloadUrl = task.getResult().toString();

                                    rootRef.child(FirebaseModel.node_users).child(sharedPreferences.getString("username", "!@#")).child(FirebaseModel.node_panPic).setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SettingsActivity.this, "Your Pan Picture has been updated.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(SettingsActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        } else {
                            String message = task.getException().getMessage();
                            Toast.makeText(SettingsActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else
                Snackbar.make(view, "Image size limit exceeded, Try compressing the image and then upload...", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Okay", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setBackgroundTint(getResources().getColor(R.color.black))
                        .setTextColor(getResources().getColor(R.color.light_grey))
                        .setActionTextColor(getResources().getColor(R.color.white))
                        .show();
            loadingBar.dismiss();
        }
    }

    public float calculateFileSize(String filepath)
    {
        //String filepathstr=filepath.toString();
        File file = new File(filepath);

        float fileSizeInBytes = file.length();

        float fileSizeInKB = fileSizeInBytes / 1024;
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        float fileSizeInMB = fileSizeInKB / 1024;

        return fileSizeInKB;
    }

    private void RetrieveData()
    {
        rootRef.child(FirebaseModel.node_users).child(sharedPreferences.getString("username", "!@#"))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if((snapshot.exists()) && (snapshot.hasChild(FirebaseModel.node_phone)) && (snapshot.hasChild(FirebaseModel.node_address)) && (snapshot.hasChild(FirebaseModel.node_profilePic)))
                        {
                            phoneEditText.setText(snapshot.child(FirebaseModel.node_phone).getValue().toString());
                            addressEditText.setText(snapshot.child(FirebaseModel.node_address).getValue().toString());
                            Picasso.get().load(snapshot.child(FirebaseModel.node_profilePic).getValue().toString()).into(profileImage);
                        }
                        else if((snapshot.exists()) && (snapshot.hasChild(FirebaseModel.node_firstName)) && (snapshot.hasChild(FirebaseModel.node_lastName)))
                        {
                            phoneEditText.setText(snapshot.child(FirebaseModel.node_phone).getValue().toString());
                            addressEditText.setText(snapshot.child(FirebaseModel.node_address).getValue().toString());
                        }
                        else
                        {
                            Toast.makeText(SettingsActivity.this, "An error occurred...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void SaveData()
    {
        String phoneNumber = phoneEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();

        if(TextUtils.isEmpty(phoneNumber))
        {
            Toast.makeText(this, "'Phone Number' Field cannot be empty.", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(address))
        {
            Toast.makeText(this, "'Address' Field cannot be empty", Toast.LENGTH_LONG).show();
        }
        else
        {
            rootRef.child(FirebaseModel.node_users).child(sharedPreferences.getString("username", "!@#")).child(FirebaseModel.node_phone).setValue(phoneNumber);
            rootRef.child(FirebaseModel.node_users).child(sharedPreferences.getString("username", "!@#")).child(FirebaseModel.node_address).setValue(address);

            Toast.makeText(this, "Your Personal Details have been updated.", Toast.LENGTH_SHORT).show();
        }
    }

    private void SendToActivity(Class<? extends Activity> activityClass, boolean backEnabled)
    {
        Intent intent = new Intent(this, activityClass);

        if (!backEnabled)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);

        if (!backEnabled)
            finish();
    }

    private void SendToImageActivity(Class<? extends Activity> activityClass, boolean backEnabled, String imageHeading, String type)
    {
        Intent intent = new Intent(this, activityClass);
        intent.putExtra("imageHeading", imageHeading);
        intent.putExtra("type", type);

        if (!backEnabled)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);

        if (!backEnabled)
            finish();
    }
}