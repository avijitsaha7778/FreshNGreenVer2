package com.gios.freshngreen.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gios.freshngreen.R;
import com.gios.freshngreen.responseModel.profile.AreaList;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AreaDialogAdapter extends RecyclerView.Adapter<AreaDialogAdapter.ViewHolder>{
    private List<AreaList> countries;
    private DialogInterface dialogInterface;
    public AreaDialogAdapter(List<AreaList> countries, DialogInterface dialogInterface) {
        this.countries=countries;
        this.dialogInterface=dialogInterface;
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.area_pin_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AreaList list = countries.get(position);
        holder.area.setText(list.getAreaName());

        holder.itemView.setOnClickListener(v -> {
            dialogInterface.dialogCallback(list);
        });
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public interface DialogInterface{
        void dialogCallback(AreaList list);
    }
}