package com.example.redhouseproject.remote;


public class ApiUtils {
    // REST API server URL
    public static final String BASE_URL = "https://hzimzmrimobilework.000webhostapp.com/prestige/";

    // return UserService instance
    public static UserService getUserService() {
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }

    // return BookService instance
    public static ItemService getItemService() {
        return RetrofitClient.getClient(BASE_URL).create(ItemService.class);
    }
}