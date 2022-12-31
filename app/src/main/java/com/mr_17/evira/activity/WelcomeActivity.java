package com.mr_17.evira.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mr_17.evira.R;

public class WelcomeActivity extends AppCompatActivity {

    private AppCompatButton userButton, adminButton, superAdminButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        InitializeFields();

        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendToActivity(LoginActivity.class, true);
            }
        });
    }

    private void InitializeFields()
    {
        userButton = findViewById(R.id.user_button);
        adminButton = findViewById(R.id.admin_button);
        superAdminButton = findViewById(R.id.super_admin_button);
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