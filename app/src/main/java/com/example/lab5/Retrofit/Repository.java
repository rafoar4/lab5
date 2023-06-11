package com.example.lab5.Retrofit;

import com.example.lab5.entity.RandomUserResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Repository {
    @GET("/api")
    Call<RandomUserResponse> getDoctor();
}
