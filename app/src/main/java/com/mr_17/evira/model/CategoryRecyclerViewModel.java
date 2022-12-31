package com.mr_17.evira.model;

public class CategoryRecyclerViewModel
{
    String categoryName;
    int categoryProductCount;

    public int getBackgroundImg() {
        return backgroundImg;
    }

    public void setBackgroundImg(int backgroundImg) {
        this.backgroundImg = backgroundImg;
    }

    int backgroundImg;

    public CategoryRecyclerViewModel(String categoryName, int categoryProductCount, int backgroundImg)
    {
        this.categoryName = categoryName;
        this.categoryProductCount = categoryProductCount;
        this.backgroundImg = backgroundImg;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryProductCount() {
        return categoryProductCount;
    }

    public void setCategoryProductCount(int categoryProductCount) {
        this.categoryProductCount = categoryProductCount;
    }
}

