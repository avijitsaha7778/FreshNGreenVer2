package com.gios.freshngreen.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gios.freshngreen.R;
import com.gios.freshngreen.responseModel.profile.PinList;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PincodeDialogAdapter extends RecyclerView.Adapter<PincodeDialogAdapter.ViewHolder>{
    private List<PinList> countries;
    private PincodeDialogAdapter.DialogInterface dialogInterface;
    
    public PincodeDialogAdapter(List<PinList> countries, PincodeDialogAdapter.DialogInterface dialogInterface) {
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
    public PincodeDialogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.area_pin_item,parent,false);
        return new PincodeDialogAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PincodeDialogAdapter.ViewHolder holder, int position) {
        PinList list = countries.get(position);
        holder.area.setText(list.getPin());

        holder.itemView.setOnClickListener(v -> {
            dialogInterface.dialogCallback(list);
        });
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public interface DialogInterface{
        void dialogCallback(PinList list);
    }
}