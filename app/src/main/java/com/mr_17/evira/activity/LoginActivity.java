package com.mr_17.evira.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mr_17.evira.R;

public class LoginActivity extends AppCompatActivity {

    private AppCompatTextView signUpLink;

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
    }

    private void InitializeFields()
    {
        signUpLink = findViewById(R.id.sign_up_link);
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
}