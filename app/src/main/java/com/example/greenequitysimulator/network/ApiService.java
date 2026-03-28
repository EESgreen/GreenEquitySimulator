package com.example.greenequitysimulator.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("quote")
    Call<StockResponse> getStockPrice(
            @Query("symbol") String symbol,
            @Query("token") String token
    );
}