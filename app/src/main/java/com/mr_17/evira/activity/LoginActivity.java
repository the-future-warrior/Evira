package com.mr_17.evira.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
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

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private AppCompatTextView needAccText, signUpLink;
    private AppCompatButton loginButton;

    private Toolbar toolbar;

    private String type;

    private FirebaseAuth myAuth;

    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InitializeFields();

        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendToActivity(SignUpActivity.class, true);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar = new ProgressDialog(LoginActivity.this);
                // setting up the loading bar
                loadingBar.setTitle(("Logging In"));
                loadingBar.setMessage("Please Wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                FirebaseModel.databaseRef_users.child(usernameEditText.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists() && !usernameEditText.getText().toString().isEmpty())
                        {
                            boolean allowed = false;
                            String msg = "";
                            switch (type)
                            {
                                case "User":
                                    allowed = !(Boolean.parseBoolean(snapshot.child(FirebaseModel.node_isBlocked).getValue().toString()));
                                    msg = "Sorry you cannot login, your account has been blocked.";
                                    break;
                                case "Admin":
                                    allowed = Boolean.parseBoolean(snapshot.child(FirebaseModel.node_isAdmin).getValue().toString());
                                    msg = "Sorry you are not authorised to enter here...";
                                    break;
                                case "Super Admin":
                                    allowed = Boolean.parseBoolean(snapshot.child(FirebaseModel.node_isSuperAdmin).getValue().toString());
                                    msg = "Sorry you are not authorised to enter here...";
                                    break;
                            }

                            if(allowed)
                                LoginUser(snapshot.child(FirebaseModel.node_email).getValue().toString(), passwordEditText.getText().toString());
                            else {
                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Invalid username...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void InitializeFields()
    {
        myAuth = FirebaseAuth.getInstance();

        type = getIntent().getStringExtra("type");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(type + " " + "Login");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendToActivity(WelcomeActivity.class, false);
            }
        });

        usernameEditText = findViewById(R.id.username_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
        needAccText = findViewById(R.id.need_an_account);
        signUpLink = findViewById(R.id.sign_up_link);
        loginButton = findViewById(R.id.login_button);

        if(type.equals("User"))
        {
            needAccText.setVisibility(View.VISIBLE);
            signUpLink.setVisibility(View.VISIBLE);
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

    private void LoginUser(String email, String password)
    {
        // checking that none of the fields are empty and giving appropriate toasts
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Invalid username...", Toast.LENGTH_LONG).show();
            loadingBar.dismiss();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter password...", Toast.LENGTH_LONG).show();
            loadingBar.dismiss();
        }
        else
        {
            // attempting to sign in using firebase
            myAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        // sending user to main activity on successful sign in
                        SendToActivity(UserDashboardActivity.class, false);
                        //Toast.makeText(LoginActivity.this, "Logged in Successfully...", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                    else
                    {
                        // otherwise displaying the error message
                        String message = task.getException().getMessage();
                        Toast.makeText(LoginActivity.this, "Error: " + message, Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        SendToActivity(WelcomeActivity.class, false);
    }
}