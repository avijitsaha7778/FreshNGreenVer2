package com.gios.freshngreen.responseModel.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UpdateAddressModel implements Serializable {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("user_address_details")
    @Expose
    private List<UserAddressDetail> userAddressDetails = null;

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

    public List<UserAddressDetail> getUserAddressDetails() {
        return userAddressDetails;
    }

    public void setUserAddressDetails(List<UserAddressDetail> userAddressDetails) {
        this.userAddressDetails = userAddressDetails;
    }
}
