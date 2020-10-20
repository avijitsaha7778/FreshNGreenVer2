package com.gios.freshngreen.responseModel.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BannerModel implements Serializable {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("banner_list")
    @Expose
    private List<BannerList> bannerList = null;
    @SerializedName("cart_item_added")
    @Expose
    private String cartItemAdded;

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

    public List<BannerList> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<BannerList> bannerList) {
        this.bannerList = bannerList;
    }

    public String getCartItemAdded() {
        return cartItemAdded;
    }

    public void setCartItemAdded(String cartItemAdded) {
        this.cartItemAdded = cartItemAdded;
    }
}
