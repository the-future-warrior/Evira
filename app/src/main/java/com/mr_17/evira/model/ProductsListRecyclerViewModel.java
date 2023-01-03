package com.mr_17.evira.model;

import android.net.Uri;

public class ProductsListRecyclerViewModel
{
    String productName;
    String productPrice;
    String productCategory;
    int image;
    String imageUrl;
    String timeStamp;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

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

    public ProductsListRecyclerViewModel(String productName, String productPrice, String productCategory, String imageUrl, String timeStamp)
    {
        this.productName = productName;
        this.productPrice = productPrice;
        this.imageUrl = imageUrl;
        this.productCategory = productCategory;
        this.timeStamp = timeStamp;
    }
}

