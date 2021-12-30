package com.neatroots.shoppinglist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neatroots.shoppinglist.R;
import com.neatroots.shoppinglist.db.Category;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {

    private Context context;
    private List<Category> categoryList;
    private HandleCategoryClick clicklistener;

    public CategoryListAdapter(Context context,HandleCategoryClick clicklistener) {
        this.context = context;
        this.clicklistener = clicklistener;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvCategoryName.setText(this.categoryList.get(position).categoryName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicklistener.itemClick(categoryList.get(position));

            }
        });

        holder.editCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicklistener.editItem(categoryList.get(position));

            }
        });

        holder.removeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicklistener.removeItem(categoryList.get(position));

            }
        });

    }

    @Override
    public int getItemCount() {
        if (categoryList == null || categoryList.size() == 0)
            return 0;
        else {
            return categoryList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        ImageView removeCategory;
        ImageView editCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            removeCategory = itemView.findViewById(R.id.removeCategory);
            editCategory = itemView.findViewById(R.id.editCategory);
        }
    }

    public interface HandleCategoryClick{
        void itemClick(Category category);
        void removeItem(Category category);
        void editItem(Category category);
    }
}
