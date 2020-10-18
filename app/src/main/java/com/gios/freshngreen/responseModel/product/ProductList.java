package com.gios.freshngreen.responseModel.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

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
    @SerializedName("price_details")
    @Expose
    private List<PriceDetail> priceDetails = null;
    @SerializedName("quantity")
    @Expose
    private String quantity;
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

    public List<PriceDetail> getPriceDetails() {
        return priceDetails;
    }

    public void setPriceDetails(List<PriceDetail> priceDetails) {
        this.priceDetails = priceDetails;
    }


    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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
        if(this.priceDetails!=null) {
            return this.priceDetails.get(0).getActualPrice().compareTo(productList.getPriceDetails().get(0).getActualPrice());
        }
        return 0;
    }
}
