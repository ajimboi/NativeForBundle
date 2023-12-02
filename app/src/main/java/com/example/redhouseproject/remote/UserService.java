package com.example.redhouseproject.remote;

import com.example.redhouseproject.model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
public interface UserService {

    @FormUrlEncoded
    @POST("api/users/login")
    Call<User> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("api/users/login")
    Call<User> loginEmail(@Field("email") String username, @Field("password") String password);
}
