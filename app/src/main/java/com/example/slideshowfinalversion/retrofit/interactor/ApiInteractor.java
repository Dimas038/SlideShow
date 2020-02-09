package com.example.slideshowfinalversion.retrofit.interactor;

import com.example.slideshowfinalversion.model.DataModel;
import com.example.slideshowfinalversion.retrofit.ApiClient;
import com.example.slideshowfinalversion.retrofit.exception.UnknownApiException;
import com.example.slideshowfinalversion.retrofit.response.ApiResponse;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Response;

@Singleton
public class ApiInteractor {

    private final ApiClient apiClient;

    @Inject
    ApiInteractor(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public List<DataModel> getDataInfo() throws IOException, UnknownApiException {
        Response<ApiResponse<List<DataModel>>> response = apiClient.dataGet().execute();

        if (response == null) throw new UnknownApiException();
        if (response.code() != 200) throw new UnknownApiException();
        if (response.errorBody() != null) throw new UnknownApiException();
        if (response.body() == null) throw new UnknownApiException();

        ApiResponse<List<DataModel>> apiResponse = response.body();

        if (apiResponse.getData() == null) throw new UnknownApiException();

        return apiResponse.getData();
    }
}
