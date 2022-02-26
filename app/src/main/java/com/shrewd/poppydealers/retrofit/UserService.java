package com.shrewd.poppydealers.retrofit;


import com.google.gson.JsonObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    @POST("home/location_update")
    Call<JsonObject> setLocation(@Body RequestBody requestBody);

}
