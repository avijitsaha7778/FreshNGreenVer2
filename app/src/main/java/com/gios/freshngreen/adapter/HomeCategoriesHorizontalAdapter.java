package com.gios.freshngreen.adapter;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gios.freshngreen.R;
import com.gios.freshngreen.responseModel.home.Category;
import com.gios.freshngreen.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class HomeCategoriesHorizontalAdapter extends RecyclerView.Adapter<HomeCategoriesHorizontalAdapter.CountryViewHolder>{
    private List<Category> categoryList;
    private HomeCategoriesHorizontalAdapterInterface mInterface;

    public HomeCategoriesHorizontalAdapter(List<Category> categoryList, HomeCategoriesHorizontalAdapterInterface mInterface) {
        this.categoryList = categoryList;
        this. mInterface = mInterface;
    }

    static class CountryViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout item;
        private TextView categoriesName;
        private ImageView categoryImg;
        private Button viewAllButton;

        CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            categoriesName = itemView.findViewById(R.id.categoriesName);
            categoryImg = itemView.findViewById(R.id.categoryImg);
            viewAllButton = itemView.findViewById(R.id.viewAllButton);
        }
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_categories_item,parent,false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        Category mCategory = categoryList.get(position);

        try {
            if(mCategory!=null && !mCategory.getCategoryName().isEmpty() &&
                    mCategory.getCategoryName().equalsIgnoreCase("View All")){
                holder.viewAllButton.setVisibility(View.VISIBLE);
                holder.categoriesName.setVisibility(View.INVISIBLE);
                holder.categoryImg.setVisibility(View.INVISIBLE);

                holder.viewAllButton.setOnClickListener(v -> {
                    mInterface.onCategoryClick(mCategory);
                });

            }else {
                holder.viewAllButton.setVisibility(View.GONE);
                holder.categoriesName.setVisibility(View.VISIBLE);
                holder.categoryImg.setVisibility(View.VISIBLE);

                holder.categoriesName.setText(mCategory.getCategoryName());
                if (mCategory.getImage() != null && !mCategory.getImage().isEmpty()) {
                    Picasso.get().load(mCategory.getImage()).transform(new CircleTransform()).into(holder.categoryImg);
                }

                holder.item.setOnClickListener(v -> {
                    mInterface.onCategoryClick(mCategory);
                });
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public interface HomeCategoriesHorizontalAdapterInterface{
        void onCategoryClick(Category mCategory);
    }
}