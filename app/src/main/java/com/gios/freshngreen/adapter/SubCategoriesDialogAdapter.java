package com.gios.freshngreen.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gios.freshngreen.R;
import com.gios.freshngreen.responseModel.home.Category;
import com.gios.freshngreen.responseModel.home.Subcategory;
import com.gios.freshngreen.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SubCategoriesDialogAdapter extends RecyclerView.Adapter<SubCategoriesDialogAdapter.CountryViewHolder>{
    private List<Subcategory> subCategoryList;
    private SubCategoriesDialogAdapter.SubCategoriesDialogAdapterInterface mInterface;

    public SubCategoriesDialogAdapter(List<Subcategory> subCategoryList, SubCategoriesDialogAdapter.SubCategoriesDialogAdapterInterface mInterface) {
        this.subCategoryList = subCategoryList;
        this. mInterface = mInterface;
    }

    static class CountryViewHolder extends RecyclerView.ViewHolder {
        private TextView categoriesName;
        private ImageView categoryImg;

        CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoriesName = itemView.findViewById(R.id.categoriesName);
            categoryImg = itemView.findViewById(R.id.categoryImg);
        }
    }

    @NonNull
    @Override
    public SubCategoriesDialogAdapter.CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_categories_adaper_item,parent,false);
        return new SubCategoriesDialogAdapter.CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoriesDialogAdapter.CountryViewHolder holder, int position) {
        Subcategory mSubcategory = subCategoryList.get(position);

        try {
            holder.categoriesName.setText(mSubcategory.getSubCategoryName());
            if(mSubcategory.getImage() != null && !mSubcategory.getImage().isEmpty()){
                Picasso.get().load(mSubcategory.getImage()).transform(new CircleTransform()).into(holder.categoryImg);
            }

            holder.itemView.setOnClickListener(v -> {
                mInterface.onSubCategoryClick(mSubcategory);
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return subCategoryList.size();
    }

    public interface SubCategoriesDialogAdapterInterface{
        void onSubCategoryClick(Subcategory mSubcategory);
    }
}