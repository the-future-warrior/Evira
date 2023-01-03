package com.mr_17.evira.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.mr_17.evira.R;
import com.mr_17.evira.adapter.CartListRecyclerViewAdapter;
import com.mr_17.evira.adapter.ProductsListRecyclerViewAdapter;
import com.mr_17.evira.model.FirebaseModel;
import com.mr_17.evira.model.ProductsListRecyclerViewModel;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private AppCompatTextView totalPrice, noItemMsg;
    private AppCompatButton buyNowButton;

    private int totalPriceNum = 0;

    private Toolbar toolbar;
    private LinearProgressIndicator progressIndicator;

    private SharedPreferences sharedPref;

    private ChipNavigationBar chipNavigationBar;

    private RecyclerView recyclerView;
    private ArrayList<ProductsListRecyclerViewModel> list;
    private CartListRecyclerViewAdapter.RecyclerViewClickListener listener;
    CartListRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        InitializeFields();
        InitializeCategoryRecyclerView();
    }

    private void InitializeFields()
    {
        totalPrice = findViewById(R.id.total_price);
        noItemMsg = findViewById(R.id.no_item_msg);
        buyNowButton = findViewById(R.id.buy_now_button);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        progressIndicator = findViewById(R.id.loading_bar);

        sharedPref = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        chipNavigationBar = findViewById(R.id.bottom_nav_bar);
        chipNavigationBar.setItemSelected(R.id.cart, true);
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (chipNavigationBar.getSelectedItemId())
                {
                    case R.id.dashboard:
                        SendToActivity(UserDashboardActivity.class, true);
                        break;

                    case R.id.settings:
                        SendToActivity(SettingsActivity.class, true);
                        break;
                }
                chipNavigationBar.setItemSelected(R.id.cart, true);
            }
        });
    }

    private void InitializeCategoryRecyclerView()
    {
        SetOnClickListener();

        recyclerView = findViewById(R.id.recycler_view);
        list = new ArrayList<>();

        adapter = new CartListRecyclerViewAdapter(list, this, listener);
        recyclerView.setAdapter(adapter);
        FirebaseModel.databaseRef_users.child(sharedPref.getString("username", "!@#")).child(FirebaseModel.node_cart).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() == 0)
                {
                    noItemMsg.setVisibility(View.VISIBLE);
                }
                for(DataSnapshot d: snapshot.getChildren())
                {
                    String timeStamp = d.getKey();

                    for(DataSnapshot ds: snapshot.child(timeStamp).getChildren())
                    {
                        String category = ds.getKey();
                        String index = ds.getValue().toString();

                        FirebaseModel.databaseRef_categories.child(category).child(index).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    list.add(new ProductsListRecyclerViewModel(snapshot.child(FirebaseModel.node_productName).getValue().toString(),
                                            "₹" + snapshot.child(FirebaseModel.node_productPrice).getValue().toString(),
                                            category,
                                            snapshot.child(FirebaseModel.node_productImage).getValue().toString(),timeStamp));

                                    GridLayoutManager gridLayoutManager = new GridLayoutManager(CartActivity.this, 1);
                                    recyclerView.setLayoutManager(gridLayoutManager);

                                    totalPriceNum += Integer.parseInt(snapshot.child(FirebaseModel.node_productPrice).getValue().toString());
                                    totalPrice.setText("₹" + Integer.toString(totalPriceNum));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
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
        listener = new CartListRecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position)
            {
                FirebaseModel.databaseRef_users.child(sharedPref.getString("username", "!@#")).child(FirebaseModel.node_cart).child(list.get(position).getTimeStamp()).child(list.get(position).getProductCategory()).removeValue();
                list.remove(position);
                adapter.notifyItemRemoved(position);
                Toast.makeText(CartActivity.this, "Item removed from cart.", Toast.LENGTH_SHORT).show();

                if(list.size() == 0)
                    noItemMsg.setVisibility(View.VISIBLE);
            }
        };
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