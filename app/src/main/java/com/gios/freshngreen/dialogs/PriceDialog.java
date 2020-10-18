package com.gios.freshngreen.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;

import com.gios.freshngreen.R;
import com.gios.freshngreen.adapter.AreaDialogAdapter;
import com.gios.freshngreen.adapter.PriceDialogAdapter;
import com.gios.freshngreen.adapter.ProductListAdapter;
import com.gios.freshngreen.databinding.AreaDialogBinding;
import com.gios.freshngreen.responseModel.product.PriceDetail;
import com.gios.freshngreen.responseModel.product.ProductList;
import com.gios.freshngreen.responseModel.profile.AreaList;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class PriceDialog extends DialogFragment implements PriceDialogAdapter.DialogInterface {
    private AreaDialogBinding binding;
    private Context context;
    private PriceDialog.SelectPriceInterface mInterface;
    private List<ProductList> mProductList;
    private int position;

    public PriceDialog(Context context, SelectPriceInterface mInterface, List<ProductList> mProductList, int position) {
        this.mInterface = mInterface;
        this.mProductList = mProductList;
        this.position = position;
        this.context = context;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        binding = AreaDialogBinding.inflate(getLayoutInflater());

        binding.dialogLabel.setText("Select Package");

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        PriceDialogAdapter adapter = new PriceDialogAdapter(context,mProductList.get(position).getPriceDetails(), this);
        binding.recyclerView.setAdapter(adapter);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireActivity());
        dialogBuilder.setView(binding.getRoot());
        AlertDialog dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(false);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setElevation(R.dimen._10sdp);

        return dialog;
    }


    @Override
    public void dialogCallback(int pricePosition, PriceDetail mPriceDetail) {
        for (int i=0;i<mProductList.get(position).getPriceDetails().size();i++){
            if(i == pricePosition){
                mProductList.get(position).getPriceDetails().get(i).setDefaultPrice(true);
            }else{
                mProductList.get(position).getPriceDetails().get(i).setDefaultPrice(false);
            }
        }
        mInterface.selectPriceCallback(mProductList);
        dismiss();
    }

    public interface SelectPriceInterface {
        void selectPriceCallback(List<ProductList> list);
    }

}