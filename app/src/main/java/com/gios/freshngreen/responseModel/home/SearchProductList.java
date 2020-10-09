package com.gios.freshngreen.responseModel.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SearchProductList implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("product_name")
    @Expose
    private String productName;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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
}
