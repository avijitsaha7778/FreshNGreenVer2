package com.gios.freshngreen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gios.freshngreen.R;
import com.gios.freshngreen.responseModel.product.PriceDetail;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PriceDialogAdapter extends RecyclerView.Adapter<PriceDialogAdapter.ViewHolder>{
    private List<PriceDetail> prices;
    private PriceDialogAdapter.DialogInterface dialogInterface;
    private Context context;

    public PriceDialogAdapter(Context context, List<PriceDetail> prices, PriceDialogAdapter.DialogInterface dialogInterface) {
        this.prices = prices;
        this.dialogInterface = dialogInterface;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView area;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            area = itemView.findViewById(R.id.area);
        }
    }

    @NonNull
    @Override
    public PriceDialogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.area_pin_item,parent,false);
        return new PriceDialogAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PriceDialogAdapter.ViewHolder holder, int position) {
        PriceDetail list = prices.get(position);
        holder.area.setText(String.format("%s / %s", context.getResources().getString(R.string.rs) +" "+ list.getActualPrice(), list.getWeight()));

        holder.itemView.setOnClickListener(v -> {
            dialogInterface.dialogCallback(position, list);
        });
    }

    @Override
    public int getItemCount() {
        return prices.size();
    }

    public interface DialogInterface{
        void dialogCallback(int position, PriceDetail mPriceDetail);
    }
}