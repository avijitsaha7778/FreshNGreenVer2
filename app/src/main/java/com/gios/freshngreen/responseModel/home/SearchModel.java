package com.gios.freshngreen.responseModel.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SearchModel implements Serializable {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("product_list")
    @Expose
    private List<SearchProductList> productList = null;

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

    public List<SearchProductList> getProductList() {
        return productList;
    }

    public void setProductList(List<SearchProductList> productList) {
        this.productList = productList;
    }
}
