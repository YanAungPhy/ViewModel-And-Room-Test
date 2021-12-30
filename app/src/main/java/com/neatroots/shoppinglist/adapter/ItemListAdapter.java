package com.neatroots.shoppinglist.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neatroots.shoppinglist.R;
import com.neatroots.shoppinglist.db.Category;
import com.neatroots.shoppinglist.db.Items;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    private Context context;
    private List<Items> itemsList;
    private HandleItemsClick clicklistener;

    public ItemListAdapter(Context context,HandleItemsClick clicklistener) {
        this.context = context;
        this.clicklistener = clicklistener;
    }

    public void setItemsList(List<Items> itemsList) {
        this.itemsList = itemsList;
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
        holder.tvItemName.setText(this.itemsList.get(position).itemName);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clicklistener.itemClick(itemsList.get(position));
//
//            }
//        });

        holder.editCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicklistener.editItem(itemsList.get(position));

            }
        });

        holder.removeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicklistener.removeItem(itemsList.get(position));

            }
        });

        if(this.itemsList.get(position).completed){
            holder.tvItemName.setPaintFlags(holder.tvItemName.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }else {
            holder.tvItemName.setPaintFlags(0);
        }

    }

    @Override
    public int getItemCount() {
        if (itemsList == null || itemsList.size() == 0)
            return 0;
        else {
            return itemsList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName;
        ImageView removeCategory;
        ImageView editCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvCategoryName);
            removeCategory = itemView.findViewById(R.id.removeCategory);
            editCategory = itemView.findViewById(R.id.editCategory);
        }
    }

    public interface HandleItemsClick{
        void itemClick(Items items);
        void removeItem(Items items);
        void editItem(Items items);
    }
}
