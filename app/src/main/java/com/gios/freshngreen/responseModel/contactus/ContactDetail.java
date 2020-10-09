package com.gios.freshngreen.responseModel.contactus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ContactDetail implements Serializable {
    @SerializedName("emailId")
    @Expose
    private String emailId;
    @SerializedName("phoneNo")
    @Expose
    private String phoneNo;
    @SerializedName("address")
    @Expose
    private String address;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
