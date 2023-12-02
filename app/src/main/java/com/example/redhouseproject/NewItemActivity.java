package com.example.redhouseproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.redhouseproject.model.Item;
import com.example.redhouseproject.model.SharedPrefManager;
import com.example.redhouseproject.model.User;
import com.example.redhouseproject.remote.ApiUtils;
import com.example.redhouseproject.remote.ItemService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewItemActivity extends AppCompatActivity {

    private EditText txtName;
    private EditText txtSize;
    private EditText txtColour;
    private EditText txtPrice;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        // store context
        context = this;

        // get view objects references
        txtName = findViewById(R.id.txtName);
        txtSize = findViewById(R.id.txtSize);
        txtColour = findViewById(R.id.txtColour);
        txtPrice = findViewById(R.id.txtPrice);
    }

    /**
     * Called when Add Item button is clicked
     * @param v
     */
    public void addNewItem(View v) {
        // get values from form
        String name = txtName.getText().toString();
        String size = txtSize.getText().toString();
        String colour = txtColour.getText().toString();
        String price = txtPrice.getText().toString();

        // create an item object
        Item newItem = new Item(0, name, size, colour, price,"image.jpg");

        // get user info from SharedPreferences
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        // get ItemService instance
        ItemService itemService = ApiUtils.getItemService();

        // execute the call
        itemService.addItems(user.getToken(), newItem).enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if (response.isSuccessful()) {
                    Item addedItem = response.body();
                    if (addedItem != null) {
                        // display success message
                        Toast.makeText(context, addedItem.getItemName() + " added successfully.", Toast.LENGTH_LONG).show();

                        // go back to the item list activity
                        Intent intent = new Intent(context, ItemListActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        displayAlert("Add New Item failed. Response body is empty.");
                    }
                } else {
                    String errorMessage = "";
                    try {
                        errorMessage = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    displayAlert("Add New Item failed. Error: " + response.code() + ", " + errorMessage);
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                displayAlert("Error: " + t.getMessage());
            }
        });
    }


    /**
     * Display an alert dialog with a single button
     * @param message the message to be displayed
     */
    public void displayAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
