package com.gios.freshngreen.responseModel.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductList implements Serializable,Comparable<ProductList> {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("product_code")
    @Expose
    private String productCode;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("actual_price")
    @Expose
    private String actualPrice;
    @SerializedName("retail_price")
    @Expose
    private String retailPrice;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("item_add_to_cart")
    @Expose
    private String itemAddToCart;
    @SerializedName("wishlist_flag")
    @Expose
    private Boolean wishlistFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItemAddToCart() {
        return itemAddToCart;
    }

    public void setItemAddToCart(String itemAddToCart) {
        this.itemAddToCart = itemAddToCart;
    }

    public Boolean getWishlistFlag() {
        return wishlistFlag;
    }

    public void setWishlistFlag(Boolean wishlistFlag) {
        this.wishlistFlag = wishlistFlag;
    }

    @Override
    public int compareTo(ProductList productList) {
        return this.getActualPrice().compareTo(productList.getActualPrice());
    }
}
