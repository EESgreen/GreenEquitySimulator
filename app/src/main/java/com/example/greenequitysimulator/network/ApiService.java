package com.example.greenequitysimulator.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.example.greenequitysimulator.network.HistoryResponse;
public interface ApiService {

    // 🔹 API 1: Current Price
    @GET("quote")
    Call<StockResponse> getStockPrice(
            @Query("symbol") String symbol,
            @Query("token") String token
    );

    // 🔹 API 2: Historical Data
    @GET("stock/candle")
    Call<HistoryResponse> getHistory(
            @Query("symbol") String symbol,
            @Query("resolution") String resolution,
            @Query("from") long from,
            @Query("to") long to,
            @Query("token") String token
    );
}