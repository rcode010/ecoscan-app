package com.ecoscan.app.api;

import com.ecoscan.app.model.ApiResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("api/v0/product/{barcode}.json")
    public Call<ResponseBody> getProduct(@Path("barcode") String barcode);
}
