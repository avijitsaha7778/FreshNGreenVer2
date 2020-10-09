package com.gios.freshngreen.responseModel.wishlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RemoveWishlistModel implements Serializable {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("wishlist_details")
    @Expose
    private List<WishlistDetail> wishlistDetails = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<WishlistDetail> getWishlistDetails() {
        return wishlistDetails;
    }

    public void setWishlistDetails(List<WishlistDetail> wishlistDetails) {
        this.wishlistDetails = wishlistDetails;
    }
}
