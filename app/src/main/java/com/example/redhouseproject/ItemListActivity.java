package com.example.redhouseproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.redhouseproject.model.ItemAdapter;
import com.example.redhouseproject.model.Item;
import com.example.redhouseproject.model.DeleteResponse;
import com.example.redhouseproject.model.SharedPrefManager;
import com.example.redhouseproject.model.User;
import com.example.redhouseproject.remote.ApiUtils;
import com.example.redhouseproject.remote.ItemService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemListActivity extends AppCompatActivity {

    ItemService itemService;
    Context context;
    RecyclerView itemList;
    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        context = this; // get current activity context

        // get reference to the RecyclerView bookList
        itemList = findViewById(R.id.itemList);

        //register for context menu
        registerForContextMenu(itemList);

        // get user info from SharedPreferences
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        // get book service instance
        itemService = ApiUtils.getItemService();

        // execute the call. send the user token when sending the query
        itemService.getAllItems(user.getToken()).enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());

                // token is not valid/expired
                if (response.code() == 401) {
                    displayAlert("Session Invalid");
                }

                // Get list of book object from response
                List<Item> items = response.body();

                // initialize adapter
                adapter = new ItemAdapter(context, items);

                // set adapter to the RecyclerView
                itemList.setAdapter(adapter);

                // set layout to recycler view
                itemList.setLayoutManager(new LinearLayoutManager(context));

                // add separator between item in the list
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(itemList.getContext(),
                        DividerItemDecoration.VERTICAL);
                itemList.addItemDecoration(dividerItemDecoration);
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(context, "Error connecting to the server", Toast.LENGTH_LONG).show();
                displayAlert("Error [" + t.getMessage() + "]");
                Log.e("MyApp:", t.getMessage());
            }
        });

        // action handler for Add Book floating button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // forward user to NewBookActivity
                Intent intent = new Intent(context, NewItemActivity.class);
                startActivity(intent);
            }
        });
        FloatingActionButton fabSearch = findViewById(R.id.fas);
        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemListActivity.this, SearchItemActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Item selectedItem = adapter.getSelectedItem();
        Log.d("MyApp", "selected "+selectedItem.toString());
        switch (item.getItemId()) {
            case R.id.menu_details://should match the id in the context menu file
                doViewDetails(selectedItem);
                break;
            case R.id.menu_delete://should match the id in the context menu file
                doDeleteItem(selectedItem);
                break;
            case R.id.menu_update://should match the id in the context menu file
                doUpdate(selectedItem);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void doUpdate(Item selectedItem) {
        // for debugging purpose
        Log.d("MyApp:", "launching update activity for "+selectedItem.toString());

        // launch UpdateBookActivity and pass the book id
        Intent intent = new Intent(context, UpdateItemActivity.class);
        intent.putExtra("item_id", selectedItem.getId());
        startActivity(intent);
    }

    private void doViewDetails(Item selectedItem) {
        Log.d("MyApp:", "viewing details "+selectedItem.toString());
        Intent intent = new Intent(context, ItemDetailActivity.class);
        intent.putExtra("item_id", selectedItem.getId());
        startActivity(intent);
    }

    /**
     * Delete book record. Called by contextual menu "Delete"
     * @param selectedItem - book selected by user
     */
    private void doDeleteItem(Item selectedItem) {
        // get user info from SharedPreferences
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        // prepare REST API call
        ItemService bookService = ApiUtils.getItemService();
        Call<DeleteResponse> call = bookService.deleteItem(user.getToken(), selectedItem.getId());

        // execute the call
        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                if (response.code() == 200) {
                    // 200 means OK
                    displayAlert("Item successfully deleted");
                } else {
                    displayAlert("Item failed to delete");
                    Log.e("MyApp:", response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                displayAlert("Error [" + t.getMessage() + "]");
                Log.e("MyApp:", t.getMessage());
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