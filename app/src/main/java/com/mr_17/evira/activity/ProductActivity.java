package com.mr_17.evira.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mr_17.evira.R;
import com.mr_17.evira.model.FirebaseModel;
import com.squareup.picasso.Picasso;

public class ProductActivity extends AppCompatActivity {

    private AppCompatImageView productImage;
    private AppCompatTextView productName, productCategory, productPrice, productDescription;
    private AppCompatButton addToCartButton;



    private String index = "";

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        InitializeFields();
        RetrieveData();

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddToCart();
            }
        });
    }

    private void InitializeFields()
    {
        sharedPref = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        productImage = findViewById(R.id.product_image);
        Picasso.get().load(getIntent().getStringExtra("imageURL")).into(productImage);

        productName = findViewById(R.id.product_name);
        productName.setText(getIntent().getStringExtra("name"));

        productCategory = findViewById(R.id.product_category);
        productCategory.setText(getIntent().getStringExtra("category"));

        productPrice = findViewById(R.id.product_price);
        productPrice.setText(getIntent().getStringExtra("price"));

        index = getIntent().getStringExtra("index");

        productDescription = findViewById(R.id.description);

        addToCartButton = findViewById(R.id.add_to_cart_button);
    }

    private void RetrieveData()
    {
        FirebaseModel.databaseRef_categories.child(productCategory.getText().toString()).child(index).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    productDescription.setText(snapshot.child(FirebaseModel.node_productDescription).getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void AddToCart()
    {
        FirebaseModel.databaseRef_users.child(sharedPref.getString("username", "!@#")).child(FirebaseModel.node_cart).child(String.valueOf(System.currentTimeMillis())).child(productCategory.getText().toString()).setValue(index).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(ProductActivity.this, "Product has been added to Cart!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}