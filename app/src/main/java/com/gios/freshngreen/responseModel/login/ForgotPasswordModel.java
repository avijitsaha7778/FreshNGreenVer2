package com.gios.freshngreen.responseModel.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ForgotPasswordModel implements Serializable {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("phone_no")
    @Expose
    private String phoneNo;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("validate_status")
    @Expose
    private Boolean validateStatus;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Boolean getValidateStatus() {
        return validateStatus;
    }

    public void setValidateStatus(Boolean validateStatus) {
        this.validateStatus = validateStatus;
    }
}
