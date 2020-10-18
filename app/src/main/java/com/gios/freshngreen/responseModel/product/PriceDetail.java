package com.gios.freshngreen.responseModel.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PriceDetail implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("actual_price")
    @Expose
    private String actualPrice;
    @SerializedName("retail_price")
    @Expose
    private String retailPrice;
    @SerializedName("weight")
    @Expose
    private String weight;

    @SerializedName("default")
    @Expose
    private boolean defaultPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public boolean isDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(boolean defaultPrice) {
        this.defaultPrice = defaultPrice;
    }
}
