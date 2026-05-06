package com.ecoscan.app.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("api/v0/product/{barcode}.json")
    Call<ResponseBody> getProduct(@Path("barcode") String barcode);
}