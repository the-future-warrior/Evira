package com.mr_17.evira.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mr_17.evira.R;
import com.mr_17.evira.model.FirebaseModel;

import java.util.HashMap;
import java.util.HashSet;

public class SignUpActivity extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText panEditText;
    private EditText aadharEditText;
    private EditText addressEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;

    private AppCompatButton createAccountButton;

    private Toolbar toolbar;

    private FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        InitializeFields();

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewAccount();
            }
        });
    }

    private void InitializeFields()
    {
        myAuth = FirebaseAuth.getInstance();

        firstNameEditText = findViewById(R.id.first_name_edittext);
        lastNameEditText = findViewById(R.id.last_name_edittext);
        usernameEditText = findViewById(R.id.username_edittext);
        emailEditText = findViewById(R.id.email_address_edittext);
        phoneEditText = findViewById(R.id.phone_number_edittext);
        panEditText = findViewById(R.id.pan_number_edittext);
        aadharEditText = findViewById(R.id.aadhar_number_edittext);
        addressEditText = findViewById(R.id.address_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edittext);

        createAccountButton = findViewById(R.id.create_account_button);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void CreateNewAccount()
    {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String pan = panEditText.getText().toString();
        String aadhar = aadharEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        ProgressDialog loadingBar;
        loadingBar = new ProgressDialog(this);

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email Required...", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Username Required...", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password Required...", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Password Confirmation Required...", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(this, "First Name Required...", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(this, "Last Name Required...", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Address Required...", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Phone Number Required...", Toast.LENGTH_LONG).show();
        }
        else if(!(password.equals(confirmPassword)))
        {
            Toast.makeText(this, "Passwords Mismatch...", Toast.LENGTH_LONG).show();
        }
        else
        {
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait, while we are creating a new account for you...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            myAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                String currentUserID = myAuth.getCurrentUser().getUid();

                                HashMap<String, String> profileMap = new HashMap<>();
                                profileMap.put(FirebaseModel.node_uid, currentUserID);
                                profileMap.put(FirebaseModel.node_firstName, firstName);
                                profileMap.put(FirebaseModel.node_lastName, lastName);
                                profileMap.put(FirebaseModel.node_username, username);
                                profileMap.put(FirebaseModel.node_email, email);
                                profileMap.put(FirebaseModel.node_phone, phone);
                                profileMap.put(FirebaseModel.node_pan, pan);
                                profileMap.put(FirebaseModel.node_aadhar, aadhar);
                                profileMap.put(FirebaseModel.node_address, address);
                                profileMap.put(FirebaseModel.node_isBlocked, "false");
                                profileMap.put(FirebaseModel.node_isAdmin, "false");
                                profileMap.put(FirebaseModel.node_isSuperAdmin, "false");

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                FirebaseModel.databaseRef_users.child(username).setValue("");
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                                FirebaseModel.databaseRef_users.child(username).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        FirebaseModel.databaseRef_uids.child(currentUserID).setValue(username).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                SendToActivity(LoginActivity.class, false);
                                                Toast.makeText(SignUpActivity.this, "Account Created Successfully.", Toast.LENGTH_LONG).show();
                                                loadingBar.dismiss();
                                            }
                                        });
                                    }
                                });
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(SignUpActivity.this, "Error: " + message, Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
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

    private void isUsernameAvailable()
    {
        HashSet<String> usernames = new HashSet<>();
        FirebaseModel.databaseRef_users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot d: snapshot.getChildren())
                    {
                        usernames.add(d.getKey());
                    }
                    if(usernames.add(usernameEditText.getText().toString()))
                    {
                        CreateNewAccount();
                    }
                    else
                    {
                        Toast.makeText(SignUpActivity.this, "Username is already taken.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}