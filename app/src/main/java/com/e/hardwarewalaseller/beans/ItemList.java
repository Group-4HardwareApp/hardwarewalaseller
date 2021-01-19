package com.e.hardwarewalaseller.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemList implements Serializable {

    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("cartId")
    @Expose
    private String cartId;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private Long price;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("qty")
    @Expose
    private Long qty;
    @SerializedName("qtyInStock")
    @Expose
    private Long qtyInStock;
    @SerializedName("shopkeeperId")
    @Expose
    private String shopkeeperId;
    @SerializedName("total")
    @Expose
    private Long total;
    @SerializedName("userId")
    @Expose
    private String userId;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public Long getQtyInStock() {
        return qtyInStock;
    }

    public void setQtyInStock(Long qtyInStock) {
        this.qtyInStock = qtyInStock;
    }

    public String getshopkeeperId() { return shopkeeperId; }

    public void setshopkeeperId(String shopkeeperId) { this.shopkeeperId = shopkeeperId; }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
