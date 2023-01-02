package com.mr_17.evira.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mr_17.evira.R;
import com.mr_17.evira.adapter.CategoryRecyclerViewAdapter;
import com.mr_17.evira.adapter.ProductsListRecyclerViewAdapter;
import com.mr_17.evira.model.CategoryRecyclerViewModel;
import com.mr_17.evira.model.FirebaseModel;
import com.mr_17.evira.model.ProductsListRecyclerViewModel;

import java.util.ArrayList;

public class ProductsListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<ProductsListRecyclerViewModel> list;
    private ProductsListRecyclerViewAdapter.RecyclerViewClickListener listener;

    private Toolbar toolbar;

    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        InitializeFields();
        InitializeCategoryRecyclerView();
    }

    private void InitializeFields()
    {
        category = getIntent().getStringExtra("category");
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(category);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void InitializeCategoryRecyclerView()
    {
        SetOnClickListener();

        recyclerView = findViewById(R.id.recycler_view);
        list = new ArrayList<>();

        ProductsListRecyclerViewAdapter adapter = new ProductsListRecyclerViewAdapter(list, this, listener);
        recyclerView.setAdapter(adapter);

        FirebaseModel.databaseRef_categories.child(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(int i = 1; i <= snapshot.getChildrenCount(); i++)
                    {
                        list.add(new ProductsListRecyclerViewModel(snapshot.child(Integer.toString(i)).child(FirebaseModel.node_productName).getValue().toString(),
                                "₹" + snapshot.child(Integer.toString(i)).child(FirebaseModel.node_productPrice).getValue().toString(),
                                snapshot.child(Integer.toString(i)).child(FirebaseModel.node_productImage).getValue().toString()));

                        GridLayoutManager gridLayoutManager = new GridLayoutManager(ProductsListActivity.this, 2);
                        recyclerView.setLayoutManager(gridLayoutManager);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*list.add(new ProductsListRecyclerViewModel("Roller Rabbit", "₹599", R.drawable.sample_prod));
        list.add(new ProductsListRecyclerViewModel("Roller Rabbit", "₹599", R.drawable.sample_prod));
        list.add(new ProductsListRecyclerViewModel("Roller Rabbit", "₹599", R.drawable.sample_prod));
        list.add(new ProductsListRecyclerViewModel("Roller Rabbit", "₹599", R.drawable.sample_prod));
        list.add(new ProductsListRecyclerViewModel("Roller Rabbit", "₹599", R.drawable.sample_prod));*/
    }

    private void SetOnClickListener()
    {
        listener = new ProductsListRecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position)
            {
                Intent intent = new Intent(ProductsListActivity.this, ProductActivity.class);
                intent.putExtra("price", list.get(position).getProductPrice());
                intent.putExtra("name", list.get(position).getProductName());
                intent.putExtra("imageURL", list.get(position).getImageUrl());
                intent.putExtra("category", category);
                intent.putExtra("index", position);
                startActivity(intent);
            }
        };
    }
}