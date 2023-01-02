package com.mr_17.evira.model;

import android.net.Uri;

public class ProductsListRecyclerViewModel
{
    String productName;
    String productPrice;
    int image;
    String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public ProductsListRecyclerViewModel(String productName, String productPrice, String imageUrl)
    {
        this.productName = productName;
        this.productPrice = productPrice;
        this.imageUrl = imageUrl;
    }
}

