package com.example.redhouseproject;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

import com.example.redhouseproject.model.Item;
import com.example.redhouseproject.model.SharedPrefManager;
import com.example.redhouseproject.model.User;
import com.example.redhouseproject.remote.ApiUtils;
import com.example.redhouseproject.remote.ItemService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailActivity extends AppCompatActivity {

    ItemService itemService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        // get book id sent by ItemListActivity, -1 if not found
        Intent intent = getIntent();
        int id = intent.getIntExtra("item_id", -1);

        // get user info from SharedPreferences
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        // get book service instance
        itemService = ApiUtils.getItemService();

        // execute the API query. send the token and book id
        itemService.getItem(user.getToken(), id).enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());

                // get book object from response
                Item item = response.body();

                // get references to the view elements
                TextView tvName = findViewById(R.id.tvName);
                TextView tvSize = findViewById(R.id.tvSize);
                TextView tvColour = findViewById(R.id.tvColour);
                TextView tvPrice = findViewById(R.id.tvPrice);
                ImageView ivImage = findViewById(R.id.ivImage);

                // set values
                tvName.setText(item.getItemName());
                tvSize.setText(item.getItemSize());
                tvColour.setText(item.getItemColour());
                tvPrice.setText(item.getItemPrice());
                // Load and display the image using Picasso
                String imageUrl = item.getItemImage(); // Assuming the itemImage field stores the image URL
                Picasso.get().load(imageUrl).into(ivImage);

            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Toast.makeText(null, "Error connecting", Toast.LENGTH_LONG).show();
            }
        });

    }
}
