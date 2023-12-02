package com.example.redhouseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redhouseproject.model.Item;
import com.example.redhouseproject.model.SharedPrefManager;
import com.example.redhouseproject.model.User;
import com.example.redhouseproject.remote.ApiUtils;
import com.example.redhouseproject.remote.ItemService;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchItemActivity extends AppCompatActivity {

    private EditText edtItemId;
    private TextView txtName;
    private TextView txtSize;
    private TextView txtPrice;
    private TextView txtColour;
    private ImageView ivImage;
    private Button btnSearch;



    private ItemService itemService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);

        // Initialize the UI elements
        edtItemId = findViewById(R.id.edtItemId);
        txtName = findViewById(R.id.txtName);
        txtSize = findViewById(R.id.txtSize);
        txtPrice = findViewById(R.id.txtPrice);
        txtColour = findViewById(R.id.txtColour);
        btnSearch = findViewById(R.id.btnSearch);
        ivImage = findViewById(R.id.ivImage);

        // Initialize the ItemService
        itemService = ApiUtils.getItemService();

        // Set click listener for the search button
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchItem();
            }
        });
    }

    private void searchItem() {
        // Get the item ID entered by the user
        int itemId = Integer.parseInt(edtItemId.getText().toString());

        // Make the API call to search for the item
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        itemService.getItem(user.getToken(), itemId).enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if (response.isSuccessful()) {
                    Item item = response.body();
                    if (item != null) {
                        // Item found, display the details
                        txtName.setText(item.getItemName());
                        txtSize.setText(item.getItemSize());
                        txtPrice.setText(item.getItemPrice());
                        txtColour.setText(item.getItemColour());
                        String imageUrl = item.getItemImage(); // Assuming the itemImage field stores the image URL
                        Picasso.get().load(imageUrl).into(ivImage);

                    } else {
                        // Item not found
                        Toast.makeText(SearchItemActivity.this, "Item not found", Toast.LENGTH_SHORT).show();
                        clearItemDetails();
                    }
                } else {
                    // API call failed
                    Toast.makeText(SearchItemActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    clearItemDetails();
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                // Network or server error
                Toast.makeText(SearchItemActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("SearchItem", "Error: " + t.getMessage());
                clearItemDetails();
            }
        });
    }

    private void clearItemDetails() {
        // Clear the item details fields
        txtName.setText("");
        txtSize.setText("");
        txtPrice.setText("");
        txtColour.setText("");
        ivImage.setImageDrawable(null);
    }
}