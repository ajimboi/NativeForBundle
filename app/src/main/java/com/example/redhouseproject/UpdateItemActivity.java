package com.example.redhouseproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.redhouseproject.model.Item;
import com.example.redhouseproject.model.SharedPrefManager;
import com.example.redhouseproject.model.User;
import com.example.redhouseproject.remote.ApiUtils;
import com.example.redhouseproject.remote.ItemService;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateItemActivity extends AppCompatActivity {

    private ItemService itemService;
    private Item item;      // store book info

    // form fields
    private EditText txtName;
    private EditText txtSize;
    private EditText txtPrice;
    private EditText txtColour;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);

        // retrieve book id from intent
        // get book id sent by BookListActivity, -1 if not found
        Intent intent = getIntent();
        int id = intent.getIntExtra("item_id", -1);

        // get references to the form fields in layout
        txtName = findViewById(R.id.txtName);
        txtSize = findViewById(R.id.txtSize);
        txtColour = findViewById(R.id.txtColour);
        txtPrice = findViewById(R.id.txtPrice);

        // retrieve book info from database using the book id
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
                item = response.body();

                // set values into forms
                txtName.setText(item.getItemName());
                txtSize.setText(item.getItemSize());
                txtColour.setText(item.getItemColour());
                txtPrice.setText(item.getItemPrice());

            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Toast.makeText(null, "Error connecting", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void updateItem(View view) {
        // get values in form
        String name = txtName.getText().toString();
        String size = txtSize.getText().toString();
        String colour = txtColour.getText().toString();
        String price = txtPrice.getText().toString();


        // update the book object retrieved in onCreate with the new data. the book object
        // already contains the id
        item.setItemName(name);
        item.setItemSize(size);
        item.setItemColour(colour);
        item.setItemPrice(price);


        Log.d("MyApp:", "Item info: " + item.toString());

        // get user info from SharedPreferences
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        // send request to update the book record to the REST API
        ItemService itemService = ApiUtils.getItemService();
        Call<Item> call = itemService.updateItem(user.getToken(), item);

        Context context = this;
        // execute
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {

                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());

                // invalid session?
                if (response.code() == 401)
                    displayAlert("Invalid session. Please re-login");

                // book updated successfully?
                Item updatedItem = response.body();
                if (updatedItem != null) {
                    // display message
                    Toast.makeText(context,
                            updatedItem.getItemName() + " updated successfully.",
                            Toast.LENGTH_LONG).show();

                    // end this activity and forward user to BookListActivity
                    Intent intent = new Intent(context, ItemListActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    displayAlert("Update Book failed.");
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                displayAlert("Error [" + t.getMessage() + "]");
                // for debug purpose
                Log.d("MyApp:", "Error: " + t.getCause().getMessage());
            }
        });
    }

    /**
     * Displaying an alert dialog with a single button
     * @param message - message to be displayed
     */
    public void displayAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}