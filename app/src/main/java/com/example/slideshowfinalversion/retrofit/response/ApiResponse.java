package com.example.slideshowfinalversion.retrofit.response;

import com.google.gson.annotations.SerializedName;

public class ApiResponse<T> {

    @SerializedName("data")
    private T data = null;

    public T getData() {
        return data;
    }
}
