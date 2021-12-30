package com.neatroots.shoppinglist.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.neatroots.shoppinglist.R;
import com.neatroots.shoppinglist.adapter.CategoryListAdapter;
import com.neatroots.shoppinglist.db.Category;
import com.neatroots.shoppinglist.viewmodel.MainActivityViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryListAdapter.HandleCategoryClick {

    private MainActivityViewModel viewModel;
    private TextView noResultTextView;
    private RecyclerView recyclerView;
    private CategoryListAdapter categoryListAdapter;
    private Category categoryForEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Shopping list");
        noResultTextView = findViewById(R.id.noResult);
        recyclerView = findViewById(R.id.recyclerView);

        ImageView addCat = findViewById(R.id.addNewCategoryImageView);
        initViewMode();
        addCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCategoryDialog(false);
            }
        });
        initViewMode();
        initRecyclerView();
        viewModel.getAllCategoryList();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryListAdapter = new CategoryListAdapter(this, this);
        recyclerView.setAdapter(categoryListAdapter);
    }

    private void initViewMode() {
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        viewModel.getListCategoryObserver().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                if (categories == null) {
                    noResultTextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    categoryListAdapter.setCategoryList(categories);
                    recyclerView.setVisibility(View.VISIBLE);
                    noResultTextView.setVisibility(View.GONE);
                    // show the recyclerview
                }
            }
        });
    }


    private void showAddCategoryDialog(boolean isForEdit) {
        AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        View dialogView = getLayoutInflater().inflate(R.layout.add_category_layout, null);
        EditText enterCategoryInput = dialogView.findViewById(R.id.enterCategoryInput);
        TextView careateButton = dialogView.findViewById(R.id.creatButton);
        TextView cancelButton = dialogView.findViewById(R.id.cancelButton);

        if (isForEdit) {
            careateButton.setText("Update");
            enterCategoryInput.setText(categoryForEdit.categoryName);
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });
        careateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = enterCategoryInput.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(MainActivity.this, "Enter category name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isForEdit) {
                    categoryForEdit.categoryName = name;
                    viewModel.updateCategory(categoryForEdit);
                } else {
                    //we need to call viewmodel
                    viewModel.insertCategory(name);
                }
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    @Override
    public void itemClick(Category category) {
        Intent intent = new Intent(MainActivity.this, ShowItemListActivity.class);
        intent.putExtra("category_id",category.uid);
        intent.putExtra("category_name",category.categoryName);
        startActivity(intent);

    }

    @Override
    public void removeItem(Category category) {
        viewModel.deleteCategory(category);
    }

    @Override
    public void editItem(Category category) {
        this.categoryForEdit = category;
        showAddCategoryDialog(true);

    }

}