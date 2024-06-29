package com.longthph30891.chatapp.network;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiService {
    @POST
    Call<String> sendMessage(
            @HeaderMap HashMap<String,String> headers,
            @Body String messageBody,
            @Url String url
            );
}
