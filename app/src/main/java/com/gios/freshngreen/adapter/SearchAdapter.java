package com.gios.freshngreen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gios.freshngreen.R;
import com.gios.freshngreen.responseModel.home.SearchModel;
import com.gios.freshngreen.responseModel.home.SearchProductList;
import com.gios.freshngreen.responseModel.product.ProductList;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SearchAdapter extends ArrayAdapter<SearchProductList> {
    private List<SearchProductList> productList;
    private List<SearchProductList> filteredProductList;
    private SearchAdapter.Interface mInterface;

    public SearchAdapter(@NonNull Context context, @NonNull List<SearchProductList> productList, SearchAdapter.Interface mInterface) {
        super(context, 0, productList);
        this. mInterface = mInterface;

        productList = new ArrayList<>(productList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return productFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.search_adapter_item, parent, false
            );
        }

        TextView itemText = convertView.findViewById(R.id.itemText);

        SearchProductList mSearchProductList = getItem(position);
        if (mSearchProductList != null) {
            itemText.setText(mSearchProductList.getProductName());
        }

        convertView.setOnClickListener(v -> {
            mInterface.onSearchItemClick(mSearchProductList);
        });

        return convertView;
    }

    private Filter productFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if(productList.size()>0) {
                filteredProductList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredProductList.addAll(productList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (SearchProductList mSearchProductList : productList) {
                        if (mSearchProductList.getProductName().toLowerCase().contains(filterPattern)) {
                            filteredProductList.add(mSearchProductList);
                        }
                    }
                }

                results.values = filteredProductList;
                results.count = filteredProductList.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            if(results.values != null) {
                addAll((List) results.values);
            }
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((SearchProductList) resultValue).getProductName();
        }
    };


    public interface Interface{
        void onSearchItemClick(SearchProductList mSearchProductList);
    }
}