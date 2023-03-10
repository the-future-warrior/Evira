package com.mr_17.evira.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.mr_17.evira.R;
import com.mr_17.evira.adapter.CategoryRecyclerViewAdapter;
import com.mr_17.evira.model.CategoryRecyclerViewModel;
import com.mr_17.evira.model.FirebaseModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDashboardActivity extends AppCompatActivity {

    private AppCompatTextView wishingMsg;
    private AppCompatTextView fullName;
    private CircleImageView profileImage;

    private ChipNavigationBar chipNavigationBar;

    private RecyclerView recyclerView;
    private ArrayList<CategoryRecyclerViewModel> list;
    private CategoryRecyclerViewAdapter.RecyclerViewClickListener listener;

    private FirebaseAuth myAuth;

    private ArrayList<String> categories = new ArrayList<>();

    private String type;
    private ConstraintLayout adminFeaturesContainer;
    private AppCompatTextView adminFeaturesHeading;
    private AppCompatButton addProductsButton, viewUserDetailsButton, viewAdminDetailsButton;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        InitializeFields();
        InitializeUserData();
        GetCategories();
        //InitializeCategoryRecyclerView();

        addProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendToActivity(AddProductsActivity.class, true);
            }
        });

        viewUserDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendToActivity(UsersListActivity.class, true, "users");
            }
        });

        viewAdminDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendToActivity(UsersListActivity.class, true, "admins");
            }
        });
    }

    private void InitializeFields()
    {
        sharedPref = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        myAuth = FirebaseAuth.getInstance();
        type = sharedPref.getString("type", "User");
        adminFeaturesContainer = findViewById(R.id.admin_features_container);
        adminFeaturesHeading = findViewById(R.id.admin_features_heading);
        addProductsButton = findViewById(R.id.add_products_button);
        viewUserDetailsButton = findViewById(R.id.view_user_details_button);
        viewAdminDetailsButton = findViewById(R.id.view_admin_details_button);

        wishingMsg = findViewById(R.id.wishing_msg);
        wishingMsg.setText("Good " + GetWishing() + ",");
        wishingMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendToActivity(ProductsListActivity.class, true);
            }
        });

        fullName = findViewById(R.id.full_name);
        profileImage = findViewById(R.id.profile_image);

        chipNavigationBar = findViewById(R.id.bottom_nav_bar);
        chipNavigationBar.setItemSelected(R.id.dashboard, true);
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (chipNavigationBar.getSelectedItemId())
                {
                    case R.id.settings:
                        SendToActivity(SettingsActivity.class, true);
                        break;

                    case R.id.cart:
                        SendToActivity(CartActivity.class, true);
                        break;
                }

                chipNavigationBar.setItemSelected(R.id.dashboard, true);
            }
        });

        switch (type)
        {
            case "User":
                break;
            case "Admin":
                adminFeaturesContainer.setVisibility(View.VISIBLE);
                break;
            case "Super Admin":
                adminFeaturesContainer.setVisibility(View.VISIBLE);
                adminFeaturesHeading.setText("Super Admin Features");
                addProductsButton.setVisibility(View.GONE);
                viewAdminDetailsButton.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void InitializeUserData()
    {
        SharedPreferences.Editor editor = sharedPref.edit();

        FirebaseModel.databaseRef_uids.child(myAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    editor.putString("username", snapshot.getValue().toString());
                    editor.apply();
                    FirebaseModel.databaseRef_users.child(snapshot.getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                fullName.setText(snapshot.child(FirebaseModel.node_firstName).getValue() + " " + snapshot.child(FirebaseModel.node_lastName).getValue());
                                if(snapshot.child(FirebaseModel.node_profilePic).exists())
                                    Picasso.get().load(snapshot.child(FirebaseModel.node_profilePic).getValue().toString()).into(profileImage);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void GetCategories()
    {
        FirebaseModel.databaseRef_categories.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot ds : snapshot.getChildren())
                    {
                        categories.add(ds.getKey());
                    }
                    InitializeCategoryRecyclerView(categories);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void InitializeCategoryRecyclerView(ArrayList<String> categories)
    {
        SetOnClickListener();

        recyclerView = findViewById(R.id.recycler_view);
        list = new ArrayList<>();

        CategoryRecyclerViewAdapter adapter = new CategoryRecyclerViewAdapter(list, this, listener);
        recyclerView.setAdapter(adapter);

        for(String category: categories)
        {
            FirebaseModel.databaseRef_categories.child(category).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        list.add(new CategoryRecyclerViewModel(category, (int)(snapshot.getChildrenCount()), R.drawable.icon_clothes));

                        GridLayoutManager gridLayoutManager = new GridLayoutManager(UserDashboardActivity.this, 1);
                        recyclerView.setLayoutManager(gridLayoutManager);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        /*list.add(new CategoryRecyclerViewModel("Clothes", 5, R.drawable.icon_clothes));
        list.add(new CategoryRecyclerViewModel("Bags", 5, R.drawable.icon_bags));
        list.add(new CategoryRecyclerViewModel("Shoes", 5, R.drawable.shoes));
        list.add(new CategoryRecyclerViewModel("Electronics", 6, R.drawable.icon_clothes));
        list.add(new CategoryRecyclerViewModel("Jewellery", 5, R.drawable.icon_clothes));*/
    }

    private void SetOnClickListener()
    {
        listener = new CategoryRecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position)
            {
                Intent intent = new Intent(UserDashboardActivity.this, ProductsListActivity.class);
                intent.putExtra("category", categories.get(position));
                startActivity(intent);
            }
        };
    }

    private String GetWishing()
    {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay < 12)
            return "Morning";
        else if(timeOfDay < 16)
            return "Afternoon";
        else if(timeOfDay < 24)
            return "Evening";
        else
            return "Morning";
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

    private void SendToActivity(Class<? extends Activity> activityClass, boolean backEnabled, String type)
    {
        Intent intent = new Intent(this, activityClass);
        intent.putExtra("type", type);

        if (!backEnabled)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);

        if (!backEnabled)
            finish();
    }
}