package com.gios.freshngreen.responseModel.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PinList implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("pin")
    @Expose
    private String pin;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
