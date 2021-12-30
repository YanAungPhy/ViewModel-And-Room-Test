package com.neatroots.shoppinglist.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.neatroots.shoppinglist.R;
import com.neatroots.shoppinglist.adapter.ItemListAdapter;
import com.neatroots.shoppinglist.db.Items;
import com.neatroots.shoppinglist.viewmodel.ShowItemListActivityViewModel;

import java.util.List;

public class ShowItemListActivity extends AppCompatActivity implements ItemListAdapter.HandleItemsClick {

    private ItemListAdapter itemListAdapter;
    private ShowItemListActivityViewModel viewModel;
    private RecyclerView recyclerView;
    private int category_id;
    private Items itemtoUpdat = null;
    private EditText addNewItemInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item_list);

        category_id = getIntent().getIntExtra("category_id", 0);
        String categoryName = getIntent().getStringExtra("category_name");

        getSupportActionBar().setTitle(categoryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addNewItemInput = findViewById(R.id.addNewItemInput);
        ImageView saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = addNewItemInput.getText().toString();
                if (TextUtils.isEmpty(itemName)) {
                    Toast.makeText(ShowItemListActivity.this, "Enter item name", Toast.LENGTH_SHORT).show();
                }
                if (itemtoUpdat == null) {
                    saveNewItem(itemName);
                }else {
                    updateNewItem(itemName);
                }

            }

        });
        initViewModel();
        initRecyclerView();
        viewModel.getAllItemsList(category_id);
    }



    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(ShowItemListActivityViewModel.class);
        viewModel.getListItemObserver().observe(this, new Observer<List<Items>>() {
            @Override
            public void onChanged(List<Items> items) {
                if (items == null) {
                    recyclerView.setVisibility(View.GONE);
                    findViewById(R.id.noResult).setVisibility(View.VISIBLE);
                } else {
                    itemListAdapter.setItemsList(items);
                    findViewById(R.id.noResult).setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemListAdapter = new ItemListAdapter(this, this);

        recyclerView.setAdapter(itemListAdapter);
    }

    private void saveNewItem(String itemName) {
        Items items = new Items();
        items.itemName = itemName;
        items.categoryId = category_id;
        viewModel.insertItems(items);
        addNewItemInput.setText("");
    }

    @Override
    public void itemClick(Items items) {
        if (items.completed) {
            items.completed = false;
        } else {
            items.completed = true;
        }
        viewModel.updateItems(items);

    }

    @Override
    public void removeItem(Items items) {
        viewModel.deleteItems(items);
    }

    @Override
    public void editItem(Items items) {
        this.itemtoUpdat = items;
        addNewItemInput.setText(items.itemName);
    }

    private void updateNewItem(String newName) {
        itemtoUpdat.itemName = newName;
        viewModel.updateItems(itemtoUpdat);
        addNewItemInput.setText("");
        itemtoUpdat = null;
    }
}
