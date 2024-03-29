package com.example.swd1.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MasterCategory implements Serializable {

    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("name")
    @Expose
    private String name;

    public MasterCategory() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
