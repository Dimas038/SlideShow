package com.example.slideshowfinalversion.retrofit;

import com.example.slideshowfinalversion.model.DataModel;
import com.example.slideshowfinalversion.retrofit.response.ApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ApiClient {

    String BASE_HOST = "api.data.com";
    String BASE_URL = "https://" + BASE_HOST + "/method/";

    @GET("data.get")
    @Headers("Content-Type: application/json")
    Call<ApiResponse<List<DataModel>>> dataGet();

}
