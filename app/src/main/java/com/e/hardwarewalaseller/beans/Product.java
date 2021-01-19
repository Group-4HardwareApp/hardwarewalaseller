package com.e.hardwarewalaseller.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Product implements Serializable {

    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("shopkeeperId")
    @Expose
    private String shopkeeperId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private double price;
    @SerializedName("discount")
    @Expose
    private double discount;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("qtyInStock")
    @Expose
    private long qtyInStock;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("secondImageUrl")
    @Expose
    private String secondImageUrl;
    @SerializedName("thirdImageurl")
    @Expose
    private String thirdImageurl;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("timestamp")
    @Expose
    private long timestamp;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getShopkeeperId() {
        return shopkeeperId;
    }

    public void setShopkeeperId(String shopkeeperId) {
        this.shopkeeperId = shopkeeperId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public long getQtyInStock() {
        return qtyInStock;
    }

    public void setQtyInStock(long qtyInStock) {
        this.qtyInStock = qtyInStock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSecondImageUrl() {
        return secondImageUrl;
    }

    public void setSecondImageUrl(String secondImageUrl) {
        this.secondImageUrl = secondImageUrl;
    }

    public String getThirdImageurl() {
        return thirdImageurl;
    }

    public void setThirdImageurl(String thirdImageurl) {
        this.thirdImageurl = thirdImageurl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}

