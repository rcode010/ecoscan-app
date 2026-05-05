package com.ecoscan.app.api;

import com.ecoscan.app.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

// Endpoints and their controller functions
public interface ApiService {
    @GET("api/v0/product/{barcode}.json")
    public Call<ApiResponse> getProduct(@Path("barcode") String barcode);
}
