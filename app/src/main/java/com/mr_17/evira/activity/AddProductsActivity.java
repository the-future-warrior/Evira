package com.mr_17.evira.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.mr_17.evira.R;
import com.mr_17.evira.model.FirebaseModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.HashMap;

public class AddProductsActivity extends AppCompatActivity {

    private AppCompatImageView productImageButton;
    private TextView addProductMsg;
    private AppCompatEditText productNameEditText, productPriceEditText, productDescriptionEditText;
    private AppCompatButton addProductButton;
    private AppCompatSpinner productCategorySpinner;

    private Uri productImageUri;

    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        InitializeFields();

        productImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddProductsActivity.this.view = view;
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(AddProductsActivity.this);
            }
        });

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveData();
            }
        });
    }

    private void InitializeFields()
    {
        productImageButton = findViewById(R.id.product_image);
        addProductMsg = findViewById(R.id.add_product_image_msg);
        productNameEditText = findViewById(R.id.product_name_edittext);
        productPriceEditText = findViewById(R.id.product_price_edittext);
        productDescriptionEditText = findViewById(R.id.product_description_edittext);
        addProductButton = findViewById(R.id.add_product_button);
        productCategorySpinner = findViewById(R.id.product_category_spinner);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.categories, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        productCategorySpinner.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            productImageUri = result.getUri();

            if (calculateFileSize(productImageUri.getPath()) <= 5000) //2048
            {

                productImageButton.setImageURI(productImageUri);
                addProductMsg.setVisibility(View.GONE);
            } else {
                Snackbar.make(view, "Image size limit exceeded, Try compressing the image and then upload...", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Okay", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setBackgroundTint(getResources().getColor(R.color.black))
                        .setTextColor(getResources().getColor(R.color.light_grey))
                        .setActionTextColor(getResources().getColor(R.color.white))
                        .show();
                productImageUri = null;
            }
        }
    }

    public float calculateFileSize(String filepath)
    {
            //String filepathstr=filepath.toString();
            File file = new File(filepath);

            float fileSizeInBytes = file.length();

            float fileSizeInKB = fileSizeInBytes / 1024;
            // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
            float fileSizeInMB = fileSizeInKB / 1024;

            return fileSizeInKB;
        }

    private void SaveData()
    {
        String productCategory = productCategorySpinner.getSelectedItem().toString();
        String productName = productNameEditText.getText().toString().trim();
        String productPrice = productPriceEditText.getText().toString().trim();
        String productDescription = productDescriptionEditText.getText().toString().trim();

        if(TextUtils.isEmpty(productName))
        {
            Toast.makeText(this, "'Product Name' Field cannot be empty.", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(productPrice))
        {
            Toast.makeText(this, "'Product Price' Field cannot be empty", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(productDescription))
        {
            Toast.makeText(this, "'Product Description' Field cannot be empty", Toast.LENGTH_LONG).show();
        }
        else if(productImageUri == null)
        {
            Toast.makeText(this, "'Product Image' cannot be empty", Toast.LENGTH_LONG).show();
        }
        else
        {
            ProgressDialog loadingBar = new ProgressDialog(this);
            loadingBar.setTitle("Adding New Product");
            loadingBar.setMessage("Please wait, while we are adding the new product...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            HashMap<String, String> productMap = new HashMap<>();
            productMap.put(FirebaseModel.node_productName, productName);
            productMap.put(FirebaseModel.node_productPrice, productPrice);
            productMap.put(FirebaseModel.node_productDescription, productDescription);

            FirebaseModel.databaseRef_categories.child(productCategory).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        int index = (int)snapshot.getChildrenCount() + 1;

                        FirebaseModel.storageRef_categories.child(productCategory).child(index + ".jpg").putFile(productImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    //Toast.makeText(getContext(), "Profile Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                                    FirebaseModel.storageRef_categories.child(productCategory).child(index + ".jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            final String downloadUrl = task.getResult().toString();
                                            productMap.put(FirebaseModel.node_productImage, downloadUrl);

                                            FirebaseModel.databaseRef_categories.child(productCategory).child(Integer.toString(index)).setValue(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    loadingBar.dismiss();
                                                    Toast.makeText(AddProductsActivity.this, "Product added successfully!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });
                                } else {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(AddProductsActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}