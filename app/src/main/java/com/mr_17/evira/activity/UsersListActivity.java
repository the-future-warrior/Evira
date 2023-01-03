package com.mr_17.evira.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mr_17.evira.R;
import com.mr_17.evira.adapter.CartListRecyclerViewAdapter;
import com.mr_17.evira.adapter.UsersListRecyclerViewAdapter;
import com.mr_17.evira.model.FirebaseModel;
import com.mr_17.evira.model.ProductsListRecyclerViewModel;
import com.mr_17.evira.model.UsersListRecyclerViewModel;

import java.util.ArrayList;

public class UsersListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<UsersListRecyclerViewModel> list;
    private UsersListRecyclerViewAdapter.RecyclerViewClickListener listener;
    UsersListRecyclerViewAdapter adapter;

    private Toolbar toolbar;
    private LinearProgressIndicator progressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        InitializeFields();
        InitializeCategoryRecyclerView();
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
        progressIndicator = findViewById(R.id.loading_bar);
    }

    private void InitializeCategoryRecyclerView()
    {
        SetOnClickListener();

        recyclerView = findViewById(R.id.recycler_view);
        list = new ArrayList<>();

        adapter = new UsersListRecyclerViewAdapter(list, this, listener);
        recyclerView.setAdapter(adapter);
        FirebaseModel.databaseRef_users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d: snapshot.getChildren())
                {
                    String username = d.getKey();
                    DataSnapshot childSnapshot = snapshot.child(username);
                    //Toast.makeText(UsersListActivity.this, snapshot.child(FirebaseModel.node_firstName).getValue().toString(), Toast.LENGTH_SHORT).show();

                    boolean check = false;

                    switch (getIntent().getStringExtra("type"))
                    {
                        case "users":
                            check = !Boolean.parseBoolean(childSnapshot.child(FirebaseModel.node_isAdmin).getValue().toString()) && !Boolean.parseBoolean(childSnapshot.child(FirebaseModel.node_isSuperAdmin).getValue().toString());
                            break;
                        case "admins":
                            check = Boolean.parseBoolean(childSnapshot.child(FirebaseModel.node_isAdmin).getValue().toString()) && !Boolean.parseBoolean(childSnapshot.child(FirebaseModel.node_isSuperAdmin).getValue().toString());
                            break;
                    }


                    if(check) {
                        list.add(new UsersListRecyclerViewModel(
                                childSnapshot.child(FirebaseModel.node_firstName).getValue().toString(),
                                childSnapshot.child(FirebaseModel.node_lastName).getValue().toString(),
                                childSnapshot.child(FirebaseModel.node_username).getValue().toString(),
                                childSnapshot.child(FirebaseModel.node_email).getValue().toString(),
                                childSnapshot.child(FirebaseModel.node_phone).getValue().toString(),
                                childSnapshot.child(FirebaseModel.node_pan).getValue().toString(),
                                childSnapshot.child(FirebaseModel.node_panPic).exists() ? childSnapshot.child(FirebaseModel.node_panPic).getValue().toString() : "",
                                childSnapshot.child(FirebaseModel.node_aadhar).getValue().toString(),
                                childSnapshot.child(FirebaseModel.node_aadharPic).exists() ? childSnapshot.child(FirebaseModel.node_aadharPic).getValue().toString() : "",
                                childSnapshot.child(FirebaseModel.node_profilePic).exists() ? childSnapshot.child(FirebaseModel.node_profilePic).getValue().toString() : "",
                                childSnapshot.child(FirebaseModel.node_address).getValue().toString(),
                                Boolean.parseBoolean(childSnapshot.child(FirebaseModel.node_isBlocked).getValue().toString()),
                                Boolean.parseBoolean(childSnapshot.child(FirebaseModel.node_isAdmin).getValue().toString()),
                                Boolean.parseBoolean(childSnapshot.child(FirebaseModel.node_isSuperAdmin).getValue().toString())
                        ));

                        GridLayoutManager gridLayoutManager = new GridLayoutManager(UsersListActivity.this, 1);
                        recyclerView.setLayoutManager(gridLayoutManager);
                    }
                }
                progressIndicator.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SetOnClickListener()
    {
        listener = new UsersListRecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position)
            {
                Intent intent = new Intent(UsersListActivity.this, UserDetailsActivity.class);
                intent.putExtra("firstName", list.get(position).getFirstName());
                intent.putExtra("lastName", list.get(position).getLastName());
                intent.putExtra("username", list.get(position).getUsername());
                intent.putExtra("email", list.get(position).getEmailAddress());
                intent.putExtra("phone", list.get(position).getPhoneNumber());
                intent.putExtra("panNum", list.get(position).getPanNumber());
                intent.putExtra("panURL", list.get(position).getPanURL());
                intent.putExtra("aadharNum", list.get(position).getAadharNumber());
                intent.putExtra("aadharURL", list.get(position).getAadharURL());
                intent.putExtra("profilePicURL", list.get(position).getProfilePicURL());
                intent.putExtra("address", list.get(position).getAddress());
                intent.putExtra("isBlocked", list.get(position).isBlocked());
                startActivity(intent);
            }
        };
    }
}