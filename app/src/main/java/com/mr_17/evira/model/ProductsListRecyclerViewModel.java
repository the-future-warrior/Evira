package com.mr_17.evira.model;

public class ProductsListRecyclerViewModel
{
    String productName;
    String productPrice;
    int image;

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

    public ProductsListRecyclerViewModel(String productName, String productPrice, int image)
    {
        this.productName = productName;
        this.productPrice = productPrice;
        this.image = image;
    }
}

