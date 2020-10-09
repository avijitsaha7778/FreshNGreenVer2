package com.gios.freshngreen.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gios.freshngreen.R;
import com.gios.freshngreen.responseModel.home.Category;
import com.gios.freshngreen.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryGridAdapter extends RecyclerView.Adapter<CategoryGridAdapter.ViewHolder>{
    private List<Category> categoryList;
    private HomeCategoriesHorizontalAdapter.HomeCategoriesHorizontalAdapterInterface mInterface;

    public CategoryGridAdapter(List<Category> categoryList, HomeCategoriesHorizontalAdapter.HomeCategoriesHorizontalAdapterInterface mInterface) {
        this.categoryList = categoryList;
        this. mInterface = mInterface;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView categoriesName;
        private ImageView categoryImg;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoriesName = itemView.findViewById(R.id.categoriesName);
            categoryImg = itemView.findViewById(R.id.categoryImg);
        }
    }

    @NonNull
    @Override
    public CategoryGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_adapter_item,parent,false);
        return new CategoryGridAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryGridAdapter.ViewHolder holder, int position) {
        Category mCategory = categoryList.get(position);

        try {
            holder.categoriesName.setText(mCategory.getCategoryName());
            if(mCategory.getImage() != null && !mCategory.getImage().isEmpty()){
                Picasso.get().load(mCategory.getImage()).transform(new CircleTransform()).into(holder.categoryImg);
            }

            holder.itemView.setOnClickListener(v -> {
                mInterface.onCategoryClick(mCategory);
            });
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