package com.gios.freshngreen.responseModel.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginModel implements Serializable {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("login_status")
    @Expose
    private Boolean loginStatus;
    @SerializedName("user_details")
    @Expose
    private LoginUserDetails LoginUserDetails;

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

    public Boolean getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(Boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

    public LoginUserDetails getLoginUserDetails() {
        return LoginUserDetails;
    }

    public void setLoginUserDetails(LoginUserDetails LoginUserDetails) {
        this.LoginUserDetails = LoginUserDetails;
    }
}
