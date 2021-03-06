package com.gios.freshngreen.responseModel.wishlist;

import com.gios.freshngreen.responseModel.product.PriceDetail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WishlistDetail implements Serializable {
    @SerializedName("wishlist_id")
    @Expose
    private String wishlistId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_code")
    @Expose
    private String productCode;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("price_details")
    @Expose
    private List<PriceDetail> priceDetails = null;
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

    public String getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(String wishlistId) {
        this.wishlistId = wishlistId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public List<PriceDetail> getPriceDetails() {
        return priceDetails;
    }

    public void setPriceDetails(List<PriceDetail> priceDetails) {
        this.priceDetails = priceDetails;
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
}
