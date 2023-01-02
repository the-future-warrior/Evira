package com.mr_17.evira.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;

import com.mr_17.evira.R;
import com.mr_17.evira.model.FirebaseModel;
import com.squareup.picasso.Picasso;

public class ProductActivity extends AppCompatActivity {

    private AppCompatImageView productImage;
    private AppCompatTextView productName, productCategory, productPrice, productDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        InitializeFields();
    }

    private void InitializeFields()
    {
        productImage = findViewById(R.id.product_image);
        Picasso.get().load(getIntent().getStringExtra("imageURL")).into(productImage);

        productName = findViewById(R.id.product_name);
        productName.setText(getIntent().getStringExtra("name"));

        productCategory = findViewById(R.id.product_category);
        productCategory.setText(getIntent().getStringExtra("category"));

        productPrice = findViewById(R.id.product_price);
        productPrice.setText(getIntent().getStringExtra("price"));

        productDescription = findViewById(R.id.description);
    }
}