package com.example.redhouseproject.remote;

import com.example.redhouseproject.model.Item;
import com.example.redhouseproject.model.DeleteResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ItemService {

    @GET("api/item")
    Call<List<Item>> getAllItems(@Header("api-key") String apiKey);

    @GET("api/item/{id}")
    Call<Item> getItem(@Header("api-key") String apiKey, @Path("id") int id);

    @POST("api/item")
    Call<Item> addItems(@Header("api-key") String apiKey, @Body Item item);

    @POST("api/item/delete/{id}")
    Call<DeleteResponse> deleteItem(@Header("api-key") String apiKey, @Path("id") int id);

    @POST("api/item/update")
    Call<Item> updateItem(@Header("api-key") String apiKey, @Body Item item);

    @GET("api/item/search")
    Call<Item> searchItem(@Header("api-key") String apiKey, @Query("id") int id);
}
